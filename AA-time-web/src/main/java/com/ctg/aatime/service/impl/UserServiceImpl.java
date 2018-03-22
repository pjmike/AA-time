package com.ctg.aatime.service.impl;

import com.ctg.aatime.dao.UserDao;
import com.ctg.aatime.domain.User;
import com.ctg.aatime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author pjmike
 * @create 2018-03-16 14:43
 */
@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserDao userDao;
    @Override
    public int insertUser(User user) {
        return userDao.insertUser(user);
    }
}
