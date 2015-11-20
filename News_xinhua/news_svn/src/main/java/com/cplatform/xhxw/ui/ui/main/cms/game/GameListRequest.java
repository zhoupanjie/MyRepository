package com.cplatform.xhxw.ui.ui.main.cms.game;

import com.cplatform.xhxw.ui.http.net.BaseRequest;

/**
 * Created by cy-love on 14-1-7.
 */
public class GameListRequest extends BaseRequest{


    private String updatetime;
    private String order;
    private String type;
    private String catid;
    private String isfirst;//catlist
    private String istest;

    public GameListRequest() {
    }

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCatid() {
		return catid;
	}

	public void setCatid(String catid) {
		this.catid = catid;
	}

	public String getIsfirst() {
		return isfirst;
	}

	public void setIsfirst(String isfirst) {
		this.isfirst = isfirst;
	}

	public String getIstest() {
		return istest;
	}

	public void setIstest(String istest) {
		this.istest = istest;
	}
}
