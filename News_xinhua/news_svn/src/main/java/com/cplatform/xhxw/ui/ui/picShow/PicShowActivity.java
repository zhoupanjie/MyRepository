package com.cplatform.xhxw.ui.ui.picShow;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.StatisticalKey;
import com.cplatform.xhxw.ui.db.CollectDB;
import com.cplatform.xhxw.ui.db.DownloadDB;
import com.cplatform.xhxw.ui.db.ReadNewsDB;
import com.cplatform.xhxw.ui.db.dao.CollectDao;
import com.cplatform.xhxw.ui.db.dao.CollectFlag;
import com.cplatform.xhxw.ui.db.dao.DownloadDao;
import com.cplatform.xhxw.ui.db.dao.ReadNewsDao;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.HttpClientConfig;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.NewsDetailRequest;
import com.cplatform.xhxw.ui.http.net.NewsDetailResponse;
import com.cplatform.xhxw.ui.model.NewPic;
import com.cplatform.xhxw.ui.model.NewsDetail;
import com.cplatform.xhxw.ui.model.PicShow;
import com.cplatform.xhxw.ui.model.Pics;
import com.cplatform.xhxw.ui.model.RecommendAtlas;
import com.cplatform.xhxw.ui.receiver.StartServiceReceiver;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.view.RecommendAtlasView;
import com.cplatform.xhxw.ui.ui.base.widget.DefaultView;
import com.cplatform.xhxw.ui.ui.cyancomment.CYanUtil;
import com.cplatform.xhxw.ui.ui.web.WebViewActivity;
import com.cplatform.xhxw.ui.util.AnimationUtil;
import com.cplatform.xhxw.ui.util.DateUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.cplatform.xhxw.ui.util.ShareUtil;
import com.cplatform.xhxw.ui.util.StringUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.sohu.cyan.android.sdk.api.CallBack;
import com.sohu.cyan.android.sdk.api.CyanSdk;
import com.sohu.cyan.android.sdk.entity.AccountInfo;
import com.sohu.cyan.android.sdk.exception.CyanException;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;
import com.wbtech.ums.UmsAgent;

/**
 * 图集查看 Created by cy-love on 13-12-31.
 */
