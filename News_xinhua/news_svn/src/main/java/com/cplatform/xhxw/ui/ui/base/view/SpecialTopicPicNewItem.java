package com.cplatform.xhxw.ui.ui.base.view;

import android.content.Context;
import android.support.v7.widget.GridLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.model.SpecialDetail;
import com.cplatform.xhxw.ui.model.SpecialDetailData;
import java.util.ArrayList;
import java.util.List;

/**
 * 新闻头条图集布局
 * Created by cy-love on 13-12-25.
 */
public class SpecialTopicPicNewItem extends RelativeLayout {

    @InjectView(R.id.tv_header)
    TextView mHeader;
    @InjectView(R.id.ly_content)
    GridLayout mContent;
    private int mColumn;
    private List<SpecialTopicPicView> mLineCash;
    private ViewGroup.LayoutParams mLp;

    public SpecialTopicPicNewItem(Context context) {
        super(context);
        this.init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_special_topic_pic_new_item, this);
        ButterKnife.inject(this);
        mColumn = getResources().getInteger(R.integer.special_topic_pic_column_count);
        mLineCash = new ArrayList<SpecialTopicPicView>();
        int width = Constants.screenWidth / mColumn;
        int height = Constants.SpecialTopicPicItemSize.height * width / Constants.SpecialTopicPicItemSize.width;
        mLp = new ViewGroup.LayoutParams(width, height);
    }

    public void setData(SpecialDetail item, boolean isShowHeander, OnSpecialTopicClickListener listener) {
        if (isShowHeander) {
            mHeader.setVisibility(View.VISIBLE);
            mHeader.setText(item.getTitle());
        } else {
            mHeader.setVisibility(View.GONE);
        }
        mContent.removeAllViews();

        List<SpecialDetailData> pics = item.getData();
        if (pics != null && pics.size() > 0) {
            int count = pics.size();
            for (int i = 0; i < count; i++) {
                SpecialTopicPicView view;
                if (mLineCash.size() < i) {
                    view = mLineCash.get(i);
                } else {
                    view = new SpecialTopicPicView(getContext(), listener);
                    mLineCash.add(view);
                    view.setLayoutParams(mLp);
                }
                view.setData(pics.get(i));
                mContent.addView(view);
            }
        }

    }

}
