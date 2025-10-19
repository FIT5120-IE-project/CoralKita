package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Trend Analysis Metadata Entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrendAnalysisMetadata implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Island Name
     */
    private String island;

    /**
     * State/Province
     */
    private String state;

    /**
     * Date
     */
    private String date;

    /**
     * LCC Value (Live Coral Cover)
     */
    private String lcc;

    /**
     * OT Value (Other)
     */
    private String ot;

    /**
     * AS Value (Algae/Sand)
     */
    private String as;

    /**
     * SD Value (Sand)
     */
    private String sd;

    /**
     * DI Value (Disease)
     */
    private String di;

    /**
     * PI Value (Physical Impact)
     */
    private String pi;

    /**
     * Unknown Column
     */
    private String myUnknownColumn;
}
