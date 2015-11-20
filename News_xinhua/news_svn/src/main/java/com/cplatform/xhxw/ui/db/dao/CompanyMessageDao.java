package com.cplatform.xhxw.ui.db.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 朋友消息
 */
@DatabaseTable(tableName = "company_message_book")
public class CompanyMessageDao {

	public static final String TABLE_NAME = "company_message_book";
	public static final String _ID = "_id";
	public static final String USER_ID = "userid";
	public static final String MY_UID = "myUid";
	public static final String LOGO = "logo";
	public static final String NAME = "name";
	public static final String NICK_NAME = "nickname";
	public static final String COMMENT = "comment";
	public static final String CTIME = "ctime";// 创建时间
	public static final String FRIENDSTIME = "friendtime";// 友好时间
	public static final String COMPANYID = "companyid";
	public static final String ACTIONTYPE = "actiontype";// 消息类型' , //1 赞 4评论
	public static final String BACKINFO = "backinfo";// '消息内容'
	public static final String INFODATA = "infodata";// 新鲜事内容
	public static final String INFOID = "infoid";// 新鲜事ID
	public static final String SYNC_TIME = "syncTime";

	@DatabaseField(generatedId = true)
	private long _id; // 自增长行id
	@DatabaseField
	private String userid; // 用户id
	@DatabaseField
	private String myUid; // 我的id
	@DatabaseField
	private String logo; // 头像
	@DatabaseField
	private String name; // 用户名
	@DatabaseField
	private String nickname; // 昵称
	@DatabaseField
	private String comment; // 备注名称
	@DatabaseField
	private String ctime; // 创建时间
	@DatabaseField
	private String friendtime; // 好有时间
	@DatabaseField
	private String companyid;
	@DatabaseField
	private String actiontype; // 消息类型
	@DatabaseField
	private String backinfo; // 消息内容
	@DatabaseField
	private String infodata; // 新鲜事内容
	@DatabaseField
	private String infoid; // 新鲜事ID
	@DatabaseField
	private long syncTime; // 同步时间

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getMyUid() {
		return myUid;
	}

	public void setMyUid(String myUid) {
		this.myUid = myUid;
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

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getFriendtime() {
		return friendtime;
	}

	public void setFriendtime(String friendtime) {
		this.friendtime = friendtime;
	}

	public String getCompanyid() {
		return companyid;
	}

	public void setCompanyid(String companyid) {
		this.companyid = companyid;
	}

	public String getActiontype() {
		return actiontype;
	}

	public void setActiontype(String actiontype) {
		this.actiontype = actiontype;
	}

	public String getBackinfo() {
		return backinfo;
	}

	public void setBackinfo(String backinfo) {
		this.backinfo = backinfo;
	}

	public String getInfodata() {
		return infodata;
	}

	public void setInfodata(String infodata) {
		this.infodata = infodata;
	}

	public String getInfoid() {
		return infoid;
	}

	public void setInfoid(String infoid) {
		this.infoid = infoid;
	}

	public long getSyncTime() {
		return syncTime;
	}

	public void setSyncTime(long syncTime) {
		this.syncTime = syncTime;
	}
}
