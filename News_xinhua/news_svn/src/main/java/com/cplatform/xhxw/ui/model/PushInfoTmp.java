package com.cplatform.xhxw.ui.model;

import java.io.Serializable;

/**
 * push平台信息
 * Created by cy-love on 14/11/4.
 */
public class PushInfoTmp implements Serializable {

    private String userId;
    private String deviceToken;
    private String channelId;
    private String push_channel;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getPush_channel() {
        return push_channel;
    }

    public void setPush_channel(String push_channel) {
        this.push_channel = push_channel;
    }
}
