package com.ctg.aatime.service;

import com.ctg.aatime.domain.Activity;

/**
 * 活动业务
 *
 * @author pjmike
 * @create 2018-03-16 16:40
 */
public interface ActivityService {
    /**
     * 新建活动
     *
     * @param activity
     * @return
     */
    Activity createActivity(Activity activity);
}
