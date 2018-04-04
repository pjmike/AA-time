package com.ctg.aatime.dao;

import com.ctg.aatime.domain.Activity;
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
     * 查询该用户参与的所有活动的Id
     * @return
     */
    List<Integer> selectJoinEventsIdById(int uId);

    /**
     * 通过event_id查询活动
     * @return
     */
    Activity selectActivityById(int id);
}
