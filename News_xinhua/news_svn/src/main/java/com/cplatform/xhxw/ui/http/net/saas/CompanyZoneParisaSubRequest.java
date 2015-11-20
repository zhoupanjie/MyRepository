package com.cplatform.xhxw.ui.http.net.saas;

import com.cplatform.xhxw.ui.http.net.BaseRequest;

/**
 * 公司圈点赞
 * Created by cy-love on 14-8-28.
 */
public class CompanyZoneParisaSubRequest extends BaseRequest {

    private String infoid;
    public CompanyZoneParisaSubRequest(String infoid) {
        this.infoid = infoid;
        setDevRequest(true);
    }

    public String getInfoid() {
        return infoid;
    }

    public void setInfoid(String infoid) {
        this.infoid = infoid;
    }
}
