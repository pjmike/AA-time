package com.ctg.aatime.service.impl;

import com.ctg.aatime.dao.ActivityDao;
import com.ctg.aatime.domain.Activity;
import com.ctg.aatime.service.ActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author pjmike
 * @create 2018-03-16 16:41
 */
@Service
@Slf4j
public class ActivityServiceImpl implements ActivityService{
    @Autowired
    private ActivityDao activityDao;
    @Override
    public Activity createActivity(Activity activity) {
        int count = activityDao.createActivity(activity);
        return activity;
    }
}
