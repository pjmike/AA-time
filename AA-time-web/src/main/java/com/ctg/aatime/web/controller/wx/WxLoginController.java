package com.ctg.aatime.web.controller.wx;

import com.ctg.aatime.commons.utils.FormatResponseUtil;
import com.ctg.aatime.domain.User;
import com.ctg.aatime.commons.enums.ErrorMsgEnum;
import com.ctg.aatime.domain.dto.WxInfo;
import com.ctg.aatime.service.UserService;
import com.ctg.aatime.commons.utils.ResponseResult;
import com.ctg.api.impl.WxServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
@Slf4j
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

    @Autowired
    private UserService userService;

    private WxServiceImpl service = new WxServiceImpl();

    @ApiOperation(value = "微信小程序登录接口", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "encrytedData", value = "用户敏感信息加密数据", required = true, type = "String"),
            @ApiImplicitParam(name = "iv", value = "加密算法的初始向量", required = true, type = "String"),
            @ApiImplicitParam(name = "code", value = "临时登录凭证code码", required = true, type = "String")
    })
    @PostMapping(value = "/login",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult loginWeiXin(@RequestBody WxInfo wxInfo, HttpServletResponse response) throws Exception {
        //get openid and session_key
        Map<String, Object> map = service.getSessionInfo(wxInfo.getCode());
        //判断是否成功获取数据，如果map里包含errcode,则代表获取session_key失败
        //当map里包含session_key时才成功连接微信服务器并返回
        if (map.containsKey("errcode")) {
            String errcode = (String) map.get("errcode");
            log.info("微信返回的错误码{}", errcode);
            return FormatResponseUtil.error(ErrorMsgEnum.SERVER_FAIL_CONNECT);
        }

        if (map.containsKey("session_key")) {
            log.info("调用微信服务器成功");
            openid = (String) map.get("openid");
            session_key = (String) map.get("session_key");
            //对encryptedData加密数据进行AES解密,获取用户信息
            Map<String, Object> userInfoMap = service.getUserInfo(wxInfo.getEncrytedData(), session_key, wxInfo.getIv(), "UTF-8");
            //用户昵称
            String nickName = (String) userInfoMap.get("nickName");
            //用户头像
            String avatarUrl = (String) userInfoMap.get("avatarUrl");
            User user = new User(openid, nickName, avatarUrl);
            if (userService.findUserByOpenId(openid) == null) {
                userService.insertUser(user);
            }
        }
        //生成一个key
        String key = UUID.randomUUID().toString();
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //将session_key放入redis
        valueOperations.set(key, session_key.toString(), EXP_TIMES, TimeUnit.SECONDS);
        //将key放入请求头里传给前端进行本地保存
        response.setHeader("3rd_session", key);
        return FormatResponseUtil.formatResponse();
    }
}
