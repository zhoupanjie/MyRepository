package com.cplatform.xhxw.ui.model;

import java.io.Serializable;
import java.util.List;

/**
 * S信未读消息
 * Created by cy-love on 14-8-10.
 */
public class GetNotreadItem implements Serializable {

    private SChatUserInfo userinfo; // 好友的用户信息
    private int count; // 未读聊天信息条数
    private List<SChat> list; // 聊天内容

    public SChatUserInfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(SChatUserInfo userinfo) {
        this.userinfo = userinfo;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<SChat> getList() {
        return list;
    }

    public void setList(List<SChat> list) {
        this.list = list;
    }
}
