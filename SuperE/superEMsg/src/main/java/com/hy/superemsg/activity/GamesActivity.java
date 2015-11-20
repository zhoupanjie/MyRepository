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
import com.hy.superemsg.req.ReqGameCategory;
import com.hy.superemsg.rsp.BaseRspApi;
import com.hy.superemsg.rsp.Category;
import com.hy.superemsg.rsp.RspGameCategory;
import com.hy.superemsg.utils.CommonUtils;
import com.hy.superemsg.utils.ConvertUtils;
import com.hy.superemsg.utils.HttpUtils;
import com.hy.superemsg.utils.MobileInfo;
import com.hy.superemsg.utils.HttpUtils.AsynHttpCallback;
import com.hy.superemsg.viewpager.AbsFragment;
import com.hy.superemsg.viewpager.ScrollTabsViewPager;
import com.hy.superemsg.viewpager.fragments.GameListFragment;
import com.hy.superemsg.viewpager.fragments.NoContentFragment;
import com.hy.superemsg.viewpager.fragments.WebViewFragment;

public class GamesActivity extends FragmentActivity {
	private ScrollTabsViewPager pager;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_sms);
		UITitle title = (UITitle) this.findViewById(R.id.ui_title);
		if (title != null) {
			title.setTitleText(R.string.game_zone);
			title.setRightButton1(R.drawable.collection,
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							startActivityForResult(
									new Intent(GamesActivity.this,
											GameCollectionsActivity.class),
									SuperEMsgApplication.REQUEST_LIST_TO_COLLECTION);
						}
					});
			title.setRightButton2(R.drawable.search,
					new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							startActivity(new Intent(GamesActivity.this,
									SearchGameActivity.class));
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
		if (CommonUtils.isNotEmpty(SuperEMsgApplication.cached_game)) {
			categories = ConvertUtils
					.mapKey2List(SuperEMsgApplication.cached_game);
		}
		if (CommonUtils.isNotEmpty(categories)) {
			for (Category cate : categories) {
				GameListFragment fragment = new GameListFragment();
				Bundle b = new Bundle();
				b.putParcelable("categoryid", cate);
				fragment.setArguments(b);
				pager.addPager(cate.categoryname, fragment);
			}
			addWebBase();
			pager.setUp(GamesActivity.this);
		} else {
			HttpUtils.getInst().excuteTask(new ReqGameCategory(),
					new AsynHttpCallback() {

						@Override
						public void onSuccess(BaseRspApi rsp) {
							RspGameCategory category = (RspGameCategory) rsp;
							if (CommonUtils.isNotEmpty(category.categorylist)) {
								SuperEMsgApplication.cached_game.clear();
								for (Category cate : category.categorylist) {
									GameListFragment fragment = new GameListFragment();
									Bundle b = new Bundle();
									b.putParcelable("categoryid", cate);
									fragment.setArguments(b);
									pager.addPager(cate.categoryname, fragment);
									SuperEMsgApplication.cached_game.put(cate,
											null);
								}
								DBUtils.getInst().saveCategory(
										DBHelper.TABLE_GAME,
										category.categorylist);
							}
							addWebBase();
							pager.setUp(GamesActivity.this);
						}

						@Override
						public void onError(String error) {
							SuperEMsgApplication.toast(error);
						}
					});
		}

	}

	private void addWebBase() {
		WebViewFragment web = new WebViewFragment();
		Bundle b = new Bundle();
		b.putString(SuperEMsgApplication.EXTRA_WEB_URL,
				"http://g.10086.cn");
		if (MobileInfo.getSimState(GamesActivity.this) == TelephonyManager.SIM_STATE_READY) {

			if (MobileInfo.getOperator(this) == 46001) {
				b.putString(SuperEMsgApplication.EXTRA_WEB_URL,
						"http://game.wo.com.cn");
			}
		}
		web.setArguments(b);
		pager.addPager("游戏基地", web);
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
