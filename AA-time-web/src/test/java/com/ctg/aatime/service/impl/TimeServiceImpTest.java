package com.ctg.aatime.service.impl;

import com.ctg.aatime.service.TimeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * Created By Cx On 2018/4/18 17:11
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class TimeServiceImpTest {
    @Autowired
    TimeService timeService;

    @Test
    public void getRecommendTime() {
    }

    @Test
    public void getFreeTimeByUid() {
        System.out.println(timeService.getFreeTimeByUid(2,6));
    }
}