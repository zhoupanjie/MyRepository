package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.model.SChat;
import com.cplatform.xhxw.ui.model.SChatUserInfo;

import java.io.Serializable;
import java.util.List;

/**
 * 聊天内容
 */
public class GetSChatResponse extends BaseResponse {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data implements Serializable {
        private SChatUserInfo userinfo; // 好友的用户信息
        private SChatUserInfo myinfo; // 个人信息
        private String lastactiontime; // 最后一次拉去时间
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

        public String getLastactiontime() {
            return lastactiontime;
        }

        public void setLastactiontime(String lastactiontime) {
            this.lastactiontime = lastactiontime;
        }

        public SChatUserInfo getMyinfo() {
            return myinfo;
        }

        public void setMyinfo(SChatUserInfo myinfo) {
            this.myinfo = myinfo;
        }
    }
}
