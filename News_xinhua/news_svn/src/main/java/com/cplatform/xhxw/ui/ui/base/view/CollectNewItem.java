package com.cplatform.xhxw.ui.ui.base.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.db.dao.CollectDao;
import com.cplatform.xhxw.ui.util.TextViewUtil;

/**
 * 收藏新闻布局
 * Created by cy-love on 13-12-25.
 */
public class CollectNewItem extends RelativeLayout {

    @Bind(R.id.tv_title) TextView mTitle;

    public CollectNewItem(Context context) {
        super(context);
        this.init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_collect_new_item, this);
        ButterKnife.bind(this);
    }

    public void setData(CollectDao item) {
        TextViewUtil.setDisplayModel(getContext(), mTitle);
        mTitle.setText(item.getTitle());
    }
}
