package com.hy.superemsg.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import com.hy.superemsg.R;
import com.hy.superemsg.components.UITitle;

public class NewsActivity extends Activity {
	private ProgressBar loadingBar;
	private WebView webView;
	private UITitle title;
	private String NEWS_URL = "http://m.news.cn";

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_news_detail);
		title = (UITitle) findViewById(R.id.ui_title);
		webView = (WebView) findViewById(R.id.web);
		loadingBar = (ProgressBar) findViewById(R.id.item_loading);
		if (title != null) {
			title.setTitleText(R.string.xinhua_news);
			title.setLeftButton(R.drawable.back, new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
		if (webView != null) {
			loadingBar.setVisibility(View.VISIBLE);
			webView.setVisibility(View.GONE);
			webView.loadUrl(NEWS_URL);
			webView.setWebViewClient(new WebViewClient());
			webView.setWebChromeClient(new WebChromeClient() {

				@Override
				public void onProgressChanged(WebView view, int newProgress) {
					if (newProgress == 100) {
						loadingBar.setVisibility(View.GONE);
						webView.setVisibility(View.VISIBLE);
					}
					super.onProgressChanged(view, newProgress);
				}

			});
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(null!=webView&&webView.canGoBack()){
			webView.goBack();
		}
		
	}
	
}
