package org.aegrotatio.types.dtos.responses;

import org.aegrotatio.types.enums.RequestStatusEnum;

public class ReqStatusResponseDto {

    private Long requestStatusId;

    private CustomerReqResponseDto customerRequest;

    private RequestStatusEnum currentStatus;

    public ReqStatusResponseDto(Long requestStatusId, CustomerReqResponseDto customerRequest, RequestStatusEnum currentStatus) {
        this.requestStatusId = requestStatusId;
        this.customerRequest = customerRequest;
        this.currentStatus = currentStatus;
    }

    public Long getRequestStatusId() {
        return requestStatusId;
    }

    public void setRequestStatusId(Long requestStatusId) {
        this.requestStatusId = requestStatusId;
    }

    public CustomerReqResponseDto getCustomerRequest() {
        return customerRequest;
    }

    public void setCustomerRequest(CustomerReqResponseDto customerRequest) {
        this.customerRequest = customerRequest;
    }

    public RequestStatusEnum getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(RequestStatusEnum currentStatus) {
        this.currentStatus = currentStatus;
    }
}