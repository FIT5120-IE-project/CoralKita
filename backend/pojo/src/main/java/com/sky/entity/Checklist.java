package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 旅行清单实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Checklist {
    private Integer userId;  // 用户ID
    private Integer q1;      // 问题1
    private Integer q2;      // 问题2
    private Integer q3;      // 问题3
    private Integer q4;      // 问题4
    private Integer q5;      // 问题5
    private Integer q6;      // 问题6
    private Integer q7;      // 问题7
    private Integer q8;      // 问题8
    private Integer q9;      // 问题9
    private Integer q10;     // 问题10
    private Integer q11;     // 问题11
    private Integer q12;     // 问题12
    private Integer q13;     // 问题13
    private Integer q14;     // 问题14
    private Integer q15;     // 问题15
    private Integer q16;     // 问题16
    private Integer q17;     // 问题17
}
