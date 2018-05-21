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
import com.ctg.aatime.domain.dto.BestTime;
import com.ctg.aatime.service.ActivityMembersService;
import com.ctg.aatime.service.ActivityService;
import com.ctg.aatime.service.TimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    @Transactional(rollbackFor = Exception.class)
    public Activity createActivity(Activity activity) {
        //将信息插入活动信息表
        User u = userDao.selectUserByUid(activity.getUid());
        if (u == null) {
            throw new CascadeException("用户id不存在");
        }
        activity.setAvatar(u.getAvatar());
        activity.setUsername(u.getUsername());
        if (activityDao.createActivity(activity) < 1) {
            //添加活动失败
            throw new CascadeException("添加活动失败");
        }
        if (membersService.insertActivityMember(activity.getUid(), activity.getEventId(), new ArrayList<Time>()) == null){
            //将创建者添加为该活动成员失败
            throw new CascadeException("添加活动失败");
        }
        return activity;
    }

    @Override
    public List<Activity> selectLiveActivitiesByUid(int uId) {
        //查询该用户参与的所有活动（包括过期的）id
        List<Integer> eventIds = activityMembersDao.selectJoinEventsIdByUid(uId);
        List<Activity> activities = new ArrayList<Activity>();
        long now =System.currentTimeMillis();
        for (Integer eventId : eventIds) {
            Activity activity = activityDao.selectActivityByEventId(eventId);
            if (activity != null) {
                //如果能查询到该活动,添加未过期活动
                if (activity.getLaunchTime() != 0 && activity.getLaunchEndTime() <= now){
                    //如果活动已发布 且 此时大于活动结束时间,则说明是过期活动，跳过
                    continue;
                }
                else if(activity.getLaunchTime() == 0 && activity.getEndTime() <= now){
                    //如果活动未发布 且 此时大于活动可选范围结束时间 则该活动已无效，直接删除
                    delActivityByEventId(activity.getEventId());
                    continue;
                }
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
            //如果删除活动成员失败
            throw new CascadeException("删除活动成员失败");
        }
        if (activityDao.delActivity(eventId) < 1) {
            //如果删除该活动失败
            throw new CascadeException("删除活动失败");
        }
    }

    @Override
    public int launchActivity(Activity activity) {
        Activity a = activityDao.selectActivityByEventId(activity.getEventId());
        long now = System.currentTimeMillis();
        if (now > a.getEndTime()){
            //如果当前时间大于活动可选时间范围，删除活动，并通知发布失败
            delActivityByEventId(activity.getEventId());
            throw new CascadeException("活动已失效");
        }
        if (a.getLaunchTime() != 0) {
            //若活动已经发布  则发布失败
            throw new CascadeException("活动已发布");
        }
        a.setLaunchTime(activity.getLaunchTime());
        a.setLaunchStartTime(activity.getLaunchStartTime());
        a.setLaunchEndTime(activity.getLaunchEndTime());
        a.setLaunchWords(activity.getLaunchWords());
        if (a.getLaunchStartTime() > a.getLaunchEndTime() || a.getLaunchStartTime() < now){
            //活动确定发布的开始时间 大于结束时间/小于此时
            throw new CascadeException("数据有误");
        }
        List<BestTime> bestTime = timeService.getRecommendTime(a.getEventId()).getBestTimes();
        int launchMembers = 0;
        if (bestTime.size()!=0){
            //如果存在最优解，查找第一个最优解的参与人数（因为最优解的参与人数都相同）
            launchMembers = bestTime.get(0).getJoinMembers().size();
        }
        activity.setLaunchMembers(launchMembers);
        activity.setLaunchTime(now);
        return activityDao.updateLaunchInfo(activity);
    }

    @Override
    public List<Activity> selectLaunchActivitiesByUid(int uId) {
        List<Activity> activities = selectLiveActivitiesByUid(uId);
        for (int i = 0; i < activities.size(); ) {
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
        long now = System.currentTimeMillis();
        for (Integer eventId : eventIds) {
            Activity activity = activityDao.selectActivityByEventId(eventId);
            if (activity != null) {
                //如果能查询到该活动,添加未过期活动
                if (activity.getLaunchTime() != 0 && activity.getLaunchEndTime() <= now){
                    //如果活动已发布 且 此时大于活动结束时间
                    activities.add(activity);
                }
                else if(activity.getLaunchTime() == 0 && activity.getEndTime() <= now){
                    //如果活动未发布 且 此时大于活动可选范围结束时间 则该活动已无效，直接删除
                    delActivityByEventId(activity.getEventId());
                }
            }
        }
        return activities;
    }

    @Override
    public List<Activity> selectEstablishedActivitiesByUid(int uId) {
        List<Activity> activities = selectLiveActivitiesByUid(uId);
        for (Activity a: activities) {
            if (a.getUid() != uId){
                //若不是该用户创建的活动，移除！
                activities.remove(a);
            }
        }
        return activities;
    }

}
