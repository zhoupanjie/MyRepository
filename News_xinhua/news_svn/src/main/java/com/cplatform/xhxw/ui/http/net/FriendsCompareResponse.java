package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.model.Friend;

import java.util.List;

/**
 * 上传通讯录
 */
public class FriendsCompareResponse extends BaseResponse {

    private List<Friend> data;

    public List<Friend> getData() {
        return data;
    }

    public void setData(List<Friend> data) {
        this.data = data;
    }
}
