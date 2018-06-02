package com.ctg.aatime.domain;

import lombok.Data;

/**
 * 时间选择类
 *
 * @author pjmike
 * @create 2018-03-23 15:50
 */
@Data
public class Time {
    /**
     * 自增id
     */
    private Integer id;
    /**
     * 活动id
     */
    private Integer eventId;
    /**
     * 用户id
     */
    private Integer uid;
    /**
     * 时间段的开始时间
     */
    private Long startTime;
    /**
     * 时间段的结束时间
     */
    private Long endTime;
    /**
     * 时间段的权重值
     */
    private Integer weight;
}
