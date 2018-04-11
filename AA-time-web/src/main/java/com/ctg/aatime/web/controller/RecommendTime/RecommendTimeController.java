package com.ctg.aatime.web.controller.RecommendTime;

import com.ctg.aatime.commons.utils.FormatResponseUtil;
import com.ctg.aatime.commons.utils.RecommendTimeUtil;
import com.ctg.aatime.commons.utils.ResponseResult;
import com.ctg.aatime.domain.Activity;
import com.ctg.aatime.domain.ActivityMembers;
import com.ctg.aatime.domain.dto.BestTime;
import com.ctg.aatime.domain.dto.RecommendTimeInfo;
import com.ctg.aatime.service.ActivityMembersService;
import com.ctg.aatime.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 推荐时间操作
 * Created By Cx On 2018/4/6 9:24
 */
@RestController
public class RecommendTimeController {
    @Autowired
    private ActivityMembersService activityMembersService;
    @Autowired
    private ActivityService activityService;

    @GetMapping(value = "/recommend/{eventId}")
    public ResponseResult RecommendTime(@PathVariable("eventId") int eventId){
        List<ActivityMembers> members = activityMembersService.selectJoinMembersByEventId(eventId);
        Activity activity = activityService.selectActivityByEventId(eventId);
        RecommendTimeInfo recommendTimeInfo = RecommendTimeUtil.getRecommendTimeInfo(activity,members);
        //TODO 这里要给他返回可选时间范围吗？最优解用时间块合适还是时间戳合适
        return FormatResponseUtil.formatResponseDomain(recommendTimeInfo);
    }

}
