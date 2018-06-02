package com.ctg.aatime.domain;

import lombok.Data;

/**
 * 活动类
 *
 * @author pjmike
 * @create 2018-03-16 16:19
 */
@Data
public class Activity {
    /**
     * 活动Id
     */
    private Integer eventId;
    /**
     * 活动名称
     */
    private String eventName;
    /**
     * 活动简介
     */
    private String eventBrief;
    /**
     * 活动标签
     */
    private String eventTag;
    /**
     * 活动地点
     */
    private String eventPlace;
    /**
     * 活动报名人数
     */
    private Integer members;
    /**
     * 活动参与人数
     */
    private Integer launchMembers;
    /**
     * 活动发起人id
     */
    private Integer uid;
    /**
     * 发起人名字
     */
    private String username;
    /**
     * 发起人头像
     */
    private String avatar;
    /**
     * 活动可选开始时间戳
     */
    private Long startTime;
    /**
     * 活动可选结束时间戳
     */
    private Long endTime;
    /**
     * 活动确认开始时间
     */
    private Long launchStartTime;
    /**
     * 活动确认结束时间
     */
    private Long launchEndTime;
    /**
     * 活动统计截止时间戳
     */
    private Long statisticTime;
    /**
     * 活动最小持续时间
     */
    private Long minTime;
    /**
     * 活动发布时间
     */
    private Long launchTime;
    /**
     * 活动发布留言
     */
    private String launchWords;
    /**
     * 活动照片
     */
    private String imageUrl;

}
