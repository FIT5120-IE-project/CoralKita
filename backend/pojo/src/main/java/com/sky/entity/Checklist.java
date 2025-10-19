package com.sky.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Travel Checklist Entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Checklist {
    private Integer userId;  // User ID
    private Integer q1;      // Question 1
    private Integer q2;      // Question 2
    private Integer q3;      // Question 3
    private Integer q4;      // Question 4
    private Integer q5;      // Question 5
    private Integer q6;      // Question 6
    private Integer q7;      // Question 7
    private Integer q8;      // Question 8
    private Integer q9;      // Question 9
    private Integer q10;     // Question 10
    private Integer q11;     // Question 11
    private Integer q12;     // Question 12
    private Integer q13;     // Question 13
    private Integer q14;     // Question 14
    private Integer q15;     // Question 15
    private Integer q16;     // Question 16
    private Integer q17;     // Question 17
}
