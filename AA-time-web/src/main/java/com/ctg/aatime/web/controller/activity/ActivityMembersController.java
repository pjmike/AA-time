package com.ctg.aatime.web.controller.activity;

import com.ctg.aatime.commons.enums.ErrorMsgEnum;
import com.ctg.aatime.commons.utils.FormatResponseUtil;
import com.ctg.aatime.commons.utils.ResponseResult;
import com.ctg.aatime.domain.ActivityMembers;
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
    @DeleteMapping("/event")
    public ResponseResult quitEvent(@RequestParam("uid") int uid, @RequestParam("eventId")int eventId, @RequestParam("reason")String reason){
        activityMembersService.quitActivity(uid,eventId,reason);
        return FormatResponseUtil.formatResponse();
    }

    @PostMapping()
    public ResponseResult joinEvent(@RequestParam("uid") int uid, @RequestParam("eventId")int eventId){
        if (activityMembersService.insertActivityMember(uid,eventId) != null) return FormatResponseUtil.formatResponse();
        else return FormatResponseUtil.error(ErrorMsgEnum.SERVER_FAIL_CONNECT);
    }

    @GetMapping("/freeTime")
    public ResponseResult findFree(@RequestParam("uid") int uid, @RequestParam("eventId")int eventId){
        return FormatResponseUtil.formatResponse(timeService.getFreeTimeByUid(uid,eventId));
    }

}
