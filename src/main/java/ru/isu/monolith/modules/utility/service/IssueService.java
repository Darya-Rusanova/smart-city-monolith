package ru.isu.monolith.modules.utility.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.domain.Sort;
import ru.isu.monolith.modules.utility.dto.IssueRequestDto;
import ru.isu.monolith.modules.utility.dto.IssueResponseDto;
import ru.isu.monolith.modules.utility.enums.IssueCategory;
import ru.isu.monolith.modules.utility.enums.IssueStatus;
import ru.isu.monolith.modules.utility.event.IssueStatusUpdatedEvent;
import ru.isu.monolith.shared.exception.BadRequestException;
import ru.isu.monolith.shared.exception.ConflictException;
import ru.isu.monolith.shared.exception.NotFoundException;
import ru.isu.monolith.modules.utility.model.Issue;
import ru.isu.monolith.modules.utility.repository.IssueRepository;
import org.springframework.context.ApplicationEventPublisher;
import ru.isu.monolith.modules.utility.event.IssueCreatedEvent;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class IssueService {
    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private ApplicationEventPublisher events;

    private static final List<String> SORT_FIELDS =
            List.of("id", "createdAt", "updatedAt", "status", "category");
    private Sort buildSort(String sortBy, String dir) {
        if (!SORT_FIELDS.contains(sortBy)) {
            throw new BadRequestException("Недопустимое поле сортировки: " + sortBy + ". Допустимые: " + SORT_FIELDS);
        }
        if (!"asc".equalsIgnoreCase(dir) && !"desc".equalsIgnoreCase(dir)) {
            throw new BadRequestException("Направление сортировки должно быть asc или desc");
        }
        return Sort.by(Sort.Direction.fromString(dir), sortBy);
    }

    @Transactional
    public IssueResponseDto createIssue(IssueRequestDto request, Integer userId){
        Issue issue= Issue.builder()
                .userId(userId)
                .category(request.getCategory())
                .address(request.getAddress())
                .description(request.getDescription())
                .status(IssueStatus.NEW)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Issue saved = issueRepository.save(issue);
        events.publishEvent(new IssueCreatedEvent(saved.getId(), userId, saved.getDescription()));
        log.info("Создана заявка №{}",saved.getId());
        return toDto(saved);
    }


    public List<IssueResponseDto> getAllIssues(Integer userId, IssueStatus status, IssueCategory type, String sortBy, String dir, boolean isAdmin){
        Specification<Issue> spec = (root, query, cb) -> cb.conjunction();
        if(!isAdmin)
            spec = spec.and((root, query, cb) -> cb.equal(root.get("userId"), userId));
        if (type != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("category"), type));
        }
        if (status != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("status"), status));
        }
        Sort sort = buildSort(sortBy, dir);

        List<Issue> issues = issueRepository.findAll(spec, sort);

        return issues.stream().map(this::toDto).toList();
    }

    @Transactional
    public IssueResponseDto updateIssueStatus(Integer id, IssueStatus newStatus,Integer userId){
        log.info("Обновление заявки пользователем {}",userId);
        Issue issue = issueRepository.findById(id).orElse(null);
        if (issue != null) {
            IssueStatus old = issue.getStatus();
            if(old.equals(IssueStatus.RESOLVED)){
                log.error("Ошибка: статус обновляемой заявки уже решен");
                throw new ConflictException("Нельзя сменить статус решенной заявки");
            }
            issue.setStatus(newStatus);
            issue.setUpdatedAt(LocalDateTime.now());
            Issue saved = issueRepository.save(issue);
            log.info("Статус заявки №{} сменился с {} на {}", id,old.getDisplayName(),saved.getStatus().getDisplayName());
            events.publishEvent(new IssueStatusUpdatedEvent(saved.getId(), userId, saved.getStatus()));

            return toDto(saved);
        }
        else{
            log.error("Ошибка: заявка с id={} не найдена",id);
            throw new NotFoundException("Заявка с id="+id+" не найдена");
        }
    }

    private IssueResponseDto toDto(Issue saved){
        IssueResponseDto response = IssueResponseDto.builder()
                .id(saved.getId())
                .userId(saved.getUserId())
                .category(saved.getCategory())
                .nameCategory(saved.getCategory().getDisplayName())
                .address(saved.getAddress())
                .status(saved.getStatus())
                .nameStatus(saved.getStatus().getDisplayName())
                .description(saved.getDescription())
                .createdAt(saved.getCreatedAt())
                .updatedAt(saved.getUpdatedAt())
                .build();
        return response;
    }
}
