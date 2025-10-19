package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Coral Data Entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coral implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Eco Region
     */
    private String ecoRegion;

    /**
     * Island Name
     */
    private String island;

    /**
     * Island Longitude
     */
    private Double islandLng;

    /**
     * Island Latitude
     */
    private Double islandLat;

    /**
     * Site Name
     */
    private String siteName;

    /**
     * Site Type
     */
    private String siteType;

    /**
     * Longitude
     */
    private Double lng;

    /**
     * Latitude
     */
    private Double lat;

    /**
     * Date
     */
    private String date;

    /**
     * Depth
     */
    private Double depth;

    /**
     * Hard Coral Coverage
     */
    private Double hc;

    /**
     * Soft Coral Coverage
     */
    private Double sc;

    /**
     * Rock Coverage
     */
    private Double rkc;

    /**
     * Non-Invertebrate Animal Coverage
     */
    private Double nia;

    /**
     * Sponge Coverage
     */
    private Double sp;

    /**
     * Reef Coverage
     */
    private Double rc;

    /**
     * Reef Base Coverage
     */
    private Double rb;

    /**
     * Sand Coverage
     */
    private Double sd;

    /**
     * Silt Coverage
     */
    private Double si;

    /**
     * Other Coverage
     */
    private Double ot;

    /**
     * Fish Biomass
     */
    private Double bf;

    /**
     * Seaweed Coverage
     */
    private Double sl;

    /**
     * Seagrass Coverage
     */
    private Double sn;

    /**
     * Reef Fish Count
     */
    private Integer bc;

    /**
     * Hard Coral Fish Count
     */
    private Integer hw;

    /**
     * Benthic Fish Count
     */
    private Integer bp;

    /**
     * Plankton Density
     */
    private Double pf;

    /**
     * Microbe Density
     */
    private Double me;

    /**
     * Growth Rate
     */
    private Double gr;

    /**
     * Reef Health Status
     */
    private Integer bcs;

    /**
     * Diversity Index
     */
    private Double du;

    /**
     * Protection Status
     */
    private Integer pu;

    /**
     * Coral Status
     */
    private Integer cu;

    /**
     * Soft Coral Status
     */
    private Integer scu;

    /**
     * Reef Type
     */
    private Integer cot;

    /**
     * Threat Level
     */
    private Integer tr;

    /**
     * Location Status
     */
    private Integer lo;

    /**
     * Geographic Conditions
     */
    private Integer gc;

    /**
     * Coral Disease A
     */
    private Integer cdba;

    /**
     * Coral Disease D
     */
    private Integer cdd;

    /**
     * Coral Disease O
     */
    private Integer cdo;

    /**
     * Total Fish Number
     */
    private Double tfn;

    /**
     * Temperature Gradient
     */
    private Double tg;

    /**
     * Biodiversity Index
     */
    private Integer bnp;

    /**
     * Biological Count
     */
    private Integer bnc;

    /**
     * Benthic Organisms
     */
    private Integer bb;

    /**
     * Water Organisms
     */
    private Integer wb;

    /**
     * Reef Health
     */
    private Integer sh;

    /**
     * Tourism Impact
     */
    private Integer tu;

    /**
     * Reef Recovery
     */
    private Integer sr;

    /**
     * Other Indicators
     */
    private Integer other;
}
