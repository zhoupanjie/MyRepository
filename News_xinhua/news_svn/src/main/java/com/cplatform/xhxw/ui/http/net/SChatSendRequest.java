package com.cplatform.xhxw.ui.http.net;

import java.io.File;

/**
 * S信发送
 */
public class SChatSendRequest extends BaseRequest {

    private String userid;
    private int contenttype;
    private String content;
    private File upfile;

    /**
     * @param userid      好友用户ID
     * @param contenttype 消息类型:1普通文本，2图片，3语音
     * @param content     文本内容
     * @param upfile
     */
    public SChatSendRequest(String userid, int contenttype, String content, File upfile) {
        this.userid = userid;
        this.contenttype = contenttype;
        this.content = content;
        this.upfile = upfile;
        this.setDevRequest(true);
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getContenttype() {
        return contenttype;
    }

    public void setContenttype(int contenttype) {
        this.contenttype = contenttype;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public File getUpfile() {
        return upfile;
    }

    public void setUpfile(File upfile) {
        this.upfile = upfile;
    }
}
