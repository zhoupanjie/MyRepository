package com.cplatform.xhxw.ui.db.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 搜索记录
 * Created by cy-love on 14-1-27.
 */
@DatabaseTable(tableName="search")
public class SearchDao {

    @DatabaseField(id = true)
    private String name;
    @DatabaseField
    private long lastTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }
}
