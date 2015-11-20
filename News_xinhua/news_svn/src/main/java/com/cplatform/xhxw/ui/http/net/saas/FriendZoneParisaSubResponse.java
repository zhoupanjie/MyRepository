package com.cplatform.xhxw.ui.http.net.saas;

import com.cplatform.xhxw.ui.http.net.BaseResponse;

import java.io.Serializable;

/**
 * 公司圈点赞
 * Created by cy-love on 14-8-28.
 */
public class FriendZoneParisaSubResponse extends BaseResponse {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data implements Serializable {

        private String allowtype;

        public String getAllowtype() {
            return allowtype;
        }

        public void setAllowtype(String allowtype) {
            this.allowtype = allowtype;
        }
    }
}
