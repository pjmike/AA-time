package com.ctg.aatime.domain;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 活动成员信息
 *
 * @author pjmike
 * @create 2018-03-16 16:29
 */
@Data
public class ActivityMembers {
    /**
     * 自增id
     */
    private int id;
    /**
     * 活动id
     */
    private int eventId;
    /**
     * 成员id
     */
    private int uid;
    /**
     * 成员名称
     */
    private String username;
    /**
     * 成员活动中的备注
     */
    private String notes;
    /**
     * 成员的头像
     */
    private String avatar;
    /**
     * 成员加入的时间戳
     */
    private long addTime;
    /**
     * 成员空闲时间范围
     */
    private HashMap<Long, Long> freeTimes;
}
