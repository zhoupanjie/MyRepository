package com.cplatform.xhxw.ui.http.net;

/**
 * 上传通讯录
 */
public class FriendsCompareRequest extends BaseRequest {

    private String data;

    /**
     * @param data 通讯录数据
     *             格式如[{"name":"\u8001\u66f2","phone":"13811751450"},{"name":"\u540d\u79f0","phone":"13910520092"}]
     */
    public FriendsCompareRequest(String data) {
        this.data = data;
        setDevRequest(true);
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
