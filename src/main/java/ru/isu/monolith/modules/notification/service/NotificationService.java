package ru.isu.monolith.modules.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.isu.monolith.modules.notification.dto.NotificationRequestDto;
import ru.isu.monolith.modules.notification.dto.NotificationResponseDto;
import ru.isu.monolith.modules.notification.enums.NotificationChannel;
import ru.isu.monolith.modules.notification.enums.NotificationStatus;
import ru.isu.monolith.modules.notification.model.Notification;
import ru.isu.monolith.modules.notification.repository.NotificationRepository;

import java.util.List;

@Service
@Slf4j
public class NotificationService {
    private static final int MY_NOTIFICATIONS_LIMIT = 50;

    @Autowired
    private NotificationRepository notificationRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public NotificationResponseDto create(NotificationRequestDto request) {
        NotificationChannel channel;
        try {
            channel = request.getChannel() != null
                    ? NotificationChannel.valueOf(request.getChannel())
                    : NotificationChannel.IN_APP;
        } catch (IllegalArgumentException e) {
            channel = NotificationChannel.IN_APP;
        }

        Notification notification = Notification.builder()
                .userId(request.getUserId())
                .channel(channel)
                .title(request.getTitle())
                .message(request.getMessage())
                .status(NotificationStatus.SENT)
                .build();

        Notification saved = notificationRepository.save(notification);
        log.info("Уведомление отправлено пользователю {}", saved.getUserId());
        return toDto(saved);
    }

    public List<NotificationResponseDto> getMy(Integer userId) {
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(
                userId, PageRequest.of(0, MY_NOTIFICATIONS_LIMIT));
        log.info("Запрос уведомлений пользователем {}: {} шт.", userId, notifications.size());
        return notifications.stream().map(this::toDto).toList();
    }

    private NotificationResponseDto toDto(Notification n) {
        return NotificationResponseDto.builder()
                .id(n.getId())
                .userId(n.getUserId())
                .channel(n.getChannel())
                .nameChannel(n.getChannel().getDisplayName())
                .title(n.getTitle())
                .message(n.getMessage())
                .status(n.getStatus())
                .nameStatus(n.getStatus().getDisplayName())
                .createdAt(n.getCreatedAt())
                .build();
    }
}
