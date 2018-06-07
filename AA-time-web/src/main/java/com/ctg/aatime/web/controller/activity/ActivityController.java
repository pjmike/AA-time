package com.ctg.aatime.web.controller.activity;

import com.alibaba.fastjson.JSON;
import com.ctg.Bean.WxTemplateMessage;
import com.ctg.aatime.commons.enums.ErrorMsgEnum;
import com.ctg.aatime.commons.qiniu.IQiNIuService;
import com.ctg.aatime.commons.utils.FormatResponseUtil;
import com.ctg.aatime.commons.utils.RedisOperator;
import com.ctg.aatime.commons.utils.ResponseResult;
import com.ctg.aatime.domain.Activity;
import com.ctg.aatime.domain.dto.RecommendTimeInfo;
import com.ctg.aatime.service.ActivityService;
import com.ctg.aatime.service.TimeService;
import com.ctg.api.WxMsgService;
import com.ctg.api.impl.WxMsgServiceImpl;
import com.google.gson.Gson;
import com.qiniu.http.Response;
import com.qiniu.storage.model.DefaultPutRet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 活动有关接口
 * Created By Cx On 2018/4/4 20:32
 * @author Cx
 */
@RestController
@RequestMapping(value = "/activity")
@Slf4j
public class ActivityController {
    @Value("${activity.defaultImageUrl}")
    private String defaultImageUrl;
    @Value("${QiNiu}")
    private String QiNiu;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    private WxMsgService msgService = new WxMsgServiceImpl(restTemplate);
    private final ActivityService activityService;
    private final TimeService timeService;
    @Autowired
    private RedisOperator redisOperator;
    @Autowired
    private IQiNIuService qiNIuService;
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
     * 可选参数：eventBrief（活动简介）、eventTag（活动标签）、eventPlace（活动地点）、minTime（最小所需时间戳：默认为15min）
     * members（最小参与人数：默认为0）
     * </p>
     *
     * @param activity 活动类
     * @return ResponseResult
     */
    @PostMapping
    public ResponseResult createActivity(@RequestParam("file")MultipartFile file,Activity activity) throws IOException {
        if (file == null) {
            //设置活动默认图片
            activity.setImageUrl(defaultImageUrl);
        }
        String originalFileName = file.getOriginalFilename();
        Response response = qiNIuService.uploadFile(file.getInputStream(),originalFileName);
        //解析上传成功的结果
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        log.info("filename: {}",putRet.key);
        //设置图片URL
        activity.setImageUrl(QiNiu+originalFileName);
        activity = activityService.createActivity(activity);
        if (activity == null) {
            return FormatResponseUtil.error(ErrorMsgEnum.SERVER_FAIL_CONNECT);
        }
        return FormatResponseUtil.formatResponseDomain(activity);
    }

    /**
     * 更新活动接口
     *
     * <p>
     * 必传参数：eventId(活动Id)
     * 可选参数：eventName（活动名称）、eventBrief（活动简介）、eventPlace（活动地点）、minTime（最小所需时间戳：默认为15min）
     * </p>
     *
     * @param activity 活动类
     * @return ResponseResult
     */
    @PostMapping(value = "/activityInfo",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseResult updateActivity(@RequestBody Activity activity) throws IOException {
        activityService.updateActivity(activity);
        return FormatResponseUtil.formatResponse();
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm-dd : HH-mm");

        if (activityService.launchActivity(activity) < 1) {
            return FormatResponseUtil.error(ErrorMsgEnum.SERVER_FAIL_CONNECT);
        } else {
            while (redisOperator.getFromSet(activity.getEventId() + "-message") != null) {
                WxTemplateMessage message = (WxTemplateMessage) redisOperator.getFromSet(activity.getEventId() + "-message");
                List<WxTemplateMessage.Data> data = new ArrayList<>();
                data.add(new WxTemplateMessage.Data("keyword1", activity
                        .getEventName()));
                data.add(new WxTemplateMessage.Data("keyword2", simpleDateFormat.format(activity.getLaunchStartTime())));
                data.add(new WxTemplateMessage.Data("keyword3",activity.getEventPlace()));
                data.add(new WxTemplateMessage.Data("keyword4", activity.getUsername()));
                message.setData(data);
                //发送模板消息
                msgService.sendTemplateMsg(message);
            }
            Map<String, Object> response = new HashMap<>(16);
            response.put("eventName", activity.getEventName());
            response.put("name", activity.getUsername());
            response.put("avatar", activity.getAvatar());
            response.put("time", System.currentTimeMillis());
            response.put("operation", "发布");
            //发布通知
            messagingTemplate.convertAndSend("/topic/notification",response);
            return FormatResponseUtil.formatResponse();
        }
    }

    /**
     * 查询该用户仅参与的所有未发布且未过期活动
     *
     * @param uId 用户id
     * @return 用户仅参与的所有未发布未过期活动
     */
    @GetMapping("/joinList/{uid}")
    public ResponseResult joinListByUid(@PathVariable("uid") int uId) {
        List<Activity> activities = activityService.selectJoinActivitiesByUid(uId);
        return FormatResponseUtil.formatResponseDomain(activities);
    }

    /**
     * 查询该用户创建的未发布的未过期的活动
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
     * 查询该用户所有的已过期活动（只有已发布的，未发布的过期自动删除）
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
     * @param eventId 活动id
     * @return 返回推荐时间有关信息
     */
    @GetMapping(value = "/recommendTime/{eventId}")
    public ResponseResult recommendTime(@PathVariable("eventId") int eventId) {
        RecommendTimeInfo recommendTimeInfo = timeService.getRecommendTime(eventId);
        return FormatResponseUtil.formatResponseDomain(JSON.toJSON(recommendTimeInfo));
    }

}
