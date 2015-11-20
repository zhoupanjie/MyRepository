package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.model.MyCommentList;

/**
 * 我的评论解析
 * Created by cy-love on 14-1-19.
 */
public class MyCommentResponse extends BaseResponse {

    private MyCommentList data;

	public MyCommentList getData() {
		return data;
	}

	public void setData(MyCommentList data) {
		this.data = data;
	}
    
}
