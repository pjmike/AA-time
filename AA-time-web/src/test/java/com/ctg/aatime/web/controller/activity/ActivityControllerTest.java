package com.ctg.aatime.web.controller.activity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Created By Cx On 2018/4/7 22:24
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ActivityControllerTest {

    @Autowired
    private TestRestTemplate template = new TestRestTemplate();
    //注意：端口号不能直接给8080，必须用这种方式注入，因为不是用TomCat启动的，每次执行端口号都不一样
    @Value("${local.server.port}")
    private int port;

    @Test
    public void listByUid() throws Exception {
        //表示测试根目录+该url能否成功被访问
        //TODO 访问请求失败
        String url = "http://localhost:" + port + "/activity/listByUid/2";
        String result = template.getForObject(url,String.class);
        System.out.println("结果："+result);
//        mvc.perform(MockMvcRequestBuilders.get("/girl/list")).andExpect(MockMvcResultMatchers.status().isOk())
//        .andExpect(MockMvcResultMatchers.content().string("a"));
    }
}