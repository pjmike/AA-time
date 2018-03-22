package com.ctg.aatime.web.controller.wx;

import com.ctg.aatime.domain.User;
import com.ctg.aatime.commons.enums.ErrorMsgEnum;
import com.ctg.aatime.service.UserService;
import com.ctg.aatime.commons.utils.ResponseResult;
import com.ctg.api.impl.WxServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 小程序登录操作
 *
 * @author pjmike
 * @create 2018-03-13 19:14
 */
@RestController
public class WxLoginController {
    /**
     * 微信服务器返回的用户唯一id
     */
    private String openid;
    /**
     * 微信服务器生成的针对用户数据加密签名的密钥
     */
    private String session_key;

    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 设置redis中key过期时间为15天
     */
    private static final long EXP_TIMES = 60 * 60 * 24 * 15;

    private static Logger logger = LoggerFactory.getLogger(WxLoginController.class);
    @Autowired
    private UserService userService;

    private WxServiceImpl service;
    @ApiOperation(value = "微信小程序登录接口", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, type = "String"),
            @ApiImplicitParam(name = "avatar", value = "用户头像", required = true, type = "String"),
            @ApiImplicitParam(name = "code", value = "code码", required = true, type = "String")
    })
    @PostMapping("/login")
    public ResponseResult loginWeiXin(@RequestParam("username") String username, @RequestParam("avatar") String avatar,
                                      @RequestParam("code") String code, HttpServletResponse response) {

        Map<String, Object> map = service.get(code);
        if (map.containsKey("errcode")) {
            String errcode = (String) map.get("errcode");
            logger.info("微信返回的错误码{}", errcode);
            return new ResponseResult(1, ErrorMsgEnum.SEVER_FAIL.getMsg(), null);
        } else if (map.containsKey("session_key")) {
            logger.info("调用微信服务器成功");
            openid = (String) map.get("openid");
            session_key = (String) map.get("session_key");
            //先判断用户数据里是否含有openid，如果没有再插入
            //TODO
            User user = new User(openid, username, avatar);
            userService.insertUser(user);
        }
        StringBuilder value = new StringBuilder();
        value.append(openid);
        value.append("," + session_key);
        //key
        String key = UUID.randomUUID().toString();
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //将openid和session_key放入redis
        valueOperations.set(key, value.toString(), EXP_TIMES, TimeUnit.SECONDS);
        //将key放入请求头里传给前端进行本地保存
        response.setHeader("3rd_session", key);
        return new ResponseResult(0, "登录成功", null);
    }
}
