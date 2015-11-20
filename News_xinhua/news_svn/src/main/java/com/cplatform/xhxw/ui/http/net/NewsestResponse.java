package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.model.New;

import java.util.List;

/**
 * Created by cy-love on 14-1-8.
 */
public class NewsestResponse extends BaseResponse {

    private List<New> data;

    public List<New> getData() {
        return data;
    }

    public void setData(List<New> data) {
        this.data = data;
    }
}
