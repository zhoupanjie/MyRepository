package com.cplatform.xhxw.ui.http.net.saas;

import com.cplatform.xhxw.ui.http.net.BaseRequest;

/**
 * 公司圈
 * Created by cy-love on 14-8-23.
 */
public class CompanyZoneListRequest extends BaseRequest {

    private int typeid;
    private String infotime;
    private int limit;

    public CompanyZoneListRequest(int type, String infotime, int limit) {
        this.typeid = type;
        this.infotime = infotime;
        this.limit = limit;
        this.setDevRequest(true);
    }

    public int getTypeid() {
        return typeid;
    }

    public void setTypeid(int typeid) {
        this.typeid = typeid;
    }

    public String getInfotime() {
        return infotime;
    }

    public void setInfotime(String infotime) {
        this.infotime = infotime;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
}
