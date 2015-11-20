package com.cplatform.xhxw.ui.model.saas;

import java.io.Serializable;

/**
 * 社区圈评论返回数据
 * Created by cy-love on 14-8-30.
 */
public class CommentSubData implements Serializable {

    private String commentid; // 评论id
    private String ctime; // 评论时间
    private String content; // 评论内容
    private boolean isCompanyCircle;

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCompanyCircle() {
        return isCompanyCircle;
    }

    public void setCompanyCircle(boolean isCompanyCircle) {
        this.isCompanyCircle = isCompanyCircle;
    }
}
