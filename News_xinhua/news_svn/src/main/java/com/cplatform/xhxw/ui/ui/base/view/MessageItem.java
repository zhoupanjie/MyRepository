package com.cplatform.xhxw.ui.ui.base.view;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.model.Message;
import com.cplatform.xhxw.ui.util.DateUtil;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.cplatform.xhxw.ui.util.TextUtil;

/**
 * 新闻消息推送布局
 * Created by cy-love on 13-12-25.
 */
public class MessageItem extends RelativeLayout {

    @InjectView(R.id.ms_img) ImageView mImg;
    @InjectView(R.id.ms_title) TextView mTitle;
    @InjectView(R.id.ms_desc) TextView mDesc;
    @InjectView(R.id.ms_tag) TextView mTag;
    @InjectView(R.id.ms_publish_date) TextView mPublishDate;

    private int mDisModel;
    public MessageItem(Context context) {
        super(context);
        this.init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_message_item, this);
        ButterKnife.inject(this);
        mDisModel = Constants.DISPLAY_MODEL_DAY;
    }

    public void setData(Message item, int headerVis) {
        resetModel();
        mTag.setVisibility(headerVis);
        mTag.setText(item.getTime());
        ImageLoader.getInstance().displayImage(item.getThumbnail(), mImg, DisplayImageOptionsUtil.listNewImgOptions);
    	mTitle.setText(item.getTitle());
    	mDesc.setText(TextUtil.newsDescInterception(item.getSummary()));
    	mPublishDate.setText(DateUtil.getFormattedDate(Long.valueOf(item.getPublished())));
    }

    private void resetModel() {
        int model = App.getPreferenceManager().getDispalyModel();
        if (mDisModel == model)
            return;
        mDisModel = model;
        Resources res = getResources();
        switch (model) {
            case Constants.DISPLAY_MODEL_DAY:
                mTitle.setTextColor(res.getColor(R.color.base_list_title_color));
                mDesc.setTextColor(res.getColor(R.color.base_list_desc_color));
                break;
            case Constants.DISPLAY_MODEL_NIGHT:
                mTitle.setTextColor(res.getColor(R.color.night_base_list_title_color));
                mDesc.setTextColor(res.getColor(R.color.night_base_list_desc_color));
                break;
        }
    }



}
