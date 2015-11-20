package com.hy.superemsg.req;

import com.alibaba.fastjson.JSON;

public abstract class BaseReqApi {
	public String error;
	public String result;
	public ReqEpHeader epheader;

	public BaseReqApi() {
		epheader = new ReqEpHeader();

	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
}
