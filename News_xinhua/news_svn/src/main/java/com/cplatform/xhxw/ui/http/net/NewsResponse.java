package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.model.New;
import com.cplatform.xhxw.ui.model.NewsInfo;

import java.util.List;

/**
 * 新闻列表
 * Created by cy-love on 14-1-2.
 */
public class NewsResponse extends BaseResponse {

    private List<New> data;

    public List<New> getData() {
        return data;
    }

    public void setData(List<New> data) {
        this.data = data;
    }
}
