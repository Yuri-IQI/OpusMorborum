package org.aegrotatio.services;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import org.aegrotatio.kafka.Communicator;
import org.aegrotatio.models.CustomerRequestEntity;
import org.aegrotatio.models.RequestStatusEntity;
import org.aegrotatio.types.dtos.mappers.RequestStatusMapper;
import org.aegrotatio.types.dtos.requests.ShuffleRequestDto;
import org.aegrotatio.types.dtos.responses.ReqStatusResponseDto;
import org.aegrotatio.types.enums.RequestLevelEnum;
import org.aegrotatio.types.enums.RequestStatusEnum;
import org.aegrotatio.types.enums.StageEnum;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class ShuffleService {

    @Inject
    Communicator communicator;

    @Inject
    CustomerRequestService customerRequestService;

    @Inject
    RequestStatusMapper requestStatusMapper;

    public Uni<List<ReqStatusResponseDto>> associateRequestsByShuffle(@Valid ShuffleRequestDto shuffleRequestDto) {
        return shuffleCustomerRequests(shuffleRequestDto)
                .map(requestStatuses -> requestStatuses.stream()
                        .map(requestStatusMapper::toResponse)
                        .toList()
                );
    }

    @WithSession
    public Uni<List<CustomerRequestEntity>> getAvailableCustomerRequests(Long registrationId) {
        return CustomerRequestEntity.find(
                "isActive = true AND NOT EXISTS " +
                        "(SELECT 1 FROM RequestStatus rs WHERE rs.registrationId = ?1)",
                registrationId).list();
    }

    public Uni<List<RequestStatusEntity>> shuffleCustomerRequests(@Valid ShuffleRequestDto shuffleRequestDto) {
        return communicator.askForRegistrationId(shuffleRequestDto.registration())
                .chain(registrationId -> getAvailableCustomerRequests(registrationId)
                        .chain(availableRequests -> {
                            if (availableRequests.isEmpty()) {
                                return Uni.createFrom().item(Collections.emptyList());
                            }
                            return shuffleRequestDto.stage() == StageEnum.INITIAL
                                    ? doInitialShuffle(availableRequests, registrationId)
                                    : assignRequests(availableRequests, registrationId);
                        }));
    }

    private Uni<List<RequestStatusEntity>> doInitialShuffle(List<CustomerRequestEntity> availableRequests, Long registrationId) {
        Map<RequestLevelEnum, List<CustomerRequestEntity>> groupedByLevel = availableRequests.stream()
                .collect(Collectors.groupingBy(request -> request.level));

        Map<RequestLevelEnum, Double> selectionDistribution = Map.of(
                RequestLevelEnum.LOW, 0.4,
                RequestLevelEnum.LOW_TO_MEDIUM, 0.4,
                RequestLevelEnum.MEDIUM, 0.25,
                RequestLevelEnum.MEDIUM_TO_HIGH, 0.1,
                RequestLevelEnum.HIGH, 0.05
        );

        int totalSelectionCount = Math.min(availableRequests.size(), 11);
        List<CustomerRequestEntity> selectedRequests = new ArrayList<>();

        groupedByLevel.forEach((level, requests) -> {
            Collections.shuffle(requests);
            int count = (int) Math.round(selectionDistribution.getOrDefault(level, 0.0) * totalSelectionCount);
            selectedRequests.addAll(requests.stream().limit(count).toList());
        });

        return saveRequestStatuses(selectedRequests, registrationId);
    }

    private List<CustomerRequestEntity> balanceRequestList(List<CustomerRequestEntity> selected, List<CustomerRequestEntity> available) {
        while (selected.size() > 12) selected.removeLast();
        while (selected.size() < 12 && !available.isEmpty()) {
            available.stream()
                    .filter(customerRequest -> !selected.contains(customerRequest))
                    .findAny()
                    .ifPresent(selected::add);
        }
        return selected;
    }

    private Uni<List<RequestStatusEntity>> assignRequests(List<CustomerRequestEntity> requests, Long registrationId) {
        return Multi.createFrom().iterable(requests)
                .onItem().transformToUni(request -> customerRequestService.addCustomerRequestStatus(
                        new RequestStatusEntity(registrationId, request, RequestStatusEnum.AVAILABLE)))
                .concatenate().collect().asList();
    }

    private Uni<List<RequestStatusEntity>> saveRequestStatuses(List<CustomerRequestEntity> requests, Long registrationId) {
        return Multi.createFrom().iterable(requests)
                .onItem().transformToUni(request -> customerRequestService.addCustomerRequestStatus(
                        new RequestStatusEntity(registrationId, request, RequestStatusEnum.AVAILABLE)))
                .concatenate().collect().asList();
    }
}