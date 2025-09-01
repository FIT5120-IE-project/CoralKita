package com.sky.service;

import com.sky.entity.Checklist;

/**
 * 旅行清单业务层接口
 */
public interface ChecklistService {

    /**
     * 根据用户ID获取或创建清单记录
     * @param userId 用户ID
     * @return 清单记录
     */
    Checklist getOrCreateChecklist(Integer userId);

    /**
     * 更新用户清单记录
     * @param checklist 清单记录
     * @return 更新后的清单记录
     */
    Checklist updateChecklist(Checklist checklist);
    
    /**
     * 检查并更新用户徽章
     * @param checklist 清单数据
     */
    void checkAndUpdateUserBadge(Checklist checklist);
}
