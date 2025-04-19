package org.aegrotatio.sole.types.dtos.responses;

import org.aegrotatio.sole.types.enums.RegistrationStatus;

import java.util.concurrent.atomic.AtomicBoolean;

public record RegistrationResponseDto(
        String registration,
        RegistrationStatus status,
        AtomicBoolean isApothecaryAvailable,
        AtomicBoolean isHerbalistAvailable
) { }