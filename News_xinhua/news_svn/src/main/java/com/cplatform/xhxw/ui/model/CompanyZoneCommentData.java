package com.cplatform.xhxw.ui.model;

import com.cplatform.xhxw.ui.model.saas.CompanyZoneItemUserInfo;

import java.io.Serializable;

/**
 * Created by cy-love on 14-8-26.
 */
public class CompanyZoneCommentData implements Serializable {

    private String commentid;
    private String content;
    private long ctime;
    private String pid;
    private CompanyZoneItemUserInfo replyuser;
    private CompanyZoneItemUserInfo senduser;

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public CompanyZoneItemUserInfo getReplyuser() {
        return replyuser;
    }

    public void setReplyuser(CompanyZoneItemUserInfo replyuser) {
        this.replyuser = replyuser;
    }

    public CompanyZoneItemUserInfo getSenduser() {
        return senduser;
    }

    public void setSenduser(CompanyZoneItemUserInfo senduser) {
        this.senduser = senduser;
    }
}
