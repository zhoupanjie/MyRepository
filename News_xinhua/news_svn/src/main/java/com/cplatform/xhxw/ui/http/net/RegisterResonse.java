package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.model.UserInfo;

/**
 * 用户注册解析
 */
public class RegisterResonse extends BaseResponse{

	private UserInfo data;

    public UserInfo getData() {
        return data;
    }

    public void setData(UserInfo data) {
        this.data = data;
    }
}
