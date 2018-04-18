package com.ctg.aatime.service.impl;

import com.ctg.aatime.commons.exception.CascadeException;
import com.ctg.aatime.dao.ActivityDao;
import com.ctg.aatime.dao.ActivityMembersDao;
import com.ctg.aatime.dao.TimeDao;
import com.ctg.aatime.dao.UserDao;
import com.ctg.aatime.domain.Activity;
import com.ctg.aatime.service.ActivityService;
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

    @Override
    @Transactional
    public Activity createActivity(Activity activity) {
        //将信息插入活动信息表
        if (activityDao.createActivity(activity) < 1) {
            //添加活动失败
            throw new CascadeException();
        }
        //将添加成功的活动Id赋给EventPlace
        int lastInsertId = activityDao.selectLastInsertId();
        //将信息插入活动地点表
        if (activityDao.createPlace(activity.getEventPlace(), lastInsertId) < 1) {
            //添加地点失败
            throw new CascadeException();
        }
        return activity;
    }

    @Override
    public List<Activity> selectActivitiesByUid(int uId) {
        //查询该用户参与的所有活动的id
        List<Integer> eventIds = activityMembersDao.selectJoinEventsIdByUid(uId);
        List<Activity> activities = new ArrayList<Activity>();
        for (Integer eventId : eventIds) {
            Activity activity = activityDao.selectActivityByEventId(eventId);
            //若该活动统计未结束，则显示出来
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
        if (activityMembersDao.delActivityMembersByEventId(eventId) < 1 ||
                activityDao.delActivityPlaceByEventId(eventId) < 1 ||
                timeDao.delMembersTimeByEventId(eventId) < 1) {
            //如果删除活动成员/选择时间/地点表相关信息失败
            throw new CascadeException();
        }
        if (activityDao.delActivity(eventId) < 1) {
            //如果删除该活动失败
            throw new CascadeException();
        }
    }
}
