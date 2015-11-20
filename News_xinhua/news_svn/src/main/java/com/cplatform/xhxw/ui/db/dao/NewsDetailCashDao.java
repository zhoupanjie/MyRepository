package com.cplatform.xhxw.ui.db.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 新闻缓存
 * Created by cy-love on 14-1-27.
 */
@DatabaseTable(tableName="news_detail_cash")
public class NewsDetailCashDao {

    @DatabaseField(id = true)
    private String newsId; // 栏目id
    @DatabaseField
    private long saveTime; // 保存时间
    @DatabaseField
    private String json;

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
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
