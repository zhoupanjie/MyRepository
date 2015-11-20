package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cplatform.xhxw.ui.R;

/**
 * 搜索布局
 * Created by cy-love on 14-8-4.
 */
public class SearchHeader extends LinearLayout {

    public SearchHeader(Context context) {
        super(context);
        init();
    }

    public SearchHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SearchHeader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_search_header, this);
    }
}
