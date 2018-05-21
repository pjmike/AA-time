package com.ctg.aatime.service;

import com.ctg.aatime.domain.ActivityMembers;
import com.ctg.aatime.domain.Time;

import java.util.List;
import java.util.Map;

/**
 * @author pjmike
 */
public interface ActivityMembersService {
    /**
     * 插入活动成员
     */
    ActivityMembers insertActivityMember(int uid, int eventId, List<Time> freeTime);

    /**
     * 通过活动id查询所有参与活动的成员
     *
     * @param eventId
     * @return
     */
    List<ActivityMembers> selectJoinMembersByEventId(int eventId);

    /**
     * 通过eventId退出活动，删除活动成员、时间选择表有关信息，活动信息表活动成员总数-1，在退出活动记录表添加退出原因
     */
    ActivityMembers quitActivity(ActivityMembers members, String reason);
}
