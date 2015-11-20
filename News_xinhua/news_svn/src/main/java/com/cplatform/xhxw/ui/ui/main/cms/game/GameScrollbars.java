package com.cplatform.xhxw.ui.ui.main.cms.game;

import java.io.Serializable;
/**
 * 
 * @ClassName GameScrollbars 
 * @Description TODO 游戏资讯滚动
 * @Version 1.0
 * @Author zxe
 * @Creation 2015年8月11日 下午5:33:35 
 * @Mender zxe
 * @Modification 2015年8月11日 下午5:33:35 
 * @Copyright Copyright © 2013 - 2015 Channelsoft (Beijing) Technology Co., Ltd.All Rights Reserved.
*
 */
public class GameScrollbars implements Serializable {
	/**
	 * @Description long serialVersionUID： TODO 
	**/
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
