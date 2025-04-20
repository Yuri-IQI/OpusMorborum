package org.aegrotatio.types.dtos.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.aegrotatio.models.RequestStatusEntity;
import org.aegrotatio.types.dtos.responses.CustomerReqResponseDto;
import org.aegrotatio.types.dtos.responses.ReqStatusResponseDto;

@ApplicationScoped
public class RequestStatusMapper {

	public ReqStatusResponseDto toResponse(RequestStatusEntity requestStatus) {
		var customerRequest = new CustomerReqResponseDto (
			requestStatus.customerRequestEntity.id,
			requestStatus.customerRequestEntity.title,
			requestStatus.customerRequestEntity.description,
			requestStatus.customerRequestEntity.level,
			requestStatus.customerRequestEntity.baseReward,
			requestStatus.customerRequestEntity.isActive,
			requestStatus.customerRequestEntity.author
		);
		
		return new ReqStatusResponseDto(requestStatus.id, customerRequest, requestStatus.currentStatus);
	}
}