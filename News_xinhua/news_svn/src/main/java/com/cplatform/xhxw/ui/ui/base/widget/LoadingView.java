package com.cplatform.xhxw.ui.ui.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

import com.cplatform.xhxw.ui.R;

/**
 * loading视图
 * Created by cy-love on 14-1-15.
 */
public class LoadingView extends RelativeLayout {

    @Bind(R.id.framework_loading_message) TextView mMsg;

    public LoadingView(Context context) {
        super(context);
        this.init();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.framework_loading_layout, this);
        ButterKnife.bind(this);
    }

    public void setMsg(CharSequence msg) {
        mMsg.setText(msg);
    }
    @Override
    protected void onDetachedFromWindow() {
        //////
        ButterKnife.bind(this);
        super.onDetachedFromWindow();
    }
}
