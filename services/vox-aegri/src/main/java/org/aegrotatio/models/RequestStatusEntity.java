package org.aegrotatio.models;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.aegrotatio.types.enums.RequestStatusEnum;

@Entity
@Table(name = "tb_request_status")
public class RequestStatusEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false)
    public Long registrationId;

    @ManyToOne
    @JoinColumn(name = "id_request", nullable = false)
    public CustomerRequestEntity customerRequestEntity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public RequestStatusEnum currentStatus;

    public RequestStatusEntity(Long registrationId, CustomerRequestEntity customerRequestEntity, RequestStatusEnum currentStatus) {
        this.registrationId = registrationId;
        this.customerRequestEntity = customerRequestEntity;
        this.currentStatus = currentStatus;
    }

    public RequestStatusEntity() {

    }
}