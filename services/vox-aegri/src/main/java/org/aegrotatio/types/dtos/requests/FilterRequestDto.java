package org.aegrotatio.types.dtos.requests;

import io.quarkus.runtime.annotations.RegisterForReflection;
import io.smallrye.common.constraint.NotNull;
import org.aegrotatio.types.enums.FilterCriteriasEnum;

@RegisterForReflection
public record FilterRequestDto (
        @NotNull String registration,
        @NotNull FilterCriteriasEnum criteria,
        @NotNull String value
) { }