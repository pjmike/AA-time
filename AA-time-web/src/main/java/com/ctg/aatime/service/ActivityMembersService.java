package com.ctg.aatime.service;

import com.ctg.aatime.domain.ActivityMembers;

/**
 * @author pjmike
 */
public interface ActivityMembersService {
    /**
     * 插入活动成员
     *
     * @param members
     * @return
     */
    int insertActivityMember(ActivityMembers members);
}
