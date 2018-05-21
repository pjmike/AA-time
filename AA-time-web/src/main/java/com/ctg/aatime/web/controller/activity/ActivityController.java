package com.ctg.aatime.web.controller.activity;

import com.ctg.aatime.commons.enums.ErrorMsgEnum;
import com.ctg.aatime.commons.utils.FormatResponseUtil;
import com.ctg.aatime.commons.utils.ResponseResult;
import com.ctg.aatime.domain.Activity;
import com.ctg.aatime.domain.ActivityMembers;
import com.ctg.aatime.domain.dto.RecommendTimeInfo;
import com.ctg.aatime.service.ActivityMembersService;
import com.ctg.aatime.service.ActivityService;
import com.ctg.aatime.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 活动有关接口
 * Created By Cx On 2018/4/4 20:32
 * @author Cx
 */
@RestController
@RequestMapping(value = "/activity")
public class ActivityController {

    private final ActivityService activityService;
    private final TimeService timeService;

    @Autowired
    public ActivityController(ActivityService activityService, TimeService timeService) {
        this.activityService = activityService;
        this.timeService = timeService;
    }


    /**
     * 创建活动接口
     *
     * <p>
     * 必传参数：eventName（活动名称）、uid（创建者Id）、startTime（可选投票开始时间戳）、endTime（可选投票结束时间戳）
     * statisticTime（投票截止时间戳）
     * 可选参数：eventBrief（活动简介）、eventTag（活动标签）、eventPlace（活动地点）、minTime（最小所需时间戳：默认为0）
     * members（最小参与人数：默认为0）
     * </p>
     *
     * @param activity 活动类
     * @return ResponseResult
     */
    @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult createActivity(@RequestBody Activity activity) {
        activity = activityService.createActivity(activity);
        if (activity == null) {
            return FormatResponseUtil.error(ErrorMsgEnum.SERVER_FAIL_CONNECT);
        }
        return FormatResponseUtil.formatResponseDomain(activity);
    }

    /**
     * 发布活动接口
     *
     * <p>
     * 必传参数：eventId(活动Id)，launchStartTime（活动开始时间）,launchEndTime(活动结束时间)
     * 可选参数：launchWords（发布留言）
     * </p>
     *
     * @param activity 活动的信息
     * @return 返回成功
     */
    @PostMapping(value = "/launchInfo",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult launchActivity(@RequestBody Activity activity) {
        if (activityService.launchActivity(activity) < 1) {
            return FormatResponseUtil.error(ErrorMsgEnum.SERVER_FAIL_CONNECT);
        } else {
            return FormatResponseUtil.formatResponse();
        }
    }


    /**
     * 查询该用户参与的所有未过期活动(包括未发布的)
     *
     * @param uId 用户id
     * @return 用户参加的所有未过期活动
     */
    @GetMapping("/liveList/{uid}")
    public ResponseResult liveListByUid(@PathVariable("uid") int uId) {
        List<Activity> activities = activityService.selectLiveActivitiesByUid(uId);
        return FormatResponseUtil.formatResponseDomain(activities);
    }


    /**
     * 查询该用户参与的已发布的未过期活动
     *
     * @param uId 用户id
     * @return 已发布的未过期活动
     */
    @GetMapping("/launchList/{uid}")
    public ResponseResult launchListByUid(@PathVariable("uid") int uId) {
        List<Activity> activities = activityService.selectLaunchActivitiesByUid(uId);
        return FormatResponseUtil.formatResponseDomain(activities);
    }

    /**
     * 查询该用户参与的已过期活动
     *
     * @param uId 用户id
     * @return 已过期的活动
     */
    @GetMapping("/deadList/{uid}")
    public ResponseResult deadListByUid(@PathVariable("uid") int uId) {
        List<Activity> activities = activityService.selectDeadActivitiesByUid(uId);
        return FormatResponseUtil.formatResponseDomain(activities);
    }

    /**
     * 查询该用户创建的活动（不管是否发布/过期）
     *
     * @param uId 用户id
     * @return 创建的活动
     */
    @GetMapping("/establishmentList/{uid}")
    public ResponseResult establishmentListByUid(@PathVariable("uid") int uId) {
        List<Activity> activities = activityService.selectEstablishedActivitiesByUid(uId);
        return FormatResponseUtil.formatResponse(activities);
    }


    /**
     * 删除活动
     *
     * @param eventId 活动id
     * @return 返回成功
     */
    @DeleteMapping("/{eventId}")
    public ResponseResult delActivity(@PathVariable("eventId") int eventId) {
        activityService.delActivityByEventId(eventId);
        return FormatResponseUtil.formatResponse();
    }

    /**
     * 获取活动推荐时间有关信息
     *
     * @param eventId 活动id
     * @return 返回推荐时间有关信息
     */
    @GetMapping(value = "/recommendTime/{eventId}")
    public ResponseResult recommendTime(@PathVariable("eventId") int eventId) {
        RecommendTimeInfo recommendTimeInfo = timeService.getRecommendTime(eventId);
        return FormatResponseUtil.formatResponseDomain(recommendTimeInfo);
    }

    /**
     * 获取活动信息
     *
     * @param eventId 活动id
     * @return 返回活动信息
     */
    @GetMapping("/activityInfo/{eventId}")
    public ResponseResult getActivityInfo(@PathVariable("eventId") int eventId) {
        return FormatResponseUtil.formatResponse(activityService.selectActivityByEventId(eventId));
    }

}
