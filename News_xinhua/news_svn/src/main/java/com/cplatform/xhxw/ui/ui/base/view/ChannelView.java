package com.cplatform.xhxw.ui.ui.base.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.Bind;
import com.cplatform.xhxw.ui.R;

/**
 * 栏目管理：单个栏目
 * Created by cy-love on 14-1-9.
 */
public class ChannelView extends LinearLayout {

    @Bind(R.id.tv_title)
    TextView mTitle;

    public ChannelView(Context context) {
        super(context);
        this.init();
    }

    public ChannelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ChannelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_channel, this);
        ButterKnife.bind(this);
    }


    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setTextColor(int color) {
        mTitle.setTextColor(color);
    }
}
