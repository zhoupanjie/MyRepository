package com.cplatform.xhxw.ui.http.net.saas;

import com.cplatform.xhxw.ui.http.net.BaseResponse;
import com.cplatform.xhxw.ui.model.saas.CompanyZoneList;

import java.io.Serializable;
import java.util.List;

/**
 * 公司圈解析
 * Created by cy-love on 14-8-23.
 */
public class CompanyZoneListResponse extends BaseResponse {

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data implements Serializable {
        private List<CompanyZoneList> list;

        public List<CompanyZoneList> getList() {
            return list;
        }

        public void setList(List<CompanyZoneList> list) {
            this.list = list;
        }
    }
}
