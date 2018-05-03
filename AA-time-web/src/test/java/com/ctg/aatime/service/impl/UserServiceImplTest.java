package com.ctg.aatime.service.impl;

import com.ctg.aatime.domain.User;
import com.ctg.aatime.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTest {
    @Autowired
    private UserService userService;
    @Test
    public void changeUserNickName() throws Exception {
        userService.changeUserNickName(2,"pjmike");
    }

    @Test
    public void findUser() {
        User user = userService.selectUserByUid(2);
        Assert.assertNotNull(user);
    }
}