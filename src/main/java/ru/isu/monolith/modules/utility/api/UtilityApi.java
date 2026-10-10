package ru.isu.monolith.modules.utility.api;
import ru.isu.monolith.modules.utility.dto.IssueResponseDto;

public interface UtilityApi {
    IssueResponseDto getIssueById(Integer id);
}
