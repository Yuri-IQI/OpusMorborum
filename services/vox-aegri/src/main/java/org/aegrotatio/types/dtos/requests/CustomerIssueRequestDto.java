package org.aegrotatio.types.dtos.requests;

import io.quarkus.runtime.annotations.RegisterForReflection;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.aegrotatio.types.enums.RequestLevelEnum;

@RegisterForReflection
public record CustomerIssueRequestDto (
        @NotNull @Size(max = 32) String title,
        @NotNull @Size(max = 240) String description,
        @NotNull RequestLevelEnum level,
        @NotNull @Positive float baseReward,
        @NotNull boolean isActive,
        @NotNull @Size(max = 40) String author
) {}