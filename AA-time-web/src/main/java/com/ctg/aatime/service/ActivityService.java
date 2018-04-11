package com.ctg.aatime.service;

import com.ctg.aatime.domain.Activity;

import java.util.List;

/**
 * 活动业务
 *
 * @author pjmike
 * @create 2018-03-16 16:40
 */
public interface ActivityService {
    /**
     * 新建活动
     * @param activity
     * @return
     */
    Activity createActivity(Activity activity);

    /**
     * 查询该用户参与的所有未过期活动
     * @param uid
     * @return
     */
    List<Activity> selectActivitiesByUid(int uid);

    /**
     * 通过活动id在活动信息和活动地点表中查询活动
     * @param eventId
     * @return
     */
    Activity selectActivityByEventId(int eventId);

    /**
     * 通过活动id删除该活动在活动信息、活动地点、活动成员、时间选择表中的所有信息
     * @param eventId
     * @return
     */
    void delActivityByEventId(int eventId);
}
