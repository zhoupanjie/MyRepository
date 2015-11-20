package com.cplatform.xhxw.ui.ui.main.cms.game;

import java.io.Serializable;

/**
 * 
 * @ClassName GameTypes 
 * @Description TODO 游戏应用分类数据模型
 * @Version 1.0
 * @Author zxe
 * @Creation 2015年6月26日 上午10:23:53 
 * @Mender zxe
 * @Modification 2015年6月26日 上午10:23:53 
 * @Copyright Copyright © 2013 - 2015 Channelsoft (Beijing) Technology Co., Ltd.All Rights Reserved.
*
 */
public class GameTypes implements Serializable {
	/**
	 * @Description long serialVersionUID： TODO 
	**/
	private static final long serialVersionUID = -358608414720788160L;
	private String catid;
	private String catname;
	public String getCatid() {
		return catid;
	}
	public void setCatid(String catid) {
		this.catid = catid;
	}
	public String getCatname() {
		return catname;
	}
	public void setCatname(String catname) {
		this.catname = catname;
	}
	
}
