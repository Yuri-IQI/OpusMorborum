package org.aegrotatio.sole.repositories;

import org.aegrotatio.sole.models.RoleplayEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoleplayRepository extends CrudRepository<RoleplayEntity, Long> {
    List<RoleplayEntity> findByRegistration_Value(String registrationValue);
}
