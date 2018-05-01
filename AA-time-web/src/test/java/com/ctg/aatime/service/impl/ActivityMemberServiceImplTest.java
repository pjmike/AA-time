package com.ctg.aatime.service.impl;

import com.ctg.aatime.dao.ActivityMembersDao;
import com.ctg.aatime.domain.ActivityMembers;
import com.ctg.aatime.service.ActivityMembersService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created By Cx On 2018/4/6 8:27
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ActivityMemberServiceImplTest {
    @Autowired
    private ActivityMembersService activityMembersService;

    @Test
    public void selectJoinMembersByEventId() {
        List<ActivityMembers> members = activityMembersService.selectJoinMembersByEventId(15);
        for (ActivityMembers member : members) {
            System.out.println(member);
        }
    }

    @Test
    public void quitActivity(){
        ActivityMembers activityMembers = activityMembersService.quitActivity(3,27,"再见");
        System.out.println(activityMembers);
    }
}