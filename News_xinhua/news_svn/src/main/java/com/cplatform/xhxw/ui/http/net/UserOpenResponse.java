package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.model.UserInfo;

/**
 * 第三方登录解析
 * Created by cy-love on 14-1-15.
 */
public class UserOpenResponse extends BaseResponse {

    private UserInfo data;

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }
}
