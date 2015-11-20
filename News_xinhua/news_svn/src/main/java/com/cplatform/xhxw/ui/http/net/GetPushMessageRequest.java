package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.http.responseType.GetPushMessageAction;

/**
 * 获得push消息列表
 * Created by cy-love on 14-1-23.
 */
public class GetPushMessageRequest extends BaseRequest{

    private int offset;
    private int pn;
    private String action;
    private String published;

    /**
     * 获得push message
     * @param offset 获取以往数据每页条数
     * @param pn 代表页面数，默认是1
     * @param action 用户动作:down获取老数据，up获取新数据
     * @param published 客户端时间戳
     */
    public GetPushMessageRequest(int offset, int pn, GetPushMessageAction action, String published) {
        this.offset = offset;
        this.pn = pn;
        this.action = action.getAction();
        this.published = published;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getPn() {
        return pn;
    }

    public void setPn(int pn) {
        this.pn = pn;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }
}
