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
    private Integer id;
    /**
     * 活动id
     */
    private Integer eventId;
    /**
     * 成员id
     */
    private Integer uid;
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
    private Long addTime;
    /**
     * 成员空闲时间范围
     */
    private HashMap<Long, Long> freeTimes;

    public ActivityMembers(Integer eventId, Integer uid) {
        this.eventId = eventId;
        this.uid = uid;
    }

    public ActivityMembers() {
    }
}
