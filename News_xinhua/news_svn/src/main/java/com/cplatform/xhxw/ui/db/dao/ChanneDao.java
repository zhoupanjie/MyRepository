package com.cplatform.xhxw.ui.db.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 栏目数据库表
 */
@DatabaseTable(tableName = "channe")
public class ChanneDao {
	public static final String CHANNEL_ENTERPRISE = "enterpriseId";
	public static final String ENTERPRISE_CHANNEL_TYPE = "channelType";
	public static final String CHANNEL_LANGUAGE = "channelLanguage";
	public static final String CHANNEL_TYPE = "channelCreateType";

	/**
	 * 固定栏目
	 */
	public static final int CHANNEL_TYPE_GUDING = 0;
	/**
	 * 推荐栏目
	 */
	public static final int CHANNEL_TYPE_RECOMMENDED = 1;
	/**
	 * 关键字栏目
	 */
	public static final int CHANNEL_TYPE_KEY_WORDS = 2;
	@DatabaseField(generatedId = true)
	private long id; // 自增长行id
	@DatabaseField
	private String channelID; // 数据id
	@DatabaseField
	private String channelName; // 栏目名称
	@DatabaseField
	private int show; // 是否显示 0隐藏 | 1显示
	@DatabaseField
	private long listorder; // 栏目顺序
	@DatabaseField
	private long operatetime; // 栏目修改时间戳
	@DatabaseField
	private String userId; // 所属用户id 系统-1
	@DatabaseField
	private int dirty; // 判断是否被修改过 1表示被修改过
	@DatabaseField
	private long syncTime; // 同步时间
	@DatabaseField
	private int enterpriseId; // 企业id
	/**
	 * 标识企业栏目是否为push消息栏目：0不是，1是
	 */
	@DatabaseField
	private int channelType;

	/**
	 * 频道语言种类
	 */
	@DatabaseField
	private String channelLanguage;

	/**
	 * 频道创建类型 0：固定频道 1：订阅频道 2：关键字频道
	 */
	@DatabaseField
	private String channelCreateType;

	/**
	 * 是否暂时隐藏
	 */
	private boolean isInVisible;

	/**
	 * 被删除前的XY坐标数组
	 */
	public int[] XY;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getChannelID() {
		return channelID;
	}

	public void setChannelID(String channelID) {
		this.channelID = channelID;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public int getShow() {
		return show;
	}

	public void setShow(int show) {
		this.show = show;
	}

	public long getListorder() {
		return listorder;
	}

	public void setListorder(long listorder) {
		this.listorder = listorder;
	}

	public long getOperatetime() {
		return operatetime;
	}

	public void setOperatetime(long operatetime) {
		this.operatetime = operatetime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getDirty() {
		return dirty;
	}

	public void setDirty(int dirty) {
		this.dirty = dirty;
	}

	public long getSyncTime() {
		return syncTime;
	}

	public void setSyncTime(long syncTime) {
		this.syncTime = syncTime;
	}

	public int getEnterpriseId() {
		return enterpriseId;
	}

	public void setEnterpriseId(int enterpriseId) {
		this.enterpriseId = enterpriseId;
	}

	public int getChannelType() {
		return channelType;
	}

	public void setChannelType(int channelType) {
		this.channelType = channelType;
	}

	public String getChannelLanguage() {
		return channelLanguage;
	}

	public void setChannelLanguage(String channelLanguage) {
		this.channelLanguage = channelLanguage;
	}

	public String getChannelCreateType() {
		return channelCreateType;
	}

	public void setChannelCreateType(String channelCreateType) {
		this.channelCreateType = channelCreateType;
	}

	public boolean isInVisible() {
		return isInVisible;
	}

	public void setInVisible(boolean isInVisible) {
		this.isInVisible = isInVisible;
	}

}
