package com.ctg.aatime.service.impl;

import com.ctg.aatime.commons.exception.CascadeException;
import com.ctg.aatime.dao.ActivityDao;
import com.ctg.aatime.dao.ActivityMembersDao;
import com.ctg.aatime.dao.TimeDao;
import com.ctg.aatime.dao.UserDao;
import com.ctg.aatime.domain.Activity;
import com.ctg.aatime.domain.ActivityMembers;
import com.ctg.aatime.domain.Time;
import com.ctg.aatime.domain.User;
import com.ctg.aatime.service.ActivityMembersService;
import com.ctg.aatime.service.ActivityService;
import com.ctg.aatime.service.TimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    @Autowired
    private ActivityService activityService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ActivityMembers insertActivityMember(int uid, int eventId, List<Time> freeTime) {
        long now = System.currentTimeMillis();
        Activity activity = activityDao.selectActivityByEventId(eventId);
        ActivityMembers members = activityMembersDao.selectActivityMembersByUEid(uid,eventId);
        if (activity.getStatisticTime() < now || activity.getLaunchTime() > 0 ){
            //若超过活动统计截止时间  或  活动已发布，则添加失败
            //TODO 这里是否应该抛出异常 表示为什么添加失败
            throw new CascadeException("加入活动失败");
        }
        for (Time t : freeTime) {
            t.setEventId(eventId);
            t.setUid(uid);
        }
        if (members!=null){
            //用户已存在，则是修改空闲时间,先删后添
            timeDao.delMembersTimeByUId(uid, eventId);
            if (freeTime.size()!=0){
                //空闲时间不为空，则添加
                if (timeDao.insertTimeList(freeTime) == 0){
                    //活动成员空闲时间添加失败
                    throw new CascadeException("加入活动失败");
                }
            }
            return members;
        }
        //如果用户不存在，则说明是加入活动
        if (activityDao.addMembersByEventId(eventId) < 1){
            //活动参与人数+1 失败
            throw new CascadeException("加入活动失败");
        }
        members = new ActivityMembers();
        //TODO 无法加入notes属性
        User user = userDao.selectUserByUid(uid);
        members.setEventId(eventId);
        members.setAvatar(user.getAvatar());
        members.setUid(uid);
        members.setUsername(user.getUsername());
        members.setAddTime(System.currentTimeMillis());
        if (activityMembersDao.insertActivityMembers(members) < 1) {
            //活动成员添加失败
            throw new CascadeException("加入活动失败");
        }
        if (freeTime.size()!=0){
            //空闲时间不为空，则添加
            if (timeDao.insertTimeList(freeTime) == 0){
                //活动成员空闲时间添加失败
                throw new CascadeException("加入活动失败");
            }
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
    @Transactional(rollbackFor = Exception.class)
    public ActivityMembers quitActivity(ActivityMembers members, String reason) {
        int uid = members.getUid();
        int eventId = members.getEventId();
        members = activityMembersDao.selectActivityMembersByUEid(uid, eventId);
        Activity activity = activityDao.selectActivityByEventId(eventId);
        if (activity.getUid() == uid){
            //TODO 创建者退出活动，则删除活动
            activityService.delActivityByEventId(eventId);
            return members;
        }
        //TODO　无法保证时间选择表中是否有数据，所以无法判断sql是否执行
        timeDao.delMembersTimeByUId(uid, eventId);
        if (activityMembersDao.quitActivityByUid(uid, eventId) != 1 ||
                activityDao.reduceMembersByEventId(eventId) != 1 ||
                activityDao.addQuitReason(members, reason) != 1) {
            //如果 退出活动成员表 / 减少活动成员数 / 添加退出原因 失败
            throw new CascadeException("退出失败");
        }
        return members;
    }
}
