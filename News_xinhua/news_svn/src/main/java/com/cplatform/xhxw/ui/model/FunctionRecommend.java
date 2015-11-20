package com.cplatform.xhxw.ui.model;

/**
 * 
 * @ClassName FunctionRecommend 汽车频道功能推荐模型
 * @Description TODO 
 * @Version 1.0
 * @Author Administrator
 * @Creation 2015年4月29日 下午3:32:11 
 * @Mender Administrator
 * @Modification 2015年4月29日 下午3:32:11 
 * @Copyright Copyright © 2012 - 2015 Petro-CyberWorks Information Technology Company Limlted.All Rights Reserved.
*
 */
public class FunctionRecommend {

    private String title;//标题
    private String detailUrl;//详情URL
    private int imgId;//图片Id
    private int index;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDetailUrl() {
		return detailUrl;
	}
	public void setDetailUrl(String detailUrl) {
		this.detailUrl = detailUrl;
	}
	public int getImgId() {
		return imgId;
	}
	public void setImgId(int imgId) {
		this.imgId = imgId;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
}
