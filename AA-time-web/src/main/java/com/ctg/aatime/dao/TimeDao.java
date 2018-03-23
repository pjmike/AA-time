package com.ctg.aatime.dao;

import com.ctg.aatime.domain.Time;

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
}
