package com.cplatform.xhxw.ui.http.net;

import com.cplatform.xhxw.ui.model.GetNotreadItem;

import java.io.Serializable;
import java.util.List;

/**
 * 获得未读消息列表
 */
public class GetNotreadListResponse extends BaseResponse {

    private GetNotreadList data;

    public GetNotreadList getData() {
        return data;
    }

    public void setData(GetNotreadList data) {
        this.data = data;
    }

    public class GetNotreadList implements Serializable {
        private List<GetNotreadItem> list;

        public List<GetNotreadItem> getList() {
            return list;
        }

        public void setList(List<GetNotreadItem> list) {
            this.list = list;
        }
    }
}
