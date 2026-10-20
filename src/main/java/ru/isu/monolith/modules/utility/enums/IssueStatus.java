package ru.isu.monolith.modules.utility.enums;

public enum IssueStatus {
    NEW("Новая"),
    IN_PROGRESS("В работе"),
    RESOLVED("Решена"),
    REJECTED("Отклонена");

    private final String displayName;

    IssueStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}