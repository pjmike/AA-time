package com.ctg.aatime.web.controller.activity;

import com.ctg.aatime.commons.utils.FormatResponseUtil;
import com.ctg.aatime.commons.utils.ResponseResult;
import com.ctg.aatime.domain.Activity;
import com.ctg.aatime.domain.dto.RecommendTimeInfo;
import com.ctg.aatime.service.ActivityService;
import com.ctg.aatime.service.TimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/create")
    public ResponseResult createActivity(Activity activity){
        activity = activityService.createActivity(activity);
        return FormatResponseUtil.formatResponseDomain(activity);
    }

    @GetMapping(value = "/listByUid/{uId}")
    public ResponseResult listByUid(@PathVariable("uId") int uId){
        //TODO 查询该用户参与的所有活动(包括未确定发布的)
        List<Activity> activities = activityService.selectActivitiesByUid(uId);
        return FormatResponseUtil.formatResponseDomain(activities);
    }

    @GetMapping("/delete/{eventId}")
    public ResponseResult delActivity(@PathVariable("eventId") int eventId) {
        activityService.delActivityByEventId(eventId);
        return FormatResponseUtil.formatResponse();
    }

    //推荐时间
    @GetMapping(value = "/recommendTime/{eventId}")
    public ResponseResult RecommendTime(@PathVariable("eventId") int eventId){
        //TODO 这里是service调用service封装成一个方法好:timeService.getRecommendTime(eventId)
        // 还是controller调用service好:timeService.getRecommendTime(Activity,joinMembers)
        // 还是在Dao层定义并调用
        RecommendTimeInfo recommendTimeInfo =timeService.getRecommendTime(eventId);

        return FormatResponseUtil.formatResponseDomain(recommendTimeInfo);
    }
}
