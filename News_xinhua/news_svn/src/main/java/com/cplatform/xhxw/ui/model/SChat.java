package com.cplatform.xhxw.ui.model;

import java.io.Serializable;

/**
 * S信息对话列表最后一条未读内容
 */
public class SChat implements Serializable {

    private String infoid; // 聊天ID
    private int isself; // 聊天类型，1自己发送，2好友发送
    private int chattype;  //1普通文本，2图片，3语音
    private String content; // 聊天内容
    private SChatExrta exrta; // 附件
    private long ctime; // 聊天时间(单位秒)

    public String getInfoid() {
        return infoid;
    }

    public void setInfoid(String infoid) {
        this.infoid = infoid;
    }

    public int getIsself() {
        return isself;
    }

    public void setIsself(int isself) {
        this.isself = isself;
    }

    public int getChattype() {
        return chattype;
    }

    public void setChattype(int chattype) {
        this.chattype = chattype;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public SChatExrta getExrta() {
        return exrta;
    }

    public void setExrta(SChatExrta exrta) {
        this.exrta = exrta;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }
}
