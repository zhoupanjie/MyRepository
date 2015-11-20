package com.cplatform.xhxw.ui.db.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: pliliang
 * Date: 13-11-26
 * Time: 上午11:48
 */
@DatabaseTable(tableName="downloaddao")
public class DownloadDao implements Serializable{

    public static final int STATUS_DOWNLOADED = 1; // 已经下载
    public static final int STATUS_DOWNLOADING = 0; // 还未下载

    @DatabaseField(generatedId = true)
    private long id;
    @DatabaseField
    private String url;
    @DatabaseField
    private int downloaded;//1：已经下载;0：还未下载
    @DatabaseField
    private long addTime; // 添加时间

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(int downloaded) {
        this.downloaded = downloaded;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAddTime() {
        return addTime;
    }

    public void setAddTime(long addTime) {
        this.addTime = addTime;
    }
}
