package org.aegrotatio.types.dtos.requests;

import jakarta.validation.constraints.NotNull;
import org.aegrotatio.models.CustomerRequestEntity;
import org.aegrotatio.types.enums.RequestStatusEnum;

public record ReqStatusRequestDto (
        @NotNull Long requestStatusId,
        @NotNull CustomerRequestEntity customerRequest,
        @NotNull RequestStatusEnum currentStatus
) { }