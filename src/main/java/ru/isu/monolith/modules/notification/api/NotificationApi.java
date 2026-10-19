package ru.isu.monolith.modules.notification.api;

public interface NotificationApi {
    void send(Integer userId, String channel, String title, String message);
}
