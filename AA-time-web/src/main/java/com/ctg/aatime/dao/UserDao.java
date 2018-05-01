package com.ctg.aatime.dao;

import com.ctg.aatime.domain.User;

/**
 * @author pjmike
 * @create 2018-03-16 14:41
 */
public interface UserDao {
    /**
     * insert user
     *
     * @param user user class
     * @return
     */
    int insertUser(User user);

    /**
     * select user by id
     *
     * @param OpenId
     * @return
     */
    User selectUserByOpenId(String OpenId);

    /**
     * 通过uid查找user
     */
    User selectUserByUid(int uid);
}
