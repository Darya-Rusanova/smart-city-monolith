package ru.isu.monolith.modules.notification.dto;

import lombok.*;
import ru.isu.monolith.modules.notification.enums.NotificationChannel;
import ru.isu.monolith.modules.notification.enums.NotificationStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class NotificationResponseDto {
    private Integer id;
    private Integer userId;
    private NotificationChannel channel;
    private String nameChannel;
    private String title;
    private String message;
    private NotificationStatus status;
    private String nameStatus;
    private LocalDateTime createdAt;
}
