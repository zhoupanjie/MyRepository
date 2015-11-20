package com.cplatform.xhxw.ui.model;


import com.cplatform.xhxw.ui.util.ListUtil;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 专题详情
 * Created by cy-love on 14-1-10.
 */
public class SpecialDetail {

    private boolean _showHeadler; //是否显示标题
    private String title; //模块标题
    private int specialmodeltype; //专题类型
    private int orders; //排序
    private List<SpecialDetailData> data;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSpecialmodeltype() {
        return specialmodeltype;
    }

    public void setSpecialmodeltype(int specialmodeltype) {
        this.specialmodeltype = specialmodeltype;
    }

    public int getOrders() {
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }

    public List<SpecialDetailData> getData() {
        if (!ListUtil.isEmpty(data)) {
            Collections.sort(data, new Comparator<SpecialDetailData>() {
                @Override
                public int compare(SpecialDetailData lhs, SpecialDetailData rhs) {
                    return lhs.getOrders() - rhs.getOrders();
                }
            });
        }
        return data;
    }

    public void setData(List<SpecialDetailData> data) {
        this.data = data;
    }

    public boolean is_showHeadler() {
        return _showHeadler;
    }

    public void set_showHeadler(boolean _showHeadler) {
        this._showHeadler = _showHeadler;
    }
}
