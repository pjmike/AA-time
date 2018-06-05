package com.ctg.aatime.web.controller.weixin;

import com.alibaba.fastjson.JSON;
import com.ctg.aatime.commons.enums.ErrorMsgEnum;
import com.ctg.aatime.commons.utils.FormatResponseUtil;
import com.ctg.aatime.commons.utils.RedisOperator;
import com.ctg.aatime.commons.utils.ResponseResult;
import com.ctg.aatime.domain.User;
import com.ctg.aatime.domain.dto.WxInfo;
import com.ctg.aatime.service.UserService;
import com.ctg.api.impl.WxServiceImpl;
import com.ctg.utils.Md5Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 小程序登录操作
 *
 * @author pjmike
 * @create 2018-03-13 19:14
 */
@RestController
@Slf4j
public class LoginController {
    /**
     * 设置redis中key过期时间为15天
     */
    private static final long EXP_TIMES = 60 * 60 * 24 * 15;

    private final UserService userService;

    private final RedisOperator redis;
    @Autowired
    private RestTemplate restTemplate;


    @Autowired
    public LoginController(UserService userService, RedisOperator redis) {
        this.userService = userService;
        this.redis = redis;
    }

    /**
     * 用户微信登录并获取用户名和头像
     *
     * @param wxInfo 获取小程序数据的封装类
     * @param response 响应体
     * @return ResponseResult
     * @throws Exception 异常
     */
    @PostMapping(value = "/login",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult loginWeiXin(@RequestBody WxInfo wxInfo, HttpServletResponse response, HttpServletRequest request) throws Exception {
        WxServiceImpl service = new WxServiceImpl(restTemplate);

        //微信服务器返回的用户唯一id
        String openid = null;
        //微信服务器生成的针对用户数据加密签名的密钥
        String sessionKey = null;

        // get openid and sessionKey
        Map<String, Object> map = service.getSessionInfo(wxInfo.getCode());
        System.out.println(JSON.toJSON(map));
        //判断是否成功获取数据，如果map里包含errcode,则代表获取session_key失败
        //当map里包含session_key时才成功连接微信服务器并返回
        if (map.containsKey("errcode")) {
            Integer errcode = (Integer) map.get("errcode");
            log.info("微信返回的错误码: {}", errcode);
            log.info("错误信息:{}",map.get("errmsg"));
            return FormatResponseUtil.error(ErrorMsgEnum.SERVER_FAIL_CONNECT);
        }
        User user = null;
        log.info("session_key: {}",map.containsKey("session_key"));
        if (map.containsKey("session_key")) {
            log.info("调用微信服务器成功");
            openid = (String) map.get("openid");
            sessionKey = (String) map.get("session_key");
            //对encryptedData加密数据进行AES解密,获取用户信息
            Map<String, Object> userInfoMap = service.getUserInfo(wxInfo.getEncryptedData(), sessionKey, wxInfo.getIv(), "UTF-8");
            System.out.println(JSON.toJSON(userInfoMap));
            String nickName = (String) userInfoMap.get("nickName");

            String avatarUrl = (String) userInfoMap.get("avatarUrl");

            user = new User(openid, nickName, avatarUrl);
            if (userService.findUserByOpenId(openid) == null) {
                user.setNickname(nickName);
                user = userService.insertUser(user);
            }
            log.info("user result:{}",user.toString());

        }
        user = userService.findUserByOpenId(openid);
        log.info("user :{}",user);
        //使用MD5生成一个key,返回给前端
        String key = Md5Util.getMD5(sessionKey);
        //将session_key放入redis
        redis.set(key, sessionKey, EXP_TIMES);
        //将user id放入session中
        HttpSession session = request.getSession();
        session.setAttribute("userId",user.getId());

        //将key放入请求头里传给前端进行本地保存
        response.setHeader("token", key);
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers","token");
        Map<String, Object> responseResult = new HashMap<>(16);
        responseResult.put("id", user.getId());
        responseResult.put("openid", openid);
        return FormatResponseUtil.formatResponse(responseResult);
    }
}