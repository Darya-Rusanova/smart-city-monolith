package ru.isu.monolith.modules.utility.event;

public record IssueStatusUpdatedEvent(Integer issueId, Integer ownerId, String newStatusName) {
}
