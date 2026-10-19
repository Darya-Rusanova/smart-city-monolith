package ru.isu.monolith.modules.notification.enums;

public enum NotificationChannel {
    IN_APP("В приложении"),
    EMAIL("Email"),
    SMS("SMS");

    private final String displayName;

    NotificationChannel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
