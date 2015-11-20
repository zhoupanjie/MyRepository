package com.cplatform.xhxw.ui.ui.base;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import com.cplatform.xhxw.ui.util.ListUtil;

public abstract class BaseAdapter<T> extends android.widget.BaseAdapter {

    protected List<T> mData;
    protected LayoutInflater mInflater;
    protected Context mContext;

    public BaseAdapter(Context context) {
        mData = new ArrayList<T>();
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
    }

    public void clearData() {
        mData.clear();
    }

    /**
     * 将历史数据追加到列表尾部
     * @param list
     */
    public void addAllData(List<T> list) {
        if (!ListUtil.isEmpty(list)) {
            mData.addAll(list);
        }
    }
    
    /**
     * 将最新数据插入列表头部
     * @param list
     */
    public void addNewestData(List<T> list) {
    	if (!ListUtil.isEmpty(list)) {
            mData.addAll(0, list);
            notifyDataSetChanged();
        }
    }

    public List<T> getData() {
        return mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
