package com.cplatform.xhxw.ui.http.net;

/**
 * Created by cy-love on 14-1-8.
 */
public class NewSestRequest extends BaseRequest {

    private String channelid;
    private long published;//推送时间戳 最新时间戳|最后时间戳
    private int type; // 0获取更多，1获取最新数据

    public NewSestRequest(String channelid, long published, int type) {
        this.channelid = channelid;
        this.published = published;
        this.type = type;
    }

    public String getChannelid() {
        return channelid;
    }

    public void setChannelid(String channelid) {
        this.channelid = channelid;
    }

    public long getPublished() {
        return published;
    }

    public void setPublished(long published) {
        this.published = published;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
