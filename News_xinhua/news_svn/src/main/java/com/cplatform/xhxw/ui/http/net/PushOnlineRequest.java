package com.cplatform.xhxw.ui.http.net;

import android.content.Context;
import android.content.res.Resources;
import com.cplatform.xhxw.ui.R;

/**
 * push注册
 * Created by cy-love on 14-1-16.
 */
public class PushOnlineRequest extends BaseRequest {

    private String user_id;
    private String device_token;
    private int device_type;
    private String push_channel;
    private String channel_id;
    private int just_transmission = 1; // 标示为消息类型

    public PushOnlineRequest(Context context, String userId, String deviceToken, String channelId, String push_channel) {
        this.user_id = userId;
        this.device_token = deviceToken;
        Resources res = context.getResources();
        this.device_type = res.getInteger(R.integer.app_push_device_type);
        this.push_channel = push_channel;
        this.channel_id = channelId;
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

    public String getPush_channel() {
        return push_channel;
    }

    public void setPush_channel(String push_channel) {
        this.push_channel = push_channel;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public int getJust_transmission() {
        return just_transmission;
    }

    public void setJust_transmission(int just_transmission) {
        this.just_transmission = just_transmission;
    }
}
