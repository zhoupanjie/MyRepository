package com.cplatform.xhxw.ui.http.net.saas;

import com.cplatform.xhxw.ui.http.net.BaseRequest;

/**
 * 公司圈评论
 * Created by cy-love on 14-8-16.
 */
public class FriendZoneCommentSubRequest extends BaseRequest {

    private String infoid; // 新鲜事信息ID
    private String userid; // 回复某条评论的发布人ID
    private String commentid; //回复某条评论的ID
    private String content; // 评论内容
    private String infouserid; //新鲜事创建者id

    /**
     * 公司圈评论
     * @param infoid 新鲜事信息ID
     * @param userid 回复某条评论的发布人ID
     * @param commentid 回复某条评论的ID
     * @param content 评论内容
     * @param infouserid 新鲜事创建者ID
     */
    public FriendZoneCommentSubRequest(String infoid, String userid, String commentid, String content, String infouserid) {
        this.setDevRequest(true);
        this.infoid = infoid;
        this.userid = userid;
        this.content = content;
        this.commentid = commentid;
        this.infouserid = infouserid;
    }
    
    public FriendZoneCommentSubRequest() {
    	
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

    public String getInfouserid() {
        return infouserid;
    }

    public void setInfouserid(String infouserid) {
        this.infouserid = infouserid;
    }
}
