package com.sky.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Trend Data Response VO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrendVO {
    
    /**
     * Date
     */
    private LocalDate date;
    
    /**
     * LCC Value
     */
    private Double LCC;
    
    /**
     * OT Value
     */
    private Double OT;
    
    /**
     * AS Value
     */
    private Double AS;
    
    /**
     * SD Value
     */
    private Double SD;
    
    /**
     * DI Value
     */
    private Double DI;
    
    /**
     * PI Value
     */
    private Double PI;
}
