package org.aegrotatio.types.dtos.requests;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.aegrotatio.types.enums.StageEnum;

@RegisterForReflection
public record ShuffleRequestDto (
        @NotNull StageEnum stage,
        @NotNull @Size(max = 80) String registration
) { }