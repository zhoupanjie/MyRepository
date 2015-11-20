package com.cplatform.xhxw.ui.ui.detailpage;

import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.PreferencesManager;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.StatisticalKey;
import com.cplatform.xhxw.ui.db.CollectDB;
import com.cplatform.xhxw.ui.db.NewsDetailCashDB;
import com.cplatform.xhxw.ui.db.ReadNewsDB;
import com.cplatform.xhxw.ui.db.dao.CollectDao;
import com.cplatform.xhxw.ui.db.dao.CollectFlag;
import com.cplatform.xhxw.ui.db.dao.NewsDetailCashDao;
import com.cplatform.xhxw.ui.db.dao.ReadNewsDao;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.HttpClientConfig;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.BaseResponse;
import com.cplatform.xhxw.ui.http.net.MsgDetailRequest;
import com.cplatform.xhxw.ui.http.net.NewsDetailRequest;
import com.cplatform.xhxw.ui.http.net.NewsDetailResponse;
import com.cplatform.xhxw.ui.http.net.SendCommentOrReplyPersonRequest;
import com.cplatform.xhxw.ui.http.net.saas.SaasNewsSignRequest;
import com.cplatform.xhxw.ui.http.responseType.NewsDetTextSize;
import com.cplatform.xhxw.ui.model.NewsDetail;
import com.cplatform.xhxw.ui.ui.base.BaseFragment;
import com.cplatform.xhxw.ui.ui.base.view.BottomMediaplayer;
import com.cplatform.xhxw.ui.ui.base.view.OnMoreListener;
import com.cplatform.xhxw.ui.ui.base.view.ShareActionSheet;
import com.cplatform.xhxw.ui.ui.base.view.ShareActionSheetEn;
import com.cplatform.xhxw.ui.ui.base.widget.DefaultView;
import com.cplatform.xhxw.ui.ui.base.widget.DefaultView.OnTapListener;
import com.cplatform.xhxw.ui.ui.base.widget.NeteaseWebView;
import com.cplatform.xhxw.ui.ui.cyancomment.CYanUtil;
import com.cplatform.xhxw.ui.ui.detailpage.JSInterface.onJsOperListener;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionManager;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionUtil;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionWidgt;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionWidgt.onExprItemClickListener;
import com.cplatform.xhxw.ui.ui.main.cms.SystemMsgShowFragment;
import com.cplatform.xhxw.ui.ui.main.forelanguage.utils.LanguageUtil;
import com.cplatform.xhxw.ui.util.AppBrightnessManager;
import com.cplatform.xhxw.ui.util.DateUtil;
import com.cplatform.xhxw.ui.util.ListUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.cplatform.xhxw.ui.util.NetUtils;
import com.cplatform.xhxw.ui.util.ShareUtil;
import com.cplatform.xhxw.ui.util.StringUtil;
import com.cplatform.xhxw.ui.util.WebSettingUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.sohu.cyan.android.sdk.api.CallBack;
import com.sohu.cyan.android.sdk.api.CyanSdk;
import com.sohu.cyan.android.sdk.entity.AccountInfo;
import com.sohu.cyan.android.sdk.exception.CyanException;
import com.umeng.analytics.MobclickAgent;
import com.wbtech.ums.UmsAgent;

/**
 * 新闻详情 Created by cy-love on 13-12-29.
 */
