package com.cplatform.xhxw.ui.model.saas;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cy-love on 14-8-23.
 */
public class CompanyZoneItemParisaData implements Serializable {
    private String conut; //赞数量;
    private List<CompanyZoneItemUserInfo> list; // 赞的人员

    public String getConut() {
        return conut;
    }

    public void setConut(String conut) {
        this.conut = conut;
    }

    public List<CompanyZoneItemUserInfo> getList() {
        return list;
    }

    public void setList(List<CompanyZoneItemUserInfo> list) {
        this.list = list;
    }
}
