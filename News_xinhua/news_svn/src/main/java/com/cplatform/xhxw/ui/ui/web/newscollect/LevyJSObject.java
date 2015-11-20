package com.cplatform.xhxw.ui.ui.web.newscollect;

import android.webkit.JavascriptInterface;

public class LevyJSObject {
	private onLevyJsListener mLis;
	
	public interface onLevyJsListener {
		void onButtonsInfo(String msg);
	}
	
	public onLevyJsListener getmLis() {
		return mLis;
	}
	public void setmLis(onLevyJsListener mLis) {
		this.mLis = mLis;
	}
	
	@JavascriptInterface
	public void androidGetLevyInfo(String msg) {
		if(mLis != null) {
			mLis.onButtonsInfo(msg);
		}
	}
}
