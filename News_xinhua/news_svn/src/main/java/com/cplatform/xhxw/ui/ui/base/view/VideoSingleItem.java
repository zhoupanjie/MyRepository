package com.cplatform.xhxw.ui.ui.base.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.Bind;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.model.New;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.cplatform.xhxw.ui.util.TextViewUtil;

/**
 * 单张新闻视频布局
 * Created by cy-love on 13-12-25.
 */
public class VideoSingleItem extends RelativeLayout {

    @Bind(R.id.iv_single_img) ImageView mImg;
    @Bind(R.id.tv_img_title) TextView mTitle;
    @Bind(R.id.tv_desc) TextView mDesc;

    public VideoSingleItem(Context context) {
        super(context);
        this.init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_video_single_item, this);
        ButterKnife.bind(this);
    }

    public void setData(New item) {
        TextViewUtil.setDisplayModel(getContext(), mTitle, mDesc, item.isRead());
        ImageLoader.getInstance().displayImage(item.getBigthumbnail(), mImg, DisplayImageOptionsUtil.listNewImgOptions);
        float textSize = NewItem.getTitleTextSize();
        mTitle.setText(item.getTitle());
        mTitle.setTextSize(textSize);
        mDesc.setText(item.getSummary());
        if(textSize > 16) {
			mDesc.setTextSize(Constants.LIST_SUMMARY_TEXT_SIZE + 2);
		} else {
			mDesc.setTextSize(Constants.LIST_SUMMARY_TEXT_SIZE);
		}
        TextViewUtil.setDisplayBgModel(getContext(), this);
    }

}
