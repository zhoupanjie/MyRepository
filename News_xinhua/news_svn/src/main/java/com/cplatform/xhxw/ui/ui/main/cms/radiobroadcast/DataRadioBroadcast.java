package com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast;

/**
 * 
 * @ClassName DataRadioBroadcast 
 * @Description TODO 广播数据模型
 * @Version 1.0
 * @Author zxe
 * @Creation 2015年9月23日 上午10:05:20 
 * @Mender zxe
 * @Modification 2015年9月23日 上午10:05:20 
 * @Copyright Copyright © 2013 - 2015 Channelsoft (Beijing) Technology Co., Ltd.All Rights Reserved.
*
 */
public class DataRadioBroadcast{
	/**
	 * @Description long serialVersionUID： TODO 
	**/
	private String audioid;
	private String name;
	private String url;
	private String audiotime;
	private String audiosec;
	private String updatetime;
	private String catid;
    private String catname;
	private boolean isRead; // 已读
	
	public String getAudioid() {
		return audioid;
	}
	public void setAudioid(String audioid) {
		this.audioid = audioid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAudiotime() {
		return audiotime;
	}
	public void setAudiotime(String audiotime) {
		this.audiotime = audiotime;
	}
	public String getAudiosec() {
		return audiosec;
	}
	public void setAudiosec(String audiosec) {
		this.audiosec = audiosec;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public boolean isRead() {
		return isRead;
	}
	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}
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
