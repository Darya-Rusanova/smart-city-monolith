package ru.isu.monolith.modules.utility.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.isu.monolith.modules.utility.enums.IssueCategory;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class IssueRequestDto {
    @NotNull(message = "Выберите категорию")
    private IssueCategory category;

    @NotBlank(message = "Опишите проблему")
    @Size(max=2000,message="Описание слишком длинное")
    private String description;

    @NotBlank(message = "Укажите адрес")
    @Size(max = 200,message = "Адрес слишком длинный")
    private String address;
}
