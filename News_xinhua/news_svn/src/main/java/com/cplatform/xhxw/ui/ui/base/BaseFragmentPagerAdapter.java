package com.cplatform.xhxw.ui.ui.base;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cy-love on 14-1-1.
 */
public abstract class BaseFragmentPagerAdapter<T> extends FragmentPagerAdapter {

    protected List<T> mData;
    public BaseFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        mData = new ArrayList<T>();
    }


    @Override
    public int getCount() {
        return mData.size();
    }

    public void clearData() {
        mData.clear();
    }

    public void addAllData(List<T> list) {
        mData.addAll(list);
    }

    public List<T> getData() {
        return mData;
    }

}
