package com.cplatform.xhxw.ui.http.net;

/**
 * 邮箱找回密码
 * Created by cy-love on 14-1-16.
 */
public class FindEmailPasswordRequest extends BaseRequest {

    private String account;

    public FindEmailPasswordRequest(String account) {
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
