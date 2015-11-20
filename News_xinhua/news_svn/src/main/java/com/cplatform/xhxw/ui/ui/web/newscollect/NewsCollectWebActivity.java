package com.cplatform.xhxw.ui.ui.web.newscollect;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Video.Thumbnails;
import android.provider.MediaStore.Video.VideoColumns;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.StatisticalKey;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.HttpClientConfig;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.NewsCollectUploadRequest;
import com.cplatform.xhxw.ui.http.net.NewsCollectUploadResponse;
import com.cplatform.xhxw.ui.plugin.gallery.PicShowActivity;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.view.ScrollListenWebView;
import com.cplatform.xhxw.ui.ui.base.view.ScrollListenWebView.ScrollInterface;
import com.cplatform.xhxw.ui.ui.base.view.ShareActionSheet;
import com.cplatform.xhxw.ui.ui.base.widget.ActionSheet;
import com.cplatform.xhxw.ui.ui.login.LoginActivity;
import com.cplatform.xhxw.ui.ui.web.newscollect.LevyJSObject.onLevyJsListener;
import com.cplatform.xhxw.ui.util.FileSizeUtil;
import com.cplatform.xhxw.ui.util.FileUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.cplatform.xhxw.ui.util.NetUtils;
import com.cplatform.xhxw.ui.util.ShareUtil;
import com.cplatform.xhxw.ui.util.WebSettingUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;
import com.wbtech.ums.UmsAgent;

