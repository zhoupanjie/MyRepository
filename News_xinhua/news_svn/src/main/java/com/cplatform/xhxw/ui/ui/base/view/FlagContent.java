package com.cplatform.xhxw.ui.ui.base.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.StatisticalKey;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.HttpClientConfig;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.BaseRequest;
import com.cplatform.xhxw.ui.http.net.NewMsgAlertResponse;
import com.cplatform.xhxw.ui.http.responseType.NewsDetTextSize;
import com.cplatform.xhxw.ui.model.UserNewMsgAlert;
import com.cplatform.xhxw.ui.receiver.StartServiceReceiver;
import com.cplatform.xhxw.ui.service.NewsCashService;
import com.cplatform.xhxw.ui.ui.base.widget.CircleProgressBar;
import com.cplatform.xhxw.ui.ui.base.widget.ScrollLayout.OnFlagAreaExpandListener;
import com.cplatform.xhxw.ui.ui.collect.CollectActivity;
import com.cplatform.xhxw.ui.ui.comment.MyCommentActivity;
import com.cplatform.xhxw.ui.ui.feedback.FeedbackActivity;
import com.cplatform.xhxw.ui.ui.login.LoginActivity;
import com.cplatform.xhxw.ui.ui.login.RegisterCheckActivity;
import com.cplatform.xhxw.ui.ui.main.HomeActivity;
import com.cplatform.xhxw.ui.ui.main.cms.MyMsgActivity;
import com.cplatform.xhxw.ui.ui.main.cms.game.GameDownloadCenterActivity;
import com.cplatform.xhxw.ui.ui.main.forelanguage.utils.LanguageUtil;
import com.cplatform.xhxw.ui.ui.main.saas.EnterpriseMainFragment;
import com.cplatform.xhxw.ui.ui.message.MessageActivity;
import com.cplatform.xhxw.ui.ui.search.SearchActivity;
import com.cplatform.xhxw.ui.ui.settings.BindPhoneActivity;
import com.cplatform.xhxw.ui.ui.settings.SettingsActivity;
import com.cplatform.xhxw.ui.ui.settings.UserInfoActivity;
import com.cplatform.xhxw.ui.ui.web.RedEnvelopesWebViewActivity;
import com.cplatform.xhxw.ui.util.Actions;
import com.cplatform.xhxw.ui.util.CommonUtils;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.wbtech.ums.UmsAgent;

/**
 * 角标布局 Created by cy-love on 13-12-26.
 */
public class FlagContent extends LinearLayout {

	private static final String TAG = "FlagContent";
	private Activity mActivity;
	@InjectView(R.id.iv_avatar)
	ImageView mAvatar;
	@InjectView(R.id.tv_nickname)
	TextView mNickName;
	@InjectView(R.id.iv_display_model)
	ImageView mDisModelIcon;
	@InjectView(R.id.tv_display_model)
	TextView mDisModelText;
	@InjectView(R.id.progressBar)
	CircleProgressBar progressBar;
	@InjectView(R.id.iv_msg_new)
	ImageView mMsgNew;
	@InjectView(R.id.ly_display_model)
	LinearLayout llDisplayModel;
	@InjectView(R.id.ly_downlist)
	LinearLayout llDownList;
	
	private OnFlagListener mLis;
	private float mMoveY = -1;
	private AsyncHttpResponseHandler mLoadAdHandler;

	private ImageView mRightArrowIv;
	private ImageView mNotifyMsgIcon;
	private LinearLayout mRegLoginLo;
	private TextView mRegBtn;
	private TextView mLoginBtn;
	private ImageView mSettingBtn;
	private Button flow_exchange_btn;

	private OnFlagAreaExpandListener mLisenter;

	public FlagContent(Context context) {
		super(context);
		this.init(context);
	}

