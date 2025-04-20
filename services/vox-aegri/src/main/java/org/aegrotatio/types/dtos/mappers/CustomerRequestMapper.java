package org.aegrotatio.types.dtos.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import org.aegrotatio.models.CustomerRequestEntity;
import org.aegrotatio.types.dtos.responses.CustomerReqResponseDto;

@ApplicationScoped
public class CustomerRequestMapper {

    public CustomerReqResponseDto toResponse(CustomerRequestEntity customerRequest) {
        return new CustomerReqResponseDto	(
				customerRequest.id,
				customerRequest.title,
				customerRequest.description,
				customerRequest.level,
				customerRequest.baseReward,
				customerRequest.isActive,
				customerRequest.author
		);
    }
}
