package com.ctg.aatime.service.impl;

import com.ctg.aatime.commons.exception.CascadeException;
import com.ctg.aatime.dao.ActivityDao;
import com.ctg.aatime.dao.UserDao;
import com.ctg.aatime.domain.Activity;
import com.ctg.aatime.service.ActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author pjmike
 * @create 2018-03-16 16:41
 */
@Service
@Slf4j
public class ActivityServiceImpl implements ActivityService{
    @Autowired
    private ActivityDao activityDao;
    @Autowired
    private UserDao userDao;

    @Override
    public Activity createActivity(Activity activity) {
        try {
            //将信息插入活动信息表
            int insertCount = activityDao.createActivity(activity);
            if (insertCount < 1) {
                //添加活动失败
                throw new CascadeException();
            }
            //将添加成功的活动Id赋给EventPlace
            int lastInsertId = activityDao.selectLastInsertId();
            //将信息插入活动地点表
            insertCount = activityDao.createPlace(activity.getEventPlace(),lastInsertId);
            if (insertCount < 1){
                //添加地点失败
                throw  new CascadeException();
            }
        } catch (CascadeException e){
            throw e;
        }
        return activity;
    }

    @Override
    public List<Activity> selectActivitiesById(int uId) {
        //查询该用户参与的所有活动的id
        List<Integer> eventIds = activityDao.selectJoinEventsIdByUid(uId);
        List<Activity> activities = new ArrayList<Activity>();
        for (Integer eventId : eventIds) {
            Activity activity = activityDao.selectActivityByEventId(eventId);
            //若该活动未过期，则显示出来
            if( activity != null && activity.getEndTime() > new Date().getTime() ){
                activities.add(activity);
            }
        }
        return activities;
    }
}
