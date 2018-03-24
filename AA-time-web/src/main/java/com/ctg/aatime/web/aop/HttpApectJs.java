package com.ctg.aatime.web.aop;

import com.ctg.aatime.commons.bean.ServerResponseMessage;
import com.ctg.aatime.commons.enums.MessageTypeEnum;
import com.ctg.aatime.domain.ActivityMembers;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 1.   使用AOP统一处理请求日志
 * 2.   使用AOP结合websocket实现服务器推送服务
 * 3.   Spring Boot对AOP的默认配置属性是开启的，也就是说spring.aop.auto属性的值默认是true，
 * 我们只要引入了AOP依赖后，默认就已经增加了@EnableAspectJAutoProxy功能，
 * 不需要我们在程序启动类上面加入注解@EnableAspectJAutoProxy
 *
 * @author pjmike
 * @create 2018-03-22 17:40
 **/
@Aspect
@Component
@Slf4j
public class HttpApectJs {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * 对controller方法定义切点
     */
    @Pointcut("execution(public * com.ctg.aatime.web.controller..*.*(..))")
    public void webLog() {
    }

    /**
     * 对service方法定义一个切点，然后利用websocket推送消息
     */
    @Pointcut("execution(public * com.ctg.aatime.service.ActivityMembersService.insertActivityMember(..))")
    public void webSocketPush() {

    }

    /**
     * 在插入活动人员之后，广播通知某某进入了活动
     *
     * @param object
     */
    @AfterReturning(returning = "object", pointcut = "webSocketPush()")
    public void WebSocketPushAfterReturn(Object object) {
        ActivityMembers members = (ActivityMembers) object;
        log.info("return={}", object.toString());
        //广播信息
        ServerResponseMessage message = new ServerResponseMessage();
        message.setResponseMessage(members.getUsername() + MessageTypeEnum.JOIN.getType());
        //向活动成员发送广播消息,某某进入了活动
        messagingTemplate.convertAndSend("/topic/notice", message.getResponseMessage());
    }

    @Before("webLog()")
    public void doBefore(JoinPoint point) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        //请求URL
        log.info("url={}", request.getRequestURL());
        //请求方法
        log.info("HTTP_Method={}", request.getMethod());
        //请求IP
        log.info("IP={}", request.getRemoteAddr());
        //请求类的方法
        log.info("Class_Method={}", point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName());
        //请求类方法的参数
        log.info("Args={}", Arrays.asList(point.getArgs()));
    }

    @AfterReturning(returning = "object", pointcut = "webLog()")
    public void doAfterReturn(Object object) {
        log.info("response={}", object.toString());
    }


}
