package com.ctg.aatime.domain;

import lombok.Data;

/**
 * 活动地点信息
 *
 * @author pjmike
 * @create 2018-03-23 15:53
 */
@Data
public class EventPlace {
    /**
     * 自增id
     */
    private int id;
    /**
     * 活动id
     */
    private int eventId;
    /**
     * 活动地点名
     */
    private String place;
}
