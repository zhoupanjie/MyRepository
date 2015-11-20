package com.cplatform.xhxw.ui.ui.main.cms.game;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @ClassName GameDetail 
 * @Description TODO 游戏详情
 * @Version 1.0
 * @Author zxe
 * @Creation 2015年6月26日 下午5:21:32 
 * @Mender zxe
 * @Modification 2015年6月26日 下午5:21:32 
 * @Copyright Copyright © 2013 - 2015 Channelsoft (Beijing) Technology Co., Ltd.All Rights Reserved.
*
 */
public class GameDetail implements Serializable {
    private String id;
    private String name;
    private String shorturl;//分享链接
    private String summary;
    private String icon;
    private String downloadurl;
    private String initialtime;
    private String updatetime;
    private String developer;
    private String version;
    private String gamesize;
    private String content;
    private String catid;//游戏类型
	private String catname;
    private List<GameImage> background;
    private List<GameImage> contentimage;
    private String packageName;//包名
	private String versionCode;//版本号
	private String versionName;//版本名称
	private int stateDown=GameUtil.GAME_DOWN_UN;
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
	public String getShorturl() {
		return shorturl;
	}
	public void setShorturl(String shorturl) {
		this.shorturl = shorturl;
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
	public String getInitialtime() {
		return initialtime;
	}
	public void setInitialtime(String initialtime) {
		this.initialtime = initialtime;
	}
	public String getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}
	public String getDeveloper() {
		return developer;
	}
	public void setDeveloper(String developer) {
		this.developer = developer;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getGamesize() {
		return gamesize;
	}
	public void setGamesize(String gamesize) {
		this.gamesize = gamesize;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public List<GameImage> getBackground() {
		return background;
	}
	public void setBackground(List<GameImage> background) {
		this.background = background;
	}
	public List<GameImage> getContentimage() {
		return contentimage;
	}
	public void setContentimage(List<GameImage> contentimage) {
		this.contentimage = contentimage;
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
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public int getStateDown() {
		return stateDown;
	}
	public void setStateDown(int stateDown) {
		this.stateDown = stateDown;
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
