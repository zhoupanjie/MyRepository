package com.cplatform.xhxw.ui.ui.main.cms.game;

import com.cplatform.xhxw.ui.http.net.BaseRequest;

/**
 * 新闻详情接口
 * Created by cy-love on 14-1-8.
 */
public class GameDetailRequest extends BaseRequest {

    private String id;
    private String istest;

    public GameDetailRequest(String id) {
        this.id = id;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIstest() {
		return istest;
	}

	public void setIstest(String istest) {
		this.istest = istest;
	}
    
}
