package com.ctg.aatime.web.controller.activity;

import com.ctg.aatime.commons.exception.CascadeException;
import com.ctg.aatime.commons.utils.FormatResponseUtil;
import com.ctg.aatime.commons.utils.ResponseResult;
import com.ctg.aatime.domain.Activity;
import com.ctg.aatime.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 首页：我的行程界面 已参与活动显示操作
 * Created By Cx On 2018/4/4 20:32
 */
@RestController
@RequestMapping(value = "/activity")
public class ActivityController {
    @Autowired
    private ActivityService activityService;

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
        //TODO
        try {
            activityService.delActivityByEventId(eventId);
            return FormatResponseUtil.formatResponse();
        } catch (CascadeException e) {
            return FormatResponseUtil.error("删除失败");
        }
    }
}
