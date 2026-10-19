package ru.isu.monolith.modules.notification.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.isu.monolith.modules.notification.api.NotificationApi;
import ru.isu.monolith.modules.notification.dto.NotificationRequestDto;

@Service
public class NotificationApiImpl implements NotificationApi {
    @Autowired
    private NotificationService notificationService;

    @Override
    public void send(Integer userId, String channel, String title, String message) {
        NotificationRequestDto request = NotificationRequestDto.builder()
                .userId(userId)
                .channel(channel)
                .title(title)
                .message(message)
                .build();
        notificationService.create(request);
    }
}
