package com.hy.superemsg.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.components.UITitle;
import com.hy.superemsg.db.DBUtils;
import com.hy.superemsg.db.DBUtils.DBHelper;
import com.hy.superemsg.req.ReqMmsCategory;
import com.hy.superemsg.req.ReqSmsCategory;
import com.hy.superemsg.rsp.BaseRspApi;
import com.hy.superemsg.rsp.Category;
import com.hy.superemsg.rsp.RspMmsCategory;
import com.hy.superemsg.rsp.RspSmsCategory;
import com.hy.superemsg.utils.CommonUtils;
import com.hy.superemsg.utils.ConvertUtils;
import com.hy.superemsg.utils.HttpUtils;
import com.hy.superemsg.utils.HttpUtils.AsynHttpCallback;
import com.hy.superemsg.viewpager.AbsFragment;
import com.hy.superemsg.viewpager.ScrollTabsViewPager;
import com.hy.superemsg.viewpager.fragments.MmsListFragment;
import com.hy.superemsg.viewpager.fragments.SmsListFragment;

public class MmsDepotActivity extends FragmentActivity {
	private ScrollTabsViewPager pager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sms);
		UITitle title = (UITitle) this.findViewById(R.id.ui_title);
		if (title != null) {
			title.setTitleText(R.string.mms_depot);
			title.setRightButton1(R.drawable.collection,
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							startActivityForResult(
									new Intent(MmsDepotActivity.this,
											MmsCollectionsActivity.class),
									SuperEMsgApplication.REQUEST_LIST_TO_COLLECTION);
						}
					});
			title.setRightButton2(R.drawable.search,
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							startActivity(new Intent(MmsDepotActivity.this,
									SearchMmsActivity.class));
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
		if (CommonUtils.isNotEmpty(SuperEMsgApplication.cached_mms)) {
			categories = ConvertUtils
					.mapKey2List(SuperEMsgApplication.cached_mms);
		}
		if (CommonUtils.isNotEmpty(categories)) {
			for (Category cate : categories) {
				MmsListFragment fragment = new MmsListFragment();
				Bundle b = new Bundle();
				b.putParcelable("categoryid", cate);
				fragment.setArguments(b);
				pager.addPager(cate.categoryname, fragment);
			}
			pager.setUp(MmsDepotActivity.this);
		} else {
			HttpUtils.getInst().excuteTask(new ReqMmsCategory(),
					new AsynHttpCallback() {

						@Override
						public void onSuccess(BaseRspApi rsp) {
							RspMmsCategory category = (RspMmsCategory) rsp;
							if (CommonUtils.isNotEmpty(category.categorylist)) {
								SuperEMsgApplication.cached_mms.clear();
								for (Category cate : category.categorylist) {
									MmsListFragment fragment = new MmsListFragment();
									Bundle b = new Bundle();
									b.putParcelable("categoryid", cate);
									fragment.setArguments(b);
									pager.addPager(cate.categoryname, fragment);
									SuperEMsgApplication.cached_mms.put(cate,
											null);
								}
								DBUtils.getInst().saveCategory(
										DBHelper.TABLE_MMS,
										category.categorylist);
							}
							pager.setUp(MmsDepotActivity.this);
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
		if (resultCode == RESULT_OK) {
			if (requestCode == SuperEMsgApplication.REQUEST_LIST_TO_COLLECTION) {
				List<AbsFragment> fragments = pager.getAllFragments();
				if (CommonUtils.isNotEmpty(fragments)) {
					for (AbsFragment frag : fragments) {
						frag.onActivityResult(requestCode, resultCode, data);
					}
				}
			} else if (requestCode == SuperEMsgApplication.REQUEST_LIST_TO_MMS_DETAIL) {
				AbsFragment fragment = pager.getCurrentSelectedFragment();
				fragment.onActivityResult(requestCode, resultCode, data);
			}
		}
	}

}
