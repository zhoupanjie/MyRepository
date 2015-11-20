package com.cplatform.xhxw.ui.ui.base.adapter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import com.cplatform.xhxw.ui.ui.base.BasePageAdapter;

/**
 * Created by cy-love on 14-2-13.
 */
public class GuideAdapter extends BasePageAdapter<View> {
    public GuideAdapter(Context context) {
        super(context);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(getItem(position), 0);
        return getItem(position);
    }
}
