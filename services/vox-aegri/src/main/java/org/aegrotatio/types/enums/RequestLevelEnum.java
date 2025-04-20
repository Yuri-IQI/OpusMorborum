package org.aegrotatio.types.enums;

public enum RequestLevelEnum {
    LOW(1), LOW_TO_MEDIUM(2), MEDIUM(3), MEDIUM_TO_HIGH(4), HIGH(5);

    private final int level;

    RequestLevelEnum(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public static RequestLevelEnum fromLevel(int level) {
        for (RequestLevelEnum reqLevel : RequestLevelEnum.values()) {
            if (reqLevel.getLevel() == level) {
                return reqLevel;
            }
        }
        throw new IllegalArgumentException("Invalid request level: " + level);
    }
}