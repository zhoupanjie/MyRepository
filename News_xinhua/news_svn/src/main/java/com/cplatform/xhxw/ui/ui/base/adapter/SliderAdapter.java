package com.cplatform.xhxw.ui.ui.base.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.cplatform.xhxw.ui.model.Focus;
import com.cplatform.xhxw.ui.ui.base.BasePageAdapter;
import com.cplatform.xhxw.ui.ui.base.view.OnSliderImgOnClickListener;
import com.cplatform.xhxw.ui.ui.base.view.SliderViewItem;

/**
 * 焦点新闻适配
 * Created by cy-love on 14-1-1.
 */
public class SliderAdapter extends BasePageAdapter<Focus> {

    private OnSliderImgOnClickListener mLis;
    public SliderAdapter(Context context, OnSliderImgOnClickListener lis) {
        super(context);
        mLis = lis;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        SliderViewItem view = new SliderViewItem(container.getContext(), mLis);
        view.setData(getItem(position));
        container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return view;
    }

}
