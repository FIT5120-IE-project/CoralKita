package com.sky.controller;

import com.sky.entity.Checklist;
import com.sky.result.Result;
import com.sky.service.ChecklistService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Travel Checklist Management Controller
 */
@RestController
@RequestMapping("/checklist")
@Api(tags = "Travel Checklist Related APIs")
@Slf4j
public class ChecklistController {

    @Autowired
    private ChecklistService checklistService;

    /**
     * Get or create checklist record by user ID
     * @param userId User ID
     * @return Checklist record
     */
    @GetMapping("/{userId}")
    @ApiOperation("Get or create checklist record by user ID")
    public Result<Checklist> getOrCreateChecklist(@PathVariable Integer userId) {
        log.info("Getting checklist record for user {}", userId);
        
        Checklist checklist = checklistService.getOrCreateChecklist(userId);
        return Result.success(checklist);
    }

    /**
     * Submit user checklist record
     * @param checklist Checklist record
     * @return Updated checklist record
     */
    @PostMapping("/submit")
    @ApiOperation("Submit user checklist record")
    public Result<Checklist> submitChecklist(@RequestBody Checklist checklist) {
        log.info("Submitting checklist record for user {}", checklist.getUserId());
        
        Checklist updatedChecklist = checklistService.updateChecklist(checklist);
        
        // Check and update user badge
        checklistService.checkAndUpdateUserBadge(updatedChecklist);
        
        return Result.success(updatedChecklist);
    }
}
