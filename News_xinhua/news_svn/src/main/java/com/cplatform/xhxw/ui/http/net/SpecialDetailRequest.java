package com.cplatform.xhxw.ui.http.net;

/**
 * 专题详情
 * Created by cy-love on 14-1-10.
 */
public class SpecialDetailRequest extends BaseRequest {

    private String specialid;

    public SpecialDetailRequest(String specialid) {
        this.specialid = specialid;
    }

    public String getSpecialid() {
        return specialid;
    }

    public void setSpecialid(String specialid) {
        this.specialid = specialid;
    }
}
