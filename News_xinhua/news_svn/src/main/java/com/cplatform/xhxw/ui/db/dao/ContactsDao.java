package com.cplatform.xhxw.ui.db.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 通讯录
 * Created by cy-love on 14-8-3.
 */
@DatabaseTable(tableName = "contacts")
public class ContactsDao {

    public static final String TABLE_NAME = "contacts";
    public static final String _ID = "_id";
    public static final String USER_ID = "userid";
    public static final String LOGO = "logo";
    public static final String NAME = "name";
    public static final String INDEX_KEY = "indexKey";
    public static final String ORDER_KEY = "orderKey";
    public static final String NICK_NAME = "nickname";
    public static final String COMMENT = "comment";
    public static final String SIGN = "sign";
    public static final String MY_UID = "myUid";
    public static final String IS_MY_CONTACTS = "isMyContacts";
    public static final String SYNC_TIME = "syncTime";

    @DatabaseField(generatedId = true)
    private long _id; // 自增长行id
    @DatabaseField
    private String userid; // 用户id
    @DatabaseField
    private String logo; // 头像
    @DatabaseField
    private String name; // 用户名
    @DatabaseField
    private String nickname; // 昵称
    @DatabaseField
    private String comment; // 备注
    @DatabaseField
    private String gender; // 性别  0默认  1男  2女
    @DatabaseField
    private String sign; // 签名
    @DatabaseField
    private String indexKey; // 索引
    @DatabaseField
    private String orderKey; // 排序
    @DatabaseField
    private String myUid; // 我的id
    @DatabaseField
    private int isMyContacts; // 1 为通讯录联系人  0为未知人员
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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getIndexKey() {
        return indexKey;
    }

    public void setIndexKey(String indexKey) {
        this.indexKey = indexKey;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getIsMyContacts() {
        return isMyContacts;
    }

	public void setIsMyContacts(int isMyContacts) {
        this.isMyContacts = isMyContacts;
    }
    
    
}
