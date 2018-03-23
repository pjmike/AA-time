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
    private int id;
    /**
     * 活动id
     */
    private int eventId;
    /**
     * 时间段的开始时间
     */
    private long start_time;
    /**
     * 时间段的结束时间
     */
    private long end_time;
    /**
     * 时间段的权重值
     */
    private int weight;
}
