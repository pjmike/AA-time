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

    /**
     * 查询用户
     *
     * @param id
     * @return
     */
    User selectUserByUid(Integer id);

    /**
     * 修改昵称
     *
     * @param id
     * @param nickname
     */
    void changeUserNickName(Integer id,String nickname);
}
