package com.cplatform.xhxw.ui.http.net;

/**
 * Created by cy-love on 14-1-7.
 */
public class AddChannelRequest extends BaseRequest {

    private String channelid;
    private long listorder;
    private long operatetime;

    public AddChannelRequest(String channelid, long listorder, long operatetime) {
        this.channelid = channelid;
        this.listorder = listorder;
        this.operatetime = operatetime;
    }

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }

    public long getListorder() {
        return listorder;
    }

    public void setListorder(long listorder) {
        this.listorder = listorder;
    }

    public long getOperatetime() {
        return operatetime;
    }

    public void setOperatetime(long operatetime) {
        this.operatetime = operatetime;
    }
}
