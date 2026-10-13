package ru.isu.monolith.modules.utility.model;

import jakarta.persistence.*;
import lombok.*;
import ru.isu.monolith.modules.utility.enums.IssueCategory;
import ru.isu.monolith.modules.utility.enums.IssueStatus;

import java.time.LocalDateTime;

@Entity
@Table(name = "utility_issue", indexes = {
        @Index(name = "idx_utility_issue_user", columnList = "user_id"),
        @Index(name = "idx_utility_issue_status", columnList = "status"),
        @Index(name = "idx_utility_issue_category", columnList = "category")
})
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Issue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "user_id",nullable = false)
    private Integer userId;
    @Enumerated(EnumType.STRING)
    private IssueCategory category;
    private String address;
    @Enumerated(EnumType.STRING)
    private IssueStatus status = IssueStatus.NEW;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
