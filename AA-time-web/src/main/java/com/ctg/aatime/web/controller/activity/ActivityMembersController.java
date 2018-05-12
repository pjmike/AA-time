package com.ctg.aatime.web.controller.activity;

import com.ctg.aatime.commons.enums.ErrorMsgEnum;
import com.ctg.aatime.commons.utils.FormatResponseUtil;
import com.ctg.aatime.commons.utils.ResponseResult;
import com.ctg.aatime.domain.ActivityMembers;
import com.ctg.aatime.service.ActivityMembersService;
import com.ctg.aatime.service.TimeService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 活动成员有关接口
 * Created By Cx On 2018/4/17 15:36
 */
@RestController
@RequestMapping("/activityMember")
public class ActivityMembersController {
    @Autowired
    private ActivityMembersService activityMembersService;
    @Autowired
    private TimeService timeService;

    /**
     * 用户退出活动接口
     * 必传参数：json1(member):uid(用户id)，eventId（退出的活动id）
     * json2（reason）：reason（退出活动原因）
     */
    @DeleteMapping("/event")
    public ResponseResult quitEvent(@RequestBody Map<String,String> data){
        Gson g = new Gson();
        ActivityMembers member = g.fromJson(data.get("member"),ActivityMembers.class);
        String reason = g.fromJson(data.get("reason"),String.class);
        activityMembersService.quitActivity(member,reason);
        return FormatResponseUtil.formatResponse();
    }

    /**
     * 用户参与活动接口
     * 必传参数：uid（用户id），eventId（参与活动Id）
     */
    @PostMapping("/{uid}/{eventId}")
    public ResponseResult joinEvent(@PathVariable("uid") int uid, @PathVariable("eventId")int eventId){
        if (activityMembersService.insertActivityMember(uid,eventId) != null) return FormatResponseUtil.formatResponse();
        else return FormatResponseUtil.error(ErrorMsgEnum.SERVER_FAIL_CONNECT);
    }

    /**
     * 获取用户选择的空闲时间
     * 必传参数：uid（用户id），eventId（参与活动Id）
     */
    @GetMapping("/freeTime/{uid}/{eventId}")
    public ResponseResult findFree(@PathVariable("uid") int uid, @PathVariable("eventId")int eventId){
        return FormatResponseUtil.formatResponse(timeService.getFreeTimeByUid(uid,eventId));
    }

}
