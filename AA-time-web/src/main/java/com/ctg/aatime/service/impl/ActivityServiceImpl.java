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


    //查询该用户所有参与的未过期活动
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
        //按统计截止时间升序排序
        Collections.sort(activities, (o1, o2) -> {
            if (o1.getStatisticTime() > o2.getStatisticTime()) return 1;
            else return -1;
        });
        return activities;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Activity createActivity(Activity activity) {
        //将信息插入活动信息表
        User u = userDao.selectUserByUid(activity.getUid());
        if (u == null) {
            log.info("创建活动失败，原因：用户id不存在");
            throw new CascadeException("用户id不存在");
        }
        activity.setAvatar(u.getAvatar());
        activity.setUsername(u.getUsername());
        if (activityDao.createActivity(activity) < 1) {
            //添加活动失败
            log.info("创建活动失败，原因：数据库更新失败");
            throw new CascadeException("添加活动失败");
        }
        if (membersService.insertActivityMember(activity.getUid(), activity.getEventId(), new ArrayList<Time>()) == null){
            //将创建者添加为该活动成员失败
            log.info("创建活动失败，原因：添加活动成员失败");
            throw new CascadeException("添加活动失败");
        }
        return activity;
    }

    //查询该用户所有仅参与（不包含创建）的未发布未过期活动
    @Override
    public List<Activity> selectJoinActivitiesByUid(int uId) {
        List<Activity> activities = selectLiveActivitiesByUid(uId);
        for (int i = 0;i<activities.size();i++) {
            Activity a = activities.get(i);
            if (a.getLaunchTime() != 0) {
                //如果该活动已发布，remove
                activities.remove(a);
            }
            else if (a.getUid() == uId){
                //如果该活动是该用户创建的，remove
                activities.remove(a);
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
            log.info("删除活动失败，原因：删除活动成员失败");
            throw new CascadeException("删除活动成员失败");
        }
        if (activityDao.delActivity(eventId) < 1) {
            //如果删除该活动失败
            log.info("删除活动失败，原因：删除活动信息失败");
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
            log.info("发布活动失败，原因：活动已失效");
            throw new CascadeException("活动已失效");
        }
        if (a.getLaunchTime() != 0) {
            //若活动已经发布  则发布失败
            log.info("发布活动失败，原因：活动已发布");
            throw new CascadeException("活动已发布");
        }
        a.setLaunchTime(activity.getLaunchTime());
        a.setLaunchStartTime(activity.getLaunchStartTime());
        a.setLaunchEndTime(activity.getLaunchEndTime());
        a.setLaunchWords(activity.getLaunchWords());
        if (a.getLaunchStartTime() > a.getLaunchEndTime() || a.getLaunchStartTime() < now){
            //活动确定发布的开始时间 大于结束时间/小于此时
            log.info("发布活动失败，原因：活动确定发布的开始时间 大于结束时间/小于此时");
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

    //查询该用户所有参与的未过期已发布的活动
    @Override
    public List<Activity> selectLaunchActivitiesByUid(int uId) {
        List<Activity> activities = selectLiveActivitiesByUid(uId);
        for (int i = 0; i < activities.size(); ) {
            Activity activity = activities.get(i);
            //如果该活动未发布，
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

    //该用户创建的未发布的未过期的活动
    @Override
    public List<Activity> selectEstablishedActivitiesByUid(int uId) {
        //该用户创建的未发布的活动，按统计截止时间的升序排序
        List<Activity> activities = activityDao.selectEstablishedActivitiesByUid(uId);
        Long now = System.currentTimeMillis();
        for (Activity a: activities) {
            if (a.getEndTime() <= now){
                //若该活动到结束时间时仍未发布，则删除该活动！
                activities.remove(a);
                delActivityByEventId(a.getEventId());
            }
        }
        return activities;
    }

}