public class PicShowActivity extends BaseActivity implements
		DefaultView.OnTapListener, PicItemView.OnViewTapListener {

	private static final String TAG = PicShowActivity.class.getSimpleName();
	@Bind(R.id.view_pager)
	HackyViewPager mVp;
	@Bind(R.id.tv_num)
	TextView mNum;
//	@Bind(R.id.news_collect_title)
//	TextView tvTitle;
	@Bind(R.id.sv_desc)
	ScrollView mDescScrollView;
	@Bind(R.id.tv_desc)
	TextView mDesc;
	@Bind(R.id.btn_favorite)
	Button mFavoriteBtn;
	@Bind(R.id.def_view)
	DefaultView mDefView;
	@Bind(R.id.btn_image_comment)
	Button mComment;
	@Bind(R.id.nv_title)
	View titleView;
	@Bind(R.id.ly_option)
	View optionView;
	private String mNewsId;
	private String mNewsTitle;
	private PicShow mData;
	private AsyncHttpResponseHandler mHttpHandler;
	private int mSelectIndex;
	private int mOriginalTextViewHeight;
	private boolean isTextAreaScaled = false;
	private NewsDetail mNewsDetail;
	private RelativeLayout mBottomBarLo;

	private boolean isEnterpriseFromBundle = false;
	
	private boolean isBottomBarShow = true;
	private boolean isBottomHideByAd = false;
	private CyanSdk mCyanSdk;

	public static Intent getIntent(Context context, String newsId,
			boolean isShowOption, boolean isEnterprise, String title) {
		return getIntent(context, null, 0, newsId, isShowOption, isEnterprise, title);
	}

	public static Intent getIntent(Context context, PicShow obj, int index,
			String newsId, boolean isShowOption, boolean isEnterPrise, String title) {
		Intent intent = new Intent(context, PicShowActivity.class);
		Bundle bun = new Bundle();
		bun.putInt("index", index);
		bun.putString("newsId", newsId);
		bun.putString("newsTitle", title);
		bun.putBoolean("isShowOption", isShowOption);
		bun.putBoolean("isEnterprise", isEnterPrise);
		intent.putExtras(bun);
		return intent;
	}

	@Override
	protected String getScreenName() {
		return "PicShowActivity";
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pic_show);
		ButterKnife.bind(this);

		Bundle bun = getIntent().getExtras();
		if (bun == null) {
			LogUtil.w(TAG, "bundle is null!");
			return;
		}
		int index = bun.getInt("index", 0);
		mNewsId = bun.getString("newsId");
		mNewsTitle = bun.getString("newsTitle");
//		tvTitle.setText(mNewsTitle);
		boolean isPush = bun.getBoolean("isPush", false);
		if (isPush) {
			App.getPreferenceManager().messageNewCountMinus();
		}
		initActionBar();

		mDefView.setHidenOtherView(mVp, optionView);
		mDefView.setListener(this);
		mDefView.setStatus(DefaultView.Status.showData);
		boolean isShowOption = bun.getBoolean("isShowOption");
		isEnterpriseFromBundle = bun.getBoolean("isEnterprise");
		if (!isShowOption) { // 新闻详情隐藏 收藏和分享
			mFavoriteBtn.setVisibility(View.GONE);
			findViewById(R.id.btn_share).setVisibility(View.GONE);
		}

		loadData(mNewsId, index);
		mDescScrollView.setBackgroundColor(Color.BLACK);
		mDescScrollView.getBackground().setAlpha(80);
		mDesc.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mDesc.getText().length() > 0) {
					resizeTextArea(false);
				}
			}
		});
		mBottomBarLo = (RelativeLayout) findViewById(R.id.pic_show_bottom_bar);
		mCyanSdk = CyanSdk.getInstance(this);
	}

	private void initData(int index) {
		if (!mData.isComment()) {
			mComment.setVisibility(View.GONE);
		} else {
			mComment.setVisibility(View.VISIBLE);
			int num = Integer.valueOf(mData.getCommentCount());
			// 评论按钮处显示省略号
			if (num >= 0 && num < Constants.COMMENT_SHOW_NUMBER_LOWER_BOUND) {
				mComment.setBackgroundResource(R.drawable.btn_new_0_comment);
			} else if (num >= Constants.COMMENT_SHOW_NUMBER_LOWER_BOUND
					&& num < Constants.COMMENT_SHOW_NUMBER_PLUS_LOWER_BOUND) {
				// 评论按钮处显示数字
				mComment.setText(mData.getCommentCount());
			} else if (num >= Constants.COMMENT_SHOW_NUMBER_PLUS_LOWER_BOUND) {
				// 评论按钮处显示数字+
				mComment.setText("99+");
			}
		}
		mVp.setAdapter(new PicPagerAdapter());
		mVp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int i, float v, int i2) {
			}

			@Override
			public void onPageSelected(int i) {
				UmsAgent.onEvent(PicShowActivity.this,
						StatisticalKey.detail_pic_slide, new String[] {
								StatisticalKey.key_channelid,
								StatisticalKey.key_newsid ,StatisticalKey.key_title}, new String[] {
								App.channel_id, mNewsId ,getDataTitle()});

				if (isExistRecmmendAtlas() && i == mData.getPics().size()) {
					mDescScrollView.setVisibility(View.INVISIBLE);
				} else {
					String type = mData.getPics().get(i).getType();
					if(!TextUtils.isEmpty(type) && type.equals("2")) {
						mDescScrollView.setVisibility(View.INVISIBLE);
						if(isBottomBarShow) {
							AnimationUtil.startViewBottomOutAndGone(PicShowActivity.this,
								optionView);
							isBottomBarShow = false;
							isBottomHideByAd = true;
						}
					} else {
						mDescScrollView.setVisibility(View.VISIBLE);
						if(isBottomHideByAd && !isBottomBarShow) {
							AnimationUtil.startViewBottomInAndVisible(PicShowActivity.this,
									optionView);
							isBottomBarShow = true;
							isBottomHideByAd = false;
						}
					}
				}
				adjustPageBackGround(i);
				if (i == mData.getPics().size()) {
					return;
				}
				setSelectNum(i + 1);
				setSelectDesc(i);
				mSelectIndex = i;
				resizeTextArea(true);
			}

			/**
			 * 推荐图集的布局跟图片展示页有所区别
			 * 
			 * @param i
			 */
			private void adjustPageBackGround(int i) {
				if (i == mData.getPics().size()) {
					mNum.setText("");
					mDesc.setText("");
					mFavoriteBtn.setVisibility(View.INVISIBLE);
					findViewById(R.id.btn_share).setVisibility(View.INVISIBLE);
					findViewById(R.id.btn_down).setVisibility(View.INVISIBLE);
				} else {
					if (mFavoriteBtn.getVisibility() != View.GONE) {
						mFavoriteBtn.setVisibility(View.VISIBLE);
						findViewById(R.id.btn_share)
								.setVisibility(View.VISIBLE);
					}
					findViewById(R.id.btn_down).setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onPageScrollStateChanged(int i) {
			}
		});
		if (index != mData.getPics().size()) {
			mVp.setCurrentItem(index);
			setSelectNum(index + 1);
			setSelectDesc(index);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		String newsId = getDataNewId();
		if (!TextUtils.isEmpty(newsId)) {
			CollectDao isCon = CollectDB.getCollectByNewsId(this, newsId);
			if (isCon == null) {
				mFavoriteBtn.setBackgroundResource(R.drawable.ic_pic_favorite);
			} else {
				mFavoriteBtn
						.setBackgroundResource(R.drawable.ic_pic_favorite_surcess);
			}
			ReadNewsDB.saveNews(this,
					new ReadNewsDao(newsId, DateUtil.getTime()));
		}
	}

	/**
	 * 获得标题
	 */
	private String getDataTitle() {
		if (mData != null) {
			return mData.getTitle();
		}
		return null;
	}

	/**
	 * 获得简介
	 *
	 * @param posi
	 * @return
	 */
	private String getDataDesc(int posi) {
		if (mData != null) {
			return mData.getPics().get(posi).getSummary();
		}
		return null;
	}

	/**
	 * 获得总数
	 *
	 * @return
	 */
	private int getDataCount() {
		if (mData != null) {
			return mData.getPics().size();
		}
		return 0;
	}

	/**
	 * 获得图片url
	 *
	 * @param posi
	 * @return
	 */
	private String getDataUrl(int posi) {
		if (mData != null) {
			return mData.getPics().get(posi).getThumbnail();
		}
		return null;
	}

	/**
	 * 获得newid
	 *
	 * @return
	 */
	private String getDataNewId() {
		return mNewsId;
	}

	/**
	 * 获得newid
	 *
	 * @return
	 */
	private CollectDao getCollectDao() {
		if (mData == null) {
			return null;
		}
		CollectDao dao = new CollectDao();
		dao.setFlag(CollectFlag.COLLECT_NEWS_TYPE_NORMAL_PICS);
		if (isEnterpriseFromBundle) {
			dao.setFlag(CollectFlag.COLLECT_NEWS_TYPE_ENTERPRISE_PICS
					+ StringUtil.parseIntegerFromString(Constants
							.getEnterpriseId()));
		}
		dao.setOperatetime(DateUtil.getTime());
		String data = new Gson().toJson(mData);
		dao.setData(data);
		dao.setTitle(mData.getTitle());
		dao.setNewsId(mData.getNewsId());
		return dao;
	}
	
	private boolean isExistAd() {
		if(mData != null) {
			if(mData.getPics() != null && mData.getPics().size() > 0) {
				String type = mData.getPics().get(mData.getPics().size() - 1).getType();
				return TextUtils.isEmpty(type) ? false : type.equals("2");
			}
		}
		return false;
	}

	/**
	 * 选中的位置 显示如：4/5
	 *
	 * @param i
	 */
	private void setSelectNum(int i) {
		mNum.setText(StringUtil.getString(i, "/", getDataCount()));
	}

	/**
	 * 简介内容
	 *
	 * @param posi
	 */
	private void setSelectDesc(int posi) {
		mDesc.setText(getDataDesc(posi));
		String type = mData.getPics().get(posi).getType();
		if (mDesc.length() == 0 || (type != null && type.equals("2"))) {
			mDescScrollView.setVisibility(View.INVISIBLE);
		} else {
			mDescScrollView.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onTapAction() {
		loadData(mNewsId, 0);
	}

	@Override
	public void onViewTap() {
		if(isExistAd() && mSelectIndex == mData.getPics().size() - 1) {
			startActivity(WebViewActivity.getIntentByURL(this, 
					mData.getPics().get(mSelectIndex).getUrl(), ""));
			return;
		}
		// 根据图片点击隐藏和显示操作类控件
		switch (titleView.getVisibility()) {
		case View.VISIBLE:
			AnimationUtil.startViewTopOutAndGone(PicShowActivity.this,
					titleView);
			AnimationUtil.startViewBottomOutAndGone(PicShowActivity.this,
					optionView);
			isBottomBarShow = false;
			break;
		default:
			AnimationUtil.startViewTopInAndVisible(PicShowActivity.this,
					titleView);
			AnimationUtil.startViewBottomInAndVisible(PicShowActivity.this,
					optionView);
			isBottomBarShow = true;
			break;
		}
	}

	private class PicPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			if (isExistRecmmendAtlas()) {
				return getDataCount() + 1;
			}
			return getDataCount();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			if (isExistRecmmendAtlas() && position == mData.getPics().size()) {
				RecommendAtlasView view = new RecommendAtlasView(
						container.getContext(), mData.getRecommendAtlas());
				view.setEnterprise(isEnterpriseFromBundle);
				container.addView(view, LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT);
				return view;
			} else {
				PicItemView view = new PicItemView(container.getContext(),
						getDataUrl(position), PicShowActivity.this);
				container.addView(view, LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT);
				return view;
			}
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

	}

	@OnClick(R.id.btn_down)
	public void onDownAction(View v) {
		showToast(R.string.start_download);
		DownloadDao dao = new DownloadDao();
		dao.setAddTime(DateUtil.getTime());
		dao.setUrl(getDataUrl(mSelectIndex));
		dao.setDownloaded(DownloadDao.STATUS_DOWNLOADING);
		boolean isAdd = DownloadDB.addDownload(this, dao);
		if (isAdd) {
			Intent intent = new Intent(this, StartServiceReceiver.class);
			intent.setAction(StartServiceReceiver.ACTION_DOWNLOAD_JOB_START);
			sendBroadcast(intent);
		} else {
			showToast(R.string.download_the_task_already_exists);
		}
	}

	@OnClick(R.id.btn_share)
	public void onShareAction(View v) {
		String newsId = getDataNewId();
		if (TextUtils.isEmpty(newsId)) {
			showToast(getString(R.string.news_id_is_empty));
			return;
		}
		String title = getDataTitle();
		String content = title;
		String clickUrl = String.format(
				getString(R.string.share_news_click_url), 
				HttpClientConfig.BASE_URL, newsId);
		ShareUtil.sendTextIntent(this, null, getString(R.string.share_news),
				getString(R.string.share_news), title, content, clickUrl,
				false, true, false, null, true);
	}

	@OnClick(R.id.btn_favorite)
	public void onFavoriteAction(View v) {
		String newsId = getDataNewId();
		if (!TextUtils.isEmpty(newsId)) {
			CollectDao isCon = CollectDB.getCollectByNewsId(this, newsId);
			if (null == isCon) {
				CollectDao dao = getCollectDao();
				if (dao == null) {
					showToast("暂不支持此类型的收藏");
					return;
				}
				CollectDB.saveNews(this, dao);
				mFavoriteBtn
						.setBackgroundResource(R.drawable.ic_pic_favorite_surcess);
				setCollectResult(false);
			} else {
				CollectDB.delCollectByNewsId(this, newsId);
				mFavoriteBtn.setBackgroundResource(R.drawable.ic_pic_favorite);
				setCollectResult(true);
			}
		}
	}

	@OnClick(R.id.btn_image_comment)
	public void goImageComment(View v) {
		if (getDataNewId() == null) {
			showToast("新闻Id为空");
		} else {
			checkAndEnterComments();
		}
	}
	
	private void checkAndEnterComments() {
		try {
			CYanUtil.initCyan(this);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		try {
			if(mCyanSdk.getAccountInfo() != null && mCyanSdk.getAccountInfo().isv_refer_id != null) {
				mCyanSdk.logOut();
			}
			
			AccountInfo maccountInfo = new AccountInfo();
			maccountInfo.isv_refer_id = Constants.getUid();
			maccountInfo.nickname = CYanUtil.getCyanNickName();
			maccountInfo.img_url = (Constants.userInfo == null) ? null : Constants.userInfo.getLogo();
			mCyanSdk.setAccountInfo(maccountInfo, new CallBack() {
				
				@Override
				public void success() {
					enterComments();
				}
				
				@Override
				public void error(CyanException arg0) {
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void enterComments() {
		if(mCyanSdk != null) {
			mCyanSdk.viewComment(mNewsId, mNewsTitle, this);
		}
	}

	/**
	 * 设置是否取消收藏返回
	 *
	 * @param isRemove
	 *            true 删除收藏
	 */
	private void setCollectResult(boolean isRemove) {
		Intent data = new Intent();
		data.putExtra("removeCollect", isRemove);
		this.setResult(Activity.RESULT_OK, data);
	}

	private void loadData(final String newsId, final int index) {
		NewsDetailRequest ndr = new NewsDetailRequest(newsId);
		if (isEnterpriseFromBundle) {
			ndr.setSaasRequest(true);
		}
		APIClient.newsDetail(ndr, new AsyncHttpResponseHandler() {
			@Override
			protected void onPreExecute() {
				if (mHttpHandler != null) {
					mHttpHandler.cancle();
				}
				mHttpHandler = this;
				mDefView.setStatus(DefaultView.Status.loading);
			}

			@Override
			public void onFinish() {
				mHttpHandler = null;
			}

			@Override
			public void onSuccess(String content) {
				NewsDetailResponse response;
				try {
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content,
							NewsDetailResponse.class);
				} catch (Exception e) {
					showToast(R.string.data_format_error);
					LogUtil.e(TAG, e);
					return;
				}
				if (response.isSuccess()) {
					mNewsDetail = response.getData();
					PicShow item = new PicShow(new String());
					item.setCommentCount(mNewsDetail.getCommentcount());
					item.setNewsId(newsId);
					item.setTitle(mNewsDetail.getTitle());
					item.setComment(mNewsDetail.getIscomment().equals("0") ? true
							: false);
					List<NewPic> pics = new ArrayList<NewPic>();
					List<Pics> detPic = mNewsDetail.getPics();
					for (Pics pic : detPic) {
						NewPic tmp = new NewPic();
						tmp.setSummary(pic.getSummary());
						tmp.setThumbnail(pic.getUrl());
						tmp.setType(pic.getType());
						tmp.setUrl(pic.getAdurl());
						pics.add(tmp);
					}
					item.setPics(pics);
					List<RecommendAtlas> recommendAtlas = mNewsDetail
							.getRecommendimg();
					item.setRecommendAtlas(recommendAtlas);

					mData = item;
					initData(index);
					mDefView.setStatus(DefaultView.Status.showData);
					// 显示数据
				} else {
					showToast(response.getMsg());
					mDefView.setStatus(DefaultView.Status.error);
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				showToast(R.string.load_server_failure);
				mDefView.setStatus(DefaultView.Status.error);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** 分享 */
		UMSocialService mController = UMServiceFactory.getUMSocialService(
				"com.umeng.share");
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	protected void onPause() {
		has_channel_id = true;
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (mHttpHandler != null) {
			mHttpHandler.cancle();
			mHttpHandler = null;
		}
		super.onDestroy();
	}

	/**
	 * 点击文字区域时根据内容高度重新设置文字区域大小
	 * 
	 * @param isScroll
	 */
	private void resizeTextArea(boolean isScroll) {
		DisplayMetrics dm = getResources().getDisplayMetrics();
		int totalTextLine = mDesc.getLineCount();
		TextPaint tp = mDesc.getPaint();
		int totalTextHeight = (int) (totalTextLine * (tp.getFontSpacing()) + 10);

		if (!isTextAreaScaled) {
			if (isScroll) {
				return;
			}
			mOriginalTextViewHeight = mDescScrollView.getHeight();
			int desiredHeight = dm.heightPixels * 1 / 3
					- mBottomBarLo.getHeight();
			if (totalTextHeight < mOriginalTextViewHeight) {
				return;
			} else {
				boolean isTooH = totalTextHeight > desiredHeight;
				totalTextHeight = isTooH ? desiredHeight : totalTextHeight;
				LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT, totalTextHeight);
				mDescScrollView.setLayoutParams(lllp);
				isTextAreaScaled = true;
			}
		} else {
			LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					mOriginalTextViewHeight);
			mDescScrollView.setLayoutParams(lllp);
			isTextAreaScaled = false;
		}
	}

	private boolean isExistRecmmendAtlas() {
		return mData.getRecommendAtlas() != null
				&& mData.getRecommendAtlas().size() > 0;
	}
}