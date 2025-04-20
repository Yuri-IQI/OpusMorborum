package org.aegrotatio.types.dtos.responses;

import org.aegrotatio.types.enums.RequestLevelEnum;

public class CustomerReqResponseDto {

    private Long customerRequestId;
    private String title;
    private String description;
    private RequestLevelEnum level;
    private float baseReward;
    private boolean isActive;
    private String author;

    public CustomerReqResponseDto(Long customerRequestId, String title, String description, RequestLevelEnum level, float baseReward, boolean isActive, String author) {
        this.customerRequestId = customerRequestId;
        this.title = title;
        this.description = description;
        this.level = level;
        this.baseReward = baseReward;
        this.isActive = isActive;
        this.author = author;
    }

    public Long getCustomerRequestId() {
        return customerRequestId;
    }

    public void setCustomerRequestId(Long customerRequestId) {
        this.customerRequestId = customerRequestId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RequestLevelEnum getLevel() {
        return level;
    }

    public void setLevel(RequestLevelEnum level) {
        this.level = level;
    }

    public float getBaseReward() {
        return baseReward;
    }

    public void setBaseReward(float baseReward) {
        this.baseReward = baseReward;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}