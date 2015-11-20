package com.cplatform.xhxw.ui.model;

import java.io.Serializable;

/**
 * S信未读消息列表用户信息
 */
public class SChatUserInfo implements Serializable {

    private String userid; //好友用户ID
    private String logo; //好友头像
    private String nickname; //好友昵称
    private String comment; //好友备注
    private String name; //好友姓名

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
