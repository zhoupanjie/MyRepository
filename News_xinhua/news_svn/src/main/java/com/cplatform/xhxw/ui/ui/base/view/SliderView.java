package com.cplatform.xhxw.ui.ui.base.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import butterknife.Bind;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.model.Focus;
import com.cplatform.xhxw.ui.ui.base.adapter.SliderAdapter;
import com.viewpagerindicator.CirclePageIndicator;
import com.cplatform.xhxw.ui.R;
import java.util.List;

/**
 * 焦点新闻
 * Created by cy-love on 14-1-1.
 */
public class SliderView extends RelativeLayout {

    @Bind(R.id.view_pager)
    ViewPager mVp;
    @Bind(R.id.view_indicator)
    CirclePageIndicator mCpi;

    private SliderAdapter mAdapter;

    public SliderView(Context context, OnSliderImgOnClickListener lis) {
        super(context);
        this.init(lis);
    }

    private void init(OnSliderImgOnClickListener lis) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_slider, this);
        ButterKnife.bind(this);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(Constants.screenWidth,  Constants.HomeSliderSize.title_height);
        setLayoutParams(lp);
        mAdapter = new SliderAdapter(getContext(), lis);
        mVp.setAdapter(mAdapter);
        mCpi.setViewPager(mVp);
    }

//    private int getSliderHeight() {
//        double height = (Constants.screenWidth * Constants.HomeSliderSize.height / Constants.HomeSliderSize.width)+Constants.HomeSliderSize.title_height ;
//        return (int) Math.ceil(height) + 1;
//    }

    public void setDate(List<Focus> focus) {
    	RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(Constants.screenWidth, Constants.HomeSliderSize.title_height);
        setLayoutParams(lp);
        mAdapter.clearData();
        mAdapter.addAllData(focus);
        mVp.setAdapter(mAdapter);
        mCpi.setViewPager(mVp);
    }
}
