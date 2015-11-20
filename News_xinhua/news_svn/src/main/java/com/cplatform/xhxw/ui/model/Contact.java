package com.cplatform.xhxw.ui.model;

import java.io.Serializable;

/**
 * 通讯录联系人数据
 */
public class Contact implements Serializable {

    private String userid;   // 用户id
    private String logo;     // 用户头衔
    private String name;     // 用户名
    private String nickname; // 用户昵称
    private String comment;  // 用户备注
    private String sign;     // 用户签名

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
