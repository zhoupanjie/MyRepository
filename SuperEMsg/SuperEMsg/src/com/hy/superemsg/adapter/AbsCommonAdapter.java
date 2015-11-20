
package com.hy.superemsg.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class AbsCommonAdapter<T> extends BaseAdapter {

    private List<T> datas;
    private Context ctx;
    private int layout;

    public AbsCommonAdapter(Context ctx, int layout) {
        datas = new ArrayList<T>();
        this.ctx = ctx;
        this.layout = layout;
    }

    protected Context getContext() {
        return ctx;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public T getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addData(T data) {
        this.datas.add(data);
        this.notifyDataSetChanged();
    }

    public void addDatas(List<T> datas) {
        if (datas != null) {
            this.datas.addAll(datas);
            this.notifyDataSetChanged();
        }
    }

    public void setDatas(List<T> datas) {
        this.datas.clear();
        this.addDatas(datas);
    }

    public void insertDatas(int position, List<T> datas) {
        if (datas != null) {
            this.datas.addAll(position, datas);
            this.notifyDataSetChanged();
        }
    }

    public void insertData(int position, T data) {
        this.datas.add(position, data);
        this.notifyDataSetChanged();
    }

    public void removeData(T data) {
        if (data != null) {
            this.datas.remove(data);
        }
    }

    public List<T> getDatas() {
        return datas;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(layout, null);
        }

        getView(position, convertView, getItem(position));
        return convertView;
    }

    public abstract void getView(int position, View convertView, T data);
}
