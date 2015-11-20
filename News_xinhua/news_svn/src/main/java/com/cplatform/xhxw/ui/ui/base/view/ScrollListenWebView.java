package com.cplatform.xhxw.ui.ui.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

public class ScrollListenWebView extends WebView {
	public ScrollInterface mScrollInterface;
	
	public ScrollListenWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ScrollListenWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScrollListenWebView(Context context) {
		super(context);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		mScrollInterface.onSChanged(l, t, oldl, oldt);

	}

	public void setOnCustomScroolChangeListener(ScrollInterface scrollInterface) {
		this.mScrollInterface = scrollInterface;
	}

	public interface ScrollInterface {
		public void onSChanged(int l, int t, int oldl, int oldt);
	}
}
