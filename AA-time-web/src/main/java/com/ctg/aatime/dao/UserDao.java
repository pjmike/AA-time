package com.ctg.aatime.dao;

import com.ctg.aatime.domain.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

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

    /**
     * 修改用户昵称
     *
     * @param nickname
     * @param id
     * @return
     */
    int updateUserNickName(@Param("id")Integer id,@Param("nickname") String nickname);
}
