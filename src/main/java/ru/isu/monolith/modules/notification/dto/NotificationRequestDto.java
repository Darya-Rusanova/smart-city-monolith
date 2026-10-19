package ru.isu.monolith.modules.notification.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class NotificationRequestDto {

    @NotNull(message = "Укажите получателя")
    private Integer userId;

    private String channel;

    @NotBlank(message = "Укажите заголовок")
    @Size(max = 150, message = "Заголовок слишком длинный")
    private String title;

    @NotBlank(message = "Укажите текст сообщения")
    @Size(max = 2000, message = "Сообщение слишком длинное")
    private String message;
}
