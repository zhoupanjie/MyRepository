package com.cplatform.xhxw.ui.http.net;

/**
 * Created by cy-love on 14-1-7.
 */
public class DelChannelRequest extends BaseRequest {

    private String channelid;
    private long operatetime;

    public DelChannelRequest(String channelid, long operatetime) {
        this.channelid = channelid;
        this.operatetime = operatetime;
    }

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }

    public long getOperatetime() {
        return operatetime;
    }

    public void setOperatetime(long operatetime) {
        this.operatetime = operatetime;
    }
}
