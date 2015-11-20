package com.cplatform.xhxw.ui.ui.main.cms.game;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 
 * @ClassName ReadGameDao 
 * @Description TODO 已读游戏
 * @Version 1.0
 * @Author zxe
 * @Creation 2015年6月26日 上午10:57:04 
 * @Mender zxe
 * @Modification 2015年6月26日 上午10:57:04 
 * @Copyright Copyright © 2013 - 2015 Channelsoft (Beijing) Technology Co., Ltd.All Rights Reserved.
*
 */
@DatabaseTable(tableName="read_game")
public class ReadGameDao {

	public ReadGameDao() {}

    public ReadGameDao(String id, long readTime) {
        this.id = id;
        this.readTime = readTime;
    }

	@DatabaseField(id = true)
    private String id; // 游戏id
	@DatabaseField
    private long readTime; // 阅读时间

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getReadTime() {
        return readTime;
    }

    public void setReadTime(long readTime) {
        this.readTime = readTime;
    }
}
