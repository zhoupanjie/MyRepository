
package com.hy.superemsg.viewpager.fragments;

import org.apache.http.protocol.HTTP;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.viewpager.AbsFragment;

public class WebViewFragment extends AbsFragment {
    private WebView web;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_web, null);
    }

    @Override
    protected void initUI() {
        web = (WebView) getView().findViewById(R.id.web);
        web.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;

            }

        });
        String url = getArguments().getString(SuperEMsgApplication.EXTRA_WEB_URL);
        if (!TextUtils.isEmpty(url)) {
            web.loadUrl(url);
        }
    }

    @Override
    protected void excuteTask() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void resetUI() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean OnKeyEvent(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (web.canGoBack()) {
                web.goBack();
                return true;
            }
        }
        return super.OnKeyEvent(keyCode, event);
    }

}
