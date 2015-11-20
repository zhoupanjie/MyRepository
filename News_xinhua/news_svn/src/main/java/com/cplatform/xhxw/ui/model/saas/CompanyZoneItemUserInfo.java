package com.cplatform.xhxw.ui.model.saas;

import java.io.Serializable;

/**
 * 用户
 * Created by cy-love on 14-8-23.
 */
public class CompanyZoneItemUserInfo implements Serializable {

    private String comment;
    private String logo;
    private String name;
    private String nickname;
    private String userid;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
