package com.sky.service;

import com.sky.entity.Checklist;

/**
 * Travel Checklist Service Interface
 */
public interface ChecklistService {

    /**
     * Get or create checklist record by user ID
     * @param userId User ID
     * @return Checklist record
     */
    Checklist getOrCreateChecklist(Integer userId);

    /**
     * Update user checklist record
     * @param checklist Checklist record
     * @return Updated checklist record
     */
    Checklist updateChecklist(Checklist checklist);
    
    /**
     * Check and update user badge
     * @param checklist Checklist data
     */
    void checkAndUpdateUserBadge(Checklist checklist);
}
