package com.cplatform.xhxw.ui.db.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 已读新闻
 */
@DatabaseTable(tableName="read_news")
public class ReadNewsDao {

	public ReadNewsDao() {}

    public ReadNewsDao(String newsId, long readTime) {
        this.newsId = newsId;
        this.readTime = readTime;
    }

	@DatabaseField(id = true)
    private String newsId; // 新闻id
	@DatabaseField
    private long readTime; // 阅读时间

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public long getReadTime() {
        return readTime;
    }

    public void setReadTime(long readTime) {
        this.readTime = readTime;
    }
}