public class NewsPageFragment extends BaseFragment implements OnTapListener,
		onExprItemClickListener, OnMoreListener, onJsOperListener {

	private static final String TAG = NewsPageFragment.class.getSimpleName();
	private static final int SIGN_STATUS_SIGNED = 1;
	private static final int SIGN_STATUS_UNSIGNED = 0;
	
	private static final int MSG_WEBVIEW_SHARE = 1;

	private String mHtml; // 新闻内容模板
	private String mNewsId; // 新闻id
	@InjectView(R.id.webview)
	NeteaseWebView mWebView;
	@InjectView(R.id.def_view)
	DefaultView mDefView;
	private AsyncHttpResponseHandler mLoadHander;
	private NewsDetail mNewsDetail;
	@InjectView(R.id.bottommediaplayer)
	BottomMediaplayer bottomMediaplayer;

	@InjectView(R.id.btn_share)
	ImageView mShare;

	private boolean isEnterpriseFromBundle = false;
	private JSInterface mJsInterface;

	private EditText mCommentEditText;
	private Button mExprShowHideBtn;
	private Button mSendOrShowCommentsBtn;
	private XWExpressionWidgt mExprWidgt;
	private RelativeLayout mEditLo;
	private Button mSignBtn;

	private RelativeLayout mCommentAreaLo;

	private boolean mIsExprShow = false;
	private boolean mIsSoftKeyboardShow = false;
	private InputMethodManager mImm;
	private Handler mUiHandler = new Handler();
	private int mSoftKeyboardHeight = 0;
	private int mOriginalHeight = 0;
	private int mRootContainerBottom = 0;
	private boolean isExprAreaResized = false;
	private boolean isSysMsgDetail = false;

	private ProgressDialog mProgressDia;
	private boolean isPush = false;
	private boolean isCollect;

	private CyanSdk mCyanSdk;
	private View mFragRootView;
	
	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == MSG_WEBVIEW_SHARE){
				int shareType = msg.getData().getInt("share_type");
				String picUrl = null;
				if (mNewsDetail.getPics() != null && mNewsDetail.getPics().size() > 0) {
					picUrl = mNewsDetail.getPics().get(0).getUrl();
				}
				String clickUrl = String.format(getString(R.string.share_news_click_url), 
						HttpClientConfig.BASE_URL, mNewsDetail.getId());
				ShareActionSheet.doDirectlyShare(mAct, shareType, 
						mNewsDetail.getSummary(), clickUrl, 
						mNewsDetail.getTitle(), picUrl);
			}
			super.handleMessage(msg);
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_news_page,
				container, false);
		ButterKnife.inject(this, rootView);
		this.initActionBar(rootView);
		this.initViews(rootView);
		isEnterpriseFromBundle = getArguments().getBoolean("isEnterprise");
		this.initHtml(rootView);
		mDefView.setHidenOtherView(mWebView);
		mDefView.setListener(this);
		mFragRootView = rootView;
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mDefView.setStatus(DefaultView.Status.loading);
		mNewsId = getArguments().getString("newsid");
		isPush = getArguments().getBoolean("isPush", false);
		if (mNewsId.startsWith(SystemMsgShowFragment.SYS_MSG_FLAG)) {
			isSysMsgDetail = true;
			mNewsId = mNewsId.substring(SystemMsgShowFragment.SYS_MSG_FLAG
					.length());
		}
		initCyanSdk();
		if (NetUtils.getNetworkState() == NetUtils.Status.NONE) {
			loadCashDate();
		} else {
			loadNetData();
		}
	}

	/**
	 * 加载缓存数据
	 */
	private void loadCashDate() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				final NewsDetailCashDao dao = NewsDetailCashDB
						.getNewsCashByColumnId(mAct, mNewsId);
				if (isAdded()) {
					mAct.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (dao != null
									&& !TextUtils.isEmpty(dao.getJson())) {
								updateDateUI(dao.getJson());
							} else {
								loadNetData();
							}
						}
					});
				}
			}
		}).start();

	}

	private void initHtml(View rootView) {
		mJsInterface = new JSInterface(mAct, getActivity(), isEnterpriseFromBundle);
		WebSettingUtil.init(mWebView, new Client(), mJsInterface, "news", true);
		this.mHtml = Constants.getHtmlPath();
		mWebView.setBackgroundColor(getResCoclor(R.color.base_main_bg_color));
		rootView.setBackgroundColor(getResCoclor(R.color.base_main_bg_color));
		CollectDao isCon = CollectDB.getCollectByNewsId(mAct, mNewsId);
		if (isCon == null) {
			isCollect = false;
		} else {
			isCollect = true;
		}
	}

	private void initViews(final View rootView) {
		mNewsId = getArguments().getString("newsid");
		if (mNewsId.startsWith(SystemMsgShowFragment.SYS_MSG_FLAG)) {
			isSysMsgDetail = true;
			mNewsId = mNewsId.substring(SystemMsgShowFragment.SYS_MSG_FLAG
					.length());
		}

		mImm = (InputMethodManager) getActivity().getSystemService(
				Context.INPUT_METHOD_SERVICE);
		mCommentEditText = (EditText) rootView
				.findViewById(R.id.detail_comment_editt);
		mCommentEditText.setHint(mAct.getResources().getString(
				R.string.edit_hint));
		mCommentEditText.addTextChangedListener(mTextWatcher);
		mExprWidgt = (XWExpressionWidgt) rootView
				.findViewById(R.id.detail_comment_expression_widgt);
		mExprWidgt.setmOnExprItemClickListener(this);
		mSendOrShowCommentsBtn = (Button) rootView
				.findViewById(R.id.detail_comment_sendbtn);
		mSendOrShowCommentsBtn.setText(R.string.comment_title);
		mExprShowHideBtn = (Button) rootView
				.findViewById(R.id.detail_comment_expression_btn);
		mEditLo = (RelativeLayout) rootView
				.findViewById(R.id.detail_comment_editt_linear);
		mSignBtn = (Button) rootView.findViewById(R.id.detail_sign_btn);
		mCommentAreaLo = (RelativeLayout) rootView
				.findViewById(R.id.detail_expr_area);

		mCommentEditText.setOnClickListener(mOnClickListener);
		mSendOrShowCommentsBtn.setOnClickListener(mOnClickListener);
		mExprShowHideBtn.setOnClickListener(mOnClickListener);
		mSignBtn.setOnClickListener(mOnClickListener);

		changeTextSize(Constants.getNewsDetTextSize());

		mSoftKeyboardHeight = PreferencesManager.getSoftKeyboardHeight(mAct);
		if (mSoftKeyboardHeight <= 0) {
			mSoftKeyboardHeight = 300;
		}
		resizeExprArea(Constants.screenWidth, mSoftKeyboardHeight);
		rootView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						Rect r = new Rect();
						rootView.getWindowVisibleDisplayFrame(r);
						int screenHeight = rootView.getHeight();
						final int screenWidth = rootView.getWidth();
						mRootContainerBottom = screenHeight;
						if (mOriginalHeight == 0) {
							mOriginalHeight = screenHeight;
						} else {
							if (screenHeight != mOriginalHeight
									&& !isExprAreaResized) {
								mSoftKeyboardHeight = Math.abs(screenHeight
										- mOriginalHeight);
								PreferencesManager.saveSoftKeyboardHeight(mAct,
										mSoftKeyboardHeight);
								isExprAreaResized = true;
								mUiHandler.post(new Runnable() {
									@Override
									public void run() {
										resizeExprArea(screenWidth,
												mSoftKeyboardHeight);
									}
								});
							}
						}
					}
				});
	}

	/**
	 * 初始化表情控件尺寸
	 * 
	 * @param viewWidth
	 */
	private void resizeExprArea(int viewWidth, int viewHeight) {
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, viewHeight);
		rlp.addRule(RelativeLayout.BELOW, R.id.detail_comment_editt_linear);
		mExprWidgt.setLayoutParams(rlp);
		mExprWidgt.setKeyboardSize(viewWidth, viewHeight);
	}

	/**
	 * 评论输入内容监控
	 */
	TextWatcher mTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (s.toString().trim().length() > 0) {
				mSendOrShowCommentsBtn.setText(R.string.comment_send_hint);
			} else {
				mSendOrShowCommentsBtn.setText(R.string.comment_title);
			}
		}
	};

	OnClickListener mOnClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.detail_comment_editt) {
				// 评论编辑框点击
				onEditTextClick();
			} else if (v.getId() == R.id.detail_comment_sendbtn) {
				// 评论提交或者进入评论页
				onClickCommentBtn();
			} else if (v.getId() == R.id.detail_comment_expression_btn) {
				// 表情区域收起显示
				onClickExprBtn();
			} else if (v.getId() == R.id.text_size_big) {
				// 选择大文字
				changeTextSize(NewsDetTextSize.BIG);
			} else if (v.getId() == R.id.text_size_middle) {
				// 选择中文字
				changeTextSize(NewsDetTextSize.MIDDLE);
			} else if (v.getId() == R.id.text_size_small) {
				// 选择小文字
				changeTextSize(NewsDetTextSize.SMALL);
			} else if (v.getId() == R.id.detail_more_fun_night_model_iv) {
				// 日间、夜间模式切换
				changeDisplayModel();
			} else if (v.getId() == R.id.detail_sign_btn) {
				// 圈阅新闻
				showSignNewsAlert();
			}
		}
	};

	/**
	 * 控制表情区和软键盘的显示
	 * 
	 * @param isShowKeyboard
	 * @param isShowExpr
	 */
	public void controlKeyboardOrExpr(boolean isShowKeyboard, boolean isSwitch) {
		if (isSwitch) {
			if (!isShowKeyboard) {
				mIsExprShow = true;
				mIsSoftKeyboardShow = false;
				mImm.hideSoftInputFromWindow(mCommentEditText.getWindowToken(),
						0);
				mUiHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						mExprWidgt.setVisibility(View.VISIBLE);
					}
				}, 100);
				mExprShowHideBtn
						.setBackgroundResource(R.drawable.selector_keyboard);
			} else {
				mIsExprShow = false;
				mIsSoftKeyboardShow = true;
				mExprWidgt.setVisibility(View.GONE);
				mExprShowHideBtn
						.setBackgroundResource(R.drawable.selector_expressions);
				mImm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		} else {
			if (isShowKeyboard) {
				mIsExprShow = false;
				mIsSoftKeyboardShow = true;
				mExprWidgt.setVisibility(View.GONE);
				mImm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			} else {
				if (mIsSoftKeyboardShow) {
					mIsSoftKeyboardShow = false;
					mImm.hideSoftInputFromWindow(
							mCommentEditText.getWindowToken(), 0);
				} else {
					mIsExprShow = false;
					mExprWidgt.setVisibility(View.GONE);
				}
			}
			mExprShowHideBtn
					.setBackgroundResource(R.drawable.selector_expressions);
		}
	}

	/**
	 * 处理非软键盘区域点击事件
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean onTouchDiapatch(float x, float y) {
		if (y > Constants.screenHeight - mSoftKeyboardHeight
				- mEditLo.getHeight()) {
			return false;
		}
		if (mAct.getCurrentFocus() != null) {
			if (mAct.getCurrentFocus().getWindowToken() != null) {
				mImm.hideSoftInputFromWindow(mAct.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				mCommentEditText.clearFocus();
			}
		}

		if (mIsExprShow || mIsSoftKeyboardShow) {
			int rootTop = 0;
			if (mIsExprShow || mIsSoftKeyboardShow) {
				rootTop = mRootContainerBottom - mSoftKeyboardHeight
						- mEditLo.getHeight();
			} else {
				rootTop = mRootContainerBottom - mEditLo.getHeight();
			}
			if (y < rootTop) {
				controlKeyboardOrExpr(false, false);
				return true;
			}
		}
		return false;
	}

	/**
	 * 按返回键时表情区域隐藏处理
	 * 
	 * @return
	 */
	public boolean onBackKeyUp() {
		if (mIsExprShow || mIsSoftKeyboardShow) {
			controlKeyboardOrExpr(false, false);
			return true;
		}
		return false;
	}

	private class Client extends WebChromeClient {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			super.onProgressChanged(view, newProgress);
			if (newProgress == 100 && mDefView != null) {
				mDefView.setStatus(DefaultView.Status.showData);
				view.getSettings().setBlockNetworkImage(false);
			}
		}
	}

	/**
	 * 再次加载数据（联网失败情况下）
	 */
	@Override
	public void onTapAction() {
		loadNetData();
	}

	@Override
	public void onDestroyView() {
		if (mLoadHander != null) {
			mLoadHander.cancle();
			mLoadHander = null;
		}
		// 内存泄露
		mWebView.destroy();
		bottomMediaplayer.destroy();
		ButterKnife.reset(this);
		super.onDestroyView();

	}

	/**
	 * 新闻分享事件
	 */
	@OnClick(R.id.btn_share)
	public void onShareAction() {
		if (NetUtils.getNetworkState() == NetUtils.Status.NONE) {
			showToast(R.string.network_invalid);
			return;
		}
		MobclickAgent.onEvent(getActivity(), StatisticalKey.detail_share);
		UmsAgent.onEvent(getActivity(), StatisticalKey.detail_share,
				new String[] { StatisticalKey.key_channelid,
						StatisticalKey.key_newsid }, new String[] {
						App.channel_id, mNewsId });
		String content = mNewsDetail.getSummary();
		String clickUrl = String.format(
				getString(R.string.share_news_click_url),
				HttpClientConfig.BASE_URL, mNewsId);
		if (isEnterpriseFromBundle) {
			clickUrl = HttpClientConfig.SAAS_SHARE_URL + mNewsId;
		}
		ShareUtil.isShow = false;
		String picUrl = null;
		if (mNewsDetail.getPics() != null && mNewsDetail.getPics().size() > 0) {
			picUrl = mNewsDetail.getPics().get(0).getUrl();
		}
		if (isFoureignLanguage()) {// 英文分享
			ShareActionSheetEn window = ShareUtil.sendTextIntentEn(mAct, this,
					getString(R.string.share_news),
					getString(R.string.share_news), mNewsDetail.getTitle(),
					content, clickUrl, true, true, true, picUrl, true);
			// window.changeCollectType(isCollect);
			window.showAsDropDown(mShare);
		} else {// 中文分享
			ShareActionSheet window = ShareUtil.sendTextIntent(mAct, this,
					getString(R.string.share_news),
					getString(R.string.share_news), mNewsDetail.getTitle(),
					content, clickUrl, true, true, true, picUrl, true);
			window.changeCollectType(isCollect);
			window.showAsDropDown(mShare);
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
		mAct.setResult(Activity.RESULT_OK, data);
	}

	/**
	 * 加载网络数据
	 */
	private void loadNetData() {
		LogUtil.d(TAG, "新闻详情请求参数：newsid=" + mNewsId);
		if (isSysMsgDetail) {
			MsgDetailRequest mdr = new MsgDetailRequest();
			mdr.setMsgid(mNewsId);
			if (isEnterpriseFromBundle) {
				mdr.setSaasRequest(true);
			}
			APIClient.getSysMsgDetail(mdr, asyncRepHanlder);
		} else {
			NewsDetailRequest ndr = new NewsDetailRequest(mNewsId);
			if (isEnterpriseFromBundle) {
				ndr.setSaasRequest(true);
			}
			if (isPush) {
				ndr.setFrom("push");
			}
			APIClient.newsDetail(ndr, asyncRepHanlder);
		}
	}

	AsyncHttpResponseHandler asyncRepHanlder = new AsyncHttpResponseHandler() {

		@Override
		public void onFinish() {
			mLoadHander = null;
		}

		@Override
		protected void onPreExecute() {
			if (mLoadHander != null)
				mLoadHander.cancle();
			mLoadHander = this;
		}

		@Override
		public void onSuccess(String content) {
			updateDateUI(content);
		}

		@Override
		public void onFailure(Throwable error, String content) {
			mDefView.setStatus(DefaultView.Status.error);
		}
	};

	/**
	 * 更新UI数据
	 */
	private void updateDateUI(String content) {
		// LogUtil.d(TAG, "新闻详情获取json::"+content);
		NewsDetailResponse response;
		try {
			ResponseUtil.checkResponse(content);
			response = new Gson().fromJson(content, NewsDetailResponse.class);
		} catch (Exception e) {
			LogUtil.w(TAG, e);
			mDefView.setStatus(DefaultView.Status.error);
			return;
		}
		if (response.isSuccess()) {
			ReadNewsDB.saveNews(mAct,
					new ReadNewsDao(mNewsId, DateUtil.getTime()));
			mNewsDetail = response.getData();
			mJsInterface.setmNewsDetail(mNewsDetail);
			mJsInterface.setJsOperListener(this);
			mWebView.loadUrl(mHtml);

			if (!ListUtil.isEmpty(mNewsDetail.getAudios())) {
				bottomMediaplayer.setVisibility(View.VISIBLE);
				bottomMediaplayer.setMediaUrl(mNewsDetail.getAudios().get(0)
						.getUrl());
			}

			setBtnStatus(mNewsDetail);
		} else {
			if (response.getCode() == -2) {
				Toast.makeText(getActivity(), response.getMsg(),
						Toast.LENGTH_LONG).show();
			}
			mDefView.setStatus(DefaultView.Status.error);
		}

		checkCyanAccountAndShowFloatBar();
	}

	private void setBtnStatus(NewsDetail news) {
		mCommentAreaLo.setVisibility(View.GONE);
		mShare.setVisibility(View.VISIBLE);
		mSignBtn.setVisibility(View.GONE);

		if (isEnterpriseFromBundle) {
			mShare.setVisibility(View.GONE);
			mSignBtn.setVisibility(View.VISIBLE);
			if (mNewsDetail.getSignStatus() == SIGN_STATUS_SIGNED) {
				mSignBtn.setBackgroundResource(R.drawable.saas_detail_signed_bg);
				if (mNewsDetail.getSignAccount() != null
						&& !mNewsDetail.getSignAccount().equals(
								Constants.userInfo.getUserId())) {
					mSignBtn.setEnabled(false);
				}
			} else {
				mSignBtn.setBackgroundResource(R.drawable.saas_sign_selector);
				mSignBtn.setEnabled(true);
			}
		}

		if (isSysMsgDetail) {
			mCommentAreaLo.setVisibility(View.GONE);
			mShare.setVisibility(View.GONE);
		}

		// 不可评论，将评论按钮隐藏
		if ("1".equals(mNewsDetail.getIscomment())) {
			mCommentAreaLo.setVisibility(View.GONE);
		}

		hideBtnsForForeignLanguage();
	}

	private void hideBtnsForForeignLanguage() {
		if (!PreferencesManager.getLanguageInfo(mAct).equals(
				LanguageUtil.LANGUAGE_CH)
				&& !isPush) {
			mCommentAreaLo.setVisibility(View.GONE);
			mShare.setVisibility(View.VISIBLE);
			mSignBtn.setVisibility(View.GONE);
		}
	}

	/**
	 * 
	 * @Name isFoureignLanguage
	 * @Description TODO 判断是否为英文版，是返回true
	 * @return
	 * @return boolean
	 * @Author
	 * @Date
	 * 
	 */
	private boolean isFoureignLanguage() {
		return !PreferencesManager.getLanguageInfo(mAct).equals(
				LanguageUtil.LANGUAGE_CH);
	}

	@Override
	public void onExprItemClick(String exprInfo, boolean isDel) {
		SpannableString ss;
		int textSize = (int) mCommentEditText.getTextSize();
		if (isDel) {
			HashMap<String, Integer> mExprFilenameIdData = XWExpressionManager
					.getInstance().getmExprInfoIdValuesCN(mAct);
			ss = XWExpressionUtil.generateSpanComment(mAct, XWExpressionUtil
					.deleteOneWord(mCommentEditText.getText().toString(),
							mExprFilenameIdData), textSize);
		} else {
			String content = mCommentEditText.getText() + exprInfo;
			ss = XWExpressionUtil.generateSpanComment(mAct, content, textSize);
		}
		mCommentEditText.setText(ss);
		mCommentEditText.setSelection(ss.length());
	}

	private void commitSignRequest(final boolean isToSign) {
		mProgressDia = ProgressDialog.show(mAct, null, "提交中，请稍候...");
		SaasNewsSignRequest request = new SaasNewsSignRequest();
		request.setSaasRequest(true);
		request.setNewsid(Integer.valueOf(mNewsId));
		request.setType(SIGN_STATUS_SIGNED);
		if (!isToSign) {
			request.setType(SIGN_STATUS_UNSIGNED);
		}
		APIClient.saasSignNews(request, new AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				mProgressDia.dismiss();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				BaseResponse response;
				try {
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content, BaseResponse.class);
				} catch (Exception e) {
					mAct.showToast(R.string.sign_news_fail);
					return;
				}
				if (response != null && response.isSuccess()) {
					mNewsDetail.setSignStatus(isToSign ? SIGN_STATUS_SIGNED
							: SIGN_STATUS_UNSIGNED);
					mNewsDetail.setSignAccount(isToSign ? Constants.userInfo
							.getUserId() : null);
					setBtnStatus(mNewsDetail);
					mAct.showToast(R.string.sign_news_success);
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				mAct.showToast(R.string.sign_news_fail);
			}
		});
	}

	@Override
	public void onResume() {
		if (CollectDB.getCollectByNewsId(mAct, mNewsId) == null) {
			setCollectResult(true);
			isCollect = false;
		} else {
			setCollectResult(false);
			isCollect = true;
		}
		super.onResume();
	}

	@Override
	public void onEditTextClick() {
		if (mIsExprShow) {
			mIsExprShow = false;
			mIsSoftKeyboardShow = true;
			mExprWidgt.setVisibility(View.GONE);
			mExprShowHideBtn
					.setBackgroundResource(R.drawable.selector_expressions);
		}
	}

	@Override
	public void onClickCommentBtn() {
		if (mNewsDetail == null) {
			mAct.showToast(R.string.comment_no_net);
			return;
		}

		String content = mCommentEditText.getText().toString().trim();
		if (content.length() > 0) {
			UmsAgent.onEvent(mAct, StatisticalKey.publish_comment,
					new String[] { StatisticalKey.key_channelid,
							StatisticalKey.key_newsid }, new String[] {
							App.channel_id, mNewsId });

			SendCommentOrReplyPersonRequest request = CommentUtil.getRequest(
					mAct, content, mNewsId, mAct.mLocationClient, null,
					isEnterpriseFromBundle);
			if (request == null) {
				return;
			}
			CommentUtil.sendCommentData(getActivity(), request,
					mCommentEditText, NewsPageFragment.this);
		} else {
			MobclickAgent.onEvent(getActivity(), StatisticalKey.detail_comment);
			UmsAgent.onEvent(getActivity(), StatisticalKey.detail_comment,
					new String[] { StatisticalKey.key_channelid,
							StatisticalKey.key_newsid }, new String[] {
							App.channel_id, App.news_id });
			startActivity(CommentActivity.getIntent(mAct,
					isEnterpriseFromBundle, mNewsDetail));
		}
	}

	@Override
	public void onClickExprBtn() {
		if (mIsExprShow) {
			controlKeyboardOrExpr(true, true);
		} else {
			controlKeyboardOrExpr(false, true);
			// if (PreferencesManager.getSoftKeyboardHeight(mAct) <= 0) {
			// controlKeyboardOrExpr(true, false);
			// } else {
			// controlKeyboardOrExpr(false, true);
			// }
		}
	}

	@Override
	public void changeTextSize(int textSize) {
		Constants.setNewsDetTextSize(textSize);
		mWebView.reload();
	}

	@Override
	public void changeDisplayModel() {
		int model = App.getDispalyModel();
		model = (model == Constants.DISPLAY_MODEL_DAY ? Constants.DISPLAY_MODEL_NIGHT
				: Constants.DISPLAY_MODEL_DAY);
		App.getPreferenceManager().setDisplayModel(model);
		AppBrightnessManager.setScreenBrightness(getActivity());
	}

	@Override
	public void showSignNewsAlert() {
		final boolean isToSign = mNewsDetail.getSignStatus() == SIGN_STATUS_UNSIGNED;
		String alertContent = getString(R.string.sign_news_verify);
		if (!isToSign) {
			alertContent = getString(R.string.unsign_news_verify);
		}
		new AlertDialog.Builder(mAct)
				.setMessage(alertContent)
				.setPositiveButton(R.string.confirm,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								commitSignRequest(isToSign);
								dialog.dismiss();
							}
						}).setNegativeButton(R.string.cancel, null).show();
	}

	@Override
	public void collect() {
		MobclickAgent.onEvent(getActivity(), StatisticalKey.detail_favorit);
		UmsAgent.onEvent(getActivity(), StatisticalKey.detail_favorit,
				new String[] { StatisticalKey.key_channelid,
						StatisticalKey.key_newsid }, new String[] {
						App.channel_id, mNewsId });
		CollectDao isCon = CollectDB.getCollectByNewsId(mAct, mNewsId);
		if (isCon == null) {

			setCollectResult(false);
			CollectDao item = new CollectDao();
			if (isEnterpriseFromBundle) {
				item.setFlag(CollectFlag.COLLECT_NEWS_TYPE_ENTERPRISE_NORM
						+ StringUtil.parseIntegerFromString(Constants
								.getEnterpriseId()));
			} else {
				item.setFlag(CollectFlag.COLLECT_NEWS_TYPE_NORMAL_NORM);
			}
			item.setNewsId(mNewsId);
			item.setOperatetime(DateUtil.getTime());
			item.setTitle(mNewsDetail.getTitle());
			CollectDB.saveNews(mAct, item);
			isCollect = true;
		} else {
			setCollectResult(true);
			CollectDB.delCollectByNewsId(mAct, isCon.getNewsId());
			isCollect = false;
		}
		// if (isFoureignLanguage()) {// 英文分享
		// ShareActionSheetEn.changeCollectType(isCollect);
		// } else {
		ShareActionSheet.changeCollectType(isCollect);
		// }
	}

	private void initCyanSdk() {
		mCyanSdk = CyanSdk.getInstance(mAct);
	}

	private void checkCyanAccountAndShowFloatBar() {
		if (isEnterpriseFromBundle) {
			mCommentAreaLo.setVisibility(View.VISIBLE);
			return;
		}
		try {
			CYanUtil.initCyan(mAct);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		try {
			final String title = mNewsDetail.getTitle();
			if (mCyanSdk.getAccountInfo() != null
					&& mCyanSdk.getAccountInfo().isv_refer_id != null) {
				mCyanSdk.logOut();
			}
			AccountInfo maccountInfo = new AccountInfo();
			maccountInfo.isv_refer_id = Constants.getUid();
			maccountInfo.nickname = CYanUtil.getCyanNickName();
			maccountInfo.img_url = (Constants.userInfo == null) ? null
					: Constants.userInfo.getLogo();
			mCyanSdk.setAccountInfo(maccountInfo, new CallBack() {

				@Override
				public void success() {
					if (!"1".equals(mNewsDetail.getIscomment())
							&& !isFoureignLanguage()) {
						mCyanSdk.addCommentToolbar((ViewGroup) mFragRootView,
								mNewsId, title, "");
						mCommentAreaLo.setVisibility(View.INVISIBLE);
					}
				}

				@Override
				public void error(CyanException arg0) {
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onVideoPlay(String url) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStartShare(int shareType) {
		Message msg = new Message();
		Bundle data = new Bundle();
		data.putInt("share_type", shareType);
		msg.setData(data);
		msg.what = MSG_WEBVIEW_SHARE;
		mHandler.sendMessage(msg);
	}
}
