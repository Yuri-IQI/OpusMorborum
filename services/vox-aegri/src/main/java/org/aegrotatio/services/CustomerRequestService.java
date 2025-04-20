package org.aegrotatio.services;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import org.aegrotatio.kafka.Communicator;
import org.aegrotatio.models.CustomerRequestEntity;
import org.aegrotatio.models.RequestStatusEntity;
import org.aegrotatio.types.dtos.mappers.CustomerRequestMapper;
import org.aegrotatio.types.dtos.mappers.RequestStatusMapper;
import org.aegrotatio.types.dtos.requests.AssociationByLevelRequestDto;
import org.aegrotatio.types.dtos.requests.CustomerIssueRequestDto;
import org.aegrotatio.types.dtos.requests.ReqStatusRequestDto;
import org.aegrotatio.types.dtos.responses.CustomerReqResponseDto;
import org.aegrotatio.types.dtos.responses.ReqStatusResponseDto;
import org.aegrotatio.types.enums.RequestStatusEnum;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class CustomerRequestService {
    @Inject
    RequestStatusMapper requestStatusMapper;

    @Inject
    CustomerRequestMapper customerRequestMapper;

    @Inject
    Communicator communicator;


    public Uni<List<ReqStatusResponseDto>> associateRequestStatusByLevel(@Valid AssociationByLevelRequestDto associationByLevelRequestDto) {
        return communicator.askForRegistrationId(associationByLevelRequestDto.registration())
                .onItem().transformToUni(registrationId -> getLeveledRequests(associationByLevelRequestDto)
                        .onItem().transformToUni(list -> {
                            List<Uni<ReqStatusResponseDto>> statusUpdates = list.stream()
                                    .map(customerRequest -> {
                                        RequestStatusEntity requestStatus = new RequestStatusEntity(registrationId, customerRequest, RequestStatusEnum.AVAILABLE);

                                        return addCustomerRequestStatus(requestStatus).onItem().ifNotNull()
                                                .transform(persistedRequestStatus -> requestStatusMapper.toResponse(persistedRequestStatus));
                                    })
                                    .collect(Collectors.toList());

                            return Uni.join().all(statusUpdates)
                                    .andCollectFailures();
                        })
                );
    }

    @WithTransaction
    public Uni<CustomerReqResponseDto> updateCustomerRequest(@Valid CustomerRequestEntity customerRequest) {
        return CustomerRequestEntity.<CustomerRequestEntity>findById(customerRequest.id)
                .onItem().ifNotNull().transformToUni(entity -> {
                    entity.title = customerRequest.title;
                    entity.description = customerRequest.description;
                    entity.level = customerRequest.level;
                    entity.baseReward = customerRequest.baseReward;
                    entity.isActive = customerRequest.isActive;
                    entity.author = customerRequest.author;

                    return entity.persistAndFlush()
                            .replaceWith(Uni.createFrom().item(entity));
                })
                .map(customerRequestMapper::toResponse);
    }

    @WithTransaction
    public Uni<ReqStatusResponseDto> updateRequestStatus(@Valid ReqStatusRequestDto requestStatusDto) {
        return RequestStatusEntity.<RequestStatusEntity>findById(requestStatusDto.requestStatusId())
                .onItem().ifNull().failWith(new IllegalArgumentException("Request status not found."))
                .flatMap(entity -> {
                    Log.infof("Updating request status with ID: %d", entity.id);

                    return CustomerRequestEntity.<CustomerRequestEntity>find("title", requestStatusDto.customerRequest().title)
                            .firstResult()
                            .flatMap(existingCustomerRequest -> {
                                if (existingCustomerRequest != null) {
                                    Log.infof("Existing customer request found: %s", existingCustomerRequest.title);
                                    entity.customerRequestEntity = existingCustomerRequest;
                                    return Uni.createFrom().item(entity);
                                } else {
                                    Log.info("No existing request found. Persisting new customer request.");
                                    return requestStatusDto.customerRequest().persist()
                                            .replaceWith(requestStatusDto.customerRequest())
                                            .invoke(newCustomerRequest -> entity.customerRequestEntity = newCustomerRequest)
                                            .replaceWith(entity);
                                }
                            })
                            .map(updatedEntity -> {
                                entity.currentStatus = requestStatusDto.currentStatus();
                                return requestStatusMapper.toResponse(entity);
                            });
                });
    }

    public Uni<CustomerReqResponseDto> createCustomerRequest(@Valid CustomerIssueRequestDto customerIssueRequestDto) {
        CustomerRequestEntity customerRequest = new CustomerRequestEntity(customerIssueRequestDto);

        return getCustomerRequestById(customerRequest)
                .onItem().ifNull().switchTo(() -> addCustomerRequest(customerRequest))
                .onItem().transform(persisted -> customerRequestMapper.toResponse(persisted))
                .onItem().ifNotNull().transform(persisted -> persisted);
    }

    @WithSession
    public Uni<List<CustomerReqResponseDto>> getAllCustomerRequests() {
        return CustomerRequestEntity.listAll()
                .map(listOfCustomerRequests -> listOfCustomerRequests.stream()
                        .map(customerRequest -> customerRequestMapper.toResponse((CustomerRequestEntity) customerRequest))
                        .collect(Collectors.toList())
                );
    }

    @WithSession
    public Uni<List<CustomerRequestEntity>> getLeveledRequests(AssociationByLevelRequestDto associationByLevelRequestDto) {
        return CustomerRequestEntity.find("FROM CustomerRequest cr WHERE cr.level = ?1", associationByLevelRequestDto.requestsLevel()).list();
    }

    @WithTransaction
    public Uni<RequestStatusEntity> addCustomerRequestStatus(RequestStatusEntity requestStatus) {
        return requestStatus.persistAndFlush()
                .map(persisted -> requestStatus);
    }

    @WithTransaction
    public Uni<CustomerRequestEntity> addCustomerRequest(CustomerRequestEntity customerRequest) {
        return customerRequest.persistAndFlush()
                .map(persisted -> customerRequest);
    }

    @WithSession
    public Uni<CustomerRequestEntity> getCustomerRequestById(CustomerRequestEntity customerRequest) {
        return CustomerRequestEntity.findById(customerRequest.id);
    }

    public Uni<Boolean> verifyExistingAssociation(String registration) {
        Log.infof("Verifying association for registration: %s", registration);

        return communicator.askForRegistrationId(registration)
                .flatMap(this::findRequestStatusByRegistrationId)
                .onFailure().invoke(error ->
                        Log.errorf("Error verifying association for registration %s: %s", registration, error.getMessage())
                );
    }

    @WithSession
    public Uni<Boolean> findRequestStatusByRegistrationId(Long registrationId) {
        return RequestStatusEntity.find("FROM RequestStatus WHERE registrationId = ?1", registrationId)
                .count()
                .map(count -> count > 0)
                .onFailure().invoke(error ->
                        Log.errorf("Database query failed for registrationId %d: %s", registrationId, error.getMessage())
                );
    }
}