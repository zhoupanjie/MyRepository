package com.cplatform.xhxw.ui.ui.base.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.model.SpecialDetailData;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.cplatform.xhxw.ui.util.TextViewUtil;

/**
 * 新闻专题 单个图片 布局
 * Created by cy-love on 13-12-25.
 */
public class SpecialTopicVideoView extends RelativeLayout {

    @Bind(R.id.iv_img)
    ImageView mImg;
    @Bind(R.id.tv_desc)
    TextView mDesc;
    private SpecialDetailData mItem;
    private OnSpecialTopicClickListener mListener;

    public SpecialTopicVideoView(Context context, OnSpecialTopicClickListener listener) {
        super(context);
        mListener = listener;
        this.init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_special_topic_video, this);
        ButterKnife.bind(this);
    }

    public void setData(SpecialDetailData item) {
        mItem = item;
        TextViewUtil.setTranslucentBgDisplayModel(getContext(), mDesc, item.isRead());
        mDesc.setText(item.getTitle());
        ImageLoader.getInstance().displayImage(item.getVideoimg(), mImg, DisplayImageOptionsUtil.listNewImgOptions);
    }

    @OnClick(R.id.iv_img)
    public void onImgClickAction() {
        if (mListener != null)
            mListener.onSpecialTopicPicViewClick(mItem);
    }
}
