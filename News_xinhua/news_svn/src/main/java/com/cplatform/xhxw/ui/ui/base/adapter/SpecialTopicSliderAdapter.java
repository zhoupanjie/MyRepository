package com.cplatform.xhxw.ui.ui.base.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.cplatform.xhxw.ui.model.SpecialDetailData;
import com.cplatform.xhxw.ui.ui.base.BasePageAdapter;
import com.cplatform.xhxw.ui.ui.base.view.OnSpecialTopicClickListener;
import com.cplatform.xhxw.ui.ui.base.view.SpecialTopicSliderViewItem;

/**
 * 专题焦点新闻适配
 * Created by cy-love on 14-1-1.
 */
public class SpecialTopicSliderAdapter extends BasePageAdapter<SpecialDetailData> {

    private OnSpecialTopicClickListener mLis;
    public SpecialTopicSliderAdapter(Context context, OnSpecialTopicClickListener lis) {
        super(context);
        mLis = lis;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        SpecialTopicSliderViewItem view = new SpecialTopicSliderViewItem(container.getContext(), mLis);
        view.setData(getItem(position));
        container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return view;
    }

}
