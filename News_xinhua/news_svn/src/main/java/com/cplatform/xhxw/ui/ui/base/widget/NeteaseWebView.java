package com.cplatform.xhxw.ui.ui.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;

/**
 * 新闻详情展现
 * Created by cy-love on 13-12-29.
 */
public class NeteaseWebView extends WebView {

    private View mBg;
    private Runnable run = new Run();

    public NeteaseWebView(Context context) {
        super(context);
    }

    public NeteaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NeteaseWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void init() {
        mBg = new View(getContext());
        addView(mBg, -1);
    }

    public void setBackgroundColor(int color) {
        super.setBackgroundColor(color);
        if (this.mBg != null)
            this.mBg.setBackgroundColor(color);
    }

    private class Run implements Runnable {

        @Override
        public void run() {
            mBg.setVisibility(View.INVISIBLE);
        }
    }


}
