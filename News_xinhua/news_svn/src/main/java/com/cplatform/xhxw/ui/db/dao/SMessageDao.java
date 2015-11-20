package com.cplatform.xhxw.ui.db.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * S信对话列表
 * Created by cy-love on 14-8-3.
 */
@DatabaseTable(tableName = "s_message")
public class SMessageDao {

    public static final String TABLE_NAME = "s_message";
    public static final String _ID = "_id";
    public static final String ROOM_ID = "room_id";
    public static final String LOGO = "logo";
    public static final String NAME = "name";
    public static final String NICK_NAME = "nickname";
    public static final String COMMENT = "comment";
    public static final String LOGO_TYPE = "logo_type";

    public static final String LAST_MSG = "last_msg";
    public static final String LAST_MSG_STATUS = "last_msg_status";
    public static final String LASTCTIONTIME = "lastactiontime";
    public static final String CTIME = "ctime";
    public static final String UNREAD_COUNT = "unread_count";

    public static final String MY_UID = "my_uid";
    public static final String ORDER_KEY = "order_key";

    @DatabaseField(generatedId = true)
    private long _id; // 自增长行id
    @DatabaseField
    private String room_id; // 房间id  个人：user_用户id  组：group_创建者用户id_编号(当前时间戳,单位秒)
    @DatabaseField
    private int logo_type; // 默认图像展示 0默认  1男 2女 3群组
    @DatabaseField
    private String logo; // 图像
    @DatabaseField
    private String name; // 用户名称
    @DatabaseField
    private String nickname; // 昵称
    @DatabaseField
    private String comment; // 备注
    @DatabaseField
    private String last_msg; // 最后一条消息
    @DatabaseField
    private int last_msg_status; // 最后一条消息状态  0: 已发送成功 1:未发送成功
    @DatabaseField
    private String ctime; // 聊天时间
    @DatabaseField
    private String lastactiontime; // 最后更新时间
    @DatabaseField
    private int unread_count; // 未读数量
    @DatabaseField
    private String my_uid; // 我的id
    @DatabaseField
    private long order_key; // 用于置顶排序

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public int getLogo_type() {
        return logo_type;
    }

    public void setLogo_type(int logo_type) {
        this.logo_type = logo_type;
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

    public String getLast_msg() {
        return last_msg;
    }

    public void setLast_msg(String last_msg) {
        this.last_msg = last_msg;
    }

    public int getLast_msg_status() {
        return last_msg_status;
    }

    public void setLast_msg_status(int last_msg_status) {
        this.last_msg_status = last_msg_status;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getLastactiontime() {
        return lastactiontime;
    }

    public void setLastactiontime(String lastactiontime) {
        this.lastactiontime = lastactiontime;
    }

    public int getUnread_count() {
        return unread_count;
    }

    public void setUnread_count(int unread_count) {
        this.unread_count = unread_count;
    }

    public String getMy_uid() {
        return my_uid;
    }

    public void setMy_uid(String my_uid) {
        this.my_uid = my_uid;
    }

    public long getOrder_key() {
        return order_key;
    }

    public void setOrder_key(long order_key) {
        this.order_key = order_key;
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
}
