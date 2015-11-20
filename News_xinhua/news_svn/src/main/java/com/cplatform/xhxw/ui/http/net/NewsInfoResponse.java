package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.model.NewsInfo;

/**
 * 新闻详情
 * Created by cy-love on 14-1-2.
 */
public class NewsInfoResponse extends BaseResponse {

    private NewsInfo data;

    public NewsInfo getData() {
        return data;
    }

    public void setData(NewsInfo data) {
        this.data = data;
    }
}
