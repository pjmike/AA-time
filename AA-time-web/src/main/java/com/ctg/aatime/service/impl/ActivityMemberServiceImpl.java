package com.ctg.aatime.service.impl;

import com.ctg.aatime.commons.exception.CascadeException;
import com.ctg.aatime.dao.ActivityDao;
import com.ctg.aatime.dao.ActivityMembersDao;
import com.ctg.aatime.dao.TimeDao;
import com.ctg.aatime.domain.ActivityMembers;
import com.ctg.aatime.service.ActivityMembersService;
import com.ctg.aatime.service.TimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

/**
 * @author pjmike
 * @create 2018-03-24 10:58
 */
@Service
@Slf4j
public class ActivityMemberServiceImpl implements ActivityMembersService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private ActivityMembersDao activityMembersDao;
    @Autowired
    private TimeDao timeDao;
    @Autowired
    private ActivityDao activityDao;
    @Autowired
    private TimeService timeService;

    @Override
    public ActivityMembers insertActivityMember(ActivityMembers members) {
        activityMembersDao.insertActivityMembers(members);
        //TODO 待重构
        return members;
    }

    @Override
    public List<ActivityMembers> selectJoinMembersByEventId(int eventId) {
        //参与活动人员
        List<ActivityMembers> members = activityMembersDao.selectActivityMembersByEventId(eventId);

        for (ActivityMembers member : members) {
            //查询每个成员的freeTime并添加进属性
            int uid = member.getUid();
            HashMap<Long, Long> freeTime = (HashMap<Long,Long>)timeService.getFreeTimeByUid(uid,eventId);
            member.setFreeTimes(freeTime);
        }
        return members;
    }

    @Override
    @Transactional
    public ActivityMembers quitActivity(int uid, int eventId, String reason) {
        ActivityMembers activityMember = activityMembersDao.selectActivityMembersByUEid(uid, eventId);
        if (activityMembersDao.quitActivityByUid(uid, eventId) != 1 ||
                activityDao.reduceMembersByEventId(eventId) != 1 ||
                timeDao.delMembersTimeByUId(uid, eventId) < 1 ||
                activityDao.addQuitReason(activityMember, reason) != 1) {
            throw new CascadeException();
        }
        return activityMember;
    }
}
