package com.cplatform.xhxw.ui.http.net;

/**
 * 我的评论列表
 * Created by cy-love on 14-1-19.
 */
public class MyCommentRequest extends BaseRequest {

    private int offset;
    private int pn;

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getPn() {
        return pn;
    }

    public void setPn(int pn) {
        this.pn = pn;
    }
}
