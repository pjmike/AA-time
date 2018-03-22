package com.ctg.aatime.dao;

import com.ctg.aatime.domain.User;

/**
 * @author pjmike
 * @create 2018-03-16 14:41
 */
public interface UserDao {
    /**
     * 插入用户
     *
     * @param user
     * @return
     */
    int insertUser(User user);

    User findUserByopenId(String OpenId);
}
