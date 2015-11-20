package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.PreferencesManager;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.model.UserInfo;
import com.cplatform.xhxw.ui.model.saas.CompanyZoneItemExrta;
import com.cplatform.xhxw.ui.model.saas.CompanyZoneItemUserInfo;
import com.cplatform.xhxw.ui.model.saas.CompanyZoneList;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.widget.InputViewSensitiveLinearLayout;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionManager;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionUtil;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionWidgt;
import com.cplatform.xhxw.ui.util.EditUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.EditText;
import android.widget.RadioButton;

/**
 * 社区发布新鲜事
 */
public class SendTextActivity extends BaseActivity implements OnClickListener,
		InputViewSensitiveLinearLayout.OnInputViewListener,
		XWExpressionWidgt.onExprItemClickListener {

	private static final String TAG = SendTextActivity.class.getSimpleName();

	private View backBtn;
	private View sendBtn;
	private EditText mEditText;
	private Button faceBtn;

	private RadioButton companyRadio;
	private RadioButton friendRadio;
	private XWExpressionWidgt mExpressionWidgt;

	private InputViewSensitiveLinearLayout mRootContainer;

	private boolean mIsExprShow = false;
	private boolean mIsSoftKeyboardShow = false;
	private InputMethodManager mImm;
	private Handler mUiHandler = new Handler();
	private int mSoftKeyboardHeight = 0;
	private int mOriginalHeight = 0;
	private int mRootContainerBottom = 0;
	private boolean isExprAreaResized = false;

	private AsyncHttpResponseHandler mHttpResponseHandler;

	public static Intent newIntent(Context context, CircleCreateType type) {
		Intent intent = new Intent(context, SendTextActivity.class);
        int createType = (type == CircleCreateType.friend) ? 1 : 2;
        intent.putExtra("createType", createType);
		return intent;
	}

	@Override
	protected String getScreenName() {
		return TAG;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_send_text);

		init();
/*findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
	
	@Override
	public void onClick(View arg0) {
		showToast("哈哈哈");
	}
});*/
		setListener();

		initView();
	}

	private void init() {
		initActionBar();
		mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		backBtn = findViewById(R.id.btn_back);
		sendBtn = findViewById(R.id.company_circle_send);
		faceBtn = (Button) findViewById(R.id.company_circle_face);
		mEditText = (EditText) findViewById(R.id.company_circle_edit);
		companyRadio = (RadioButton) findViewById(R.id.company_circle_radiao);
		friendRadio = (RadioButton) findViewById(R.id.friend_circle_radiao);
		mExpressionWidgt = (XWExpressionWidgt) findViewById(R.id.comment_expression_widgt);
		mRootContainer = (InputViewSensitiveLinearLayout) findViewById(R.id.layout_content);

		mSoftKeyboardHeight = PreferencesManager.getSoftKeyboardHeight(this);
        if (Constants.hasLogin() && Constants.userInfo != null && Constants.userInfo.getEnterprise() != null && Constants.userInfo.getEnterprise().getAliases() != null) {
            UserInfo.EnterpriseAlias alias = Constants.userInfo.getEnterprise().getAliases();
            companyRadio.setText(alias.getC_community_alias());
            friendRadio.setText(alias.getF_community_alias());
        }

	}

	private void setListener() {
		backBtn.setOnClickListener(this);
		sendBtn.setOnClickListener(this);
		faceBtn.setOnClickListener(this);
		mExpressionWidgt.setmOnExprItemClickListener(this);
		mRootContainer.setOnInputViewListener(this);

		mEditText.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (mIsExprShow) {
						mIsExprShow = false;
						mIsSoftKeyboardShow = true;
						mExpressionWidgt.setVisibility(View.GONE);
						faceBtn.setBackgroundResource(R.drawable.selector_s_chat_expressions);
					}
				}
				return false;
			}
		});
        int createType = getIntent().getIntExtra("createType", 1);
        if (createType == 1) {
            friendRadio.setChecked(true);
        } else {
            companyRadio.setChecked(true);
        }
	}

	private void initView() {
		faceBtn.setBackgroundResource(R.drawable.selector_s_chat_expressions);

		/**
		 * 测量软键盘所占区域高度
		 */
		mRootContainer.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
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
                                PreferencesManager.saveSoftKeyboardHeight(SendTextActivity.this, mSoftKeyboardHeight);
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

		if (mSoftKeyboardHeight > 0) {
			resizeExprArea(Constants.screenWidth);
		}

		mSoftKeyboardHeight = 300;
		resizeExprArea(Constants.screenWidth);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			showExitDialog();
			break;
		case R.id.company_circle_send:
			sendCompanyCircle();
			break;
		case R.id.company_circle_face:
			if (mIsExprShow) {
				controlKeyboardOrExpr(true, true);
			} else {
				controlKeyboardOrExpr(false, true);
			}
			break;
		default:
			break;
		}
	}

	/** 发布新鲜事 */
	private void sendCompanyCircle() {

		EditUtil.hideMethod(this, mEditText);

		if (TextUtils.isEmpty(mEditText.getText().toString().trim())) {
			showToast("请输入要发布的内容");
			return;
		}

		SendCompanyCircleRequest request = new SendCompanyCircleRequest();
		if (companyRadio.isChecked()) {// 1朋友圈，2公司圈
			request.setZonetype("2");
		} else {
			request.setZonetype("1");
		}

		request.setConntenttype("1");
		request.setContent(mEditText.getText().toString().trim());
		request.setDevRequest(true);

		APIClient.sendCompanyCircle(request, new AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				mHttpResponseHandler = null;
				hideLoadingView();
			}

			@Override
			protected void onPreExecute() {
				if (mHttpResponseHandler != null) {
					mHttpResponseHandler.cancle();
				}
				mHttpResponseHandler = this;

				showLoadingView("正在发布...");
			}

			@Override
			public void onSuccess(String content) {
				SendCompanyCircleResponse response;
				try {
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content,
							SendCompanyCircleResponse.class);
				} catch (Exception e) {
					showToast(R.string.data_format_error);
					LogUtil.w(TAG, e);
					return;
				}
				if (response.isSuccess()) {
					if (response != null) {
						showToast("发布成功");
						
						Intent intent = new Intent();
						CompanyZoneList companyZoneList = new CompanyZoneList();
						if (response.getData() != null) {
							CompanyZoneItemUserInfo userInfo = new CompanyZoneItemUserInfo();
							userInfo.setName(Constants.userInfo.getNickName());
							userInfo.setLogo(Constants.userInfo.getLogo());
                            userInfo.setUserid(Constants.userInfo.getUserId());
							companyZoneList.setUserinfo(userInfo);
							companyZoneList.setContent(mEditText.getText().toString().trim());
							companyZoneList.setInfoid(response.getData().getInfoid());
							companyZoneList.setFtime("1秒前");
                            companyZoneList.setIscomment("1"); // 设置为可评论
							List<CompanyZoneItemExrta> list = new ArrayList<CompanyZoneItemExrta>();
							if (response.getData().getExrta() != null && response.getData().getExrta().size() > 0) {
								for (int i = 0; i < response.getData().getExrta().size(); i++) {
									CompanyZoneItemExrta companyZoneItemExrta= new CompanyZoneItemExrta();
									companyZoneItemExrta.setFile(response.getData().getExrta().get(i).getFile());
									companyZoneItemExrta.setThumb(response.getData().getExrta().get(i).getThumb());
									list.add(companyZoneItemExrta);
								}
								companyZoneList.setExrta(list);
							}
						}
						intent.putExtra("data", companyZoneList);
						intent.putExtra("isFriends", friendRadio.isChecked());
						setResult(Activity.RESULT_OK, intent);
						finish();
					}
				} else {
					showToast(response.getMsg());
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				showToast(R.string.load_server_failure);
			}
		});
	}

	@Override
	public void onExprItemClick(String exprInfo, boolean isDelete) {
		SpannableString ss;
		int textSize = (int) mEditText.getTextSize();
		if (isDelete) {
			HashMap<String, Integer> mExprFilenameIdData = XWExpressionManager
					.getInstance().getmExprInfoIdValuesCN(this);
			ss = XWExpressionUtil.generateSpanComment(getApplicationContext(),
					XWExpressionUtil.deleteOneWord(mEditText.getText()
							.toString(), mExprFilenameIdData), textSize);
		} else {
			String content = mEditText.getText() + exprInfo;
			ss = XWExpressionUtil.generateSpanComment(getApplicationContext(),
					content, textSize);
		}
		mEditText.setText(ss);
		mEditText.setSelection(ss.length());
	}

	@Override
	public void onShowInputView() {

	}

	@Override
	public void onHideInputView() {

	}

	/**
	 * 控制表情区和软键盘的显示
	 * 
	 * @param isShowKeyboard
	 * @param isSwitch
	 */
	private void controlKeyboardOrExpr(boolean isShowKeyboard, boolean isSwitch) {
		if (isSwitch) {
			if (!isShowKeyboard) {
				mIsExprShow = true;
				mIsSoftKeyboardShow = false;
				mImm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
				mUiHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						mExpressionWidgt.setVisibility(View.VISIBLE);
					}
				}, 100);
				faceBtn.setBackgroundResource(R.drawable.selector_s_chat_keyboard);
			} else {
				mIsExprShow = false;
				mIsSoftKeyboardShow = true;
				mExpressionWidgt.setVisibility(View.GONE);
				faceBtn.setBackgroundResource(R.drawable.selector_s_chat_expressions);
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
					mImm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
				} else {
					mIsExprShow = false;
					mExpressionWidgt.setVisibility(View.GONE);
				}
			}
			faceBtn.setBackgroundResource(R.drawable.selector_s_chat_expressions);
		}
	}

	private void resizeExprArea(int viewWidth) {
		RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, mSoftKeyboardHeight);
		rlp.addRule(RelativeLayout.BELOW, R.id.comment_editt_linear);
		mExpressionWidgt.setLayoutParams(rlp);
		mExpressionWidgt.setKeyboardSize(viewWidth, mSoftKeyboardHeight);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			if (mIsExprShow || mIsSoftKeyboardShow) {
				int rootTop = 0;
				if (mIsExprShow) {
					rootTop = mRootContainerBottom - mSoftKeyboardHeight;
					// -mCommentOperateLo.getHeight();
				} else {
					rootTop = mRootContainerBottom;
					// - mCommentOperateLo.getHeight();
				}
				if (ev.getY() < rootTop) {
					controlKeyboardOrExpr(false, false);
					return true;
				}
			}
		}
		return super.dispatchTouchEvent(ev);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			showExitDialog();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**退出提示对话框*/
	private void showExitDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("确定要退出吗？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				SendTextActivity.this.finish();
			}
		});
		builder.setNegativeButton("取消", null);
		
		builder.show();
	}
}
