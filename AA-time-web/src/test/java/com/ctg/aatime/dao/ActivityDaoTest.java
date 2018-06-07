package com.ctg.aatime.dao;

import com.ctg.aatime.domain.Activity;
import com.ctg.aatime.domain.ActivityMembers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class ActivityDaoTest {
    @Autowired
    private ActivityDao activityDao;

    private Logger logger = LoggerFactory.getLogger(ActivityDaoTest.class);

    @Test
    public void addQuitReason() {
        ActivityMembers activityMembers = new ActivityMembers();
        activityMembers.setUid(2);
        activityMembers.setEventId(16);
        activityMembers.setAvatar("hao");
        activityMembers.setNotes("再说");
        System.out.println(activityDao.addQuitReason(activityMembers,"我想回家"));
    }

    @Test
    public void reduceJoinNumByEventId(){
        System.out.println(activityDao.reduceMembersByEventId(16));
    }

    @Rollback
    @Test
    public void createActivity() throws Exception {
        Activity activity = new Activity();
        activity.setEventName("demo活动");
        activity.setEventBrief("demo活动简介");
        activity.setEventTag("开会");
        activity.setUid(2);
        int count = activityDao.createActivity(activity);
        logger.info("count : " + count);
        logger.info("activity id :"+activity.getEventId());
    }

    @Test
    public void selectActivityByEventId() throws Exception {
        Activity activity = activityDao.selectActivityByEventId(20);
        System.out.println(activity);
    }

    @Test
    public void updateLaunchInfo(){
        Activity activity = new Activity();
        activity.setLaunchWords("你好");
        activity.setLaunchEndTime(1527296808888L);
        activity.setLaunchStartTime(1527276808888L);
        activity.setLaunchTime(System.currentTimeMillis());
        activity.setEventId(35);
        System.out.println(activityDao.updateLaunchInfo(activity));
    }

    @Test
    public void updateActivity(){
        Activity activity = new Activity();
        activity.setEventId(54);
        activity.setEventBrief("hello");
        System.out.println(activityDao.updateActivity(activity));
    }



}
