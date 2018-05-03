package com.ctg.aatime.web.controller.user;

import com.ctg.aatime.commons.enums.ErrorMsgEnum;
import com.ctg.aatime.commons.utils.FormatResponseUtil;
import com.ctg.aatime.commons.utils.ResponseResult;
import com.ctg.aatime.domain.User;
import com.ctg.aatime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 个人中心
 *
 * @author pjmike
 * @create 2018-05-02 11:10
 */
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/user/{id}")
    public ResponseResult userCenter(@PathVariable("id") Integer id) {
        User user = userService.selectUserByUid(id);
        if (user == null) {
            FormatResponseUtil.error(ErrorMsgEnum.USER_NOT_FOUND);
        }
        return FormatResponseUtil.formatResponse(user);
    }

    @PutMapping("/user/{id}")
    public ResponseResult changeNickName(@PathVariable("id") Integer id, @RequestParam("nickname") String nickname) {
        userService.changeUserNickName(id, nickname);
        return FormatResponseUtil.formatResponse();
    }
}
