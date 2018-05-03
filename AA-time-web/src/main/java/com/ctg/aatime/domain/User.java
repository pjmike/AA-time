package com.ctg.aatime.domain;

import lombok.Data;

/**
 * 用户类
 *
 * @author pjmike
 * @create 2018-03-13 19:27
 */
@Data
public class User {
    /**
     * 用户自增
     */
    private int id;
    /**
     * 微信服务器生成的唯一id
     */
    private String openid;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 用户昵称
     */
    private String nickname;

    public User() {
    }

    public User(String openid, String username, String avatar) {
        this.nickname = username;
        this.openid = openid;
        this.username = username;
        this.avatar = avatar;
    }

    public User(int id, String username, String avatar,String nickname) {
        this.nickname = nickname;
        this.id = id;
        this.username = username;
        this.avatar = avatar;
    }
}
