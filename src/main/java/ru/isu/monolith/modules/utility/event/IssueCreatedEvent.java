package ru.isu.monolith.modules.utility.event;

public record IssueCreatedEvent(Integer issueId, Integer userId, String description){}
