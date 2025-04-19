package org.aegrotatio.sole.types.dtos.requests;

import jakarta.validation.constraints.NotNull;
import org.aegrotatio.sole.types.enums.AvailableRoles;

public record RoleSelectionRequestDto(
        @NotNull AvailableRoles selectedRole,
        @NotNull String registration
) { }