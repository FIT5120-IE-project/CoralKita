package com.sky.service;

import com.sky.dto.RagQueryDTO;
import com.sky.vo.RagAnswerVO;

/**
 * RAG问答服务接口
 */
public interface RagService {
    
    /**
     * 处理RAG问答请求
     * @param queryDTO 查询请求
     * @return 问答结果
     */
    RagAnswerVO query(RagQueryDTO queryDTO);
    
    /**
     * 检查RAG系统状态
     * @return 系统是否可用
     */
    boolean isSystemReady();
}
