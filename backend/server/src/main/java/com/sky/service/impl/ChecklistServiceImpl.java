package com.sky.service.impl;

import com.sky.entity.Checklist;
import com.sky.mapper.ChecklistMapper;
import com.sky.service.ChecklistService;
import com.sky.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 旅行清单业务层实现类
 */
@Service
@Slf4j
public class ChecklistServiceImpl implements ChecklistService {

    @Autowired
    private ChecklistMapper checklistMapper;
    
    @Autowired
    private UserService userService;

    /**
     * 根据用户ID获取或创建清单记录
     * @param userId 用户ID
     * @return 清单记录
     */
    @Override
    public Checklist getOrCreateChecklist(Integer userId) {
        // 根据用户ID查询现有记录
        Checklist checklist = checklistMapper.getByUserId(userId);
        
        // 如果记录不存在，创建新记录
        if (checklist == null) {
            checklist = Checklist.builder()
                    .userId(userId)
                    .q1(0).q2(0).q3(0).q4(0).q5(0)
                    .q6(0).q7(0).q8(0).q9(0).q10(0)
                    .q11(0).q12(0).q13(0).q14(0).q15(0)
                    .q16(0).q17(0)
                    .build();
            
            // 插入新记录
            checklistMapper.insert(checklist);
            log.info("为用户 {} 创建了新的清单记录", userId);
        }
        
        return checklist;
    }

    /**
     * 更新用户清单记录
     * @param checklist 清单记录
     * @return 更新后的清单记录
     */
    @Override
    public Checklist updateChecklist(Checklist checklist) {
        // 检查用户记录是否存在，如果不存在则创建
        Checklist existingChecklist = checklistMapper.getByUserId(checklist.getUserId());
        
        if (existingChecklist == null) {
            // 如果记录不存在，先创建新记录
            checklistMapper.insert(checklist);
            log.info("为用户 {} 创建了新的清单记录", checklist.getUserId());
        } else {
            // 如果记录存在，更新记录
            checklistMapper.update(checklist);
            log.info("更新了用户 {} 的清单记录", checklist.getUserId());
        }
        
        return checklist;
    }
    
    /**
     * 检查并更新用户徽章
     * @param checklist 清单数据
     */
    @Override
    public void checkAndUpdateUserBadge(Checklist checklist) {
        // 检查所有q值是否都是1
        if (checklist.getQ1() == 1 && checklist.getQ2() == 1 && checklist.getQ3() == 1 &&
            checklist.getQ4() == 1 && checklist.getQ5() == 1 && checklist.getQ6() == 1 &&
            checklist.getQ7() == 1 && checklist.getQ8() == 1 && checklist.getQ9() == 1 &&
            checklist.getQ10() == 1 && checklist.getQ11() == 1 && checklist.getQ12() == 1 &&
            checklist.getQ13() == 1 && checklist.getQ14() == 1 && checklist.getQ15() == 1 &&
            checklist.getQ16() == 1 && checklist.getQ17() == 1) {
            
            // 所有任务都完成了，授予"Coral Guardian"徽章
            userService.updateUserBadge(checklist.getUserId(), "Coral Guardian");
            log.info("用户 {} 完成了所有清单任务，授予Coral Guardian徽章", checklist.getUserId());
        }
    }
}
