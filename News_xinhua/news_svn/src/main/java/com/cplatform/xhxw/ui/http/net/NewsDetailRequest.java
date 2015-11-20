package com.cplatform.xhxw.ui.http.net;

/**
 * 新闻详情接口
 * Created by cy-love on 14-1-8.
 */
public class NewsDetailRequest extends BaseRequest {

    private String newsid;
    private String from;

    public NewsDetailRequest(String newsid) {
        this.newsid = newsid;
    }

    public String getNewsid() {
        return newsid;
    }

    public void setNewsid(String newsid) {
        this.newsid = newsid;
    }

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}
}
