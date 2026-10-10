package ru.isu.monolith.modules.utility.event;

import ru.isu.monolith.modules.utility.enums.IssueStatus;

public record IssueStatusUpdatedEvent(Integer issueId, Integer userId, IssueStatus newStatus) {
}
