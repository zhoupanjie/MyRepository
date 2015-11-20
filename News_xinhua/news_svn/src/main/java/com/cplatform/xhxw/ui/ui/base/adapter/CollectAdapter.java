package com.cplatform.xhxw.ui.ui.base.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.db.dao.CollectDao;
import com.cplatform.xhxw.ui.ui.base.BaseAdapter;
import com.cplatform.xhxw.ui.ui.base.view.*;

/**
 * 新闻列表适配器
 * Created by cy-love on 13-12-25.
 */
public class CollectAdapter extends BaseAdapter<CollectDao> {


    public CollectAdapter(Context context) {
        super(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CollectDao item = getItem(position);
        if (convertView == null) {
            convertView = new CollectNewItem(mContext);
        }
        CollectNewItem view = (CollectNewItem) convertView;
        view.setData(item);

        int bgRes = position == 0 ? R.drawable.bg_list_top : R.drawable.bg_list_bom;
        convertView.setBackgroundResource(bgRes);
        return convertView;
    }
}
