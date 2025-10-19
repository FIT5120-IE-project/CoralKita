package com.sky.service.impl;

import com.sky.entity.Checklist;
import com.sky.mapper.ChecklistMapper;
import com.sky.service.ChecklistService;
import com.sky.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Travel Checklist Service Implementation
 */
@Service
@Slf4j
public class ChecklistServiceImpl implements ChecklistService {

    @Autowired
    private ChecklistMapper checklistMapper;
    
    @Autowired
    private UserService userService;

    /**
     * Get or create checklist record by user ID
     * @param userId User ID
     * @return Checklist record
     */
    @Override
    public Checklist getOrCreateChecklist(Integer userId) {
        // Query existing record by user ID
        Checklist checklist = checklistMapper.getByUserId(userId);
        
        // If record doesn't exist, create new record
        if (checklist == null) {
            checklist = Checklist.builder()
                    .userId(userId)
                    .q1(0).q2(0).q3(0).q4(0).q5(0)
                    .q6(0).q7(0).q8(0).q9(0).q10(0)
                    .q11(0).q12(0).q13(0).q14(0).q15(0)
                    .q16(0).q17(0)
                    .build();
            
            // Insert new record
            checklistMapper.insert(checklist);
            log.info("Created new checklist record for user {}", userId);
        }
        
        return checklist;
    }

    /**
     * Update user checklist record
     * @param checklist Checklist record
     * @return Updated checklist record
     */
    @Override
    public Checklist updateChecklist(Checklist checklist) {
        // Check if user record exists, create if not
        Checklist existingChecklist = checklistMapper.getByUserId(checklist.getUserId());
        
        if (existingChecklist == null) {
            // If record doesn't exist, create new record first
            checklistMapper.insert(checklist);
            log.info("Created new checklist record for user {}", checklist.getUserId());
        } else {
            // If record exists, update record
            checklistMapper.update(checklist);
            log.info("Updated checklist record for user {}", checklist.getUserId());
        }
        
        return checklist;
    }
    
    /**
     * Check and update user badge
     * @param checklist Checklist data
     */
    @Override
    public void checkAndUpdateUserBadge(Checklist checklist) {
        // Check if all q values are 1
        if (checklist.getQ1() == 1 && checklist.getQ2() == 1 && checklist.getQ3() == 1 &&
            checklist.getQ4() == 1 && checklist.getQ5() == 1 && checklist.getQ6() == 1 &&
            checklist.getQ7() == 1 && checklist.getQ8() == 1 && checklist.getQ9() == 1 &&
            checklist.getQ10() == 1 && checklist.getQ11() == 1 && checklist.getQ12() == 1 &&
            checklist.getQ13() == 1 && checklist.getQ14() == 1 && checklist.getQ15() == 1 &&
            checklist.getQ16() == 1 && checklist.getQ17() == 1) {
            
            // All tasks completed, award "Coral Guardian" badge
            userService.updateUserBadge(checklist.getUserId(), "Coral Guardian");
            log.info("User {} completed all checklist tasks, awarded Coral Guardian badge", checklist.getUserId());
        }
    }
}
