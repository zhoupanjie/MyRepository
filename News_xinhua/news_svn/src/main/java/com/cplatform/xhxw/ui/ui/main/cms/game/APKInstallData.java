package com.cplatform.xhxw.ui.ui.main.cms.game;

import java.io.Serializable;

public class APKInstallData implements Serializable {
	/**
	 * @Description long serialVersionUID： TODO 
	**/
	private static final long serialVersionUID = -8137773069850389881L;
	private String gameId;
	private int launchUI;//1.列表，2.详情页。3.下载中心
	private String packageName;
	private String filePath;
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public int getLaunchUI() {
		return launchUI;
	}
	public void setLaunchUI(int launchUI) {
		this.launchUI = launchUI;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
