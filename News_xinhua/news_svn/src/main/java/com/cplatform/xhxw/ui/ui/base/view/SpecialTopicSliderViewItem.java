package com.cplatform.xhxw.ui.ui.base.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.Bind;
import com.cplatform.xhxw.ui.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.cplatform.xhxw.ui.model.SpecialDetailData;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;

/**
 * 专题单个焦点新闻
 * Created by cy-love on 13-12-25.
 */
public class SpecialTopicSliderViewItem extends RelativeLayout {

    @Bind(R.id.iv_img) ImageView mImg;
    @Bind(R.id.tv_title) TextView mTitle;
    private OnSpecialTopicClickListener mLis;
    private SpecialDetailData mItem;

    public SpecialTopicSliderViewItem(Context context, OnSpecialTopicClickListener listener) {
        super(context);
        mLis = listener;
        this.init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_slider_item, this);
        ButterKnife.bind(this);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mLis.onSpecialTopicPicViewClick(mItem);
            }
        });

    }

    public void setData(SpecialDetailData item) {
        mItem = item;
        ImageLoader.getInstance().displayImage(item.getImg(), mImg, DisplayImageOptionsUtil.newsSliderImgOptions);
        mTitle.setText(item.getTitle());
    }
}
