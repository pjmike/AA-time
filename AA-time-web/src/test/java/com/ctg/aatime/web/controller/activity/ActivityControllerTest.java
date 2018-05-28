package com.ctg.aatime.web.controller.activity;

import com.ctg.aatime.domain.Activity;
import org.hibernate.validator.constraints.EAN;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Created By Cx On 2018/4/7 22:24
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
//@Transactional
public class ActivityControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private TestRestTemplate template = new TestRestTemplate();
    //注意：端口号不能直接给8080，必须用这种方式注入
    @Value("${local.server.port}")
    private int port;

    @Test
    public void joinListByUid() throws Exception {
        //表示测试根目录+该url能否成功被访问

        String responseString = mvc.perform(MockMvcRequestBuilders.get("/activity/joinList/2")    //请求的url,请求的方法是get
        ).andExpect(MockMvcResultMatchers.status().isOk())    //返回的状态是200
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();
//        mvc.perform(MockMvcRequestBuilders.get("/activity/joinList?uId=2")).andExpect(MockMvcResultMatchers.status().isOk())

//        .andExpect(MockMvcResultMatchers.content().string("a"));
    }

    @Test
    public void createActivity() throws Exception {
        String requestBody = "{\"eventName\":\"问问\",\"eventBrief\":\"是是是\",\"eventPlace\":\"天上\"" +
                ",\"startTime\":\"1627176808888\",\"endTime\":\"1927422288207\",\"statisticTime\":\"1727076808888\"" +
                ",\"minTime\":\"7200000\",\"uid\":\"2\"}";
        try {
            String responseString = mvc.perform(MockMvcRequestBuilders.post("/activity")    //请求的url,请求的方法是post
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE).content(requestBody)//数据的格式
//                    .param("eventName","看书").param("eventBrief","是是是")
//                    .param("eventPlace","天上").param("username","pj")
//                    .param("avatar","hello").param("startTime","1525176808888")
//                    .param("endTime","1525722288207").param("statisticTime","1525276808888")
//                    .param("minTime","7200000").param("uid","2")//添加参数
            ).andExpect(MockMvcResultMatchers.status().isOk())    //返回的状态是200
                    .andDo(print())         //打印出请求和相应的内容
                    .andReturn().getResponse().getContentAsString();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void delActivity(){
        try {
            String responseString = mvc.perform(MockMvcRequestBuilders.delete("/activity/38")    //请求的url,请求的方法是post
            ).andExpect(MockMvcResultMatchers.status().isOk())    //返回的状态是200
                    .andDo(print())         //打印出请求和相应的内容
                    .andReturn().getResponse().getContentAsString();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void launchActivity(){
        try {
            String requestBody = "{\"eventId\":\"38\",\"launchStartTime\":\"1527276808888\",\"launchEndTime\":\"1527296808888\"" +
                    ",\"launchWords\":\"聊天\"}";
            String responseString = mvc.perform(MockMvcRequestBuilders.post("/activity/launchInfo")    //请求的url,请求的方法是post
                    .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)//数据的格式
                    .content(requestBody)
            ).andExpect(MockMvcResultMatchers.status().isOk())    //返回的状态是200
                    .andDo(print())         //打印出请求和相应的内容
                    .andReturn().getResponse().getContentAsString();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void launchListByUid(){
        try {
            String responseString = mvc.perform(MockMvcRequestBuilders.get("/activity/launchList/2")    //请求的url,请求的方法是post
            ).andExpect(MockMvcResultMatchers.status().isOk())    //返回的状态是200
                    .andDo(print())         //打印出请求和相应的内容
                    .andReturn().getResponse().getContentAsString();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void deadListByUid(){
        try {
            String responseString = mvc.perform(MockMvcRequestBuilders.get("/activity/deadList/2")    //请求的url,请求的方法是post
            ).andExpect(MockMvcResultMatchers.status().isOk())    //返回的状态是200
                    .andDo(print())         //打印出请求和相应的内容
                    .andReturn().getResponse().getContentAsString();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void establishmentListByUid(){
        try {
            String responseString = mvc.perform(MockMvcRequestBuilders.get("/activity/establishmentList/2")    //请求的url,请求的方法是post
            ).andExpect(MockMvcResultMatchers.status().isOk())    //返回的状态是200
                    .andDo(print())         //打印出请求和相应的内容
                    .andReturn().getResponse().getContentAsString();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void getActivityInfo(){
        try {
            String responseString = mvc.perform(MockMvcRequestBuilders.get("/activity/activityInfo/38")    //请求的url,请求的方法是post
            ).andExpect(MockMvcResultMatchers.status().isOk())    //返回的状态是200
                    .andDo(print())         //打印出请求和相应的内容
                    .andReturn().getResponse().getContentAsString();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void recommendTime(){
        try {
            String responseString = mvc.perform(MockMvcRequestBuilders.get("/activity/recommendTime/34")    //请求的url,请求的方法是post
            ).andExpect(MockMvcResultMatchers.status().isOk())    //返回的状态是200
                    .andDo(print())         //打印出请求和相应的内容
                    .andReturn().getResponse().getContentAsString();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}