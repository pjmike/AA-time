package com.ctg.aatime.service.impl;

import com.ctg.aatime.commons.exception.CascadeException;
import com.ctg.aatime.commons.utils.RecommendTimeUtil;
import com.ctg.aatime.dao.ActivityDao;
import com.ctg.aatime.dao.ActivityMembersDao;
import com.ctg.aatime.dao.TimeDao;
import com.ctg.aatime.dao.UserDao;
import com.ctg.aatime.domain.Activity;
import com.ctg.aatime.domain.ActivityMembers;
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

    @Override
    @Transactional
    public Activity createActivity(Activity activity) {
        //将信息插入活动信息表
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
        if (activityMembersDao.insertActivityMembers(activityMembers) < 1){
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
            //若该活动统计未结束，则查询参与人数，并显示出来
            if (activity != null && activity.getEndTime() > new Date().getTime()) {
                activities.add(activity);
            }
        }
        return activities;
    }

    @Override
    public Activity selectActivityByEventId(int eventId) {
        Activity activity = activityDao.selectActivityByEventId(eventId);
        return activity;
    }

    @Override
    @Transactional
    public void delActivityByEventId(int eventId) {
        //删除退出活动记录表相关信息
        activityDao.delQuitInfoByEventId(eventId);
        if (activityMembersDao.delActivityMembersByEventId(eventId) < 1 ||
                timeDao.delMembersTimeByEventId(eventId) < 1) {
            //如果删除活动成员/选择时间相关信息失败
            throw new CascadeException();
        }
        if (activityDao.delActivity(eventId) < 1) {
            //如果删除该活动失败
            throw new CascadeException();
        }
    }

    @Override
    public int launchActivity(int eventId, String launchWords) {
        int launchMembers = timeService.getRecommendTime(eventId).getBestTimes().get(0).getJoinMembers().size();
        return activityDao.updateLaunchInfo(eventId, launchMembers, new Date().getTime(), launchWords);
    }

    @Override
    public List<Activity> selectLaunchActivitiesByUid(int uId) {
        List<Activity> activities = selectLiveActivitiesByUid(uId);
        for (Activity activity : activities){
            if (activity.getLaunchTime() == 0) activities.remove(activity);
        }
        return activities;
    }

    @Override
    public List<Activity> selectDeadActivitiesByUid(int uId) {
        List<Integer> eventIds = activityMembersDao.selectJoinEventsIdByUid(uId);
        List<Activity> activities = new ArrayList<Activity>();
        for (Integer eventId : eventIds) {
            Activity activity = activityDao.selectActivityByEventId(eventId);
            //若该活动统计未结束，则查询参与人数，并显示出来
            if (activity != null && activity.getEndTime() < new Date().getTime()) {
                activities.add(activity);
            }
        }
        return activities;
    }

    @Override
    public List<Activity> selectInitActivitiesByUid(int uId) {
        //TODO
        return null;
    }

}
