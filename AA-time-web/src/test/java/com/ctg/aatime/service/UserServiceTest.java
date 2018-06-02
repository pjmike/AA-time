package com.ctg.aatime.service;

import com.ctg.aatime.domain.User;
import io.swagger.annotations.Authorization;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @Test
    public void selectUserByUid() throws Exception {
        User user = userService.selectUserByUid(2);
        System.out.println(user);
    }

    @Test
    @Transactional
    public void insertUser() {
        User user = new User("sdf","sdf","sdf");
        User result = userService.insertUser(user);
        System.out.println(result);
    }

    @Test
    public void changeName() {
        userService.changeUserNickName(2,"pjmike");
    }

}