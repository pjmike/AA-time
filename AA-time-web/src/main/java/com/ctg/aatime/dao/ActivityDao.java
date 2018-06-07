package com.ctg.aatime.dao;

import com.ctg.aatime.domain.Activity;
import com.ctg.aatime.domain.ActivityMembers;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author pjmike
 */
public interface ActivityDao {
    /**
     * 新建活动
     *
     * @return
     */
    int createActivity(Activity activity);

    /**
     * 更新活动信息
     * @param activity
     * @return
     */
    int updateActivity(Activity activity);

    /**
     * 从活动信息表通过event_id查询活动
     *
     * @return
     */
    Activity selectActivityByEventId(int eventId);

    /**
     * 通过该Id从活动信息表中删除该活动
     *
     * @return
     */
    int delActivity(int eventId);

    /**
     * 从活动信息表中将活动报名人数减一
     */
    int reduceMembersByEventId(int eventId);

    /**
     * 从活动信息表中将活动报名人数加一
     */
    int addMembersByEventId(int eventId);

    /**
     * 添加成员退出原因进退出活动记录表
     *
     * @return
     */
    int addQuitReason(@Param("member") ActivityMembers member, @Param("reason") String reason);

    /**
     * 通过eventId删除退出活动记录表有关信息
     * @return
     */
    int delQuitInfoByEventId(@Param("eventId")int eventId);

    /**
     * 通过eventId在活动信息表中更新发布活动信息
     * @return
     */
    int updateLaunchInfo(Activity activity);

    /**
     * 通过uId在活动信息表查询该用户创建的未发布的活动，按统计截止时间的升序排序
     */
    List<Activity> selectEstablishedActivitiesByUid(@Param("uId")int uId);

}
