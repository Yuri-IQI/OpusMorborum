package org.aegrotatio.sole.controllers;

import jakarta.validation.Valid;
import org.aegrotatio.sole.models.RoleplayEntity;
import org.aegrotatio.sole.services.RegistrationService;
import org.aegrotatio.sole.types.dtos.requests.AccessCodesRequestDto;
import org.aegrotatio.sole.types.dtos.requests.RoleSelectionRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/access")
public class AccessController {

    private final RegistrationService registrationService;

    @Autowired
    public AccessController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<?> createGame(@RequestBody @Valid AccessCodesRequestDto accessCodesDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(registrationService.registerGame(accessCodesDto));
    }

    @PostMapping("/roles")
    public ResponseEntity<?> selectRole(@RequestBody @Valid RoleSelectionRequestDto roleSelectionDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(registrationService.assignRoleToRegistration(roleSelectionDto));
    }

    @PutMapping("/roles/update")
    public ResponseEntity<Map<String, String>> updateRoleAvailability(@RequestBody @Valid RoleSelectionRequestDto roleSelectionDto) {
        boolean updated = registrationService.toggleRoleAvailability(roleSelectionDto);
        Map<String, String> response = new HashMap<>();
        response.put("message", updated ? "Role updated successfully" : "Registration not found or role update failed");

        return ResponseEntity.status(updated ? HttpStatus.ACCEPTED : HttpStatus.NOT_FOUND).body(response);
    }
}