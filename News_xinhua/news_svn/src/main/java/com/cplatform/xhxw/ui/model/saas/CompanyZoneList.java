package com.cplatform.xhxw.ui.model.saas;

import com.cplatform.xhxw.ui.model.CompanyZoneCommentData;

import java.io.Serializable;
import java.util.List;

/**
 * 公司圈解析
 * Created by cy-love on 14-8-23.
 */
public class CompanyZoneList implements Serializable {
    private List<CompanyZoneCommentData> commentdata;
    private String content; // 评论
    private String contenttype; // 1文本，2图片，3图文，4视频，5分享连接
    private String ctime; // 创建时间
    private List<CompanyZoneItemExrta> exrta;
    private String ftime;  // 时间（如1小时前）
    private String infoid; // 信息ID
    private String iscomment;  // 1可评论 2不可评论
    private String isparisa; // 是否可点赞 1是 2否
    private String parisa; //是否已赞 1是 0否
    private CompanyZoneItemParisaData parisadata;
    private CompanyZoneItemUserInfo userinfo;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContenttype() {
        return contenttype;
    }

    public void setContenttype(String contenttype) {
        this.contenttype = contenttype;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

//    public CompanyZoneItemExrta getExrta() {
//        return exrta;
//    }
//
//    public void setExrta(CompanyZoneItemExrta exrta) {
//        this.exrta = exrta;
//    }

    public String getFtime() {
        return ftime;
    }

    public void setFtime(String ftime) {
        this.ftime = ftime;
    }

    public String getInfoid() {
        return infoid;
    }

    public void setInfoid(String infoid) {
        this.infoid = infoid;
    }

    public String getIscomment() {
        return iscomment;
    }

    public void setIscomment(String iscomment) {
        this.iscomment = iscomment;
    }

    public String getIsparisa() {
        return isparisa;
    }

    public void setIsparisa(String isparisa) {
        this.isparisa = isparisa;
    }

    public String getParisa() {
        return parisa;
    }

    public void setParisa(String parisa) {
        this.parisa = parisa;
    }

    public CompanyZoneItemParisaData getParisadata() {
        return parisadata;
    }

    public void setParisadata(CompanyZoneItemParisaData parisadata) {
        this.parisadata = parisadata;
    }

    public CompanyZoneItemUserInfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(CompanyZoneItemUserInfo userinfo) {
        this.userinfo = userinfo;
    }

    public List<CompanyZoneItemExrta> getExrta() {
        return exrta;
    }

    public void setExrta(List<CompanyZoneItemExrta> exrta) {
        this.exrta = exrta;
    }

    public List<CompanyZoneCommentData> getCommentdata() {
        return commentdata;
    }

    public void setCommentdata(List<CompanyZoneCommentData> commentdata) {
        this.commentdata = commentdata;
    }
}
