package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.model.UserInfo;

/**
 * 普通登录解析
 */
public class LoginResponse extends BaseResponse{

	private UserInfo data;

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }
}
