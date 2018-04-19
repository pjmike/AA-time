package com.ctg.aatime.dao;

import com.ctg.aatime.domain.ActivityMembers;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @author pjmike
 * @create 2018-03-24 10:59
 */
public interface ActivityMembersDao {
    /**
     * 插入活动人员
     * @param activityMembers
     * @return
     */
    int insertActivityMembers(ActivityMembers activityMembers);

    /**
     * 从活动成员表查询该用户参与的所有活动的Id
     * @return
     */
    List<Integer> selectJoinEventsIdByUid(int uId);

    /**
     * 通过eventId在活动成员表中查询所有参与用户
     * 若无查询结果，返回空列表
     * @param eventId
     * @return
     */
    List<ActivityMembers> selectActivityMembersByEventId(int eventId);

    /**
     * 通过uid和eventId在活动成员表中找出唯一符合条件的活动成员
     */
    ActivityMembers selectActivityMembersByUEid(@Param("uid") int uid,@Param("eventId") int eventId);

    /**
     * 通过该Id从活动成员表中删除所有参与该活动的成员
     * @return
     */
    int delActivityMembersByEventId(int eventId);

    /**
     * 从活动成员表中删除该活动下的该成员
     * @return
     */
    int quitActivityByUid(@Param("uid") int uid,@Param("eventId") int eventId);
}
