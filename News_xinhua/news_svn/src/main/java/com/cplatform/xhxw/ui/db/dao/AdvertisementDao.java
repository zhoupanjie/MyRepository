package com.cplatform.xhxw.ui.db.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 广告信息缓存
 * @author jinyz
 *
 */
@DatabaseTable(tableName = "advertisement_info")
public class AdvertisementDao {
	/**
	 * 广告脏数据，策略删除
	 */
	public static final int ADVER_TYPE_DIRT = 0;
	public static final int ADVER_TYPE_IMG = 1;
	public static final int ADVER_TYPE_TXT = 2;
	public static final int ADVER_TYPE_MULTI = 3;
	
	/**
	 * 广告显示位置为loading页上半部分
	 */
	public static final int ADVER_SHOW_POSITION_LOADING = 1;
	/**
	 * 广告显示位置为焦点区
	 */
	public static final int ADVER_SHOW_POSITION_FOCUS = 2;
	/**
	 * 广告显示位置为角标区
	 */
	public static final int ADVER_SHOW_POSITION_FLAG = 3;
	/**
	 * 广告显示位置为详情页
	 */
	public static final int ADVER_SHOW_POSITION_DETAIL = 4;
	/**
	 * 广告显示位置为loading页下半部分
	 */
	public static final int ADVER_SHOW_POSITION_BOTTOM = 5;
	
	public static final String ADVER_COLUMN_ADVERID = "adverId";
	public static final String ADVER_COLUMN_ADVERENDTIME = "adverEndTime";
	public static final String ADVER_COLUMN_ADVERTITLE = "adverTitle";
	public static final String ADVER_COLUMN_ADVERURL = "adverUrl";
	public static final String ADVER_COLUMN_ADVERSHORTURL = "adverShortUrl";
	public static final String ADVER_COLUMN_ADVERTYPEID = "adverTypeId";
	public static final String ADVER_COLUMN_ADVERIMG = "adverImg";
	public static final String ADVER_COLUMN_ADVERRANK = "adverRank";
	public static final String ADVER_COLUMN_ADVERSHOWPOSITION = "adverShowPosition";
	public static final String ADVER_COLUMN_ADVERPOSTIONINFO = "adverPositionInfo";
	
	@DatabaseField(id = true)
	private String adverId; //广告
	@DatabaseField
	private long adverEndTime; //过期时间
	@DatabaseField 
	private String adverTitle; //广告标题
	@DatabaseField
	private String adverUrl; //原始URL地址
	@DatabaseField
	private String adverShortUrl; //炫闻短网址跳转地址
	/**
	 * 广告类型ID：1为图片；2为文字；3为图文；0为待删除脏数据
	 */
	@DatabaseField
	private int adverTypeId; 
	
	@DatabaseField
	private String adverImg; //新闻图片
	@DatabaseField
	private int adverRank; //新闻显示顺序
	/**
	 * 广告展示位置：1为loading页上部；2为焦点区；3为角标区；4为详情页；5为loading页下部
	 */
	@DatabaseField
	private int adverShowPosition;
	
	@DatabaseField
	private String adverPositionInfo;//定位信息
	
	public String getAdverId() {
		return adverId;
	}
	public void setAdverId(String adverId) {
		this.adverId = adverId;
	}
	public long getAdverEndTime() {
		return adverEndTime;
	}
	public void setAdverEndTime(long adverEndTime) {
		this.adverEndTime = adverEndTime;
	}
	public String getAdverTitle() {
		return adverTitle;
	}
	public void setAdverTitle(String adverTitle) {
		this.adverTitle = adverTitle;
	}
	public String getAdverUrl() {
		return adverUrl;
	}
	public void setAdverUrl(String adverUrl) {
		this.adverUrl = adverUrl;
	}
	public String getAdverShortUrl() {
		return adverShortUrl;
	}
	public void setAdverShortUrl(String adverShortUrl) {
		this.adverShortUrl = adverShortUrl;
	}
	public int getAdverTypeId() {
		return adverTypeId;
	}
	public void setAdverTypeId(int adverTypeId) {
		this.adverTypeId = adverTypeId;
	}
	public String getAdverImg() {
		return adverImg;
	}
	public void setAdverImg(String adverImg) {
		this.adverImg = adverImg;
	}
	public int getAdverRank() {
		return adverRank;
	}
	public void setAdverRank(int adverRank) {
		this.adverRank = adverRank;
	}
	public int getAdverShowPosition() {
		return adverShowPosition;
	}
	public void setAdverShowPosition(int adverShowPosition) {
		this.adverShowPosition = adverShowPosition;
	}
	public String getPositionInfo() {
		return adverPositionInfo;
	}
	public void setPositionInfo(String positionInfo) {
		this.adverPositionInfo = positionInfo;
	}
}
