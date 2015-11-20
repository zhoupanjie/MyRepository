package com.cplatform.xhxw.ui.http.net;

/**
 * Created by cy-love on 14-8-10.
 */
public class GetSChatRequest extends BaseRequest {


    private String userid;
    private String lastactiontime;

    /**
     * 获得消息列表
     *
     * @param userid         好友用户ID
     * @param lastactiontime 最后一次获取时间
     */
    public GetSChatRequest(String userid, String lastactiontime) {
        this.userid = userid;
        this.lastactiontime = lastactiontime;
        this.setDevRequest(true);
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getLastactiontime() {
        return lastactiontime;
    }

    public void setLastactiontime(String lastactiontime) {
        this.lastactiontime = lastactiontime;
    }
}
