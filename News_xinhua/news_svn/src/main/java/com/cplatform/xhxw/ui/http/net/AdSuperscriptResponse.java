package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.model.Ad;

import java.util.List;

/**
 * 广告模型
 * Created by cy-love on 14-3-9.
 */
public class AdSuperscriptResponse extends BaseResponse {

    private List<Ad> data;

    public List<Ad> getData() {
        return data;
    }

    public void setData(List<Ad> data) {
        this.data = data;
    }
}
