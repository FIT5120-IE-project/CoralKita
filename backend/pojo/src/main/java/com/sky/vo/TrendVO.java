package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 趋势数据返回VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrendVO {
    
    /**
     * 日期
     */
    private LocalDate date;
    
    /**
     * LCC值
     */
    private Double LCC;
    
    /**
     * OT值
     */
    private Double OT;
    
    /**
     * AS值
     */
    private Double AS;
    
    /**
     * SD值
     */
    private Double SD;
    
    /**
     * DI值
     */
    private Double DI;
    
    /**
     * PI值
     */
    private Double PI;
}
