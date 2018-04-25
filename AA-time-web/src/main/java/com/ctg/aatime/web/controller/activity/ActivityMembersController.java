package com.ctg.aatime.web.controller.activity;

import com.ctg.aatime.commons.utils.FormatResponseUtil;
import com.ctg.aatime.commons.utils.ResponseResult;
import com.ctg.aatime.service.ActivityMembersService;
import com.ctg.aatime.service.TimeService;
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
     */
    @DeleteMapping("/quit")
    public ResponseResult quitEvent(@RequestParam("uId") int uid, @RequestParam("eventId")int eventId, @RequestParam("reason")String reason){
        activityMembersService.quitActivity(uid,eventId,reason);
        return FormatResponseUtil.formatResponse();
    }

    @GetMapping("/freeTime")
    public ResponseResult findFree(@RequestParam("uId") int uid, @RequestParam("eventId")int eventId){
        return FormatResponseUtil.formatResponse(timeService.getFreeTimeByUid(uid,eventId));
    }

}
