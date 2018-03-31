package com.ctg.aatime.service;

import com.ctg.aatime.domain.User;

/**
 * @author pjmike
 * @create 2018-03-16 14:42
 */
public interface UserService {
    /**
     * insert user
     * @param user user class
     * @return
     */
    User insertUser(User user);

    /**
     * find user by openid
     * @param openid
     * @return
     */
    User findUserByOpenId(String openid);
}
