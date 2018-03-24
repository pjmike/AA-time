package com.ctg.aatime.service.impl;

import com.ctg.aatime.commons.bean.ServerResponseMessage;
import com.ctg.aatime.commons.enums.MessageTypeEnum;
import com.ctg.aatime.dao.ActivityDao;
import com.ctg.aatime.dao.ActivityMembersDao;
import com.ctg.aatime.domain.ActivityMembers;
import com.ctg.aatime.service.ActivityMembersService;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * @author pjmike
 * @create 2018-03-24 10:58
 */
@Service
@Slf4j
public class ActivityMemberServiceImpl implements ActivityMembersService{
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private ActivityMembersDao activityMembersDao;
    @Override
    public ActivityMembers insertActivityMember(ActivityMembers members) {
        activityMembersDao.insertActivityMembers(members);
        //TODO 待重构
        return members;
    }
}
