package org.aegrotatio.sole.repositories;

import org.aegrotatio.sole.models.RegistrationEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RegistrationRepository extends CrudRepository<RegistrationEntity, Long> {
    Optional<RegistrationEntity> findByValue(String value);
}
