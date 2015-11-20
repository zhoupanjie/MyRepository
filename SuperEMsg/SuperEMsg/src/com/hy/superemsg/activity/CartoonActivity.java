package com.hy.superemsg.activity;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;

import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.components.UITitle;
import com.hy.superemsg.db.DBUtils;
import com.hy.superemsg.db.DBUtils.DBHelper;
import com.hy.superemsg.req.ReqAnimationCategory;
import com.hy.superemsg.rsp.BaseRspApi;
import com.hy.superemsg.rsp.Category;
import com.hy.superemsg.rsp.RspAnimationCategory;
import com.hy.superemsg.utils.CommonUtils;
import com.hy.superemsg.utils.HttpUtils;
import com.hy.superemsg.utils.HttpUtils.AsynHttpCallback;
import com.hy.superemsg.utils.MobileInfo;
import com.hy.superemsg.viewpager.AbsFragment;
import com.hy.superemsg.viewpager.ScrollTabsViewPager;
import com.hy.superemsg.viewpager.fragments.CartoonShelfFragment2;
import com.hy.superemsg.viewpager.fragments.WebViewFragment;

public class CartoonActivity extends FragmentActivity {
	private ScrollTabsViewPager pager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sms);
		UITitle title = (UITitle) this.findViewById(R.id.ui_title);
		if (title != null) {
			title.setTitleText("动漫乐园");
			title.setLeftButton(R.drawable.back, new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
		pager = (ScrollTabsViewPager) this.findViewById(R.id.viewpager);
		List<Category> categories = DBUtils.getInst().queryCategory(
				DBHelper.TABLE_CARTOON);
		if (CommonUtils.isNotEmpty(categories)) {
			for (Category cate : categories) {
				CartoonShelfFragment2 fragment = new CartoonShelfFragment2();
				Bundle b = new Bundle();
				b.putString("categoryid", cate.categoryid);
				fragment.setArguments(b);
				pager.addPager(cate.categoryname, fragment);
			}
//			addWebBase();
			pager.setUp(CartoonActivity.this);
		} else {
			HttpUtils.getInst().excuteTask(new ReqAnimationCategory(),
					new AsynHttpCallback() {

						@Override
						public void onSuccess(BaseRspApi rsp) {
							RspAnimationCategory category = (RspAnimationCategory) rsp;
							if (CommonUtils.isNotEmpty(category.categorylist)) {
								for (Category cate : category.categorylist) {
									CartoonShelfFragment2 fragment = new CartoonShelfFragment2();
									Bundle b = new Bundle();
									b.putString("categoryid", cate.categoryid);
									fragment.setArguments(b);
									pager.addPager(cate.categoryname, fragment);
								}
								DBUtils.getInst().saveCategory(
										DBHelper.TABLE_CARTOON,
										category.categorylist);
							}
//							addWebBase();
							pager.setUp(CartoonActivity.this);
						}

						@Override
						public void onError(String error) {
							SuperEMsgApplication.toast(error);
						}
					});
		}
	}
//
//	private void addWebBase() {
//		WebViewFragment web = new WebViewFragment();
//		Bundle b = new Bundle();
//		b.putString(SuperEMsgApplication.EXTRA_WEB_URL, "http://dm.10086.cn");
//		if (MobileInfo.getSimState(CartoonActivity.this) == TelephonyManager.SIM_STATE_READY) {
//			if (MobileInfo.getOperator(this) == 46001) {
//				b.putString(SuperEMsgApplication.EXTRA_WEB_URL, "http://wodm.cn");
//			}
//		}
//		web.setArguments(b);
//		pager.addPager("动漫基地", web);
//	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		AbsFragment frag = pager.getCurrentSelectedFragment();
		boolean handled = false;
		if (frag != null) {
			handled = frag.OnKeyEvent(keyCode, event);
		}
		if (handled)
			return true;
		return super.onKeyDown(keyCode, event);
	}
}
