package com.hy.superemsg.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.components.UITitle;
import com.hy.superemsg.db.DBUtils;
import com.hy.superemsg.db.DBUtils.DBHelper;
import com.hy.superemsg.req.ReqSmsCategory;
import com.hy.superemsg.rsp.BaseRspApi;
import com.hy.superemsg.rsp.Category;
import com.hy.superemsg.rsp.RspSmsCategory;
import com.hy.superemsg.utils.CommonUtils;
import com.hy.superemsg.utils.ConvertUtils;
import com.hy.superemsg.utils.HttpUtils;
import com.hy.superemsg.utils.HttpUtils.AsynHttpCallback;
import com.hy.superemsg.viewpager.AbsFragment;
import com.hy.superemsg.viewpager.ScrollTabsViewPager;
import com.hy.superemsg.viewpager.fragments.SmsListFragment;

public class SmsDepotActivity extends FragmentActivity {
	private ScrollTabsViewPager pager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sms);
		UITitle title = (UITitle) this.findViewById(R.id.ui_title);
		if (title != null) {
			title.setTitleText(this.getString(R.string.sms_depot));
			title.setRightButton1(R.drawable.collection,
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							startActivityForResult(
									new Intent(SmsDepotActivity.this,
											SmsCollectionsActivity.class),
									SuperEMsgApplication.REQUEST_LIST_TO_COLLECTION);
						}
					});
			title.setRightButton2(R.drawable.search,
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							startActivity(new Intent(SmsDepotActivity.this,
									SearchSmsActivity.class));
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
		List<Category> categories = null;
		if (CommonUtils.isNotEmpty(SuperEMsgApplication.cached_sms)) {
			categories = ConvertUtils
					.mapKey2List(SuperEMsgApplication.cached_sms);
		}
		if (CommonUtils.isNotEmpty(categories)) {
			for (Category cate : categories) {
				SmsListFragment fragment = new SmsListFragment();
				Bundle b = new Bundle();
				b.putParcelable("categoryid", cate);
				fragment.setArguments(b);
				pager.addPager(cate.categoryname, fragment);
			}
			pager.setUp(SmsDepotActivity.this);
		} else {
			HttpUtils.getInst().excuteTask(new ReqSmsCategory(),
					new AsynHttpCallback() {

						@Override
						public void onSuccess(BaseRspApi rsp) {
							RspSmsCategory category = (RspSmsCategory) rsp;
							if (CommonUtils.isNotEmpty(category.categorylist)) {
								SuperEMsgApplication.cached_sms.clear();
								for (Category cate : category.categorylist) {
									SmsListFragment fragment = new SmsListFragment();
									Bundle b = new Bundle();
									b.putParcelable("categoryid", cate);
									fragment.setArguments(b);
									pager.addPager(cate.categoryname, fragment);
									SuperEMsgApplication.cached_sms.put(cate,
											null);
								}
								DBUtils.getInst().saveCategory(
										DBHelper.TABLE_SMS,
										category.categorylist);
							}
							pager.setUp(SmsDepotActivity.this);
						}

						@Override
						public void onError(String error) {
							SuperEMsgApplication.toast(error);
						}
					});
		}

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

	
}
