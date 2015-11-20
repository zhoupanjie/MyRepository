package com.cplatform.xhxw.ui.ui.base.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.model.SpecialDetail;
import com.viewpagerindicator.CirclePageIndicator;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.ui.base.adapter.SpecialTopicSliderAdapter;

/**
 * 专题焦点新闻
 * Created by cy-love on 14-1-1.
 */
public class SpecialTopicSliderView extends RelativeLayout {

    @InjectView(R.id.view_pager)
    ViewPager mVp;
    @InjectView(R.id.view_indicator)
    CirclePageIndicator mCpi;

    private SpecialTopicSliderAdapter mAdapter;
    private SpecialDetail mSpecialDetail;

    public SpecialTopicSliderView(Context context, OnSpecialTopicClickListener lis) {
        super(context);
        this.init(lis);
    }

    private void init(OnSpecialTopicClickListener lis) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_slider, this);
        ButterKnife.inject(this);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(Constants.screenWidth, getSliderHeight());
        setLayoutParams(lp);
        mAdapter = new SpecialTopicSliderAdapter(getContext(), lis);
        mVp.setAdapter(mAdapter);
        mCpi.setViewPager(mVp);
    }

    private int getSliderHeight() {
        double height = Constants.screenWidth * Constants.HomeSliderSize.height / Constants.HomeSliderSize.width;
        return (int) Math.ceil(height) + 1;
    }

    public void setDate(SpecialDetail specialDetail) {
        if (mSpecialDetail != null && mSpecialDetail == specialDetail) {
            return;
        }
        mSpecialDetail = specialDetail;
        mAdapter.clearData();
        mAdapter.addAllData(specialDetail.getData());
        mAdapter.notifyDataSetChanged();
        mVp.setCurrentItem(0);
    }
}
