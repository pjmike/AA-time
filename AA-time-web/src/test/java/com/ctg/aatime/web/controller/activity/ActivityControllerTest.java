package com.ctg.aatime.web.controller.activity;

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
@Transactional
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
        String responseString = mvc.perform(MockMvcRequestBuilders.get("/activity/joinList")    //请求的url,请求的方法是get
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)//数据的格式
                .param("uId","2")         //添加参数
        ).andExpect(MockMvcResultMatchers.status().isOk())    //返回的状态是200
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();
//        mvc.perform(MockMvcRequestBuilders.get("/activity/joinList?uId=2")).andExpect(MockMvcResultMatchers.status().isOk())
//        .andExpect(MockMvcResultMatchers.content().string("a"));
    }

    @Test
    public void createActivity(){
        try {
            String responseString = mvc.perform(MockMvcRequestBuilders.post("/activity")    //请求的url,请求的方法是post
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)//数据的格式
                    .param("eventName","吃饭").param("eventBrief","吃肉")
                    .param("eventPlace","食堂").param("username","pj")
                    .param("avatar","hello").param("startTime","1523952288207")
                    .param("endTime","1525952288207").param("statisticTime","1524952288207")
                    .param("minTime","288207").param("uid","2")//添加参数
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
            String responseString = mvc.perform(MockMvcRequestBuilders.delete("/activity/delete")    //请求的url,请求的方法是post
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)//数据的格式
                    .param("eventId","16")//添加参数
            ).andExpect(MockMvcResultMatchers.status().isOk())    //返回的状态是200
                    .andDo(print())         //打印出请求和相应的内容
                    .andReturn().getResponse().getContentAsString();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @Rollback(false)
    public void launchActivity(){
        try {
            String responseString = mvc.perform(MockMvcRequestBuilders.post("/activity/launchInfo")    //请求的url,请求的方法是post
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)//数据的格式
                    .param("eventId","22").param("launchWords","吃嘛嘛香")//添加参数
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
            String responseString = mvc.perform(MockMvcRequestBuilders.get("/activity/launchList")    //请求的url,请求的方法是post
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)//数据的格式
                    .param("uId","2")//添加参数
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
            String responseString = mvc.perform(MockMvcRequestBuilders.get("/activity/deadList")    //请求的url,请求的方法是post
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)//数据的格式
                    .param("uId","2")//添加参数
            ).andExpect(MockMvcResultMatchers.status().isOk())    //返回的状态是200
                    .andDo(print())         //打印出请求和相应的内容
                    .andReturn().getResponse().getContentAsString();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}