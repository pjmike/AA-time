package com.ctg.aatime.web.controller.homepage;

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
@RequestMapping(value = "/homepage")
public class ShowActivityController {
    @Autowired
    private ActivityService activityService;

    @GetMapping(value = "{uId}/list")
    public ResponseResult list(@PathVariable("uId") int uId){
        List<Activity> activities = activityService.selectActivitiesById(uId);
        return FormatResponseUtil.formatResponseDomain(activities);
    }
}
