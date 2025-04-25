package org.aegrotatio.resources;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.aegrotatio.models.CustomerRequestEntity;
import org.aegrotatio.services.CustomerRequestService;
import org.aegrotatio.services.FilterService;
import org.aegrotatio.services.ShuffleService;
import org.aegrotatio.types.dtos.requests.*;
import org.aegrotatio.types.dtos.responses.CustomerReqResponseDto;
import org.aegrotatio.types.dtos.responses.ReqStatusResponseDto;
import org.aegrotatio.types.enums.FilterCriteriasEnum;
import org.aegrotatio.types.enums.StageEnum;
import org.jboss.resteasy.reactive.RestResponse;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Path("/customer-request")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerRequestResource {

    @Inject
    CustomerRequestService customerRequestService;

    @Inject
    FilterService filterService;

    @Inject
    ShuffleService shuffleService;

    @POST
    public Uni<RestResponse<CustomerReqResponseDto>> createCustomerRequest(@Valid CustomerIssueRequestDto customerIssueRequestDto) {
        return customerRequestService.createCustomerRequest(customerIssueRequestDto)
                .onFailure().recoverWithItem(e -> {
                    Log.errorf("Error creating customer request: {}", e.getMessage());
                    return null;
                })
                .onItem().ifNotNull().transform(customerRequest ->
                        RestResponse.ResponseBuilder.ok(customerRequest)
                                .status(Response.Status.CREATED)
                                .build()
                );
    }
    
    @POST
    @Path("/create-many")
    public Uni<RestResponse<List<CustomerReqResponseDto>>> createManyCustomerRequests(@Valid List<CustomerIssueRequestDto> customerIssueRequestDtoList) {
    	return customerRequestService.createManyCustomerRequests(customerIssueRequestDtoList)
    			.onFailure().recoverWithItem(e -> {
                    Log.errorf("Error creating customer request: {}", e.getMessage());
                    return null;
                })
    			.onItem().ifNotNull().transform(customerRequestList -> 
    					RestResponse.ResponseBuilder.ok(customerRequestList)
    							.status(Response.Status.CREATED)
    							.build()
    			);
    }

    @GET
    public Uni<RestResponse<List<CustomerReqResponseDto>>> getAllCustomerRequests() {
        return customerRequestService.getAllCustomerRequests()
                .onItem().ifNotNull().transform(RestResponse::ok)
                .onItem().ifNull().continueWith(RestResponse::notFound)
                .onFailure().invoke(() -> Log.error("Error getting all customer requests"));
    }

    @PUT
    @Path("/update-customer-request")
    public Uni<RestResponse<CustomerReqResponseDto>> updateCustomerRequest(CustomerRequestEntity customerRequest) {
        return customerRequestService.updateCustomerRequest(customerRequest)
                .onFailure().recoverWithItem(e -> {
                    Log.error("Error updating customer request: " + e.getMessage());
                    return null;
                })
                .onItem().transform(updatedCustomerRequest -> {
                    if (updatedCustomerRequest == null) return RestResponse.status(Response.Status.BAD_REQUEST);

                    return RestResponse.ResponseBuilder
                            .ok(updatedCustomerRequest)
                            .status(Response.Status.OK)
                            .build();
                });
    }

    @GET
    @Path("/shuffle")
    public Uni<RestResponse<List<ReqStatusResponseDto>>> associateRequestsByShuffle(@QueryParam("registration") String registration, @QueryParam("stage") StageEnum stage) {
        ShuffleRequestDto shuffleRequestDto = new ShuffleRequestDto(stage, registration);

        return shuffleService.associateRequestsByShuffle(shuffleRequestDto)
                .onItem().ifNotNull().transform(RestResponse::ok)
                .onItem().ifNull().continueWith(RestResponse::notFound)
                .onFailure().invoke(()-> Log.errorf("associateRequestsByShuffle: %s", shuffleRequestDto));
    }

    @GET
    @Path("/association")
    public Uni<RestResponse<Boolean>> verifyExistingAssociation(@QueryParam("registration") String registration) {
        return customerRequestService.verifyExistingAssociation(registration)
                .onItem().transform(RestResponse::ok)
                .onFailure().invoke(throwable -> Log.errorf("verifyExistingAssociation failed: %s", throwable.getMessage()));
    }

    @GET
    @Path("/filter")
    public Uni<RestResponse<List<ReqStatusResponseDto>>> getAllRequestsByFilter(@QueryParam("registration") String registration, @QueryParam("criteria") FilterCriteriasEnum criteria, @QueryParam("value") String value) {
        String decodedValue = URLDecoder.decode(value, StandardCharsets.UTF_8);
        FilterRequestDto filterRequestDto = new FilterRequestDto(registration, criteria, decodedValue);

        return filterService.getAllRequestsByFilter(filterRequestDto)
                .onItem().ifNotNull().transform(RestResponse::ok)
                .onItem().ifNull().continueWith(RestResponse::notFound)
                .onFailure().invoke(()-> Log.errorf("getAllRequestsByFilter: %s", filterRequestDto));
    }

    @POST
    @Path("/level")
    public Uni<RestResponse<List<ReqStatusResponseDto>>> associateRequestsByLevel(@Valid AssociationByLevelRequestDto associationByLevelRequestDto) {
        return customerRequestService.associateRequestStatusByLevel(associationByLevelRequestDto)
                .onFailure().recoverWithItem(e -> {
                    Log.error("Error associating request status by level: " + e.getMessage());
                    return null;
                })
                .onItem().transform(requestStatuses -> {
                    if (customerRequestService == null) return RestResponse.status(Response.Status.BAD_REQUEST);

                    return RestResponse.ResponseBuilder
                            .ok(requestStatuses)
                            .status(Response.Status.CREATED)
                            .build();
                });
    }

    @PUT
    @Path("/update-status")
    public Uni<RestResponse<ReqStatusResponseDto>> updateRequestStatus(@Valid ReqStatusRequestDto requestStatusDto) {
        return customerRequestService.updateRequestStatus(requestStatusDto)
                .onFailure().recoverWithItem(e -> {
                    Log.error("Error updating request status: " + e.getMessage());
                    return null;
                })
                .onItem().transform(updatedRequestStatus -> {
                    if (updatedRequestStatus == null) return RestResponse.status(Response.Status.BAD_REQUEST);

                    return RestResponse.ResponseBuilder
                            .ok(updatedRequestStatus)
                            .status(Response.Status.OK)
                            .build();
                });
    }
}