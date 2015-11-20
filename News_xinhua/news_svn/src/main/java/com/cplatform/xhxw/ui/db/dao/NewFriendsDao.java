package com.cplatform.xhxw.ui.db.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 新的好友 Created by cy-love on 14-8-3.
 */
@DatabaseTable(tableName = "new_friends_book")
public class NewFriendsDao {

	public static final String TABLE_NAME = "new_friends_book";
	public static final String _ID = "_id";
	public static final String USER_ID = "userid";
	public static final String MY_UID = "myUid";
	public static final String INFO_ID = "infoid";
	public static final String LOGO = "logo";
	public static final String NAME = "name";
	public static final String NICK_NAME = "nickname";	
	public static final String RENAME = "rename";
	public static final String SEX = "sex"; 
	public static final String COMMENT = "comment"; 
	public static final String SYNC_TIME = "syncTime";
	public static final String ORDER_KEY = "orderKey";
	public static final String STATUS = "status";//1 添加  2 等待验证  3 已添加  4 接受

	@DatabaseField(generatedId = true)
	private long _id; // 自增长行id
	@DatabaseField
	private String userid; // 用户id
	@DatabaseField
	private String myUid; // 我的id
	@DatabaseField
	private String infoid; // 信息id
	@DatabaseField
	private String logo; // 头像
	@DatabaseField
	private String name; // 用户名
	@DatabaseField
	private String nickname; // 昵称
	@DatabaseField
	private String rename; // 备注名称
	@DatabaseField
	private String sex; // 性别
	@DatabaseField
	private String comment; // 备注描述
	@DatabaseField
	private String orderKey; // 排序
	@DatabaseField
	private long syncTime; // 同步时间
	@DatabaseField
	private String status; // 好友状态

	public String getRename() {
		return rename;
	}

	public void setRename(String rename) {
		this.rename = rename;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public String getInfoid() {
		return infoid;
	}

	public void setInfoid(String infoid) {
		this.infoid = infoid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getOrderKey() {
		return orderKey;
	}

	public void setOrderKey(String orderKey) {
		this.orderKey = orderKey;
	}

	public String getMyUid() {
		return myUid;
	}

	public void setMyUid(String myUid) {
		this.myUid = myUid;
	}

	public long getSyncTime() {
		return syncTime;
	}

	public void setSyncTime(long syncTime) {
		this.syncTime = syncTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
