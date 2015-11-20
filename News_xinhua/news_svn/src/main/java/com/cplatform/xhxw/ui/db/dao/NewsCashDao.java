package com.cplatform.xhxw.ui.db.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 栏目缓存
 * Created by cy-love on 14-1-27.
 */
@DatabaseTable(tableName="news_cash")
public class NewsCashDao {

    @DatabaseField(id = true)
    private String columnId; // 栏目id
    @DatabaseField
    private long saveTime; // 保存时间
    @DatabaseField
    private String json;

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
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
