package com.ctg.aatime.dao;

import com.ctg.aatime.domain.ActivityMembers;

/**
 * @author pjmike
 * @create 2018-03-24 10:59
 */
public interface ActivityMembersDao {
    /**
     * 插入活动人员
     *
     * @param activityMembers
     * @return
     */
    int insertActivityMembers(ActivityMembers activityMembers);
}
