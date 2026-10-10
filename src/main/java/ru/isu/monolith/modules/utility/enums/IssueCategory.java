package ru.isu.monolith.modules.utility.enums;

public enum IssueCategory {
    WATER("Водоснабжение"),
    ELECTRICITY("Электроснабжение"),
    ROAD("Дорожное покрытие"),
    OTHER("Другое");

    private final String displayName;

    IssueCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}