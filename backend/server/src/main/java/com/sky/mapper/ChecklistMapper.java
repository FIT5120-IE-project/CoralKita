package com.sky.mapper;

import com.sky.entity.Checklist;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 旅行清单数据访问层
 */
@Mapper
public interface ChecklistMapper {

    /**
     * 根据用户ID查询清单记录
     * @param userId 用户ID
     * @return 清单记录
     */
    Checklist getByUserId(@Param("userId") Integer userId);

    /**
     * 插入新的清单记录
     * @param checklist 清单记录
     */
    void insert(Checklist checklist);

    /**
     * 更新清单记录
     * @param checklist 清单记录
     */
    void update(Checklist checklist);
}
