package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * 相冊管理
 * Created by cy-love on 14-6-9.
 */
public class CompanyCircleAlbumGroupManager {

    private static CompanyCircleAlbumGroupManager mInstance;
    private HashMap<String, List<String>> mData;

    private CompanyCircleAlbumGroupManager(){}

    public static CompanyCircleAlbumGroupManager getInstance() {
        if (mInstance == null) {
            mInstance = new CompanyCircleAlbumGroupManager();
        }
        return mInstance;
    }

    public void addData(HashMap<String, List<String>> data) {
        if (mData == null) {
            mData = new HashMap<String, List<String>>();
        }
        mData.putAll(data);
    }

    public void cleanData() {
        if (mData == null) {
            return;
        }
        mData.clear();
        mData = null;
    }

    public List<String> getListForKey(String key) {
        if (mData == null) {
            return null;
        }
        return mData.get(key);
    }

    public List<String> getListKey() {
        if (mData == null) {
            return null;
        }
        Set<String> keys = mData.keySet();
        List<String> list = new ArrayList<String>();
        for (String key : keys) {
            list.add(key);
        }
        return list;
    }
}
