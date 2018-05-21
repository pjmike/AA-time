package com.ctg.aatime.web.controller.activity;

import com.ctg.aatime.commons.enums.ErrorMsgEnum;
import com.ctg.aatime.commons.utils.FormatResponseUtil;
import com.ctg.aatime.commons.utils.ResponseResult;
import com.ctg.aatime.domain.ActivityMembers;
import com.ctg.aatime.domain.Time;
import com.ctg.aatime.service.ActivityMembersService;
import com.ctg.aatime.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
    private final ActivityMembersService activityMembersService;

    private final TimeService timeService;

    @Autowired
    public ActivityMembersController(ActivityMembersService activityMembersService, TimeService timeService) {
        this.activityMembersService = activityMembersService;
        this.timeService = timeService;
    }


    /**
     * 用户退出活动接口
     *
     * @param eventId 用户id
     * @param uid 退出的活动id
     * @param reason 退出活动原因
     * @return ResponseResult
     */
    @DeleteMapping("/event/{uid}/{eventId}")
    public ResponseResult quitEvent(@PathVariable("eventId")Integer eventId,@PathVariable("uid")Integer uid,
                                    @RequestBody String reason) {
        ActivityMembers member = new ActivityMembers(eventId,uid);
        activityMembersService.quitActivity(member, reason);
        return FormatResponseUtil.formatResponse();
    }

    /**
     * 用户参与活动接口/用户修改空闲时间接口
     *
     * @param uid     用户id
     * @param eventId 参与活动Id
     * @param freeTime 用户空闲时间(传入参数startTime , endTime表示每段空闲时间的开始时间和结束时间)
     */
    @PostMapping(value = "/{uid}/{eventId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult joinEvent(@PathVariable("uid") int uid, @PathVariable("eventId") int eventId, @RequestBody List<Time> freeTime) {
        if (activityMembersService.insertActivityMember(uid, eventId, freeTime) != null) {
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
