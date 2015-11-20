package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.model.Start;

/**
 * Created by cy-love on 14-1-7.
 */
public class StartResponse extends BaseResponse {

    private Start data;

    public Start getData() {
        return data;
    }

    public void setData(Start data) {
        this.data = data;
    }
}
