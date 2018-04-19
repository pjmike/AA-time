package com.ctg.aatime.service.impl;

import com.ctg.aatime.commons.bean.ServerResponseMessage;
import com.ctg.aatime.commons.enums.MessageTypeEnum;
import com.ctg.aatime.commons.exception.CascadeException;
import com.ctg.aatime.dao.ActivityDao;
import com.ctg.aatime.dao.ActivityMembersDao;
import com.ctg.aatime.dao.TimeDao;
import com.ctg.aatime.domain.ActivityMembers;
import com.ctg.aatime.service.ActivityMembersService;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pjmike
 * @create 2018-03-24 10:58
 */
@Service
@Slf4j
public class ActivityMemberServiceImpl implements ActivityMembersService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private ActivityMembersDao activityMembersDao;
    @Autowired
    private TimeDao timeDao;
    @Autowired
    private ActivityDao activityDao;

    @Override
    public ActivityMembers insertActivityMember(ActivityMembers members) {
        activityMembersDao.insertActivityMembers(members);
        //TODO 待重构
        return members;
    }

    @Override
    public List<ActivityMembers> selectJoinMembersByEventId(int eventId) {
        //参与活动人员
        List<ActivityMembers> members = activityMembersDao.selectActivityMembersByEventId(eventId);

        for (ActivityMembers member : members) {
            //查询每个成员的freeTime并添加进属性
            int uid = member.getUid();
            HashMap<Long, Long> freeTime = new HashMap<Long, Long>();
            List<HashMap<String, Long>> freeTimeList = timeDao.selectFreeTimes(uid, eventId);
            for (HashMap<String, Long> time : freeTimeList) {
                //将该成员每段freeTime置入同一个map中
                freeTime.put(time.get("key"), time.get("value"));
            }
            member.setFreeTimes(freeTime);
        }
        return members;
    }

    @Override
    @Transactional
    public ActivityMembers quitActivity(int uid, int eventId, String reason) {
        ActivityMembers activityMember = activityMembersDao.selectActivityMembersByUEid(uid, eventId);
        if (activityMembersDao.quitActivityByUid(uid, eventId) != 1 ||
                activityDao.reduceJoinNumByEventId(eventId) != 1 ||
                timeDao.delMembersTimeByUId(uid, eventId) < 1 ||
                activityDao.addQuitReason(activityMember, reason) != 1) {
            throw new CascadeException();
        }
        return activityMember;
    }
}
