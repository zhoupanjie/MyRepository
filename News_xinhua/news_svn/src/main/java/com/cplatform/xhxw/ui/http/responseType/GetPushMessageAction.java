package com.cplatform.xhxw.ui.http.responseType;

/**
 * get push message动作
 * Created by cy-love on 14-1-23.
 */
public enum GetPushMessageAction {
    /**下一页*/
    down("down"),
    /**上一页*/
    up("up");

    private String action;

    public String getAction() {
        return action;
    }

    private GetPushMessageAction(String action) {
        this.action = action;
    }
}
