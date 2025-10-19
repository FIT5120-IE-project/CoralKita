package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Trend Data Entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Trend {
    
    /**
     * Primary Key ID
     */
    private Long id;
    
    /**
     * Island Name
     */
    private String island;
    
    /**
     * Date
     */
    private LocalDate date;
    
    /**
     * LCC Value
     */
    private Double lcc;
    
    /**
     * OT Value
     */
    private Double ot;
    
    /**
     * AS Value
     */
    private Double as;
    
    /**
     * SD Value
     */
    private Double sd;
    
    /**
     * DI Value
     */
    private Double di;
    
    /**
     * PI Value
     */
    private Double pi;
}
