package com.cplatform.xhxw.ui.ui.detailpage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.PreferencesManager;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.StatisticalKey;
import com.cplatform.xhxw.ui.db.CollectDB;
import com.cplatform.xhxw.ui.db.dao.CollectDao;
import com.cplatform.xhxw.ui.db.dao.CollectFlag;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.CommentDeleteRequest;
import com.cplatform.xhxw.ui.http.net.CommentDeleteResponse;
import com.cplatform.xhxw.ui.http.net.CommentPraiseRequest;
import com.cplatform.xhxw.ui.http.net.CommentPraiseResponse;
import com.cplatform.xhxw.ui.http.net.CommentRequest;
import com.cplatform.xhxw.ui.http.net.CommentResponse;
import com.cplatform.xhxw.ui.http.net.SendCommentOrReplyPersonRequest;
import com.cplatform.xhxw.ui.http.net.SendCommentOrReplyPersonResponse;
import com.cplatform.xhxw.ui.location.LocationUtil;
import com.cplatform.xhxw.ui.model.Comment;
import com.cplatform.xhxw.ui.model.CommentHot;
import com.cplatform.xhxw.ui.model.LocationPoint;
import com.cplatform.xhxw.ui.model.NewComment;
import com.cplatform.xhxw.ui.model.NewsDetail;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.adapter.CommentAdapter;
import com.cplatform.xhxw.ui.ui.base.adapter.CommentAdapter.OnShowActionSheetListener;
import com.cplatform.xhxw.ui.ui.base.view.CommentEdittext;
import com.cplatform.xhxw.ui.ui.base.widget.CommentActionSheet;
import com.cplatform.xhxw.ui.ui.base.widget.CommentListView;
import com.cplatform.xhxw.ui.ui.base.widget.CommentListView.CommentPullRefreshListener;
import com.cplatform.xhxw.ui.ui.base.widget.InputViewSensitiveLinearLayout;
import com.cplatform.xhxw.ui.ui.base.widget.InputViewSensitiveLinearLayout.OnInputViewListener;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionManager;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionUtil;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionWidgt;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionWidgt.onExprItemClickListener;
import com.cplatform.xhxw.ui.ui.login.LoginActivity;
import com.cplatform.xhxw.ui.ui.main.HomeActivity;
import com.cplatform.xhxw.ui.util.DateUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.cplatform.xhxw.ui.util.StringUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;
import com.wbtech.ums.UmsAgent;

/**
 * 新闻评论
 */
