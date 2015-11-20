package com.cplatform.xhxw.ui.http.net;

import android.content.Context;
import com.cplatform.xhxw.ui.R;

/**
 * push解绑
 * Created by cy-love on 14-1-16.
 */
public class PushCancelRequest extends BaseRequest {

    private String user_id;
    private String device_token;
    private int device_type;

    public PushCancelRequest(Context context, String userId, String deviceToken) {
        this.user_id = userId;
        this.device_token = deviceToken;
        this.device_type = context.getResources().getInteger(R.integer.app_push_device_type);
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public int getDevice_type() {
        return device_type;
    }

    public void setDevice_type(int device_type) {
        this.device_type = device_type;
    }
}
