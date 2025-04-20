package org.aegrotatio.services;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import org.aegrotatio.kafka.Communicator;
import org.aegrotatio.models.RequestStatusEntity;
import org.aegrotatio.types.dtos.mappers.RequestStatusMapper;
import org.aegrotatio.types.dtos.requests.FilterRequestDto;
import org.aegrotatio.types.dtos.responses.ReqStatusResponseDto;
import org.aegrotatio.types.enums.RequestLevelEnum;
import org.aegrotatio.types.enums.RequestStatusEnum;

import java.util.List;

@ApplicationScoped
public class FilterService {

    @Inject
    Communicator communicator;

    @Inject
    RequestStatusMapper requestStatusMapper;

    public Uni<List<ReqStatusResponseDto>> getAllRequestsByFilter(@Valid FilterRequestDto filterRequestDto) {
        return communicator.askForRegistrationId(filterRequestDto.registration())
                .flatMap(registrationId -> applyFilter(registrationId, filterRequestDto))
                .map(this::mapToResponse);
    }

    private Uni<List<RequestStatusEntity>> applyFilter(Long registrationId, FilterRequestDto filterRequestDto) {
        return switch (filterRequestDto.criteria()) {
            case STATUS -> getAllRequestStatuses(registrationId, parseEnum(RequestStatusEnum.class, filterRequestDto.value()));
            case AUTHOR -> getAllRequestsByAuthor(registrationId, filterRequestDto.value());
            case LEVEL -> getAllRequestsByLevel(registrationId, RequestLevelEnum.fromLevel(Integer.parseInt(filterRequestDto.value())));
            case TITLE -> getAllRequestsByTitle(registrationId, filterRequestDto.value());
            case BASE_REWARD -> getAllRequestsByBaseReward(registrationId, parseInteger(filterRequestDto.value()));
            default -> Uni.createFrom().failure(new IllegalArgumentException("Unsupported filter criteria: " + filterRequestDto.criteria()));
        };
    }

    private <E extends Enum<E>> E parseEnum(Class<E> enumClass, String value) {
        try {
            return Enum.valueOf(enumClass, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid enum value: " + value);
        }
    }

    private int parseInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid integer value: " + value);
        }
    }

    @WithSession
    public Uni<List<RequestStatusEntity>> getAllRequestStatuses(Long registrationId, RequestStatusEnum requestStatusEnum) {
        return RequestStatusEntity.find("registrationId = ?1 AND currentStatus = ?2", registrationId, requestStatusEnum).list();
    }

    @WithSession
    public Uni<List<RequestStatusEntity>> getAllRequestsByAuthor(Long registrationId, String author) {
        return RequestStatusEntity.find("registrationId = ?1 AND customerRequest.author ILIKE ?2", registrationId, "%" + author + "%").list();
    }

    @WithSession
    public Uni<List<RequestStatusEntity>> getAllRequestsByLevel(Long registrationId, RequestLevelEnum requestLevel) {
        return RequestStatusEntity.find("registrationId = ?1 AND customerRequest.level = ?2", registrationId, requestLevel).list();
    }

    @WithSession
    public Uni<List<RequestStatusEntity>> getAllRequestsByTitle(Long registrationId, String title) {
        return RequestStatusEntity.find("registrationId = ?1 AND customerRequest.title ILIKE ?2", registrationId, "%" + title + "%").list();
    }

    @WithSession
    public Uni<List<RequestStatusEntity>> getAllRequestsByBaseReward(Long registrationId, int baseReward) {
        return RequestStatusEntity.find("registrationId = ?1 AND customerRequest.baseReward BETWEEN ?2 AND ?3",
                registrationId, baseReward - 200, baseReward + 200).list();
    }

    private List<ReqStatusResponseDto> mapToResponse(List<RequestStatusEntity> requestStatuses) {
        return requestStatuses.stream().map(requestStatusMapper::toResponse).toList();
    }
}