	public FlagContent(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init(context);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public FlagContent(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.init(context);
	}

	public void setOnFlagListener(OnFlagListener listener) {
		mLis = listener;
	}

	public void setActivity(Activity activity) {
		mActivity = activity;
	}

	private void init(Context context) {
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_flag_content, this);
		ButterKnife.inject(this);
		mRightArrowIv = (ImageView) findViewById(R.id.flag_account_arrow_right);
		mNotifyMsgIcon = (ImageView) findViewById(R.id.flag_newmsg_icon);
		mRegLoginLo = (LinearLayout) findViewById(R.id.flag_reg_login_lo);
		mRegBtn = (TextView) findViewById(R.id.flag_reg_tv);
		mLoginBtn = (TextView) findViewById(R.id.flag_login_tv);
		mSettingBtn = (ImageView) findViewById(R.id.flag_settings_btn);
		flow_exchange_btn = (Button) findViewById(R.id.flow_exchange_btn);
		mRightArrowIv.setOnClickListener(mOnClickListener);
		mNotifyMsgIcon.setOnClickListener(mOnClickListener);
		mRegBtn.setOnClickListener(mOnClickListener);
		mLoginBtn.setOnClickListener(mOnClickListener);
		mSettingBtn.setOnClickListener(mOnClickListener);
		mAvatar.setOnClickListener(mOnClickListener);
		mNickName.setOnClickListener(mOnClickListener);
		flow_exchange_btn.setOnClickListener(mOnClickListener);
		setDisModel();
	}

	OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.iv_avatar:
				UmsAgent.onEvent(mActivity, StatisticalKey.menu_head);
				flagContentRelative();
				break;
			case R.id.tv_nickname:
			case R.id.flag_account_arrow_right:
				if (Constants.hasLogin()) {
					mActivity.startActivity(UserInfoActivity
							.getIntent(mActivity));
				}
				break;
			case R.id.flag_newmsg_icon:
				UmsAgent.onEvent(mActivity, StatisticalKey.menu_sysmsg);
				Intent it = new Intent();
				it.setClass(mActivity, MyMsgActivity.class);
				mActivity.startActivity(it);
				break;
			case R.id.flag_reg_tv:
				UmsAgent.onEvent(mActivity, StatisticalKey.menu_register);
				mActivity.startActivityForResult(
						RegisterCheckActivity.getIntent(mActivity), 1);
				break;
			case R.id.flag_login_tv:
				UmsAgent.onEvent(mActivity, StatisticalKey.menu_login);
				mActivity.startActivityForResult(
						LoginActivity.getIntent(mActivity),
						HomeActivity.REQUEST_CODE_LOGIN);
				break;
			case R.id.flag_settings_btn:
				MobclickAgent
						.onEvent(getContext(), StatisticalKey.menu_setting);
				UmsAgent.onEvent(getContext(), StatisticalKey.menu_setting);
				mActivity.startActivityForResult(
						SettingsActivity.getIntent(mActivity),
						EnterpriseMainFragment.REQUEST_CODE_SETTING);
				break;
			case R.id.flow_exchange_btn:
				if (!Constants.hasLogin()) {
					showLoginAlert();
				} else {
					// 已登录
					if (Constants.getBindmobile() == null
							|| Constants.getBindmobile().equals("")) {
						// 如果绑定手机号为空,弹出绑定提示对话框
						showBindAlert();
					} else {
						// 跳转到兑换流量网页
						mActivity
								.startActivity(RedEnvelopesWebViewActivity
										.getIntent(mActivity,
												HttpClientConfig.EXCHANGE));
					}
				}
				break;

