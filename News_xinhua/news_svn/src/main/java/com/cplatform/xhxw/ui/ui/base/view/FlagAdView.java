package com.cplatform.xhxw.ui.ui.base.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.Bind;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.StatisticalKey;
import com.cplatform.xhxw.ui.model.Ad;
import com.cplatform.xhxw.ui.ui.web.WebViewActivity;
import com.wbtech.ums.UmsAgent;

/**
 * 角标区域广告
 * Created by cy-love on 14-3-9.
 */
public class FlagAdView extends LinearLayout {

    @Bind(R.id.tv_ad_title)
    TextView mTitle;
    private Ad mData;

    public FlagAdView(Context context) {
        super(context);
        init();
    }

    public FlagAdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public FlagAdView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_flag_ad, this);
        ButterKnife.bind(this);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mData != null) {
                	UmsAgent.onEvent(getContext(), StatisticalKey.xwad_flag,
            				new String[] { StatisticalKey.key_newsid,StatisticalKey.key_title },
            				new String[] { mData.getId(),mData.getTitle() });
                    Intent intent = WebViewActivity.getIntentByURL(getContext(), mData.getShorturl(), mData.getTitle());
                    getContext().startActivity(intent);
                }
            }
        });
    }

    public void setAds(Ad data) {
        mTitle.setText(data.getTitle());
        mData = data;
    }
}
