package com.hy.superemsg.activity;

import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.components.UITitle;
import com.hy.superemsg.db.DBUtils;
import com.hy.superemsg.db.DBUtils.DBHelper;
import com.hy.superemsg.req.ReqHolidayCategory;
import com.hy.superemsg.rsp.BaseRspApi;
import com.hy.superemsg.rsp.Category;
import com.hy.superemsg.rsp.RspHolidayCategory;
import com.hy.superemsg.utils.CommonUtils;
import com.hy.superemsg.utils.HttpUtils;
import com.hy.superemsg.utils.HttpUtils.AsynHttpCallback;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class HolidayDepotActiviy extends Activity {
	private ProgressBar loadingBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_holiday);
		initView();
	}

	private void initView() {
		UITitle title = (UITitle) this.findViewById(R.id.ui_title);
		loadingBar = (ProgressBar)findViewById(R.id.item_loading);
		loadingBar.setVisibility(View.VISIBLE);
		if (title != null) {
			title.setTitleText(R.string.holiday);
			title.setLeftButton(R.drawable.back, new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
		// List<Category> categories = null;
		// if (CommonUtils.isNotEmpty(SuperEMsgApplication.cached_holiday)) {
		// categories = ConvertUtils
		// .mapKey2List(SuperEMsgApplication.cached_holiday);
		// }
		// if (CommonUtils.isNotEmpty(categories)) {
		// int i = 0;
		// for (Category cate : categories) {
		// // MmsListFragment fragment = new MmsListFragment();
		// // Bundle b = new Bundle();
		// // b.putParcelable("categoryid", cate);
		// // fragment.setArguments(b);
		// // pager.addPager(cate.categoryname, fragment);
		// i++;
		// }
		// }
		HttpUtils.getInst().excuteTask(new ReqHolidayCategory(),
				new AsynHttpCallback() {

					@Override
					public void onSuccess(BaseRspApi rsp) {
						RspHolidayCategory category = (RspHolidayCategory) rsp;
						loadingBar.setVisibility(View.GONE);
						if (CommonUtils.isNotEmpty(category.categorylist)) {
							SuperEMsgApplication.cached_holiday.clear();
							int i = 0;
							for (Category cate : category.categorylist) {
								Log.i(cate.categoryname, cate.categoryid);
								final Bundle b = new Bundle();
								b.putParcelable("categoryid", cate);
								if (i < 18) {
									ImageView v = (ImageView) findViewById(getId(i));
									v.setImageResource(getImg(cate.categoryname));
									v.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											Intent intent = new Intent();
											intent.setClass(
													HolidayDepotActiviy.this,
													HolidayDetailActivity.class);
											intent.putExtras(b);
											startActivity(intent);
										}
									});
									i++;
								}
								SuperEMsgApplication.cached_holiday.put(cate,
										null);
							}
							DBUtils.getInst().saveCategory(
									DBHelper.TABLE_HOLIDAY,
									category.categorylist);
						}
					}

					@Override
					public void onError(String error) {
						loadingBar.setVisibility(View.GONE);
						SuperEMsgApplication.toast(error);
					}

				});

	}

	private int getId(int i) {
		switch (i) {
		case 0:
			return R.id.holiday_img1;
		case 1:
			return R.id.holiday_img2;
		case 2:
			return R.id.holiday_img3;
		case 3:
			return R.id.holiday_img4;
		case 4:
			return R.id.holiday_img5;
		case 5:
			return R.id.holiday_img6;
		case 6:
			return R.id.holiday_img7;
		case 7:
			return R.id.holiday_img8;
		case 8:
			return R.id.holiday_img9;
		case 9:
			return R.id.holiday_img10;
		case 10:
			return R.id.holiday_img11;
		case 11:
			return R.id.holiday_img12;
		case 12:
			return R.id.holiday_img13;
		case 13:
			return R.id.holiday_img14;
		case 14:
			return R.id.holiday_img15;
		case 15:
			return R.id.holiday_img16;
		case 16:
			return R.id.holiday_img17;
		case 17:
			return R.id.holiday_img18;
		default:
			return 0;
		}
	}

	private int getImg(String s) {
		if (s.equals("春节")) {
			return R.drawable.chunjie;
		}
		if (s.equals("七夕")) {
			return R.drawable.qixi;
		}
		if (s.equals("中秋节")) {
			return R.drawable.zhongqiu;
		}
		if (s.equals("国庆节")) {
			return R.drawable.guoqing;
		}
		if (s.equals("重阳节")) {
			return R.drawable.chongyang;
		}
		if (s.equals("元旦")) {
			return R.drawable.yuandan;
		}
		if (s.equals("万圣节")) {
			return R.drawable.wansheng;
		}
		if (s.equals("光棍节")) {
			return R.drawable.guanggun;
		}
		if (s.equals("感恩节")) {
			return R.drawable.ganen;
		}
		if (s.equals("平安夜")) {
			return R.drawable.pingan;
		}

		if (s.equals("圣诞节")) {
			return R.drawable.shengdan;
		}
		if (s.equals("除夕")) {
			return R.drawable.chuxi;
		}
		if (s.equals("元宵节")) {
			return R.drawable.yaunxiao;
		}
		if (s.equals("妇女节")) {
			return R.drawable.funv;
		}
		if (s.equals("清明节")) {
			return R.drawable.qingming;
		}
		if (s.equals("儿童节")) {
			return R.drawable.ertong;
		}
		if (s.equals("端午节")) {
			return R.drawable.duanwu;
		}
		if (s.equals("劳动节")) {
			return R.drawable.laodong;
		}

		return 0;
	}

}
