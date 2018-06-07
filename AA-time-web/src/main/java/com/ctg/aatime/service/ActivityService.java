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
     *
     * @param activity
     * @return Activity
     */
    Activity createActivity(Activity activity);

    /**
     * 更新活动信息
     * @param activity
     */
    void updateActivity(Activity activity);

    /**
     * 查询该用户所有仅参与（不包含创建）的未发布未过期活动
     */
    List<Activity> selectJoinActivitiesByUid(int uid);

    /**
     * 通过活动id在活动信息和活动地点表中查询活动
     * @param eventId
     * @return
     */
    Activity selectActivityByEventId(int eventId);

    /**
     * 通过活动id删除该活动在活动信息、活动地点、活动成员、时间选择表中的所有信息
     *
     * @param eventId
     * @return
     */
    void delActivityByEventId(int eventId);

    /**
     * 发布活动时更新活动信息的发布时间和发布留言（默认为0，null）
     * @return
     */
    int launchActivity(Activity activity);

    /**
     * 查询该用户所有参与的未过期已发布的活动
     */
    List<Activity> selectLaunchActivitiesByUid(int uId);

    /**
     * 查询该用户参与的已过期活动
     */
    List<Activity> selectDeadActivitiesByUid(int uId);

    /**
     * 查询该用户创建的未发布的未过期的活动
     */
    List<Activity> selectEstablishedActivitiesByUid(int uId);
}
