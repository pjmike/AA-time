package com.ctg.aatime.domain.dto;

import com.ctg.aatime.domain.ActivityMembers;
import lombok.Data;

import java.util.List;

/**
 * 最优时间类
 * 包含最优解开始时间，结束时间，参与人集合
 * Created By Cx On 2018/4/5 11:52
 */
@Data
public class BestTime {
    //开始时间戳
    private long startTime;
    //结束时间戳
    private long endTime;
    //所有参与活动的人集合
    private List<ActivityMembers> joinMembers;
    //最优时间解未参与人
    private List<ActivityMembers> notJoinMembers;

    public BestTime() {
    }

    public BestTime(long startTime, long endTime, List<ActivityMembers> joinMembers, List<ActivityMembers> notJoinMembers) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.joinMembers = joinMembers;
        this.notJoinMembers = notJoinMembers;
    }
}
