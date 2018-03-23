package com.ctg.aatime.dao;

import com.ctg.aatime.domain.Time;

import java.util.List;

/**
 * @author pjmike
 * @create 2018-03-23 16:42
 */
public interface TimeDao {

    int insertTimeList(List<Time> times);
}
