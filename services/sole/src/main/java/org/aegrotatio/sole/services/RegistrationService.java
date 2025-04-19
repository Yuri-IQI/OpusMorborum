package org.aegrotatio.sole.services;

import jakarta.validation.Valid;
import org.aegrotatio.sole.exceptions.RegistrationNotFoundException;
import org.aegrotatio.sole.models.RegistrationEntity;
import org.aegrotatio.sole.models.RoleplayEntity;
import org.aegrotatio.sole.repositories.RegistrationRepository;
import org.aegrotatio.sole.repositories.RoleplayRepository;
import org.aegrotatio.sole.types.dtos.requests.AccessCodesRequestDto;
import org.aegrotatio.sole.types.dtos.requests.RoleSelectionRequestDto;
import org.aegrotatio.sole.types.dtos.responses.RegistrationResponseDto;
import org.aegrotatio.sole.types.enums.AvailableRoles;
import org.aegrotatio.sole.types.enums.RegistrationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final RoleplayRepository roleplayRepository;

    @Autowired
    public RegistrationService(RegistrationRepository registrationRepository, RoleplayRepository roleplayRepository) {
        this.registrationRepository = registrationRepository;
        this.roleplayRepository = roleplayRepository;
    }

    public RegistrationResponseDto registerGame(@Valid AccessCodesRequestDto accessCodesDto) {
        if (accessCodesDto.apothecaryCode() == null || accessCodesDto.herbalistCode() == null) {
            throw new IllegalArgumentException("Both codes must be provided.");
        }

        return saveOrRetrieveRegistration(accessCodesDto);
    }

    private String generateRegistration(AccessCodesRequestDto accessCodesDto) {
        String apothecaryCode = Optional.ofNullable(accessCodesDto.apothecaryCode()).orElse("").trim();
        String herbalistCode = Optional.ofNullable(accessCodesDto.herbalistCode()).orElse("").trim();

        int maxLength = Math.max(apothecaryCode.length(), herbalistCode.length());
        StringBuilder interleaved = new StringBuilder();

        for (int i = 0; i < maxLength; i++) {
            if (i < apothecaryCode.length()) interleaved.append(apothecaryCode.charAt(i));
            if (i < herbalistCode.length()) interleaved.append(herbalistCode.charAt(i));
        }

        byte[] hashBytes = hashSHA256(interleaved.toString());

        return Base64.getUrlEncoder().withoutPadding().encodeToString(hashBytes);
    }

    private byte[] hashSHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(input.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    private RegistrationResponseDto saveOrRetrieveRegistration(AccessCodesRequestDto accessCodesDto) {
        String registration = generateRegistration(accessCodesDto);

        return registrationRepository.findByValue(registration)
                .map(existingRegistration -> {
                    List<RoleplayEntity> assignedRoles = this.roleplayRepository.findByRegistration_Value(registration);
                    AtomicBoolean isApothecaryAvailable = new AtomicBoolean(true);
                    AtomicBoolean isHerbalistAvailable = new AtomicBoolean(true);

                    if (!assignedRoles.isEmpty()) {
                        assignedRoles.forEach(assignment -> {
                            if (assignment.getRoleName().equals(AvailableRoles.APOTHECARY))
                                isApothecaryAvailable.set(assignment.isAvailable());
                            if (assignment.getRoleName().equals(AvailableRoles.HERBALIST))
                                isHerbalistAvailable.set(assignment.isAvailable());
                        });
                    }
                    return new RegistrationResponseDto(
                            existingRegistration.getValue(),
                            RegistrationStatus.PREVIOUSLY_REGISTERED,
                            isApothecaryAvailable,
                            isHerbalistAvailable);
                })
                .orElseGet(() -> {
                    RegistrationEntity newRegistration = new RegistrationEntity();
                    newRegistration.setValue(registration);

                    registrationRepository.save(newRegistration);
                    return new RegistrationResponseDto(newRegistration.getValue(), RegistrationStatus.REGISTERED, new AtomicBoolean(true), new AtomicBoolean(true));
                });
    }

    public Long verifyRegistration(String registration) {
        return registrationRepository.findByValue(registration)
                .map(RegistrationEntity::getId)
                .orElseThrow(() -> new RegistrationNotFoundException("Registration not found for: " + registration));
    }

    private Optional<RoleplayEntity> findAssignedRole(RoleSelectionRequestDto roleSelectionDto) {
        return roleplayRepository
                .findByRegistration_Value(roleSelectionDto.registration())
                .stream()
                .filter(assignment -> assignment.getRoleName().equals(roleSelectionDto.selectedRole()))
                .findFirst();
    }

    public RoleplayEntity assignRoleToRegistration(@Valid RoleSelectionRequestDto roleSelectionDto) {
        Long registrationId = verifyRegistration(roleSelectionDto.registration());
        RegistrationEntity signedRegistration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new RegistrationNotFoundException("Registration not found for: " + registrationId));

        Optional<RoleplayEntity> existingAssignment = findAssignedRole(roleSelectionDto);

        if (existingAssignment.isPresent()) {
            RoleplayEntity assignment = existingAssignment.get();
            if (!assignment.isAvailable()) {
                throw new IllegalStateException("Role is already assigned and unavailable: " + roleSelectionDto.selectedRole());
            }
            assignment.setIsAvailable(false);
            return roleplayRepository.save(assignment);
        }

        RoleplayEntity newAssignment = new RoleplayEntity(signedRegistration, roleSelectionDto.selectedRole(), false);
        return roleplayRepository.save(newAssignment);
    }

    public boolean toggleRoleAvailability(@Valid RoleSelectionRequestDto roleSelectionDto) {
        return findAssignedRole(roleSelectionDto)
                .map(assignment -> {
                    assignment.setIsAvailable(!assignment.isAvailable());
                    roleplayRepository.save(assignment);
                    return true;
                })
                .orElse(false);
    }
}