public class NewsCollectWebActivity extends BaseActivity implements
		ScrollInterface, OnRefreshListener, onLevyJsListener {
	private static final String TAG = NewsCollectWebActivity.class.getName();

	private static final String TITLE = "title";
	private static final String LIVE = "live";
	private static final String NEWSID = "newsid";
	private static final String URL = "url";

	private static final int COLLECT_OPERATOR_TEXT = 1;
	private static final int COLLECT_OPERATOR_PHOTOS = 2;
	private static final int COLLECT_OPERATOR_TAKE_VIDIOS = 3;
	private static final int COLLECT_OPERATOR_LOCAL_VIDIOS = 4;

	private static final int MSG_JS_LEVY_INFO = 0;
	private static final int MSG_JS_LOAD_DONE = 1;
	private static final int MSG_UPLOAD_VIDEO_TIMEOUT = 2;

	private static final int MAX_FILE_SIZE_TO_UPLOAD = 15 * 1024 * 1024;

	private static final int UPLOAD_WAITING_TIME = 30 * 1000;

	private static final String HTML_OPER_TYPE_SUBMIT = "submit";
	private static final String HTML_OPER_TYPE_PICS = "pics";

	private SwipeRefreshLayout mSwipeRefreshLo;
	private ProgressBar mLoadMorePb;
	private ScrollListenWebView mWebView;
	private LinearLayout mEmptyView;
	private ProgressBar mLoadingProgress;
	private TextView mTitle;
	private boolean isLoadUrlSuccess = true;
	private ProgressDialog mUploadPd;
	private LinearLayout mOperateLo;
	private Button mAskBtn;
	private Button mSurveyBtn;
	private ImageView mShare;

	// 视频参数
	private String mVideoPath;
	private String mVideoName;
	private long mVideoSize;
	private int mVideoDuration;
	private String mVideoThumb;

	private String mRelativeString;
	private JSONObject mReplyDict;
	private int mLevyId = 0;
	private int mStrLen = 300;
	private String mSurveyUrl;
	private String mBtnTypes;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == MSG_JS_LEVY_INFO) {
				dealWithJsBtnInfo(msg.getData().get("button_info").toString());
			} else if (msg.what == MSG_JS_LOAD_DONE) {
				mSwipeRefreshLo.setRefreshing(false);
				mLoadMorePb.setVisibility(View.GONE);
			} else if (msg.what == MSG_UPLOAD_VIDEO_TIMEOUT) {
				if (mUploadPd != null && mUploadPd.isShowing()) {
					mUploadPd.dismiss();
				}
			}
			super.handleMessage(msg);
		}
	};

	public static Intent getCollectIntent(Context context, String newsid,
			String title, boolean isLive) {
		Intent intent = new Intent(context, NewsCollectWebActivity.class);
		intent.putExtra(NEWSID, newsid);
		intent.putExtra(TITLE, title);
		intent.putExtra(LIVE, isLive);
		return intent;
	}

	// public static Intent getCollectIntentByURL(Context context, String title,
	// String url, String newsid) {
	// Intent intent = new Intent(context, NewsCollectWebActivity.class);
	// intent.putExtra(NEWSID, newsid);
	// intent.putExtra(TITLE, title);
	// intent.putExtra(URL, url);
	// return intent;
	// }

	@Override
	protected String getScreenName() {
		return null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_collect);
		initActionBar();
		initViews();
		setViewStatus(true, false);
		initWebView();
	}

	private void initViews() {
		mTitle = (TextView) findViewById(R.id.news_collect_title);
		mTitle.setText(getMyTitle());
		mWebView = (ScrollListenWebView) findViewById(R.id.news_collect_wv);
		LevyJSObject jsObject = new LevyJSObject();
		jsObject.setmLis(this);
		WebSettingUtil.init(mWebView, null, jsObject, "myjs", false);
		mWebView.setOnCustomScroolChangeListener(this);
		mWebView.requestFocusFromTouch();
		mEmptyView = (LinearLayout) findViewById(R.id.news_collect_empty);
		mLoadingProgress = (ProgressBar) findViewById(R.id.news_collect_pb);
		mOperateLo = (LinearLayout) findViewById(R.id.news_collect_operate_lo);
		mOperateLo.setVisibility(View.GONE);
		mAskBtn = (Button) findViewById(R.id.news_collect_ask_btn);
		mSurveyBtn = (Button) findViewById(R.id.news_collect_survey_btn);
		mSwipeRefreshLo = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
		mSwipeRefreshLo.setOnRefreshListener(this);
		mSwipeRefreshLo.setColorScheme(R.color.webview_refresh_color1,
				R.color.webview_refresh_color2, R.color.webview_refresh_color3,
				R.color.webview_refresh_color4);
		mLoadMorePb = (ProgressBar) findViewById(R.id.news_collect_loadmore_pb);
		mShare = (ImageView) findViewById(R.id.news_collect_share);

		mShare.setOnClickListener(mOnClick);
		mAskBtn.setOnClickListener(mOnClick);
		mSurveyBtn.setOnClickListener(mOnClick);
		mEmptyView.setOnClickListener(mOnClick);
	}

	OnClickListener mOnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.news_collect_empty:
				setViewStatus(true, false);
				mWebView.loadUrl(getUrl());
				break;
			case R.id.news_collect_ask_btn:
				initPopUpActions();
				break;
			case R.id.news_collect_survey_btn:
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				Uri content_url = Uri.parse(mSurveyUrl);
				intent.setData(content_url);
				startActivity(intent);
				break;
			case R.id.news_collect_share:
				shareLiveNews();
				break;
			}
		}
	};

	@Override
	protected void onResume() {
		mWebView.onResume();
		super.onResume();
	}

	/**
	 * 分享征集
	 */
	private void shareLiveNews() {
		if (NetUtils.getNetworkState() == NetUtils.Status.NONE) {
			showToast(R.string.network_invalid);
			return;
		}
		MobclickAgent.onEvent(NewsCollectWebActivity.this,
				StatisticalKey.detail_share);

		UmsAgent.onEvent(NewsCollectWebActivity.this,
				StatisticalKey.detail_share,
				new String[] { StatisticalKey.key_channelid,
						StatisticalKey.key_newsid }, new String[] {
						App.channel_id, getNewsid() });

		String content = getMyTitle();
		String clickUrl = getUrl();
		ShareUtil.isShow = false;
		ShareActionSheet window = ShareUtil
				.sendTextIntent(NewsCollectWebActivity.this, null,
						getString(R.string.share_news),
						getString(R.string.share_news), getMyTitle(), content,
						clickUrl, false, true, false, null, true);
		window.showAsDropDown(mShare);
	}

	private void setViewStatus(boolean isToStartLoad, boolean isLoadSuccess) {
		if (isToStartLoad) {
			isLoadUrlSuccess = true;
			mLoadingProgress.setVisibility(View.VISIBLE);
		} else {
			mLoadingProgress.setVisibility(View.GONE);
			if (isLoadSuccess) {
				mEmptyView.setVisibility(View.GONE);
				mWebView.setVisibility(View.VISIBLE);
			} else {
				mEmptyView.setVisibility(View.VISIBLE);
				mWebView.setVisibility(View.GONE);
			}
		}
	}

	private void initWebView() {
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith("levy://")) {
					String relativeString = url.substring(7);
					try {
						mRelativeString = java.net.URLDecoder.decode(
								relativeString, "utf-8");
						mReplyDict = new JSONObject(mRelativeString);
						dealWithHtmlClick();
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} catch (JSONException e) {
						e.printStackTrace();
					}
					return true;
				}
				showLoadingView();
				view.loadUrl(url, HttpClientConfig.getHeader());
				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				hideLoadingView();
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				if (isLoadUrlSuccess) {
					mWebView.loadUrl("javascript:GetLevyInfo()");
					setViewStatus(false, true);
				}
				super.onPageFinished(view, url);
				hideLoadingView();
				mSwipeRefreshLo.setRefreshing(false);
				mLoadMorePb.setVisibility(View.GONE);
			}

			@Override
			public void doUpdateVisitedHistory(WebView view, String url,
					boolean isReload) {
			}

			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				handler.proceed();
				isLoadUrlSuccess = false;
				setViewStatus(false, false);
				Toast.makeText(NewsCollectWebActivity.this,
						R.string.load_server_failure, Toast.LENGTH_LONG).show();
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				isLoadUrlSuccess = false;
				setViewStatus(false, false);
				Toast.makeText(NewsCollectWebActivity.this,
						R.string.load_server_failure, Toast.LENGTH_LONG).show();
				super.onReceivedError(view, errorCode, description, failingUrl);
			}
		});

		mWebView.setWebChromeClient(new WebChromeClient() {

			@Override
			public void onShowCustomView(View view, CustomViewCallback callback) {
				super.onShowCustomView(view, callback);
			}
		});

		String url = getUrl();
		LogUtil.d(TAG, "help url = " + url);
		if (isLive()) {
			url = HttpClientConfig.BASE_URL + "/show/live?newsid="
					+ getNewsid();
			mWebView.loadUrl(url, HttpClientConfig.getHeader());
			return;
		}
		mWebView.loadUrl(url, HttpClientConfig.getHeader());
	}

	private String getUrl() {
		String url = getIntent().getStringExtra(URL);
		if (isLive()) {
			url = HttpClientConfig.BASE_URL + "/show/live?newsid="
					+ getNewsid();
		}
		return url;
	}

	private String getMyTitle() {
		return getIntent().getStringExtra(TITLE);
	}

	private boolean isLive() {
		return getIntent().getBooleanExtra(LIVE, false);
	}

	private String getNewsid() {
		return getIntent().getStringExtra(NEWSID);
	}

	/**
	 * 初始化操作类型弹出框
	 */
	private void initPopUpActions() {
		if (!Constants.hasLogin()) {
			showLoginAlert();
			return;
		}
		ActionSheet.Builder builder = new ActionSheet.Builder(this);
		
		if(mBtnTypes == null || mBtnTypes.indexOf("1,") >= 0) {
			builder.appendMenuItem(getString(R.string.news_collect_text), null,
					new ActionSheet.GPopupMenuListener() {
						@Override
						public void onMenuSelected(ActionSheet.MenuItem item) {
							onCollectOperate(COLLECT_OPERATOR_TEXT);
						}
					});
		}
		
		if(mBtnTypes == null || mBtnTypes.indexOf("2,") >= 0) {
			builder.appendMenuItem(getString(R.string.news_collect_photos), null,
					new ActionSheet.GPopupMenuListener() {
						@Override
						public void onMenuSelected(ActionSheet.MenuItem item) {
							onCollectOperate(COLLECT_OPERATOR_PHOTOS);
						}
					});
		}
		
		if(mBtnTypes == null || mBtnTypes.indexOf("4,") >= 0) {
			builder.appendMenuItem(getString(R.string.news_collect_take_vidios),
					null, new ActionSheet.GPopupMenuListener() {
						@Override
						public void onMenuSelected(ActionSheet.MenuItem item) {
							onCollectOperate(COLLECT_OPERATOR_TAKE_VIDIOS);
						}
					});
			builder.appendMenuItem(getString(R.string.news_collect_local_vidios),
					null, new ActionSheet.GPopupMenuListener() {
						@Override
						public void onMenuSelected(ActionSheet.MenuItem item) {
							onCollectOperate(COLLECT_OPERATOR_LOCAL_VIDIOS);
						}
					});
		}
		
		builder.show();
	}

	private void showLoginAlert() {
		new AlertDialog.Builder(this)
				.setTitle(R.string.not_login)
				.setMessage(R.string.news_collect_not_login_alert)
				.setPositiveButton(R.string.confirm,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								startActivity(LoginActivity
										.getIntent(NewsCollectWebActivity.this));
							}
						}).setNegativeButton(R.string.cancel, null).show();
	}

	private void onCollectOperate(int operatorType) {
		switch (operatorType) {
		case COLLECT_OPERATOR_TEXT:
			startActivity(TextOrPhotoNewsCollectActivity.getIntent(this,
					mLevyId, NewsCollectUploadRequest.COLLECT_TYPE_TEXT,
					mStrLen));
			break;
		case COLLECT_OPERATOR_PHOTOS:
			startActivity(TextOrPhotoNewsCollectActivity.getIntent(this,
					mLevyId, NewsCollectUploadRequest.COLLECT_TYPE_PHOTO,
					mStrLen));
			break;
		case COLLECT_OPERATOR_TAKE_VIDIOS:
			// Intent takeVideoIntent = new
			// Intent(MediaStore.ACTION_VIDEO_CAPTURE);
			// takeVideoIntent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
			// takeVideoIntent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 30 * 1024 *
			// 1024);
			// startActivityForResult(takeVideoIntent,
			// COLLECT_OPERATOR_TAKE_VIDIOS);

			Intent it = new Intent();
			it.setClass(NewsCollectWebActivity.this, VideoRecordActivity.class);
			startActivityForResult(it, COLLECT_OPERATOR_TAKE_VIDIOS);
			break;
		case COLLECT_OPERATOR_LOCAL_VIDIOS:
			Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("video/*"); // String VIDEO_UNSPECIFIED = "video/*";
			Intent wrapperIntent = Intent.createChooser(intent, null);
			startActivityForResult(wrapperIntent, COLLECT_OPERATOR_LOCAL_VIDIOS);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case COLLECT_OPERATOR_TAKE_VIDIOS:
			if (resultCode == RESULT_OK) {
				dealWithTookenVideo(data);
				Intent it = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
				it.setData(Uri.fromFile(new File(mVideoPath)));
				sendBroadcast(it);
			}
			break;
		case COLLECT_OPERATOR_LOCAL_VIDIOS:
			if (resultCode == RESULT_OK) {
				dealPickedVideoData(data.getData());
			}
			break;

		default:
			break;
		}
	}

	private void dealWithTookenVideo(Intent data) {
		mVideoPath = data.getStringExtra("file_path");
		mVideoDuration = data.getIntExtra("time", 0);
		mVideoName = data.getStringExtra("file_name");
		mVideoSize = new File(mVideoPath).length();
		Bitmap thumb = ThumbnailUtils.createVideoThumbnail(mVideoPath,
				Thumbnails.MINI_KIND);
		mVideoThumb = Constants.Directorys.TEMP + System.currentTimeMillis()
				+ ".png";
		FileUtil.saveBitmap(thumb, mVideoThumb);
		showVideoUploadAlert();
	}

	private void dealPickedVideoData(Uri uriVideo) {
		Cursor cursor = this.getContentResolver().query(
				uriVideo,
				new String[] { VideoColumns.DATA, VideoColumns.DISPLAY_NAME,
						VideoColumns.SIZE, VideoColumns.DURATION }, null, null,
				null);
		if (cursor != null && cursor.moveToNext()) {
			/* _data：文件的绝对路径 ，_display_name：文件名 */
			mVideoPath = cursor.getString(cursor
					.getColumnIndex(VideoColumns.DATA));
			mVideoName = cursor.getString(cursor
					.getColumnIndex(VideoColumns.DISPLAY_NAME));
			mVideoSize = cursor.getLong(cursor
					.getColumnIndex(VideoColumns.SIZE));
			mVideoDuration = cursor.getInt(cursor
					.getColumnIndex(VideoColumns.DURATION)) / 1000;
			Bitmap thumb = ThumbnailUtils.createVideoThumbnail(mVideoPath,
					Thumbnails.MINI_KIND);
			mVideoThumb = Constants.Directorys.TEMP
					+ System.currentTimeMillis() + ".png";
			FileUtil.saveBitmap(thumb, mVideoThumb);
			showVideoUploadAlert();
		} else {
			showToast(R.string.news_collect_video_read_false);
		}
	}

	private void showVideoUploadAlert() {
		if (isFileSizeTooLarge()) {
			showToast(R.string.video_size_too_large);
			return;
		}
		new AlertDialog.Builder(this)
				.setTitle(R.string.change_pw_commit)
				.setMessage(
						getString(R.string.news_collect_video_upload_alert,
								mVideoName,
								FileSizeUtil.formetFileSize(mVideoSize)))
				.setPositiveButton(R.string.confirm,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								submitVideoLevy();
							}
						}).setNegativeButton(R.string.cancel, null).show();
	}

	private void submitVideoLevy() {
		mUploadPd = ProgressDialog.show(this, null,
				getString(R.string.uploading));
		mHandler.sendEmptyMessageDelayed(MSG_UPLOAD_VIDEO_TIMEOUT,
				UPLOAD_WAITING_TIME);
		NewsCollectUploadRequest request = composeRequest();
		APIClient.submitCollectContent(request, new AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				mUploadPd.dismiss();
				FileUtil.deleteFile(mVideoThumb);
				super.onFinish();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				mUploadPd.dismiss();
				NewsCollectUploadResponse response = null;
				try {
					response = new Gson().fromJson(content,
							NewsCollectUploadResponse.class);
					ResponseUtil.checkObjResponse(response);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (response == null) {
					showToast(R.string.submit_fail);
					return;
				}
				if (response.isSuccess()) {
					showToast(R.string.sign_news_success);
				} else {
					showToast(response.getMsg());
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				mUploadPd.dismiss();
				super.onFailure(error, content);
				showToast(R.string.sign_news_fail);
			}
		});
	}

	private NewsCollectUploadRequest composeRequest() {
		NewsCollectUploadRequest request = new NewsCollectUploadRequest();
		request.setType(NewsCollectUploadRequest.COLLECT_TYPE_VIDIO);
		request.setListid(mLevyId);

		HashMap<String, File> videos = new HashMap<String, File>();
		videos.put(NewsCollectUploadRequest.COLLECT_FILENAME_VIDIO_AUDIO,
				new File(mVideoPath));
		request.setUpfile(videos);

		if (FileUtil.exists(mVideoThumb)) {
			HashMap<String, File> thumb = new HashMap<String, File>();
			videos.put(NewsCollectUploadRequest.COLLECT_FILENAME_THUMB,
					new File(mVideoThumb));
			request.setThumb(thumb);
		}

		request.setDuration(mVideoDuration);
		return request;
	}

	private void dealWithHtmlClick() {
		if (mReplyDict == null)
			return;
		try {
			if (mReplyDict.getString("type").equals(HTML_OPER_TYPE_SUBMIT)) {
				mLevyId = mReplyDict.getJSONObject("data").getInt("listid");
				mStrLen = mReplyDict.getJSONObject("data").getInt("strlen");
				initPopUpActions();
			} else if (mReplyDict.getString("type").equals(HTML_OPER_TYPE_PICS)) {
				JSONArray picList = mReplyDict.getJSONArray("list");
				List<String> picsUrls = new ArrayList<String>();
				for (int i = 0; i < picList.length(); i++) {
					picsUrls.add(picList.getJSONObject(i).getString("file"));
				}
				startActivity(PicShowActivity.newIntent(this, picsUrls,
						mReplyDict.getInt("index"), false));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private void dealWithJsBtnInfo(String info) {
		try {
			JSONObject btnInfo = new JSONObject(info);
			if (btnInfo.has("listid")) {
				mLevyId = btnInfo.getInt("listid");
			}
			if (btnInfo.has("strlen")) {
				mStrLen = btnInfo.getInt("strlen");
			}
			if (btnInfo.has("button")) {
				JSONArray btns = btnInfo.getJSONArray("button");
				if (btns.length() > 0) {
					mOperateLo.setVisibility(View.VISIBLE);
					mAskBtn.setVisibility(View.GONE);
					mSurveyBtn.setVisibility(View.GONE);
					for (int i = 0; i < btns.length(); i++) {
						JSONObject btn = btns.getJSONObject(i);
						if (btn.getInt("type") == 1) {
							mAskBtn.setVisibility(View.VISIBLE);
							mAskBtn.setText(btn.getString("name"));
						} else if (btn.getInt("type") == 2) {
							mSurveyBtn.setVisibility(View.VISIBLE);
							mSurveyBtn.setText(btn.getString("name"));
							mSurveyUrl = btn.getString("url");
						}
					}
				}
			}
			if(btnInfo.has("types")) {
				JSONArray btnTypes = btnInfo.getJSONArray("types");
				if(btnTypes != null && btnTypes.length() > 0) {
					StringBuilder sb = new StringBuilder();
					for(int i = 0; i < btnTypes.length() ; i++) {
						sb.append(btnTypes.getString(i));
						sb.append(",");
					}
					mBtnTypes = sb.toString();
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onPause() {
		has_channel_id = true;
		super.onPause();
		mWebView.onPause();
		// ((AudioManager)getSystemService(
		// Context.AUDIO_SERVICE)).requestAudioFocus(
		// new OnAudioFocusChangeListener() {
		// @Override
		// public void onAudioFocusChange(int focusChange) {}
		// }, AudioManager.STREAM_MUSIC,
		// AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
	}

	@Override
	public void onSChanged(int l, int t, int oldl, int oldt) {
		// WebView的总高度
		float webViewContentHeight = mWebView.getContentHeight()
				* mWebView.getScale();
		// WebView的现高度
		float webViewCurrentHeight = (mWebView.getHeight() + mWebView
				.getScrollY());
		if (webViewCurrentHeight - webViewContentHeight == 0) {
			mLoadMorePb.setVisibility(View.VISIBLE);
			mLoadMorePb.bringToFront();
			mWebView.loadUrl("javascript:LoadMore()");
			mHandler.sendEmptyMessageDelayed(MSG_JS_LOAD_DONE, 2000);
		}
	}

	@Override
	public void onRefresh() {
		mWebView.loadUrl("javascript:LoadNew()");
		mHandler.sendEmptyMessageDelayed(MSG_JS_LOAD_DONE, 2000);
	}

	@Override
	public void onButtonsInfo(String msg) {
		LogUtil.e(TAG, "----levy button info--->>>>" + msg);
		Message message = new Message();
		message.what = MSG_JS_LEVY_INFO;
		Bundle data = new Bundle();
		data.putString("button_info", msg);
		message.setData(data);
		mHandler.sendMessage(message);
	}

	private boolean isFileSizeTooLarge() {
		return mVideoSize > MAX_FILE_SIZE_TO_UPLOAD;
	}

	@Override
	protected void onDestroy() {
		if(mWebView!=null)
		{
			mWebView.destroy();
		}
		super.onDestroy();
	}
}
