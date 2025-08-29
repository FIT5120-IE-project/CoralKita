package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 趋势数据实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trend {
    
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 岛屿名称
     */
    private String island;
    
    /**
     * 日期
     */
    private LocalDate date;
    
    /**
     * LCC值
     */
    private Double lcc;
    
    /**
     * OT值
     */
    private Double ot;
    
    /**
     * AS值
     */
    private Double as;
    
    /**
     * SD值
     */
    private Double sd;
    
    /**
     * DI值
     */
    private Double di;
    
    /**
     * PI值
     */
    private Double pi;
}
