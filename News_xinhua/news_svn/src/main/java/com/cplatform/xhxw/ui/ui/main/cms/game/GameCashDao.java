package com.cplatform.xhxw.ui.ui.main.cms.game;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 
 * @ClassName GameCashDao 
 * @Description TODO 游戏缓存
 * @Version 1.0
 * @Author zxe
 * @Creation 2015年6月29日 下午1:59:58 
 * @Mender zxe
 * @Modification 2015年6月29日 下午1:59:58 
 * @Copyright Copyright © 2013 - 2015 Channelsoft (Beijing) Technology Co., Ltd.All Rights Reserved.
*
 */
@DatabaseTable(tableName="game_cash")
public class GameCashDao {

    @DatabaseField(id = true)
    private String appId; // 栏目id
    @DatabaseField
    private long saveTime; // 保存时间
    @DatabaseField
    private String json;
    public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public long getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(long saveTime) {
        this.saveTime = saveTime;
    }

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}
}
