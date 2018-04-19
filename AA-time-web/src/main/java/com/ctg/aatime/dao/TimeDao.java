package com.ctg.aatime.dao;

import com.ctg.aatime.domain.Time;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

/**
 * @author pjmike
 * @create 2018-03-23 16:42
 */
public interface TimeDao {
    /**
     * 插入时间段
     *
     * @param times
     * @return
     */
    int insertTimeList(List<Time> times);

    /**
     * 通过活动Id从时间选择表中删除所有该活动的时间选择数据
     * @return
     */
    int delMembersTimeByEventId(int eventId);

    /**
     * 通过uid和eventId在时间选择表中查询参与成员所有空闲时间
     * key表示开始时间，value表示结束时间，一个map只返回一个结果
     * 若无查询结果，返回空列表
     * @return
     */
    List<HashMap<String,Long>> selectFreeTimes(@Param("uid")int uid, @Param("eventId")int eventId);

    /**
     * 通过uid，eventId从时间选择表中删除该用户该活动的时间选择数据
     * @return
     */
    int delMembersTimeByUId(@Param("uid") int uid,@Param("eventId") int eventId);
}
