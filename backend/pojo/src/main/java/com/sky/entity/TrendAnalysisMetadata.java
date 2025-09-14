package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 趋势分析元数据实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrendAnalysisMetadata implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 岛屿名称
     */
    private String island;

    /**
     * 州/省
     */
    private String state;

    /**
     * 日期
     */
    private String date;

    /**
     * LCC值（Live Coral Cover）
     */
    private String lcc;

    /**
     * OT值（Other）
     */
    private String ot;

    /**
     * AS值（Algae/Sand）
     */
    private String as;

    /**
     * SD值（Sand）
     */
    private String sd;

    /**
     * DI值（Disease）
     */
    private String di;

    /**
     * PI值（Physical Impact）
     */
    private String pi;

    /**
     * 未知列
     */
    private String myUnknownColumn;
}
