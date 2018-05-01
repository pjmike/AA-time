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
import com.ctg.aatime.service.ActivityService;
import com.ctg.aatime.service.TimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author pjmike
 * @create 2018-03-16 16:41
 */
@Service
@Slf4j
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityDao activityDao;
    @Autowired
    private ActivityMembersDao activityMembersDao;
    @Autowired
    private TimeDao timeDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private TimeService timeService;
    @Autowired
    private ActivityMembersService membersService;

    @Override
    @Transactional
    public Activity createActivity(Activity activity) {
        //将信息插入活动信息表
        activity.setMembers(1);
        if (activityDao.createActivity(activity) < 1) {
            //添加活动失败
            throw new CascadeException();
        }
        ActivityMembers activityMembers = new ActivityMembers();
        activityMembers.setEventId(activity.getEventId());
        activityMembers.setAvatar(activity.getAvatar());
        activityMembers.setUid(activity.getUid());
        activityMembers.setAddTime(new Date().getTime());
        activityMembers.setUsername(activity.getUsername());
        if (membersService.insertActivityMember(activity.getUid(),activity.getEventId()) == null){
            throw new CascadeException();
        }
        return activity;
    }

    @Override
    public List<Activity> selectLiveActivitiesByUid(int uId) {
        //查询该用户参与的所有活动的id
        List<Integer> eventIds = activityMembersDao.selectJoinEventsIdByUid(uId);
        List<Activity> activities = new ArrayList<Activity>();
        for (Integer eventId : eventIds) {
            Activity activity = activityDao.selectActivityByEventId(eventId);
            if (activity != null && activity.getEndTime() > new Date().getTime()) {
                activities.add(activity);
            }
        }
        return activities;
    }

    @Override
    public Activity selectActivityByEventId(int eventId) {
        return activityDao.selectActivityByEventId(eventId);
    }

    @Override
    @Transactional
    public void delActivityByEventId(int eventId) {
        //TODO　删除退出活动记录表和活动成员时间选择表相关信息，这里因为表中信息为空，无法判断是否执行ｓｑｌ
        activityDao.delQuitInfoByEventId(eventId);
        timeDao.delMembersTimeByEventId(eventId);
        if (activityMembersDao.delActivityMembersByEventId(eventId) < 1) {
            //如果删除活动成员/选择时间相关信息失败
            throw new CascadeException();
        }
        if (activityDao.delActivity(eventId) < 1) {
            //如果删除该活动失败
            throw new CascadeException();
        }
    }

    @Override
    public int launchActivity(int eventId, String launchWords, long launchStartTime, long launchEndTime) {
        Activity activity = activityDao.selectActivityByEventId(eventId);
        if (activity.getLaunchTime() != 0 ){
            //若活动已经发布  则发布失败
            return -1;
        }
        //TODO 推荐时间方法仍存在问题，需要合适数据
//        int launchMembers = timeService.getRecommendTime(eventId).getBestTimes().get(0).getJoinMembers().size();
        activity.setLaunchTime(new Date().getTime());
        activity.setLaunchMembers(1);
        activity.setLaunchStartTime(launchStartTime);
        activity.setLaunchEndTime(launchEndTime);
        activity.setLaunchWords(launchWords);
        return activityDao.updateLaunchInfo(activity);
    }

    @Override
    public List<Activity> selectLaunchActivitiesByUid(int uId) {
        List<Activity> activities = selectLiveActivitiesByUid(uId);
        for (int i = 0; i < activities.size();){
            Activity activity = activities.get(i);
            if (activity.getLaunchTime() == 0) activities.remove(activity);
            else i++;
        }
        return activities;
    }

    @Override
    public List<Activity> selectDeadActivitiesByUid(int uId) {
        List<Integer> eventIds = activityMembersDao.selectJoinEventsIdByUid(uId);
        List<Activity> activities = new ArrayList<Activity>();
        for (Integer eventId : eventIds) {
            Activity activity = activityDao.selectActivityByEventId(eventId);
            if (activity != null && activity.getEndTime() < new Date().getTime()) {
                activities.add(activity);
            }
        }
        return activities;
    }

    @Override
    public List<Activity> selectEstablishedActivitiesByUid(int uId) {
        List<Activity> activities = activityDao.selectEstablishedActivitiesByUid(uId);
        for (int i = 0; i < activities.size();){
            Activity activity = activities.get(i);
            if (activity.getLaunchTime() == 0) activities.remove(activity);
            else i++;
        }
        return activities;
    }

}