			default:
				break;
			}
		}
	};

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP:
			// 滑动超过20像素，关闭角标布局
			if ((mMoveY - event.getY()) > 20 && mLis != null) {
				mLis.onFlagClose();
			}
			mMoveY = -1;
			break;
		default: {
			if (mMoveY == -1) {
				mMoveY = event.getY();
			}
		}
			break;
		}
		return true;
	}
	@OnClick(R.id.ly_downlist)
	public void openGameDownList(){//下载列表
		Intent intent = new Intent(getContext(), GameDownloadCenterActivity.class);
		mActivity.startActivity(intent);
	}
	@OnClick(R.id.ly_display_model)
	public void chengeDisplayModelAction() {
		MobclickAgent.onEvent(getContext(), StatisticalKey.menu_daynight);

		int model = App.getDispalyModel();
		String type = "";
		if (model == Constants.DISPLAY_MODEL_DAY) {
			model = Constants.DISPLAY_MODEL_NIGHT;
			type = "night";
		} else {
			model = Constants.DISPLAY_MODEL_DAY;
			type = "day";
		}
		UmsAgent.onEvent(getContext(), StatisticalKey.menu_daynight,
				new String[] { StatisticalKey.key_type }, new String[] { type });
		App.getPreferenceManager().setDisplayModel(model);
		LocalBroadcastManager.getInstance(getContext()).sendBroadcast(
				new Intent(Actions.ACTION_DISPLAY_MODEL_CHANGE));
		setDisModel();
	}

	/**
	 * 跳转到登陆界面
	 */
	public void flagContentRelative() {
		if (!Constants.hasLogin()) {
			mActivity.startActivityForResult(
					LoginActivity.getIntent(mActivity),
					HomeActivity.REQUEST_CODE_LOGIN);
		} else {
			mActivity.startActivity(UserInfoActivity.getIntent(mActivity));
		}
	}

	/**
	 * 消息跳转
	 */
	@OnClick(R.id.btn_flag_alert)
	public void flagAlertAction() {
		MobclickAgent.onEvent(getContext(), StatisticalKey.menu_message);
		UmsAgent.onEvent(getContext(), StatisticalKey.menu_message);
		App.getPreferenceManager().setMessageNewCount(0); // 清空消息数量
		mActivity.startActivity(MessageActivity.getIntent(mActivity));
	}

	/**
	 * 评论跳转
	 */
	@OnClick(R.id.btn_flag_comment)
	public void flagCommentAction() {
		MobclickAgent.onEvent(getContext(), StatisticalKey.menu_comment);
		UmsAgent.onEvent(getContext(), StatisticalKey.menu_comment);
		mActivity.startActivity(MyCommentActivity.getIntent(mActivity));
	}

	/**
	 * 搜索跳转
	 */
	@OnClick(R.id.btn_flag_search)
	public void flagSearchAction() {
		MobclickAgent.onEvent(getContext(), StatisticalKey.menu_search);
		UmsAgent.onEvent(getContext(), StatisticalKey.menu_search);
		mActivity.startActivity(SearchActivity.getIntent(mActivity));
	}

	/**
	 * 分享跳转
	 */
	@OnClick(R.id.btn_flag_invite)
	public void flagInviteAction() {
		onChangeNewsTextSizeAction();
		// MobclickAgent.onEvent(getContext(), StatisticalKey.menu_share);
		// String share = getContext().getString(R.string.share);
		// ShareUtil.sendTextIntent(mActivity, share, share, share,
		// getResources().getString(R.string.invite_content),
		// getResources().getString(R.string.invite_content_cick_url));
	}

	/**
	 * 收藏
	 */
	@OnClick(R.id.btn_collect)
	public void flagCollectAction() {
		MobclickAgent.onEvent(getContext(), StatisticalKey.menu_favorit);
		UmsAgent.onEvent(getContext(), StatisticalKey.menu_favorit);
		mActivity.startActivity(CollectActivity.getIntent(mActivity));
	}

	/**
	 * 设置
	 */
	@OnClick(R.id.btn_flag_setting)
	public void flagSettingAction() {
		UmsAgent.onEvent(mActivity, StatisticalKey.menu_feedback);
		mActivity.startActivity(FeedbackActivity.getIntent(mActivity));
	}

	/**
	 * 离线缓存
	 */
	@OnClick(R.id.btn_flag_offlinedownload)
	public void flagOfflinedownloadAction() {
		if (mLisenter != null) {
			mLisenter.onSwitchLanguage(LanguageUtil.LANGUAGE_EN);
		}
		// MobclickAgent.onEvent(getContext(), StatisticalKey.menu_offline);
		// if (!NewsCashService.isStart()) {
		// NetUtils.Status status = NetUtils.getNetworkState();
		// switch (status) {
		// case NONE:
		// // 无网络链接
		// NetUtils.setNetworkMethod(getContext());
		// break;
		// case MOBILE:
		// // 2g/3g网络
		// {
		// AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		// builder.setTitle(R.string.networking_tips)
		// .setMessage(R.string.phone_network_to_download_tips)
		// .setPositiveButton(R.string.download, new
		// DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// executeOfflineDownload();
		// }
		// }).setNegativeButton(R.string.cancel, new
		// DialogInterface.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// dialog.dismiss();
		// }
		// }).show();
		// }
		// break;
		// default:
		// executeOfflineDownload();
		// break;
		// }
		//
		// }
	}

	@OnClick(R.id.progressBar)
	public void flagOfflinedownloadStopAction() {
		if (NewsCashService.isStart()) {
			LocalBroadcastManager.getInstance(getContext()).sendBroadcast(
					new Intent(NewsCashService.ACTION_NEWS_CASH_STOP));
		}
	}

	/**
	 * 执行离线下载
	 */
	private void executeOfflineDownload() {
		Intent intent = new Intent(getContext(), StartServiceReceiver.class);
		intent.setAction(StartServiceReceiver.ACTION_START_NEWS_CASH_SERVICE);
		getContext().sendBroadcast(intent);
		getContext().startService(
				new Intent(getContext(), NewsCashService.class));
		findViewById(R.id.btn_flag_offlinedownload).setVisibility(
				View.INVISIBLE);
		progressBar.setProgress(0);
		progressBar.setVisibility(VISIBLE);
	}

	/**
	 * 显示未登录提示
	 */
	private void showLoginAlert() {
		new AlertDialog.Builder(getContext())
				.setTitle(R.string.not_login)
				.setMessage(R.string.news_collect_not_login_alert)
				.setPositiveButton(R.string.confirm,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								getContext().startActivity(
										LoginActivity.getIntent(getContext()));
							}
						}).setNegativeButton(R.string.cancel, null).show();
	}

	/**
	 * 显示未绑定手机号提示
	 */
	private void showBindAlert() {
		new AlertDialog.Builder(getContext())
				.setTitle(R.string.not_login)
				.setMessage(R.string.bind_phone_please)
				.setPositiveButton(R.string.bind_phone_go,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// 跳转到绑定手机界面
								getContext().startActivity(
										BindPhoneActivity
												.getIntent(getContext()));
							}
						}).setNegativeButton(R.string.cancel, null).show();
	}

	public OnFlagAreaExpandListener getmLisenter() {
		return mLisenter;
	}

	public void setmLisenter(OnFlagAreaExpandListener mLisenter) {
		this.mLisenter = mLisenter;
	}

	public void onResume() {
		if (Constants.hasLogin()) {
			ImageLoader.getInstance().displayImage(
					Constants.userInfo.getLogo(), mAvatar,
					DisplayImageOptionsUtil.avatarImagesOptions);
			mNickName.setText(Constants.userInfo.getNickName());
		} else {
			mAvatar.setImageResource(R.drawable.ic_avatar_bg);
			mNickName.setText("");
		}
		setViewDisplayByLogStatus();
		// 更改下载进度显示
		if (NewsCashService.isStart()) {
			changeOfflineDownloadSpeed(NewsCashService.getSpeed());
		}
		updateMessageNewStatus();
		if (App.getDispalyModel() == Constants.DISPLAY_MODEL_DAY) {
			mDisModelIcon.setImageResource(R.drawable.btn_flag_nightmode);
			mDisModelText.setText(R.string.night_model);
		} else {
			mDisModelIcon.setImageResource(R.drawable.btn_flag_moringmode);
			mDisModelText.setText(R.string.day_model);
		}
	}

	/**
	 * 更新新消息提示
	 */
	public void updateMessageNewStatus() {
		int msgVis = (App.getPreferenceManager().getMessageNewCount() == 0) ? View.GONE
				: View.VISIBLE;
		mMsgNew.setVisibility(msgVis);
	}

	/**
	 * 更改下载进度
	 */
	public void changeOfflineDownloadSpeed(int speed) {
		if (speed == 100) {
			findViewById(R.id.btn_flag_offlinedownload).setVisibility(
					View.VISIBLE);
			progressBar.setVisibility(GONE);
		} else {
			progressBar.setProgress(speed);
		}
	}

	private void setDisModel() {
		switch (App.getDispalyModel()) {
		case Constants.DISPLAY_MODEL_DAY:
			mDisModelIcon.setImageResource(R.drawable.btn_flag_nightmode);
			mDisModelText.setText(R.string.night_model);
			break;
		case Constants.DISPLAY_MODEL_NIGHT:
			mDisModelIcon.setImageResource(R.drawable.btn_flag_moringmode);
			mDisModelText.setText(R.string.day_model);
			break;
		}
	}

	/**
	 * 加载广告
	 */
	// private void loadAd() {
	// APIClient.adSuperscript(new AsyncHttpResponseHandler() {
	// @Override
	// protected void onPreExecute() {
	// if (mLoadAdHandler != null) {
	// mLoadAdHandler.cancle();
	// }
	// mLoadAdHandler = this;
	// }
	//
	// @Override
	// public void onSuccess(int statusCode, String content) {
	// AdSuperscriptResponse response;
	// try {
	// ResponseUtil.checkResponse(content);
	// response = new Gson().fromJson(content, AdSuperscriptResponse.class);
	// } catch (Exception e) {
	// LogUtil.e(TAG, e);
	// return;
	// }
	// if (response.isSuccess()) {
	// // mAdView.clearAnimation();
	// mAdView.removeAllViews();
	// List<Ad> list = response.getData();
	// if (!ListUtil.isEmpty(list)) {
	// FlagAdView view = new FlagAdView(getContext());
	// view.setAds(list.get(0));
	// mAdView.addView(view);
	// }
	// // mAdView.startFlipping();
	// }
	// }
	//
	// @Override
	// public void onFailure(Throwable error, String content) {
	// LogUtil.w(TAG, "角标获得广告内容::网络链接失败！");
	// }
	//
	// @Override
	// public void onFinish() {
	// mLoadAdHandler = null;
	// }
	// });
	// }

	@Override
	protected void onDetachedFromWindow() {
		if (mLoadAdHandler != null) {
			mLoadAdHandler.cancle();
			mLoadAdHandler = null;
		}
		ButterKnife.reset(this);
		detachAllViewsFromParent();
		removeAllViews();
		super.onDetachedFromWindow();
	}

	private void setViewDisplayByLogStatus() {
		if (Constants.hasLogin()) {
			mRegLoginLo.setVisibility(View.GONE);
			mRightArrowIv.setVisibility(View.VISIBLE);
		} else {
			mRegLoginLo.setVisibility(View.VISIBLE);
			mRightArrowIv.setVisibility(View.GONE);
		}
	}

	/**
	 * 设置详情页字体大小
	 */
	public void onChangeNewsTextSizeAction() {
		if (CommonUtils.isFastDoubleClick())
			return;
		new AlertDialog.Builder(mActivity)
				.setTitle(mActivity.getString(R.string.main_body_typeface))
				.setSingleChoiceItems(
						new String[] {
								mActivity
										.getString(R.string.super_big_text_size),
								mActivity.getString(R.string.big_text_size),
								mActivity.getString(R.string.middle_text_size),
								mActivity.getString(R.string.small_text_size) },
						Constants.getNewsDetTextSize(),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								int size;
								switch (which) {
								case 0:
									size = NewsDetTextSize.SUPER_BIG;
									break;
								case 1:
									size = NewsDetTextSize.BIG;
									break;
								case 2:
									size = NewsDetTextSize.MIDDLE;
									break;
								default:
									size = NewsDetTextSize.SMALL;
									break;
								}
								Constants.setNewsDetTextSize(size);
								UmsTextMode();
								dialog.dismiss();
							}
						})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								UmsTextMode();
							}
						}).setOnCancelListener(new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						UmsTextMode();
					}
				}).show();
	}

	public void UmsTextMode() {
		UmsAgent.onEvent(getContext(), StatisticalKey.menu_textmode,
				new String[] { StatisticalKey.key_type },
				new String[] { Constants.getNewsDetTextSize() + "" });
	}
	public void isShowDownList(boolean isShow){
		if(isShow){
			llDisplayModel.setVisibility(View.GONE);
			llDownList.setVisibility(View.VISIBLE);
		}else{
			llDisplayModel.setVisibility(View.VISIBLE);
			llDownList.setVisibility(View.GONE);
		}
		
	}
	/**
	 * 获取未读系统消息数目
	 */
	public void getNewMsgCount() {
		APIClient.getIsHaveNewMsg(new BaseRequest(),
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, String content) {
						NewMsgAlertResponse response = null;
						try {
							ResponseUtil.checkResponse(content);
							response = new Gson().fromJson(content,
									NewMsgAlertResponse.class);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (response == null)
							return;
						if (response.isSuccess()) {
							UserNewMsgAlert data = response.getData();
							if (data.getNewMsgNum() > 0) {
								mNotifyMsgIcon.setImageResource(R.drawable.icon_sys_msg_have);
							} else {
								mNotifyMsgIcon.setImageResource(R.drawable.icon_sys_msg);
							}
						}
					}
				});
	}

}
