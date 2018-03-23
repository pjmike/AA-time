package com.ctg.aatime.dao;

import com.ctg.aatime.domain.Activity;

/**
 * @author pjmike
 * @create 2018-03-16 16:42
 */
public interface ActivityDao {
    /**
     * 新建活动
     *
     * @return
     */
    int createActivity(Activity activity);
}
