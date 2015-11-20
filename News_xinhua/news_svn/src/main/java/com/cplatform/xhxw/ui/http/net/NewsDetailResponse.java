package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.model.NewsDetail;

/**
 * 新闻详情接口
 * Created by cy-love on 14-1-8.
 */
public class NewsDetailResponse extends BaseResponse {

    private NewsDetail data;

    public NewsDetail getData() {
        return data;
    }

    public void setData(NewsDetail data) {
        this.data = data;
    }
}
