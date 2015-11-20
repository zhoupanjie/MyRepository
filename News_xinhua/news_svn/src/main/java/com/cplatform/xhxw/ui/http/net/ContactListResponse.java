package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.model.Contact;

import java.io.Serializable;
import java.util.List;

/**
 * 联系人数据获取
 * Created by cy-love on 14-8-6.
 */
public class ContactListResponse extends BaseResponse {

    private List<Contact> data;

    public List<Contact> getData() {
        return data;
    }

    public void setData(List<Contact> data) {
        this.data = data;
    }
}
