package com.cplatform.xhxw.ui.model;

/**
 * 第三方授权登录信息
 * Created by cy-love on 14-1-15.
 */
public class OauthVerify {

    private int appkey; // 第三方应用对应ID
    private String openid; // 第三方应用USERID
    private String opennickname; // 第三方应用昵称
    private String openlogo; // 第三方应用头像
    private String opentoken; // 第三方授权码

    public int getAppkey() {
        return appkey;
    }

    public void setAppkey(int appkey) {
        this.appkey = appkey;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getOpennickname() {
        return opennickname;
    }

    public void setOpennickname(String opennickname) {
        this.opennickname = opennickname;
    }

    public String getOpenlogo() {
        return openlogo;
    }

    public void setOpenlogo(String openlogo) {
        this.openlogo = openlogo;
    }

    public String getOpentoken() {
        return opentoken;
    }

    public void setOpentoken(String opentoken) {
        this.opentoken = opentoken;
    }
}
