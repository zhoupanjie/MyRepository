package com.cplatform.xhxw.ui.http.net.saas;

import com.cplatform.xhxw.ui.http.net.BaseResponse;

import java.io.Serializable;

/**
 * 获得消息列表
 * Created by cy-love on 14-8-26.
 */
public class SumGetResponse extends BaseResponse {

    private SumGetData data;

    public SumGetData getData() {
        return data;
    }

    public void setData(SumGetData data) {
        this.data = data;
    }

    public class SumGetData implements Serializable {
        private String companyzonenotreadlimit; // 社区未读通知消息数
        private String schatnotreadlimit; // S信未读消息数
        private String newfriendslimit;  // 新朋友数
        private String friendzonenotreadlimit; // 朋友圈未读消息数

        public int get_companyzonenotreadlimit() {
            try {
                return Integer.valueOf(companyzonenotreadlimit);
            } catch (Exception e) {}
            return 0;
        }

        public int get_schatnotreadlimit() {
            try {
                return Integer.valueOf(schatnotreadlimit);
            } catch (Exception e) {}
            return 0;
        }

        public int get_newfriendslimit() {
            try {
                return Integer.valueOf(newfriendslimit);
            } catch (Exception e) {}
            return 0;
        }

        public int get_friendzonenotreadlimit() {
            try {
                return Integer.valueOf(friendzonenotreadlimit);
            } catch (Exception e) {}
            return 0;
        }

        public String getCompanyzonenotreadlimit() {
            return companyzonenotreadlimit;
        }

        public void setCompanyzonenotreadlimit(String companyzonenotreadlimit) {
            this.companyzonenotreadlimit = companyzonenotreadlimit;
        }

        public String getSchatnotreadlimit() {
            return schatnotreadlimit;
        }

        public void setSchatnotreadlimit(String schatnotreadlimit) {
            this.schatnotreadlimit = schatnotreadlimit;
        }

        public String getNewfriendslimit() {
            return newfriendslimit;
        }

        public void setNewfriendslimit(String newfriendslimit) {
            this.newfriendslimit = newfriendslimit;
        }

        public String getFriendzonenotreadlimit() {
            return friendzonenotreadlimit;
        }

        public void setFriendzonenotreadlimit(String friendzonenotreadlimit) {
            this.friendzonenotreadlimit = friendzonenotreadlimit;
        }
    }
}
