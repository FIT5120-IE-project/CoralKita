package com.sky.mapper;

import com.sky.entity.Checklist;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * Travel Checklist Data Access Layer
 */
@Mapper
public interface ChecklistMapper {

    /**
     * Query checklist record by user ID
     * @param userId User ID
     * @return Checklist record
     */
    Checklist getByUserId(@Param("userId") Integer userId);

    /**
     * Insert new checklist record
     * @param checklist Checklist record
     */
    void insert(Checklist checklist);

    /**
     * Update checklist record
     * @param checklist Checklist record
     */
    void update(Checklist checklist);
}
