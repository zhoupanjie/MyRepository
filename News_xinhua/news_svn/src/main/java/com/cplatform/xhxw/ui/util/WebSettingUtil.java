package com.cplatform.xhxw.ui.util;

import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;

/**
 * webView工具类
 * Created by cy-love on 13-12-29.
 */
public class WebSettingUtil {

    /**
     * 初始化WebView设置
     * @param webView
     * @param client
     * @param interfaceObj js回调对象
     * @param interfaceName js指向名称
     * @param blockNetworkImage true为拦截网络图片加载, 否则为false
     */
    public static void init(WebView webView, WebChromeClient client, Object interfaceObj, String interfaceName, boolean blockNetworkImage) {
        webView.clearCache(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setBlockNetworkImage(blockNetworkImage);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.refreshDrawableState();
        if (App.getDispalyModel() == Constants.DISPLAY_MODEL_NIGHT) {
            webView.setBackgroundColor(webView.getResources().getColor(R.color.night_base_main_bg_color));
        }
        webView.setWebChromeClient(client);
        webView.addJavascriptInterface(interfaceObj, interfaceName);
    }

    public static void destroyView(WebView webView) {
        ViewGroup viewGroup = (ViewGroup)webView.getParent();
        if (viewGroup != null)
            viewGroup.removeView(webView);
        webView.removeAllViews();
        webView.getSettings().setJavaScriptEnabled(false);
        webView.destroyDrawingCache();
        webView.destroy();
    }

}
