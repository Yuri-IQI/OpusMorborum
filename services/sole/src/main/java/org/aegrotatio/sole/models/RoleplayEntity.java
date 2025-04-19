package org.aegrotatio.sole.models;

import jakarta.persistence.*;
import org.aegrotatio.sole.types.enums.AvailableRoles;

@Entity
@Table(name = "tb_roleplay")
public class RoleplayEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_registration", nullable = false)
    private RegistrationEntity registration;

    @Enumerated(EnumType.STRING)
    @Column(name = "selected_role", nullable = false)
    private AvailableRoles roleName;

    @Column(name = "is_available", nullable = false)
    private boolean isAvailable;

    public RoleplayEntity() {}

    public RoleplayEntity(RegistrationEntity registration, AvailableRoles roleName, boolean isAvailable) {
        this.registration = registration;
        this.roleName = roleName;
        this.isAvailable = isAvailable;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RegistrationEntity getRegistration() {
        return registration;
    }

    public void setRegistration(RegistrationEntity registration) {
        this.registration = registration;
    }

    public AvailableRoles getRoleName() {
        return roleName;
    }

    public void setRoleName(AvailableRoles roleName) {
        this.roleName = roleName;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
