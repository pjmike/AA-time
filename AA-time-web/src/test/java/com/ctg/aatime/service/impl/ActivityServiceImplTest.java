package com.ctg.aatime.service.impl;

import com.ctg.aatime.domain.Activity;
import com.ctg.aatime.service.ActivityService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Transactional
public class ActivityServiceImplTest {
    @Autowired
    private ActivityService activityService;

    @Rollback(value = false)
    @Test
    public void createActivity() throws Exception {
        Activity activity = new Activity();
        activity.setEventName("demo活动");
        activity.setEventBrief("demo活动简介");
        activity.setEventTag("开会");
        activity.setEventPlace("B楼");
        activity.setUid(2);

        Activity result = activityService.createActivity(activity);
        System.out.println(result);
    }

    @Test
    public void selectActivitiesByUid() throws Exception {
        List<Activity> activity = activityService.selectActivitiesByUid(2);
        System.out.println("结果是:"+activity);
    }

    @Test
    @Rollback(false)
    public void delActivityByEventId() throws Exception {
        try {
            activityService.delActivityByEventId(10);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
