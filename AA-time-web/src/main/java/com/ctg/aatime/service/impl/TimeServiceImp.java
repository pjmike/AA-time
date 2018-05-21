package com.ctg.aatime.service.impl;

import com.ctg.aatime.commons.utils.RecommendTimeUtil;
import com.ctg.aatime.dao.ActivityDao;
import com.ctg.aatime.dao.ActivityMembersDao;
import com.ctg.aatime.dao.TimeDao;
import com.ctg.aatime.domain.Activity;
import com.ctg.aatime.domain.ActivityMembers;
import com.ctg.aatime.domain.Time;
import com.ctg.aatime.domain.dto.RecommendTimeInfo;
import com.ctg.aatime.service.ActivityMembersService;
import com.ctg.aatime.service.ActivityService;
import com.ctg.aatime.service.TimeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created By Cx On 2018/4/17 16:04
 */
@Service
@Slf4j
public class TimeServiceImp implements TimeService{
    @Autowired
    ActivityDao activityDao;
    @Autowired
    TimeDao timeDao;
    @Autowired
    ActivityMembersDao activityMembersDao;
    @Autowired
    ActivityMembersService activityMembersService;

    @Override
    public int changeFreeTime(int uid, int eventId, List<Time> freeTime) {
        return 1;
    }

    @Override
    public RecommendTimeInfo getRecommendTime(int eventId) {
        List<ActivityMembers> members = activityMembersService.selectJoinMembersByEventId(eventId);
        Activity activity = activityDao.selectActivityByEventId(eventId);
        ActivityMembers user = null;
        int uid = activity.getUid();
        for (ActivityMembers m : members) {
            if (m.getUid() == uid){
                user = m;
                members.remove(m);
                break;
            }
        }
        if(user == null) throw new RuntimeException("创建者用户缺失");
        return RecommendTimeUtil.getRecommendTimeInfo(activity, members,user);
    }

    @Override
    public Map<Long, Long> getFreeTimeByUid(int uid,int eventId) {
        Map<Long, Long> freeTime = new HashMap<Long, Long>();
        List<HashMap<String, Long>> freeTimeList = timeDao.selectFreeTimes(uid, eventId);
        for (HashMap<String, Long> time : freeTimeList) {
            //将该成员每段freeTime置入同一个map中
            freeTime.put(time.get("key"), time.get("value"));
        }
        return freeTime;
    }
}
