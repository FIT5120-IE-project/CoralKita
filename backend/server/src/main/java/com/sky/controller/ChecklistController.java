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
 * 旅行清单管理
 */
@RestController
@RequestMapping("/checklist")
@Api(tags = "旅行清单相关接口")
@Slf4j
public class ChecklistController {

    @Autowired
    private ChecklistService checklistService;

    /**
     * 根据用户ID获取或创建清单记录
     * @param userId 用户ID
     * @return 清单记录
     */
    @GetMapping("/{userId}")
    @ApiOperation("根据用户ID获取或创建清单记录")
    public Result<Checklist> getOrCreateChecklist(@PathVariable Integer userId) {
        log.info("获取用户 {} 的清单记录", userId);
        
        Checklist checklist = checklistService.getOrCreateChecklist(userId);
        return Result.success(checklist);
    }

    /**
     * 提交用户清单记录
     * @param checklist 清单记录
     * @return 更新后的清单记录
     */
    @PostMapping("/submit")
    @ApiOperation("提交用户清单记录")
    public Result<Checklist> submitChecklist(@RequestBody Checklist checklist) {
        log.info("提交用户 {} 的清单记录", checklist.getUserId());
        
        Checklist updatedChecklist = checklistService.updateChecklist(checklist);
        
        // 检查并更新用户徽章
        checklistService.checkAndUpdateUserBadge(updatedChecklist);
        
        return Result.success(updatedChecklist);
    }
}
