package com.ctg.aatime.dao;

import com.ctg.aatime.domain.Activity;
import com.ctg.aatime.domain.ActivityMembers;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 新建活动地点
     * @return
     */
    int createPlace(@Param("place") String place, @Param("eventId") int eventId);

    /**
     * 查询最新生成的主键Id值
     */
    int selectLastInsertId();

    /**
     * 从活动信息表和活动地点表通过event_id查询活动
     * @return
     */
    Activity selectActivityByEventId(int eventId);

    /**
     * 通过该Id从活动地点表中删除该活动的活动地点信息
     * @return
     */
    int delActivityPlaceByEventId(int eventId);

    /**
     * 通过该Id从活动信息表中删除该活动
     * @return
     */
    int delActivity(int eventId);

    /**
     * 从活动信息表中将活动参与人数减一
     */
    int reduceJoinNumByEventId(int eventId);

    /**
     * 添加成员退出原因进退出活动记录表
     * TODO sql语句可能不对
     * @return
     */
    int addQuitReason(@Param("member") ActivityMembers member,@Param("reason") String reason,@Param("eventId") int eventId);
}
