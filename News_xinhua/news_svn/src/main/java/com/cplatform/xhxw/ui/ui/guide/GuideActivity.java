package com.cplatform.xhxw.ui.ui.guide;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.adapter.GuideAdapter;

/**
 * 引导 Created by cy-love on 14-2-13.
 */
public class GuideActivity extends BaseActivity implements OnClickListener {

	private CloudyView cloud_view_1;
	private RelativeLayout view1;
	private CloudyView cloud_view_2;
	private CloudyView cloud_view_3;
	private CloudyView cloud_view_5;
	private CloudyView cloud_view_6;
	private CloudyView cloud_view_4;
	private RelativeLayout guide_root_layout;
	private LinearLayout view4;
	private ImageView cloud_view_air;
	private RelativeLayout guide_airplane_layout;

	private boolean isStartFly = false;
	private TranslateAnimation flyAnimation;
	private ImageView guide_go_home_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		// 初始化页面
		initViews();
	}

	private void initViews() {
		CloudyView.IsRunning = true;
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		int half_width = width / 2;
		int screenHeight = wm.getDefaultDisplay().getHeight();
		int half_height = screenHeight / 2;
		int height = screenHeight * 2 / 5;

		LayoutInflater inflater = LayoutInflater.from(this);

		ArrayList<View> views = new ArrayList<View>();
		// 初始化引导图片列表
		guide_root_layout = (RelativeLayout) findViewById(R.id.guide_root_layout);
		view1 = (RelativeLayout) inflater
				.inflate(R.layout.view_guide_one, null);
		view4 = (LinearLayout) inflater.inflate(R.layout.view_guide_four, null);
		cloud_view_1 = new CloudyView(this);
		cloud_view_1.init(half_width - 80, height - 150, width,
				R.drawable.cloud_1, 30);
		guide_root_layout.addView(cloud_view_1);
		cloud_view_1.move();

		cloud_view_2 = new CloudyView(this);
		cloud_view_2.init(half_width - 200, height - 250, width,
				R.drawable.cloud_2, 30);
		guide_root_layout.addView(cloud_view_2);
		cloud_view_2.move();

		cloud_view_3 = new CloudyView(this);
		cloud_view_3.init(-80, height - 200, width, R.drawable.cloud_3, 30);
		guide_root_layout.addView(cloud_view_3);
		cloud_view_3.move();

		cloud_view_4 = new CloudyView(this);
		cloud_view_4.init(width - 100, height - 170, width, R.drawable.cloud_1,
				30);
		guide_root_layout.addView(cloud_view_4);
		cloud_view_4.move();

		cloud_view_5 = new CloudyView(this);
		cloud_view_5.init(half_width + 150, height - 230, width,
				R.drawable.cloud_2, 30);
		guide_root_layout.addView(cloud_view_5);
		cloud_view_5.move();

		cloud_view_6 = new CloudyView(this);
		cloud_view_6.init(half_width + 100, height - 100, width,
				R.drawable.cloud_3, 30);
		guide_root_layout.addView(cloud_view_6);
		cloud_view_6.move();

		guide_airplane_layout = (RelativeLayout) view4
				.findViewById(R.id.guide_airplane_layout);
		guide_airplane_layout.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, screenHeight / 2));
		cloud_view_air = (ImageView) view4
				.findViewById(R.id.guide_airplane_img);
		int airWidth = width - 200;
		int airHeight = (int) (airWidth * 0.34);
		cloud_view_air.setLayoutParams(new RelativeLayout.LayoutParams(
				airWidth, airHeight));
		flyAnimation = new TranslateAnimation(-(100 + airWidth), 100,
				(half_height - airHeight) / 2, (half_height - airHeight) / 2); // 位置变化动画效果
		flyAnimation.setDuration(2000); // 设置动画持续时间
		flyAnimation.setRepeatCount(0); // 设置重复次数
		flyAnimation.setFillAfter(true);
		cloud_view_air.setAnimation(flyAnimation); // 设置动画效果

		guide_go_home_btn = (ImageView) view4
				.findViewById(R.id.guide_go_home_btn);
		guide_go_home_btn.setOnClickListener(this);
		views.add(view1);
		views.add(inflater.inflate(R.layout.view_guide_two, null));
		views.add(inflater.inflate(R.layout.view_guide_three, null));
		views.add(view4);

		GuideAdapter vpAdapter = new GuideAdapter(this);
		vpAdapter.addAllData(views);

		ViewPager vp = (ViewPager) findViewById(R.id.viewpager);
		vp.setAdapter(vpAdapter);
		vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				if (!isStartFly && arg0 == 3) {
					flyAnimation.startNow();
					isStartFly = true;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

	}

	@Override
	public void finish() {
		Constants.updateGuideVersion(Constants.AppInfo.getVersionName());
		super.finish();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.guide_go_home_btn) {
			finish();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		release();
		super.onDestroy();
	}

	public void release() {
		CloudyView.IsRunning = false;
		if (cloud_view_1 != null) {
			cloud_view_1.release();
		}
		if (cloud_view_2 != null) {
			cloud_view_2.release();
		}
		if (cloud_view_3 != null) {
			cloud_view_3.release();
		}
		if (cloud_view_4 != null) {
			cloud_view_4.release();
		}
		if (cloud_view_5 != null) {
			cloud_view_5.release();
		}
		if (cloud_view_6 != null) {
			cloud_view_6.release();
		}
	}

	@Override
	protected String getScreenName() {
		return GuideActivity.class.getName();
	}

}