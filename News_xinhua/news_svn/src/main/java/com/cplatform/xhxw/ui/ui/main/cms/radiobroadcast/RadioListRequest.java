package com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast;

import com.cplatform.xhxw.ui.http.net.BaseRequest;

/**
 * 
 * @ClassName RadioListRequest 
 * @Description TODO 广播列表请求
 * @Version 1.0
 * @Author zxe
 * @Creation 2015年8月24日 上午10:43:34 
 * @Mender zxe
 * @Modification 2015年8月24日 上午10:43:34 
 * @Copyright Copyright © 2013 - 2015 Channelsoft (Beijing) Technology Co., Ltd.All Rights Reserved.
*
 */
public class RadioListRequest extends BaseRequest{
    private String updatetime;//更新时间
    private String type;//获取跟多0，获取最新数据1
    private String catid;//游戏类型id
    private String catlist;//当type>0，参数才起作用。1，需要栏目列表，2是不需要。默认值是2
    private String offset;//返回文章列表，默认20
    
    public RadioListRequest() {
    }

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
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

	public String getCatlist() {
		return catlist;
	}

	public void setCatlist(String catlist) {
		this.catlist = catlist;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}
    
	
}
