package com.ctg.Bean;

import com.alibaba.fastjson.JSON;
import lombok.Builder;
import lombok.Data;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author pjmike
 * @create 2018-04-19 23:27
 */
@Data
public class WxTemplateMessage implements Serializable{

    private static final long serialVersionUID = -8886030688845601044L;
    /**
     * <pre>
     * 参数：touser
     * 是否必填： 是
     * 描述：接受者(用户)的openId
     * </pre>
     */
    private String toUser;
    /**
     * <pre>
     * 参数:template_id
     * 是否必填:是
     * 描述: 所需下发的模板消息id
     * </pre>
     */
    private static  String templateId = "FJBgwjykvqNK32P6kabC3k6NHvR6JPrfLbIrIVAnCSI";

    /**
     * <pre>
     * 参数：page
     * 是否必填： 否
     * 描述： 点击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转。
     * </pre>
     */
    private String page;

    /**
     * <pre>
     * 参数：form_id
     * 是否必填： 是
     * 描述： 表单提交场景下，为 submit 事件带上的 formId；支付场景下，为本次支付的 prepay_id
     * </pre>
     */
    private String formId;

    /**
     * <pre>
     * 参数：data
     * 是否必填： 是
     * 描述： 模板内容，不填则下发空模板
     * </pre>
     */
    private  List<Data> data = new ArrayList<>();

    /**
     * <pre>
     * 参数：color
     * 是否必填： 否
     * 描述： 模板内容字体的颜色，不填默认黑色
     * </pre>
     */
    private String color;

    /**
     * <pre>
     * 参数：emphasis_keyword
     * 是否必填： 否
     * 描述： 模板需要放大的关键词，不填则默认无放大
     * </pre>
     */
    private String emphasisKeyword;

    public WxTemplateMessage(String toUser, String page, String formId) {
        this.toUser = toUser;
        this.page = page;
        this.formId = formId;
    }

    public WxTemplateMessage() {
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    @lombok.Data
    public static class Data {
        private String name;
        private String value;
        private String color;

        public Data(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public Data(String name, String value, String color) {
            this.name = name;
            this.value = value;
            this.color = color;
        }

        public Data(String value) {
            this.value = value;
        }
    }

}
