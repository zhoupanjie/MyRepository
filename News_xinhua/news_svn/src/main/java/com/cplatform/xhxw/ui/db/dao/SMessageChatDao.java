package com.cplatform.xhxw.ui.db.dao;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * S信对话列表
 * Created by cy-love on 14-8-3.
 */
@DatabaseTable(tableName = "s_message_chat")
public class SMessageChatDao {

    public static final String TABLE_NAME = "s_message_chat";
    public static final String _ID = "_id";
    public static final String ROOM_ID = "room_id";
    public static final String INFO_ID = "infoid";
    public static final String MY_UID = "my_uid";
    public static final String ISSELF = "isself";
    public static final String USER_ID = "userid";
    public static final String EXRTA_THUMB = "exrtaThumb"; // 缩略图
    public static final String EXRTA_FILE = "exrtaFile"; // 音频或者文件地址
    public static final String MSG = "msg";
    public static final String MSG_TYPE = "msg_type";
    public static final String STATUS = "status";
    public static final String TIME = "time";
    public static final String IS_READ = "is_read";

    /*消息类型*/
    /** 发送成功 */
    public static final int MSG_STATUS_SUCCESS = 0;
    /** 发送失败 */
    public static final int MSG_STATUS_ERROR = 1;


    @DatabaseField(generatedId = true)
    private long _id; // 自增长行id
    @DatabaseField
    private long room_id; // 对话房间id  与sMessageDao#room_id对应
    @DatabaseField
    private String infoid; // 消息id
    @DatabaseField
    private String isself; //聊天类型，1自己发送，2好友发送
    @DatabaseField
    private String my_uid; // 我的id
    @DatabaseField
    private String userid; // 用户id
    @DatabaseField
    private String msg; // 内容
    @DatabaseField
    private int msg_type; // 内容类型 1普通文本，2图片，3语音
    @DatabaseField
    private String exrtaThumb; // 缩略图
    @DatabaseField
    private String exrtaFile; // 音频或者文件地址
    @DatabaseField
    private int status; // 消息状态  0: 已发送成功 1:未发送成功
    @DatabaseField
    private long time; // 消息创建时间
    @DatabaseField
    private int is_read; // 是否已读 0已读  1未读

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long getRoom_id() {
        return room_id;
    }

    public void setRoom_id(long room_id) {
        this.room_id = room_id;
    }

    public String getInfoid() {
        return infoid;
    }

    public void setInfoid(String infoid) {
        this.infoid = infoid;
    }

    public String getIsself() {
        return isself;
    }

    public void setIsself(String isself) {
        this.isself = isself;
    }

    public String getMy_uid() {
        return my_uid;
    }

    public void setMy_uid(String my_uid) {
        this.my_uid = my_uid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(int msg_type) {
        this.msg_type = msg_type;
    }

    public String getExrtaThumb() {
        return exrtaThumb;
    }

    public void setExrtaThumb(String exrtaThumb) {
        this.exrtaThumb = exrtaThumb;
    }

    public String getExrtaFile() {
        return exrtaFile;
    }

    public void setExrtaFile(String exrtaFile) {
        this.exrtaFile = exrtaFile;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getIs_read() {
        return is_read;
    }

    public void setIs_read(int is_read) {
        this.is_read = is_read;
    }
}
