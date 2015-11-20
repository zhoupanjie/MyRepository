package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.model.Channe;

import java.util.List;

/**
 * 服务器栏目
 * Created by cy-love on 14-1-7.
 */
public class ChannelListResponse extends BaseResponse {

    private List<Channe> data;

    public List<Channe> getData() {
        return data;
    }

    public void setData(List<Channe> data) {
        this.data = data;
    }
}
