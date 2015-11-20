package com.cplatform.xhxw.ui.ui.main.cms.game;

import java.io.Serializable;

/**
 * 
 * @ClassName GameImage 
 * @Description TODO 游戏图片
 * @Version 1.0
 * @Author zxe
 * @Creation 2015年6月26日 下午5:17:11 
 * @Mender zxe
 * @Modification 2015年6月26日 下午5:17:11 
 * @Copyright Copyright © 2013 - 2015 Channelsoft (Beijing) Technology Co., Ltd.All Rights Reserved.
*
 */
public class GameImage implements Serializable {
	
    /**
	 * @Description long serialVersionUID： TODO 
	**/
	private static final long serialVersionUID = -8155178375336967663L;
	private String normal;
    private String thumb;
	public String getNormal() {
		return normal;
	}
	public void setNormal(String normal) {
		this.normal = normal;
	}
	public String getThumb() {
		return thumb;
	}
	public void setThumb(String thumb) {
		this.thumb = thumb;
	}
    
}
