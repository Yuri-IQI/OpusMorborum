package org.aegrotatio.sole.types.dtos.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AccessCodesRequestDto(
        @NotNull @Size(max = 24) String apothecaryCode,
        @NotNull @Size(max = 24) String herbalistCode
) { }