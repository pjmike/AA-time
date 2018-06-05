package com.ctg.aatime.web.controller.activity;

import com.ctg.Bean.WxTemplateMessage;
import com.ctg.aatime.commons.enums.ErrorMsgEnum;
import com.ctg.aatime.commons.utils.Constants;
import com.ctg.aatime.commons.utils.FormatResponseUtil;
import com.ctg.aatime.commons.utils.RedisOperator;
import com.ctg.aatime.commons.utils.ResponseResult;
import com.ctg.aatime.domain.Activity;
import com.ctg.aatime.domain.ActivityMembers;
import com.ctg.aatime.domain.Time;
import com.ctg.aatime.domain.User;
import com.ctg.aatime.service.ActivityMembersService;
import com.ctg.aatime.service.ActivityService;
import com.ctg.aatime.service.TimeService;
import com.ctg.aatime.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 活动成员有关接口
 * Created By Cx On 2018/4/17 15:36
 *
 * @author Cx
 */
@RestController
@RequestMapping("/activityMember")
public class ActivityMembersController {
    @Autowired
    private RedisOperator redisOperator;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private final UserService userService;
    private final ActivityMembersService activityMembersService;
    private final ActivityService activityService;
    private final TimeService timeService;

    @Autowired
    public ActivityMembersController(UserService userService, ActivityMembersService activityMembersService, ActivityService activityService, TimeService timeService) {
        this.userService = userService;
        this.activityMembersService = activityMembersService;
        this.activityService = activityService;
        this.timeService = timeService;
    }


    /**
     * 用户退出活动接口
     *
     * @param eventId 用户id
     * @param uid 退出的活动id
     * @param map 退出活动原因
     * @return ResponseResult
     */
    @PostMapping("/event/{uid}/{eventId}")
    public ResponseResult quitEvent(@PathVariable("eventId")Integer eventId,@PathVariable("uid")Integer uid,
                                    @RequestBody Map<String,String> map) {
        String reason = map.get("reason");
        ActivityMembers member = new ActivityMembers(eventId,uid);
        activityMembersService.quitActivity(member, reason);

        //使用websocket发送消息
        User user = userService.selectUserByUid(uid);
        Activity activity = activityService.selectActivityByEventId(eventId);
        Map<String, Object> response = new HashMap<>(16);
        response.put("username", user.getId());
        response.put("avatar", user.getAvatar());
        response.put("eventName", activity.getEventName());
        response.put("time", System.currentTimeMillis());
        response.put("operation", "退出");
        if (redisOperator.get(Constants.WEBSOCKET_ACCOUNT + activity.getUid()) != null) {
            messagingTemplate.convertAndSendToUser(activity.getUid().toString(),"/queue/notification",response);
        }
        return FormatResponseUtil.formatResponse();
    }
    /**
     * 用户参与活动接口/用户修改空闲时间接口
     *
     * @param uid     用户id
     * @param eventId 参与活动Id
     * @param map 用户空闲时间(传入参数startTime , endTime表示每段空闲时间的开始时间和结束时间)
     */
    @PostMapping(value = "/{uid}/{eventId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult joinEvent(@PathVariable("uid") int uid, @PathVariable("eventId") int eventId, @RequestBody Map<String,Object> map) {
        //当用户提交表单时
        //模板消息所需字段
        String openid = (String) map.get("openid");
        String form_id = (String) map.get("form_id");
        String path = (String) map.get("path");
        WxTemplateMessage templateMessage = new WxTemplateMessage(openid, path, form_id);
        redisOperator.setForSet(eventId+"-message",templateMessage);

        List<Time> freeTime = (List<Time>) map.get("freeTime");
        if (activityMembersService.insertActivityMember(uid, eventId, freeTime) != null) {
            //发送通知
            User user = userService.selectUserByUid(uid);
            Activity activity = activityService.selectActivityByEventId(eventId);
            Map<String, Object> response = new HashMap<>(16);
            response.put("username", user.getId());
            response.put("avatar", user.getAvatar());
            response.put("eventName", activity.getEventName());
            response.put("time", System.currentTimeMillis());
            response.put("operation", "参加");

            if (redisOperator.get(Constants.WEBSOCKET_ACCOUNT + activity.getUid()) != null) {
                messagingTemplate.convertAndSendToUser(activity.getUid().toString(),"/queue/notification",response);
            }

            return FormatResponseUtil.formatResponse();
        } else {
            return FormatResponseUtil.error(ErrorMsgEnum.SERVER_FAIL_CONNECT);
        }

    }

    /**
     * 获取用户选择的空闲时间
     *
     * @param uid     用户id
     * @param eventId 参与活动Id
     * @return ResponseResult
     */
    @GetMapping("/freeTime/{uid}/{eventId}")
    public ResponseResult findFree(@PathVariable("uid") int uid, @PathVariable("eventId") int eventId) {
        return FormatResponseUtil.formatResponse(timeService.getFreeTimeByUid(uid, eventId));
    }

}
