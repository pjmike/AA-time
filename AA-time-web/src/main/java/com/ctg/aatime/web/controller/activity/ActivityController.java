package com.ctg.aatime.web.controller.activity;

import com.ctg.aatime.commons.enums.ErrorMsgEnum;
import com.ctg.aatime.commons.utils.FormatResponseUtil;
import com.ctg.aatime.commons.utils.ResponseResult;
import com.ctg.aatime.domain.Activity;
import com.ctg.aatime.domain.dto.RecommendTimeInfo;
import com.ctg.aatime.service.ActivityService;
import com.ctg.aatime.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 活动有关接口
 * Created By Cx On 2018/4/4 20:32
 */
@RestController
@RequestMapping(value = "/activity")
public class ActivityController {
    @Autowired
    private ActivityService activityService;
    @Autowired
    private TimeService timeService;

    @PostMapping()
    //TODO 待命名
    public ResponseResult createActivity(Activity activity) {
        activity = activityService.createActivity(activity);
        if(activity == null){
            return FormatResponseUtil.error(ErrorMsgEnum.SERVER_FAIL_CONNECT);
        }
        return FormatResponseUtil.formatResponseDomain(activity);
    }

    @PostMapping("/launchInfo")
    public ResponseResult launchActivity(@RequestParam("eventId") int eventId,
                                         @RequestParam("launchWords") String launchWords,
                                         @RequestParam("launchStartTime")long launchStartTime,
                                         @RequestParam("launchEndTime")long launchEndTime) {

        if(eventId < 0){
            //TODO 所有controller方法每次运行前是否都需要判定数据是否合法？
            return FormatResponseUtil.error(ErrorMsgEnum.SERVER_FAIL_CONNECT);
        }
        if(activityService.launchActivity(eventId, launchWords, launchStartTime, launchEndTime) < 1){
            return FormatResponseUtil.error(ErrorMsgEnum.SERVER_FAIL_CONNECT);
        }else return FormatResponseUtil.formatResponse();
    }

    @GetMapping("/liveList")
    public ResponseResult liveListByUid(@RequestParam("uid") int uId) {
        //查询该用户参与的所有活动(包括未确定发布的)
        List<Activity> activities = activityService.selectLiveActivitiesByUid(uId);
        return FormatResponseUtil.formatResponseDomain(activities);
    }

    @GetMapping("/launchList")
    public ResponseResult launchListByUid(@RequestParam("uid") int uId) {
        //查询该用户参与的已发布活动
        List<Activity> activities = activityService.selectLaunchActivitiesByUid(uId);
        return FormatResponseUtil.formatResponseDomain(activities);
    }

    @GetMapping("/deadList")
    public ResponseResult deadListByUid(@RequestParam("uid") int uId) {
        //查询该用户参与的已过期活动
        List<Activity> activities = activityService.selectDeadActivitiesByUid(uId);
        return FormatResponseUtil.formatResponseDomain(activities);
    }

    @GetMapping("/establishmentList")
    public ResponseResult establishmentListByUid(@RequestParam("uid")int uId){
        //查询该用户创建的活动
        List<Activity> activities = activityService.selectEstablishedActivitiesByUid(uId);
        return FormatResponseUtil.formatResponse(activities);
    }

    @DeleteMapping("/delete")
    public ResponseResult delActivity(@RequestParam("eventId") int eventId) {
        activityService.delActivityByEventId(eventId);
        return FormatResponseUtil.formatResponse();
    }

    //TODO 待测试
    @GetMapping(value = "/recommendTime")
    public ResponseResult RecommendTime(@RequestParam("eventId") int eventId) {
        RecommendTimeInfo recommendTimeInfo = timeService.getRecommendTime(eventId);
        return FormatResponseUtil.formatResponseDomain(recommendTimeInfo);
    }

    @GetMapping("/activityInfo")
    public ResponseResult getActivityInfo(@RequestParam("eventId")int eventId){
        return FormatResponseUtil.formatResponse(activityService.selectActivityByEventId(eventId));
    }

}