public class CommentActivity extends BaseActivity implements
		OnInputViewListener, CommentPullRefreshListener,
		OnShowActionSheetListener, onExprItemClickListener {

	private static final String TAG = CommentActivity.class.getSimpleName();
	private static final int COUNT = 20;
	private int page = 1;
	private int pageToFetch = 1;
	private int state = 2;
	private boolean isReply = false;
	private boolean isMove;// 判断触碰屏幕时，是否有移动
	/**
	 * 用于标记回复的是哪一条评论position
	 * 
	 * position是从适配器里面传过来
	 * 
	 * position从 0 开始
	 */
	private int position;

	private String mActionDown = "down";
	private String mActionUp = "up";
	private String mOldCommentId;

	@Bind(R.id.layout)
	LinearLayout linearLayout;

	@Bind(R.id.listview)
	CommentListView listView;

	@Bind(R.id.comment_edittext)
	CommentEdittext commentEdittext;

	@Bind(R.id.comment_sendbtn)
	Button sendbtn;

	@Bind(R.id.comment_editt)
	EditText editText;

	@Bind(R.id.layout_content)
	InputViewSensitiveLinearLayout inputLayout;

	@Bind(R.id.comment_expression_widgt)
	XWExpressionWidgt mExpressionWidgt;

	@Bind(R.id.comment_expression_btn)
	Button mExprBtn;

//	@Bind(R.id.layout_content)
	InputViewSensitiveLinearLayout mRootContainer;

	@Bind(R.id.comment_editt_linear)
	RelativeLayout mCommentOperateLo;

	private CommentAdapter mAdapter;
	private String mNewId;

	private MyThread myThread = null;
	private MyHandle myHandle = null;

	// 触摸屏幕的坐标
	private int x;
	private int y;

	private boolean mIsExprShow = false;
	private boolean mIsSoftKeyboardShow = false;
	private InputMethodManager mImm;
	private Handler mUiHandler = new Handler();
	private int mSoftKeyboardHeight = 0;
	private int mOriginalHeight = 0;
	private int mRootContainerBottom = 0;
	private boolean isExprAreaResized = false;
	private boolean isEnterprise = false;

	private TextView mTitle;
	private TextView mNewsSource;
	private TextView mReadCount;
	private ImageView mCollectIv;
	private String mTitleStr;

	public static Intent getIntent(Context context, boolean isEnterprise,
			NewsDetail news) {
		Intent intent = new Intent(context, CommentActivity.class);
		intent.putExtra("newId", news.getId());
		intent.putExtra("enterprise", isEnterprise);
		intent.putExtra("title", news.getTitle());
		intent.putExtra("source", news.getSource());
		intent.putExtra("readCount", news.getReadCount());
		return intent;
	}

	@Override
	protected String getScreenName() {
		return "CommentActivity";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);

		ButterKnife.bind(this);
		initViews();
		init();
	}

	@Override
	protected void onPause() {
		has_channel_id = true;
		super.onPause();
	}

	private void initViews() {
		mNewId = getIntent().getStringExtra("newId");
		isEnterprise = getIntent().getBooleanExtra("enterprise", false);

		mTitle = (TextView) findViewById(R.id.comment_news_title_tv);
		mNewsSource = (TextView) findViewById(R.id.comment_news_source);
		mReadCount = (TextView) findViewById(R.id.comment_news_readcount);
		mCollectIv = (ImageView) findViewById(R.id.comment_collect_iv);
		mCollectIv.setOnClickListener(mOnClickLs);

		mTitleStr = getIntent().getStringExtra("title");
		mTitle.setText(mTitleStr);
		mNewsSource.setText("来源：" + getIntent().getStringExtra("source"));
		int readCount = getIntent().getIntExtra("readCount", 0);
		String readCo = readCount > 0 ? "阅读数：" + readCount : "";
		mReadCount.setText(readCo);

		CollectDao isCon = CollectDB.getCollectByNewsId(this, mNewId);
		if (isCon == null) {
			mCollectIv.setBackgroundResource(R.drawable.btn_new_start);
		} else {
			mCollectIv.setBackgroundResource(R.drawable.btn_new_start_success);
		}
	}

	OnClickListener mOnClickLs = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.comment_collect_iv) {
				onCollectNews();
			}
		}
	};

	private void init() {
		mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		listView.setPullRefreshListener(this);
		listView.setCanRefresh(true);
		listView.setCanLoadMore(false);
		// listView.setOnItemClickListener(this);

		mAdapter = new CommentAdapter(this);
		mAdapter.setOnShowCommentNewsListener(this);
		listView.setAdapter(mAdapter);
		listView.triggerRefresh(true);

		inputLayout.setOnInputViewListener(this);

		editText.setHint(this.getResources().getString(R.string.edit_hint));

		hideMethod(editText);

		myHandle = new MyHandle();

		mSoftKeyboardHeight = PreferencesManager.getSoftKeyboardHeight(this);
		if (mSoftKeyboardHeight > 0) {
			resizeExprArea(Constants.screenWidth);
		}

		/**
		 * 测量软键盘所占区域高度
		 */
		mRootContainer.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						Rect r = new Rect();
						mRootContainer.getWindowVisibleDisplayFrame(r);
						int screenHeight = mRootContainer.getHeight();
						final int screenWidth = mRootContainer.getWidth();
						mRootContainerBottom = screenHeight;
						if (mOriginalHeight == 0) {
							mOriginalHeight = screenHeight;
						} else {
							if (screenHeight != mOriginalHeight
									&& !isExprAreaResized) {
								mSoftKeyboardHeight = Math.abs(screenHeight
										- mOriginalHeight);
								PreferencesManager.saveSoftKeyboardHeight(
										CommentActivity.this,
										mSoftKeyboardHeight);
								isExprAreaResized = true;
								mUiHandler.post(new Runnable() {
									@Override
									public void run() {
										resizeExprArea(screenWidth);
									}
								});
							}
						}
					}
				});
		mExpressionWidgt.setmOnExprItemClickListener(this);
	}

	@OnClick(R.id.comment_expression_btn)
	public void onClickExprBtn() {
		if (mIsExprShow) {
			controlKeyboardOrExpr(true, true);
		} else {
			controlKeyboardOrExpr(false, true);
		}
	}

	@OnClick(R.id.comment_editt)
	public void onEditTextClick() {
		if (mIsExprShow) {
			mIsExprShow = false;
			mIsSoftKeyboardShow = true;
			mExpressionWidgt.setVisibility(View.GONE);
			mExprBtn.setBackgroundResource(R.drawable.selector_expressions);
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mIsExprShow || mIsSoftKeyboardShow) {
				controlKeyboardOrExpr(false, false);
				return true;
			} else {
				finish();
			}
			// if (state == 1) {
			// editText.setText("");
			// editText.setHint(this.getResources().getString(
			// R.string.edit_hint));
			// state = 0;
			// isReply = false;
			// return false;
			// } else if (state == 0) {
			// finish();
			// }
		}
		return super.onKeyUp(keyCode, event);
	}

	/**
	 * 控制表情区和软键盘的显示
	 * 
	 * @param isShowKeyboard
	 * @param isShowExpr
	 */
	private void controlKeyboardOrExpr(boolean isShowKeyboard, boolean isSwitch) {
		if (isSwitch) {
			if (!isShowKeyboard) {
				mIsExprShow = true;
				mIsSoftKeyboardShow = false;
				mImm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
				mUiHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						mExpressionWidgt.setVisibility(View.VISIBLE);
					}
				}, 100);
				mExprBtn.setBackgroundResource(R.drawable.selector_keyboard);
			} else {
				mIsExprShow = false;
				mIsSoftKeyboardShow = true;
				mExpressionWidgt.setVisibility(View.GONE);
				mExprBtn.setBackgroundResource(R.drawable.selector_expressions);
				mImm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		} else {
			if (isShowKeyboard) {
				mIsExprShow = false;
				mIsSoftKeyboardShow = true;
				mExpressionWidgt.setVisibility(View.GONE);
				mImm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			} else {
				if (mIsSoftKeyboardShow) {
					mIsSoftKeyboardShow = false;
					mImm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
				} else {
					mIsExprShow = false;
					mExpressionWidgt.setVisibility(View.GONE);
				}
			}
			mExprBtn.setBackgroundResource(R.drawable.selector_expressions);
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			if (mIsExprShow || mIsSoftKeyboardShow) {
				int rootTop = 0;
				if (mIsExprShow) {
					rootTop = mRootContainerBottom - mSoftKeyboardHeight
							- mCommentOperateLo.getHeight();
				} else {
					rootTop = mRootContainerBottom
							- mCommentOperateLo.getHeight();
				}
				if (ev.getY() < rootTop) {
					controlKeyboardOrExpr(false, false);
					return true;
				}
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	/** 获取评论数据 */
	private AsyncHttpResponseHandler mHttpResponseHandler;

	private void resizeExprArea(int viewWidth) {
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, mSoftKeyboardHeight);
		rlp.addRule(RelativeLayout.BELOW, R.id.comment_editt_linear);
		mExpressionWidgt.setLayoutParams(rlp);
		mExpressionWidgt.setKeyboardSize(viewWidth, mSoftKeyboardHeight);
	}

	private void getComment(final int p, boolean isLoadMore) {
		pageToFetch = p;
		CommentRequest request = new CommentRequest();
		request.setPn(p);
		request.setOffset(COUNT);
		request.setNewsId(mNewId);
		if (isLoadMore) {
			request.setAction(mActionDown);
			request.setCommentid(mOldCommentId);
		}

		if (isEnterprise) {
			request.setSaasRequest(true);
			APIClient.getSAASComment(request, getCommentListHandler);
		} else {
			APIClient.getComment(request, getCommentListHandler);
		}
	}

	AsyncHttpResponseHandler getCommentListHandler = new AsyncHttpResponseHandler() {

		@Override
		public void onFinish() {
			mHttpResponseHandler = null;
			listView.onRefreshComplete(null);
			listView.onLoadMoreComplete();
		}

		@Override
		protected void onPreExecute() {
			if (mHttpResponseHandler != null) {
				mHttpResponseHandler.cancle();
			}
			mHttpResponseHandler = this;
		}

		@Override
		public void onSuccess(String content) {
			page = pageToFetch;
			CommentResponse response;
			try {
				ResponseUtil.checkResponse(content);
				response = new Gson().fromJson(content, CommentResponse.class);
			} catch (Exception e) {
				showToast(R.string.data_format_error);
				LogUtil.w(TAG, e);
				return;
			}
			if (response.isSuccess()) {

				if (page == 1) {
					mAdapter.clearData();
				}

				if (response.getData() != null) {
					if (response.getData().getHotComments() != null
							&& response.getData().getComments() != null) {

						List<Comment> listComment = new ArrayList<Comment>();

						if (page == 1) {
							for (int i = 0; i < response.getData()
									.getHotComments().size(); i++) {
								if (response.getData().getHotComments().get(i) != null) {
									listComment
											.add(hotToComment(response
													.getData().getHotComments()
													.get(i)));
								}
							}
							mAdapter.setHotCount(response.getData()
									.getHotComments().size());
						}

						for (int i = 0; i < response.getData().getComments()
								.size(); i++) {
							NewComment nc = response.getData().getComments()
									.get(i);
							if (nc != null) {
								listComment.add(newToComment(nc));
								if (page == 1 && i == 0) {
									mOldCommentId = nc.getId();
								}
							}
						}

						mAdapter.setCommentCount(response.getData()
								.getHotComment_sum_total(), response.getData()
								.getComment_sum_total());
						mAdapter.addAllData(listComment);
						mAdapter.notifyDataSetChanged();
					}

					if (response.getData().getIsnext() == 1) {
						listView.setCanLoadMore(true);
					} else if (response.getData().getIsnext() == 0) {
						listView.setCanLoadMore(false);
					}
				}

			} else {
				showToast(response.getMsg());
			}
		}

		@Override
		public void onFailure(Throwable error, String content) {
			showToast(R.string.load_server_failure);
		}
	};

	/** 发送提交 */
	@OnClick(R.id.comment_sendbtn)
	public void send() {
		UmsAgent.onEvent(this, StatisticalKey.publish_comment, new String[] {
				StatisticalKey.key_channelid, StatisticalKey.key_newsid },
				new String[] { App.channel_id, mNewId });
		if (myThread != null) {
			myThread = null;
		}

		if (TextUtils.isEmpty(editText.getText().toString())) {
			Toast.makeText(
					this,
					this.getResources().getString(
							R.string.comment_content_demand),
					Toast.LENGTH_SHORT).show();
			return;
		}

		SendCommentOrReplyPersonRequest request = new SendCommentOrReplyPersonRequest();
		if (isEnterprise) {
			request.setSaasRequest(true);
			request.setLogo(Constants.userInfo.getLogo());
			request.setNickname(Constants.userInfo.getNickName());
		}
		request.setNewsId(Integer.valueOf(mNewId));
		request.setContent(editText.getText().toString());
		LocationPoint point = LocationUtil.getAPosition(
				getApplicationContext(), mLocationClient);
		if (point != null) {
			request.setLongitude(point.getLongitude());
			request.setLatitude(point.getLatitude());
		}

		if (isReply) {
			// request.setUid(Constants.userInfo.getUserId());
			request.setUid(mAdapter.getData().get(position).getSenderId());
			request.setCommentid(mAdapter.getData().get(position).getId());
			// request.setToname(mAdapter.getData().get(position).getSnickName());
		}

		sendDate(request);

		editText.setText("");
		editText.setHint(CommentActivity.this.getResources().getString(
				R.string.edit_hint));
		state = 0;
		hideMethod(editText);
	}

	/** 提交评论或者回复 */
	private AsyncHttpResponseHandler mHandler;

	public void sendDate(final SendCommentOrReplyPersonRequest request) {

		APIClient.sendCommentOrReplyPerson(request,
				new AsyncHttpResponseHandler() {

					@Override
					public void onFinish() {
						mHandler = null;
						// context.hideLoadingView();
					}

					@Override
					protected void onPreExecute() {
						if (mHandler != null)
							mHandler.cancle();
						mHandler = this;
						// context.showLoadingView(R.string.comment_content_upping);
					}

					@Override
					public void onSuccess(String content) {
						SendCommentOrReplyPersonResponse response;
						try {
							ResponseUtil.checkResponse(content);
							response = new Gson().fromJson(content,
									SendCommentOrReplyPersonResponse.class);
						} catch (Exception e) {
							// mDefView.setStatus(DefaultView.Status.error);
							showToast(R.string.data_format_error);
							return;
						}

						if (response.isSuccess()) {

							/*
							 * 本地刷新，临时注销 Comment comment = new Comment();
							 * comment.setId(response.getData().getId());
							 * comment
							 * .setSenderId(Constants.userInfo.getUserId());
							 * comment
							 * .setSnickName(Constants.userInfo.getNickName());
							 * comment.setLogo(Constants.userInfo.getLogo());
							 * comment.setPraise("");
							 * comment.setContent(request.getContent());
							 * comment.
							 * setPublished(String.valueOf(System.currentTimeMillis
							 * ()/1000)); comment.setRlogo("");
							 * comment.setPraiseCount("0"); if (isReply) {
							 * comment.setReciverId("");
							 * comment.setRnickName(request.getToname()); }else
							 * { comment.setReciverId("");
							 * comment.setRnickName(""); }
							 * 
							 * // mAdapter.getData().add(comment);
							 * mAdapter.getData().add(mAdapter.getHotCount(),
							 * comment); mAdapter.notifyDataSetChanged();
							 */

							/**
							 * 每次评论或者回复，都联网刷新界面
							 */
							listView.triggerRefresh(true);

							// showToast(response.getMsg());
							if (isReply) {
								showToast("回复成功");
							} else {
								showToast("评论成功");
							}

							isReply = false;
						} else {
							showToast(response.getMsg());
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						// mDefView.setStatus(DefaultView.Status.error);
					}
				});
	}

	/** 返回 */
	@OnClick(R.id.btn_back)
	public void goBack() {
		this.finish();
	}

	@Override
	public void onRefresh() {
		getComment(1, false);
	}

	@Override
	public void onLoadMore() {
		getComment(page + 1, true);
	}

	@Override
	public void onShow(int x, int y, boolean isMove) {
		this.x = x;
		this.y = y;

		this.isMove = isMove;
		Log.d("新华炫文popu", y + "");
		Log.d("新华炫文move", isMove + "");
	}

	@Override
	public void onShowInputView() {
		state = 2;
		editText.setHint("");
	}

	@Override
	public void onHideInputView() {
		if (state == 0) {

		} else {
			state = 1;
		}
		if (!mIsExprShow) {
			mExprBtn.setBackgroundResource(R.drawable.selector_expressions);
		}
	}

	@Override
	public void onClear() {
		editText.setText("");
		editText.setHint(this.getResources().getString(R.string.edit_hint));
		state = 0;
		hideMethod(editText);
	}

	/** 隐藏输入法 */
	private void hideMethod(EditText editText) {
		InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}

	/** 显示输入法 */
	private void showKeyboard(EditText editText) {
		editText.requestFocus();
		InputMethodManager imm = (InputMethodManager) this
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(editText, InputMethodManager.RESULT_UNCHANGED_SHOWN);
	}

	/**
	 * 用户删除评论
	 * */
	private AsyncHttpResponseHandler mDeleteHandler;

	private void setDelete(int id) {

		CommentDeleteRequest request = new CommentDeleteRequest();
		request.setCommentid(id);
		if (isEnterprise) {
			request.setSaasRequest(true);
		}

		APIClient.setDelete(request, new AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				mDeleteHandler = null;
				hideLoadingView();
			}

			@Override
			protected void onPreExecute() {
				if (mDeleteHandler != null)
					mDeleteHandler.cancle();
				mDeleteHandler = this;
				// mDefView.setStatus(DefaultView.Status.loading);
				showLoadingView(R.string.sending);
			}

			@Override
			public void onSuccess(String content) {
				CommentDeleteResponse response;
				try {
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content,
							CommentDeleteResponse.class);
				} catch (Exception e) {
					showToast(R.string.data_format_error);
					return;
				}
				if (response != null) {
					if (response.isSuccess()) {
						listView.triggerRefresh(true);
						showToast("删除成功");
					} else {
						// mDefView.setStatus(DefaultView.Status.error);
						showToast(response.getMsg());
					}

					if (response.getCode() == -9) {
						startActivity(LoginActivity
								.getIntent(CommentActivity.this));
						showToast(R.string.please_loging);
					}
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				// mDefView.setStatus(DefaultView.Status.error);
			}
		});
	}

	/**
	 * 用户赞评论与取消赞
	 * */
	private AsyncHttpResponseHandler mPraiseHandler;

	private void setPraise(int id) {

		CommentPraiseRequest request = new CommentPraiseRequest();
		if (isEnterprise) {
			request.setSaasRequest(true);
		}
		request.setCommentid(id);
		LocationPoint point = LocationUtil.getAPosition(
				getApplicationContext(), mLocationClient);
		if (point != null) {
			request.setLongitude(point.getLongitude());
			request.setLatitude(point.getLatitude());
		}

		APIClient.setPraise(request, new AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				mPraiseHandler = null;
				hideLoadingView();
			}

			@Override
			protected void onPreExecute() {
				if (mPraiseHandler != null)
					mPraiseHandler.cancle();
				mPraiseHandler = this;
				// mDefView.setStatus(DefaultView.Status.loading);
				showLoadingView(R.string.sending);
			}

			@Override
			public void onSuccess(String content) {
				CommentPraiseResponse response;
				try {
					// <br />
					// <b>Notice</b>: Undefined index: usertoken in
					// <b>/home/xuanwen/sites/api/library/XINHUA.class.php</b>
					// on line <b>111</b><br />
					// {"code":-9,"msg":"\u767b\u5f55\u8d85\u65f6","data":{"tips":"\u767b\u5f55\u6821\u9a8c\u5931\u8d251"}}
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content,
							CommentPraiseResponse.class);
				} catch (Exception e) {
					showToast(R.string.data_format_error);
					return;
				}
				if (response != null) {
					if (response.isSuccess()) {

						/*
						 * 本地刷新界面，临时取消 int count =
						 * Integer.valueOf(mAdapter.getData
						 * ().get(position).getPraiseCount()); if
						 * ("1".equals(response.getData().getActionType())) {
						 * mAdapter
						 * .getData().get(position).setPraiseCount(String
						 * .valueOf(count - 1));
						 * mAdapter.notifyDataSetChanged(); showToast("成功取消赞");
						 * }else if
						 * ("2".equals(response.getData().getActionType())) {
						 * showToast("赞成功");
						 * mAdapter.getData().get(position).setPraiseCount
						 * (String.valueOf(count + 1));
						 * mAdapter.notifyDataSetChanged(); }
						 */

						/**
						 * 每次赞或者取消赞成功后，都联网刷新界面
						 */
						if ("1".equals(response.getData().getActionType())) {
							showToast("取消赞成功");
						} else if ("2".equals(response.getData()
								.getActionType())) {
							showToast("赞成功");
						}

						listView.triggerRefresh(true);
					} else {
						// mDefView.setStatus(DefaultView.Status.error);
						showToast(response.getMsg());
					}

					if (response.getCode() == -9) {
						startActivity(LoginActivity
								.getIntent(CommentActivity.this));
						showToast(R.string.please_loging);
					}
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				// mDefView.setStatus(DefaultView.Status.error)
			}
		});
	}

	/**
	 * CommentHot转换成Comment
	 * */
	private Comment hotToComment(CommentHot hot) {
		Comment comment = new Comment();

		comment.setId(hot.getId());
		comment.setNewsId(hot.getId());
		comment.setContent(hot.getContent());
		comment.setSenderId(hot.getSenderId());
		comment.setReciverId(hot.getReciverId());
		comment.setNewsUrl(hot.getNewsUrl());
		comment.setNewsTitle(hot.getNewsTitle());
		comment.setSnickName(hot.getSnickName());
		comment.setRnickName(hot.getRnickName());
		comment.setPraiseCount(hot.getPraiseCount());
		comment.setPublished(hot.getPublished());
		comment.setLogo(hot.getLogo());
		comment.setRlogo(hot.getRlogo());
		comment.setPraise(hot.getPraise());
		comment.setType("hot");
		return comment;
	}

	/**
	 * NewComment转换成Comment
	 * */
	private Comment newToComment(NewComment newC) {
		Comment comment = new Comment();

		comment.setId(newC.getId());
		comment.setNewsId(newC.getId());
		comment.setContent(newC.getContent());
		comment.setSenderId(newC.getSenderId());
		comment.setReciverId(newC.getReciverId());
		comment.setNewsUrl(newC.getNewsUrl());
		comment.setNewsTitle(newC.getNewsTitle());
		comment.setSnickName(newC.getSnickName());
		comment.setRnickName(newC.getRnickName());
		comment.setPraiseCount(newC.getPraiseCount());
		comment.setPublished(newC.getPublished());
		comment.setLogo(newC.getLogo());
		comment.setRlogo(newC.getRlogo());
		comment.setPraise(newC.getPraise());
		comment.setType("new");
		return comment;
	}

	private class MyThread extends Thread {
		@Override
		public void run() {
			super.run();

			try {
				sleep(500);
				myHandle.sendEmptyMessage(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private class MyHandle extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			showKeyboard(editText);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mHttpResponseHandler != null) {
			mHttpResponseHandler.cancle();
			mHttpResponseHandler = null;
		}

		if (mHandler != null) {
			mHandler.cancle();
			mHandler = null;
		}

		if (mPraiseHandler != null) {
			mPraiseHandler.cancle();
			mPraiseHandler = null;
		}

		myHandle = null;
		myThread = null;

		handler.removeCallbacks(showPopu);
	}

	@Override
	public void OnShowActionSheet(final int position) {

		this.position = position;

		handler.postDelayed(showPopu, 0);
	}

	private Handler handler = new Handler();
	// private int c =
	// CommentActivity.this.getResources().getDimensionPixelOffset(R.dimen.activity_comment_popu_transt);
	private int c = 80;
	private Runnable showPopu = new Runnable() {

		@Override
		public void run() {
			if (isMove) {
				int praise = Integer.valueOf(mAdapter.getData().get(position)
						.getPraise());
				boolean isPraised = (praise == 1) ? false : true;
				String userId = mAdapter.getData().get(position).getSenderId();
				CommentActionSheet actionSheet = CommentActionSheet
						.getInstance(CommentActivity.this, isPraised, userId);
				actionSheet
						.setOnCommentClickListener(new CommentActionSheet.OnCommentClickListener() {

							@Override
							public void onCommentTouch() {
								setPraise(Integer.valueOf(mAdapter.getData()
										.get(position).getId()));
							}

							@Override
							public void onCommentReply() {
								editText.setFocusable(true);
								showKeyboard(editText);
								isReply = true;
								if (myThread != null) {
									myThread = null;
								}
								myThread = new MyThread();
								myThread.start();
							}

							@Override
							public void onCommentDelete() {
								if (Constants.hasLogin()) {
									if (Constants.userInfo.getUserId().equals(
											mAdapter.getData().get(position)
													.getSenderId())) {
										setDelete(Integer.valueOf(mAdapter
												.getData().get(position)
												.getId()));
									} else {
										showToast("您没有权限删除此评论");
									}
								} else {
									setDelete(Integer.valueOf(mAdapter
											.getData().get(position).getId()));
								}
							}

							@Override
							public void onCommentCancelTouch() {
								setPraise(Integer.valueOf(mAdapter.getData()
										.get(position).getId()));
							}

							@Override
							public void onCommentCopy() {
								ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
								cmb.setPrimaryClip(ClipData.newPlainText("",
										mAdapter.getData().get(position)
												.getContent()));
								showToast(R.string.comment_copy_done);
							}
						});

				actionSheet.show(linearLayout, x, y - c);

				Log.d("新华炫文show", y + "");
			}
		}
	};

	@Override
	public void onExprItemClick(String exprInfo, boolean isDel) {
		SpannableString ss;
		int textSize = (int) editText.getTextSize();
		if (isDel) {
			HashMap<String, Integer> mExprFilenameIdData = XWExpressionManager
					.getInstance().getmExprInfoIdValuesCN(this);
			ss = XWExpressionUtil.generateSpanComment(getApplicationContext(),
					XWExpressionUtil.deleteOneWord(editText.getText()
							.toString(), mExprFilenameIdData), textSize);
		} else {
			String content = editText.getText() + exprInfo;
			ss = XWExpressionUtil.generateSpanComment(getApplicationContext(),
					content, textSize);
		}
		editText.setText(ss);
		editText.setSelection(ss.length());
	}

	@Override
	public void onPraiseComment(Comment comment) {
		setPraise(Integer.valueOf(comment.getId()));
	}

	public void onCollectNews() {
		MobclickAgent.onEvent(this, StatisticalKey.detail_favorit);
		UmsAgent.onEvent(this, StatisticalKey.detail_favorit, new String[] {
				StatisticalKey.key_channelid, StatisticalKey.key_newsid },
				new String[] { App.channel_id, mNewId });
		CollectDao isCon = CollectDB.getCollectByNewsId(this, mNewId);
		if (isCon == null) {
			CollectDao item = new CollectDao();
			if (isEnterprise || HomeActivity.mIsUnderEnterprise) {
				item.setFlag(CollectFlag.COLLECT_NEWS_TYPE_ENTERPRISE_NORM
						+ StringUtil.parseIntegerFromString(Constants
								.getEnterpriseId()));
			} else {
				item.setFlag(CollectFlag.COLLECT_NEWS_TYPE_NORMAL_NORM);
			}

			item.setNewsId(mNewId);
			item.setOperatetime(DateUtil.getTime());
			item.setTitle(mTitleStr);
			CollectDB.saveNews(this, item);
			mCollectIv.setBackgroundResource(R.drawable.btn_new_start_success);
		} else {
			CollectDB.delCollectByNewsId(this, isCon.getNewsId());
			mCollectIv.setBackgroundResource(R.drawable.btn_new_start);
		}
	}
}
