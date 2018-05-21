package com.ctg.aatime.service;

import com.ctg.aatime.domain.Time;
import com.ctg.aatime.domain.dto.RecommendTimeInfo;

import java.util.List;
import java.util.Map;

/**
 * 时间业务
 * Created By Cx On 2018/4/17 16:00
 */
public interface TimeService {

    int changeFreeTime(int uid, int eventId, List<Time> freeTime);

    /**
     * 通过eventId获取最优时间所有信息
     */
    RecommendTimeInfo getRecommendTime(int eventId);

    /**
     * 通过uid,eventId查询某人的空闲时间
     * @return
     */
    Map<Long, Long> getFreeTimeByUid(int uid,int eventId);
}
