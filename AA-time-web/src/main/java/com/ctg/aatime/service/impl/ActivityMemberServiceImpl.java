package com.ctg.aatime.service.impl;

import com.ctg.aatime.commons.exception.CascadeException;
import com.ctg.aatime.dao.ActivityDao;
import com.ctg.aatime.dao.ActivityMembersDao;
import com.ctg.aatime.dao.TimeDao;
import com.ctg.aatime.dao.UserDao;
import com.ctg.aatime.domain.Activity;
import com.ctg.aatime.domain.ActivityMembers;
import com.ctg.aatime.domain.User;
import com.ctg.aatime.service.ActivityMembersService;
import com.ctg.aatime.service.TimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
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
    private UserDao userDao;
    @Autowired
    private TimeService timeService;

    @Override
    @Transactional
    public ActivityMembers insertActivityMember(int uid, int eventId) {
        //TODO 待重构
        long now = new Date().getTime();
        Activity activity = activityDao.selectActivityByEventId(eventId);
        ActivityMembers members = activityMembersDao.selectActivityMembersByUEid(uid,eventId);
        if (activity.getStatisticTime() < now || activity.getLaunchTime() > 0 || members!=null){
            //若超过活动统计截止时间  或  活动已发布  或  已加入活动，则添加失败
            //TODO 这里是否应该告诉前端为什么添加失败
            return null;
        }
        if (activityDao.addMembersByEventId(eventId) < 1){
            throw new CascadeException();
        }
        members = new ActivityMembers();
        User user = userDao.selectUserByUid(uid);
        members.setEventId(eventId);
        members.setAvatar(user.getAvatar());
        members.setUid(uid);
        members.setAddTime(new Date().getTime());
        if (activityMembersDao.insertActivityMembers(members) < 1) {
            throw new CascadeException();
        }
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
        //TODO　无法保证时间选择表中是否有数据，所以无法判断sql是否执行
        timeDao.delMembersTimeByUId(uid, eventId);
        if (activityMembersDao.quitActivityByUid(uid, eventId) != 1 ||
                activityDao.reduceMembersByEventId(eventId) != 1 ||
                activityDao.addQuitReason(activityMember, reason) != 1) {
            throw new CascadeException();
        }
        return activityMember;
    }
}
