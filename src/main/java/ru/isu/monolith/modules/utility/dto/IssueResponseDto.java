package ru.isu.monolith.modules.utility.dto;

import lombok.*;
import ru.isu.monolith.modules.utility.enums.IssueCategory;
import ru.isu.monolith.modules.utility.enums.IssueStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class IssueResponseDto {
    private Integer id;
    private Integer userId;
    private IssueCategory category;
    private String nameCategory;
    private String address;
    private IssueStatus status;
    private String nameStatus;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
