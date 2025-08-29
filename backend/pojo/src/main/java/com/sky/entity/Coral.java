package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 珊瑚数据实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coral implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 生态区域
     */
    private String ecoRegion;

    /**
     * 岛屿名称
     */
    private String island;

    /**
     * 岛屿经度
     */
    private Double islandLng;

    /**
     * 岛屿纬度
     */
    private Double islandLat;

    /**
     * 站点名称
     */
    private String siteName;

    /**
     * 站点类型
     */
    private String siteType;

    /**
     * 经度
     */
    private Double lng;

    /**
     * 纬度
     */
    private Double lat;

    /**
     * 日期
     */
    private String date;

    /**
     * 深度
     */
    private Double depth;

    /**
     * 硬珊瑚覆盖度
     */
    private Double hc;

    /**
     * 软珊瑚覆盖度
     */
    private Double sc;

    /**
     * 岩石覆盖度
     */
    private Double rkc;

    /**
     * 无脊椎动物覆盖度
     */
    private Double nia;

    /**
     * 海绵覆盖度
     */
    private Double sp;

    /**
     * 珊瑚覆盖度
     */
    private Double rc;

    /**
     * 珊瑚礁覆盖度
     */
    private Double rb;

    /**
     * 沙地覆盖度
     */
    private Double sd;

    /**
     * 淤泥覆盖度
     */
    private Double si;

    /**
     * 其他覆盖度
     */
    private Double ot;

    /**
     * 鱼类生物量
     */
    private Double bf;

    /**
     * 海藻覆盖度
     */
    private Double sl;

    /**
     * 海草覆盖度
     */
    private Double sn;

    /**
     * 珊瑚礁鱼类数量
     */
    private Integer bc;

    /**
     * 硬珊瑚鱼类数量
     */
    private Integer hw;

    /**
     * 底栖鱼类数量
     */
    private Integer bp;

    /**
     * 浮游生物密度
     */
    private Double pf;

    /**
     * 微生物密度
     */
    private Double me;

    /**
     * 生长率
     */
    private Double gr;

    /**
     * 珊瑚礁健康状态
     */
    private Integer bcs;

    /**
     * 多样性指数
     */
    private Double du;

    /**
     * 保护状态
     */
    private Integer pu;

    /**
     * 珊瑚状态
     */
    private Integer cu;

    /**
     * 软珊瑚状态
     */
    private Integer scu;

    /**
     * 珊瑚礁类型
     */
    private Integer cot;

    /**
     * 威胁等级
     */
    private Integer tr;

    /**
     * 位置状态
     */
    private Integer lo;

    /**
     * 地理条件
     */
    private Integer gc;

    /**
     * 珊瑚疾病A
     */
    private Integer cdba;

    /**
     * 珊瑚疾病D
     */
    private Integer cdd;

    /**
     * 珊瑚疾病O
     */
    private Integer cdo;

    /**
     * 鱼类数量
     */
    private Double tfn;

    /**
     * 温度梯度
     */
    private Double tg;

    /**
     * 生物多样性指数
     */
    private Integer bnp;

    /**
     * 生物数量
     */
    private Integer bnc;

    /**
     * 底栖生物
     */
    private Integer bb;

    /**
     * 水生物
     */
    private Integer wb;

    /**
     * 珊瑚礁健康
     */
    private Integer sh;

    /**
     * 旅游影响
     */
    private Integer tu;

    /**
     * 珊瑚礁恢复
     */
    private Integer sr;

    /**
     * 其他指标
     */
    private Integer other;
}
