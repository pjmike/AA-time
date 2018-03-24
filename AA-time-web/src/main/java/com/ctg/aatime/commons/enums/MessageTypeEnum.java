package com.ctg.aatime.commons.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 消息类型
 *
 * @author pjmike
 * @create 2018-03-24 15:28
 */
@AllArgsConstructor
public enum MessageTypeEnum {
    /**
     * 更改
     */
    CHANGE("更改了活动地点"),
    /**
     * 加入
     */
    JOIN("参与了活动"),
    /**
     * 退出
     */
    LEAVE("退出了活动"),
    /**
     * 放弃
     */
    GIVEUP("放弃了活动"),
    /**
     * 确认行程
     */
    CONFIRM("活动已确认行程");
    @Getter
    private String type;
}
