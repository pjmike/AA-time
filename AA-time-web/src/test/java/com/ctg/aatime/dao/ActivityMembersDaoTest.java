package com.ctg.aatime.dao;

import com.ctg.aatime.domain.ActivityMembers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ActivityMembersDaoTest {
    @Autowired
    private ActivityMembersDao activityMembersDao;
    @Autowired
    private TimeDao timeDao;
    private Logger logger = LoggerFactory.getLogger(ActivityMembersDaoTest.class);
    @Test
    public void insertActivityMembers() throws Exception {
        ActivityMembers activityMembers = new ActivityMembers();
        activityMembers.setUsername("pjmike");
        activityMembers.setEventId(6);
        activityMembers.setUid(2);
        System.out.println(activityMembers);
        activityMembersDao.insertActivityMembers(activityMembers);
    }

    @Test
    public void selectJoinEventsIdByUid() throws Exception {
        List<Integer> eventIds = activityMembersDao.selectJoinEventsIdByUid(2);
        for (int id : eventIds){
            System.out.println(id);
        }
    }

    @Test
    public void selectFreeTimes() throws Exception {
        List<HashMap<String,Long>> h = timeDao.selectFreeTimes(2,6);
        for (HashMap<String,Long> hh : h) {
            for (String key : hh.keySet()){
                System.out.println(hh);
            }
        }
    }

    @Test
    public void selectActivityMembersByEventId() throws Exception {
        List<ActivityMembers> l = activityMembersDao.selectActivityMembersByEventId(9);
        System.out.println(l);
    }
}
