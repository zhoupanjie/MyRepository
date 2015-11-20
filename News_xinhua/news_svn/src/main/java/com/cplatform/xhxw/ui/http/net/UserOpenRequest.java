package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.model.OauthVerify;

/**
 * 第三方认证请求
 * Created by cy-love on 14-1-15.
 */
public class UserOpenRequest extends BaseRequest {

    private int appkey;
    private String openid;
    private String opennickname;
    private String openlogo;
    private String opentoken;

    public UserOpenRequest(OauthVerify oauth) {
        this.appkey = oauth.getAppkey();
        this.openid = oauth.getOpenid();
        this.opennickname = oauth.getOpennickname();
        this.openlogo = oauth.getOpenlogo();
        this.opentoken = oauth.getOpentoken();
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

    public int getAppkey() {
        return appkey;
    }

    public void setAppkey(int appkey) {
        this.appkey = appkey;
    }
}
