package org.aegrotatio.models;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.aegrotatio.types.dtos.requests.CustomerIssueRequestDto;
import org.aegrotatio.types.enums.RequestLevelEnum;

@Entity
@Table(name = "tb_patient_request")
public class CustomerRequestEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(length = 32, unique = true, nullable = false)
    public String title;

    @Column(length = 240, nullable = false)
    public String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public RequestLevelEnum level;

    @Column(nullable = false, precision = 2)
    public float baseReward;

    @Column(nullable = false)
    public boolean isActive;

    @Column(length = 40, nullable = false)
    public String author;

    public CustomerRequestEntity(CustomerIssueRequestDto customerIssueRequestDto) {
        this.title = customerIssueRequestDto.title();
        this.description = customerIssueRequestDto.description();
        this.level = customerIssueRequestDto.level();
        this.baseReward = customerIssueRequestDto.baseReward();
        this.isActive = customerIssueRequestDto.isActive();
        this.author = customerIssueRequestDto.author();
    }

    public CustomerRequestEntity() {}
}