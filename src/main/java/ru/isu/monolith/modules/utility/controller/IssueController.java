package ru.isu.monolith.modules.utility.controller;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.isu.monolith.modules.utility.dto.IssueRequestDto;
import ru.isu.monolith.modules.utility.dto.IssueResponseDto;
import ru.isu.monolith.modules.utility.dto.IssueStatusUpdateDto;
import ru.isu.monolith.modules.utility.enums.IssueCategory;
import ru.isu.monolith.modules.utility.enums.IssueStatus;
import ru.isu.monolith.modules.utility.service.IssueService;

import java.util.List;

@RestController
@RequestMapping("/issues")
public class IssueController {
    @Autowired
    private IssueService issueService;
    @PostMapping
    public ResponseEntity<IssueResponseDto> createIssue(Authentication authentication,
                                                        @Valid @RequestBody IssueRequestDto request){
        Integer userId = Integer.parseInt(authentication.getName());
        IssueResponseDto response = issueService.createIssue(request,userId);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<List<IssueResponseDto>> getAllIssues(Authentication authentication,
                                                               @RequestParam(required = false) IssueStatus status,
                                                               @RequestParam(required = false) IssueCategory type,
                                                               @RequestParam(defaultValue = "id") String sortBy,
                                                               @RequestParam(defaultValue = "asc") String dir){
        Integer userId = Integer.parseInt(authentication.getName());
        boolean isAdmin = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        List<IssueResponseDto> issues = issueService.getAllIssues(userId,status,type,sortBy,dir,isAdmin);
        return ResponseEntity.ok(issues);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IssueResponseDto> updateIssueStatus(@PathVariable Integer id,
                                                              @Valid @RequestBody IssueStatusUpdateDto request,
                                                              Authentication authentication){
        Integer userId = Integer.parseInt(authentication.getName());
        IssueResponseDto response = issueService.updateIssueStatus(id,request.getStatus(),userId);
        return ResponseEntity.ok(response);
    }
}
