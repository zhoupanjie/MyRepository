package com.cplatform.xhxw.ui.db.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName="collect")
public class CollectDao {
	
	/**
	 * 收藏
	 * 
	 * 可收藏类型:图片、新闻
	 * 
	 * 收藏时，只能同时收藏一种类型的文件
	 * 
	 * flag 收藏类型
	 * 
	 * flag = 1   收藏普通用户新闻图片
	 * flag = 2   收藏普通用户普通新闻
     * flag = 101 收藏企业用户图片新闻
     * flag = 102 收藏企业用户普通新闻 
	 * */

    @DatabaseField(id = true)
    private String newsId; // 新闻id
	@DatabaseField
	private String title; // 标题
    @DatabaseField
    private String img; // 图片类型的缩略图（为2.0预留）
    @DatabaseField
    private String summary; // 简介（为2.0预留）
    /**
     * 数据类型
     * 1:new
     * 2:SpecialDetailData
     * 3:Focus
     */
    @DatabaseField
    private int dataType; // 数据类型
    @DatabaseField
    private String data; // 新闻json数据
    @DatabaseField
    private int flag; // 收藏类型
    @DatabaseField
    private long operatetime; // 修改时间戳

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public long getOperatetime() {
        return operatetime;
    }

    public void setOperatetime(long operatetime) {
        this.operatetime = operatetime;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
