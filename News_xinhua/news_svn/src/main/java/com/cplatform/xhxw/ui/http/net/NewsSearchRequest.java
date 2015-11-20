package com.cplatform.xhxw.ui.http.net;

/**
 * 搜索新闻
 * Created by cy-love on 14-1-25.
 */
public class NewsSearchRequest extends BaseRequest {
    private int offset;
    private int pn;
    private String keyword;

    public NewsSearchRequest(int offset, int pn, String keyword) {
        this.offset = offset;
        this.pn = pn;
        this.keyword = keyword;
    }

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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
