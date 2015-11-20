package com.hy.superemsg.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.components.UITitle;
import com.hy.superemsg.db.DBUtils;
import com.hy.superemsg.db.DBUtils.DBHelper;
import com.hy.superemsg.req.ReqMmsCategory;
import com.hy.superemsg.req.ReqRingCategory;
import com.hy.superemsg.rsp.BaseRspApi;
import com.hy.superemsg.rsp.Category;
import com.hy.superemsg.rsp.RspMmsCategory;
import com.hy.superemsg.rsp.RspRingCategory;
import com.hy.superemsg.utils.CommonUtils;
import com.hy.superemsg.utils.ConvertUtils;
import com.hy.superemsg.utils.HttpUtils;
import com.hy.superemsg.utils.MobileInfo;
import com.hy.superemsg.utils.HttpUtils.AsynHttpCallback;
import com.hy.superemsg.viewpager.AbsFragment;
import com.hy.superemsg.viewpager.ScrollTabsViewPager;
import com.hy.superemsg.viewpager.fragments.MmsListFragment;
import com.hy.superemsg.viewpager.fragments.NoContentFragment;
import com.hy.superemsg.viewpager.fragments.RingListFragment;
import com.hy.superemsg.viewpager.fragments.WebViewFragment;

public class RingZoneActivity extends FragmentActivity {
	private ScrollTabsViewPager pager;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_sms);
		UITitle title = (UITitle) this.findViewById(R.id.ui_title);
		if (title != null) {
			title.setTitleText(R.string.ring_zone);
			title.setRightButton1(R.drawable.collection,
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							startActivityForResult(
									new Intent(RingZoneActivity.this,
											RingsCollectionsActivity.class),
									SuperEMsgApplication.REQUEST_LIST_TO_COLLECTION);
						}
					});
			title.setRightButton2(R.drawable.search,
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							startActivity(new Intent(RingZoneActivity.this,
									SearchRingActivity.class));
						}
					});
			title.setLeftButton(R.drawable.back, new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
		pager = (ScrollTabsViewPager) this.findViewById(R.id.viewpager);
		if (SuperEMsgApplication.account != null) {
			List<Category> categories = null;

			if (CommonUtils.isNotEmpty(SuperEMsgApplication.cached_ring)) {
				categories = ConvertUtils
						.mapKey2List(SuperEMsgApplication.cached_ring);
			}
			if (CommonUtils.isNotEmpty(categories)) {
				for (Category cate : categories) {
					RingListFragment fragment = new RingListFragment();
					Bundle b = new Bundle();
					b.putParcelable("categoryid", cate);
					b.putString("provinceid",
							SuperEMsgApplication.account.province);
					b.putInt("corpid", SuperEMsgApplication.account.operator);
					fragment.setArguments(b);
					pager.addPager(cate.categoryname, fragment);
				}
				addWebBase();
				pager.setUp(RingZoneActivity.this);
			} else {
				HttpUtils.getInst().excuteTask(new ReqRingCategory(),
						new AsynHttpCallback() {

							@Override
							public void onSuccess(BaseRspApi rsp) {
								RspRingCategory category = (RspRingCategory) rsp;
								if (CommonUtils
										.isNotEmpty(category.categorylist)) {
									SuperEMsgApplication.cached_ring.clear();
									for (Category cate : category.categorylist) {
										RingListFragment fragment = new RingListFragment();
										Bundle b = new Bundle();
										b.putParcelable("categoryid", cate);
										b.putString(
												"provinceid",
												SuperEMsgApplication.account.province);
										b.putInt(
												"corpid",
												SuperEMsgApplication.account.operator);
										fragment.setArguments(b);
										pager.addPager(cate.categoryname,
												fragment);
										SuperEMsgApplication.cached_ring.put(
												cate, null);
									}
									DBUtils.getInst().saveCategory(
											DBHelper.TABLE_RING,
											category.categorylist);
								}
								addWebBase();
								pager.setUp(RingZoneActivity.this);
							}

							@Override
							public void onError(String error) {
								SuperEMsgApplication.toast(error);
							}
						});
			}
		} else {
			addWebBase();
			pager.setUp(RingZoneActivity.this);
		}
	}

	private void addWebBase() {
		WebViewFragment web = new WebViewFragment();
		Bundle b = new Bundle();
		b.putString(SuperEMsgApplication.EXTRA_WEB_URL, "http://www.migu.cn");
		if (MobileInfo.getSimState(RingZoneActivity.this) == TelephonyManager.SIM_STATE_READY) {

			if (MobileInfo.getOperator(this) == 46001) {
				b.putString(SuperEMsgApplication.EXTRA_WEB_URL,
						"http://www.10155.com");
			}
		}
		web.setArguments(b);
		pager.addPager(getString(R.string.music_zone), web);
	}

	public ScrollTabsViewPager getCurrentViewPager() {
		return pager;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK
				&& requestCode == SuperEMsgApplication.REQUEST_LIST_TO_COLLECTION) {
			List<AbsFragment> fragments = pager.getAllFragments();
			if (CommonUtils.isNotEmpty(fragments)) {
				for (AbsFragment frag : fragments) {
					frag.onActivityResult(requestCode, resultCode, data);
				}
			}
		}
	}

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
