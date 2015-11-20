package com.cplatform.xhxw.ui.http.net.saas;

import com.cplatform.xhxw.ui.http.net.BaseRequest;

/**
 * 朋友圈点赞
 * Created by cy-love on 14-8-28.
 */
public class FriendZoneParisaSubRequest extends BaseRequest {

    private String infoid;
    private String infouserid;
    public FriendZoneParisaSubRequest(String infoid, String infouserid) {
        this.infoid = infoid;
        this.infouserid = infouserid;
        setDevRequest(true);
    }

    public String getInfoid() {
        return infoid;
    }

    public void setInfoid(String infoid) {
        this.infoid = infoid;
    }

    public String getInfouserid() {
        return infouserid;
    }

    public void setInfouserid(String infouserid) {
        this.infouserid = infouserid;
    }
}
