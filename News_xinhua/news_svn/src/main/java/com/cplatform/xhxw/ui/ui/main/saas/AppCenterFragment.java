package com.cplatform.xhxw.ui.ui.main.saas;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.ui.base.BaseFragment;

public class AppCenterFragment extends BaseFragment implements OnClickListener {
	private static final String APP_URL_XW = "http://218.106.246.85/cms/wap/app/v_list.do?type=android";
	private WebView mAppWebView;
	private ProgressBar mProgressBar;
	private LinearLayout mDefaultView;
	private boolean isLoadUrlSuccess = true;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_app_center, container, false);
		initViews(rootView);
		return rootView;
	}

	private void initViews(View rootView) {
		mAppWebView = (WebView) rootView.findViewById(R.id.app_center_app_webview);
		mProgressBar = (ProgressBar) rootView.findViewById(R.id.app_center_pb);
		mDefaultView = (LinearLayout) rootView.findViewById(R.id.app_center_empty_iv);
		mDefaultView.setOnClickListener(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initWebView();
		mAppWebView.loadUrl(APP_URL_XW);
		setViewStatus(true, false);
	}
	
	private void initWebView() {
		mAppWebView.clearCache(true);
        mAppWebView.setFocusable(false);
        mAppWebView.getSettings().setJavaScriptEnabled(true);
        mAppWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mAppWebView.getSettings().setBlockNetworkImage(false);
        mAppWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mAppWebView.refreshDrawableState();
        mAppWebView.setDownloadListener(new MyWebDownloadLis());
        mAppWebView.setWebViewClient(new MyWebViewClient());
	}
	
	private void setViewStatus(boolean isToStartLoad, boolean isLoadSuccess) {
		if(isToStartLoad) {
			isLoadUrlSuccess = true;
			mProgressBar.setVisibility(View.VISIBLE);
		} else {
			mProgressBar.setVisibility(View.GONE);
			if(isLoadSuccess) {
				mDefaultView.setVisibility(View.GONE);
				mAppWebView.setVisibility(View.VISIBLE);
			} else {
				mDefaultView.setVisibility(View.VISIBLE);
				mAppWebView.setVisibility(View.GONE);
			}
		}
	}
	
	/**
	 * 实现页面加载失败及完成事件监听，处理失败时页面友好显示
	 * @author jinyz
	 *
	 */
	private class MyWebViewClient extends WebViewClient {
		
		@Override
		public void onPageFinished(WebView view, String url) {
			if(isLoadUrlSuccess) {
				setViewStatus(false, true);
			}
			super.onPageFinished(view, url);
		}
		
		

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			isLoadUrlSuccess = false;
			setViewStatus(false, false);
			Toast.makeText(getActivity(), R.string.load_server_failure, Toast.LENGTH_LONG).show();
		}
	}
	
	/**
	 * 监听webview 中下载链接点击事件，并跳转到浏览器进行下载
	 * @author jinyz
	 *
	 */
	private class MyWebDownloadLis implements DownloadListener {

		@Override
		public void onDownloadStart(String url, String userAgent,
				String contentDisposition, String mimetype, long contentLength) {
			Uri uri = Uri.parse(url);  
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);  
            startActivity(intent);
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.app_center_empty_iv) {
			setViewStatus(true, true);
			mAppWebView.loadUrl(APP_URL_XW);
		}
	}
}
