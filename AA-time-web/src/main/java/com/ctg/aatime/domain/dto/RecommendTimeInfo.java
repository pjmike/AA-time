package com.ctg.aatime.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * 推荐时间界面所需返回内容
 * Created By Cx On 2018/4/6 12:15
 */
@Data
public class RecommendTimeInfo {
    //最优时间解
    private List<BestTime> bestTimes;
    //可选时间的起始时间戳
    private long activityStart;
    //可选时间的结束时间戳
    private long activityEnd;
    //该活动参与统计的人数
    private int allMembersNum;

    public RecommendTimeInfo(List<BestTime> bestTimes, long activityStart, long activityEnd, int allMembersNum) {
        this.bestTimes = bestTimes;
        this.activityStart = activityStart;
        this.activityEnd = activityEnd;
        this.allMembersNum = allMembersNum;
    }
}
