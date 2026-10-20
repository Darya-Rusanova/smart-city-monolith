package ru.isu.monolith.modules.utility.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.isu.monolith.modules.utility.enums.IssueStatus;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class IssueStatusUpdateDto {

    @NotNull(message = "Укажите новый статус")
    private IssueStatus status;
}
