package com.hy.superemsg.activity;

import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.components.UITitle;
import com.hy.superemsg.req.ReqNewsDetailQeury;
import com.hy.superemsg.rsp.BaseRspApi;
import com.hy.superemsg.rsp.NewsContentDetail;
import com.hy.superemsg.rsp.RspNewsDetailQeury;
import com.hy.superemsg.utils.HttpUtils;
import com.hy.superemsg.utils.HttpUtils.AsynHttpCallback;

public class NewsDetailActivity extends Activity {
	private NewsContentDetail news;
	private WebView web;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_detail);
		web = (WebView) this.findViewById(R.id.web);
		web.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		web.setScrollbarFadingEnabled(true);
		news = getIntent().getParcelableExtra(
				SuperEMsgApplication.EXTRA_NEWS_DETAIL);
		UITitle title = (UITitle) this.findViewById(R.id.ui_title);
		if (title != null) {
			title.setTitleText(R.string.baiku_info);

			title.setLeftButton(R.drawable.back, new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
		HttpUtils.getInst().excuteTask(new ReqNewsDetailQeury(news.newsid),
				new AsynHttpCallback() {

					@Override
					public void onSuccess(BaseRspApi rsp) {
						RspNewsDetailQeury detail = (RspNewsDetailQeury) rsp;
						detail.newsdetail.replaceImgRefWithRealImgSrc();
//						detail.newsdetail.replaceImgExplicitPixel();
						String content = detail.newsdetail.newscontent;
						web.getSettings()
								.setDefaultTextEncodingName(HTTP.UTF_8);
						web.loadDataWithBaseURL("", content, "text/html",
								HTTP.UTF_8, "");
					}

					@Override
					public void onError(String error) {
						SuperEMsgApplication.toast(error);
					}
				});
	}
}
