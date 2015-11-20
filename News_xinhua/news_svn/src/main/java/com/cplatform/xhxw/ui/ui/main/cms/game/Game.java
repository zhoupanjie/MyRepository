package com.cplatform.xhxw.ui.ui.main.cms.game;

import java.io.Serializable;

/**
 * 
 * @ClassName Game 
 * @Description TODO 游戏应用列表项数据模型
 * @Version 1.0
 * @Author zxe
 * @Creation 2015年6月26日 上午10:23:53 
 * @Mender zxe
 * @Modification 2015年6月26日 上午10:23:53 
 * @Copyright Copyright © 2013 - 2015 Channelsoft (Beijing) Technology Co., Ltd.All Rights Reserved.
*
 */
public class Game implements Serializable {
	/**
	 * @Description long serialVersionUID： TODO 
	**/
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	private String summary;
	private String icon;
	private String downloadurl;
	private String order;
	private String updatetime;
	private boolean isRead; // 已读
	private String catid;//游戏类型
	private String catname;
	private String gamesizetext;
	private int stateDown=GameUtil.GAME_DOWN_UN;
	private String filename;//文件名称
	private String savePath;//存储路径
	private String packageName;//包名
	private String versionCode;//版本号
	private String versionName;//版本名称
	public int fileSize;//文件大小
    private int complete;//完成度
    private String downloadCount;//下载次数
    private String downTime; // 保存时间
    private String installTime;//安装时间
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
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getDownloadurl() {
		return downloadurl;
	}
	public void setDownloadurl(String downloadurl) {
		this.downloadurl = downloadurl;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
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
	
	public String getGamesizetext() {
		return gamesizetext;
	}
	public void setGamesizetext(String gamesizetext) {
		this.gamesizetext = gamesizetext;
	}
	public int getStateDown() {
		return stateDown;
	}
	public void setStateDown(int stateDown) {
		this.stateDown = stateDown;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getSavePath() {
		return savePath;
	}
	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	public int getFileSize() {
		return fileSize;
	}
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	public int getComplete() {
		return complete;
	}
	public void setComplete(int complete) {
		this.complete = complete;
	}
	public String getDownloadCount() {
		return downloadCount;
	}
	public void setDownloadCount(String downloadCount) {
		this.downloadCount = downloadCount;
	}
	public String getDownTime() {
		return downTime;
	}
	public void setDownTime(String downTime) {
		this.downTime = downTime;
	}
	public String getInstallTime() {
		return installTime;
	}
	public void setInstallTime(String installTime) {
		this.installTime = installTime;
	}
}
