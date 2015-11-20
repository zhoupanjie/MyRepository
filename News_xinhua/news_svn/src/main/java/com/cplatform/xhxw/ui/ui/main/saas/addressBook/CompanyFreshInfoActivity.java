package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.PreferencesManager;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.HttpClientConfig;
import com.cplatform.xhxw.ui.http.httputils.HttpHanderUtil;
import com.cplatform.xhxw.ui.http.net.saas.CompanyZoneCommentSubRequest;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.widget.InputViewSensitiveLinearLayout;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionManager;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionUtil;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionWidgt;
import com.cplatform.xhxw.ui.util.EditUtil;
import com.cplatform.xhxw.ui.util.WebSettingUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 公司圈消息
 */
public class CompanyFreshInfoActivity extends BaseActivity implements InputViewSensitiveLinearLayout.OnInputViewListener, XWExpressionWidgt.onExprItemClickListener {

	private static final String TAG = CompanyFreshInfoActivity.class.getSimpleName();

    @Bind(R.id.fresh_info_web)
    WebView mWebView;
    private String mRelativeString;
    private JSONObject mReplyDict;
    private AsyncHttpResponseHandler mHttpHandler;
    @Bind(R.id.comment_editt)
    EditText editText;
    @Bind(R.id.comment_expression_widgt)
    XWExpressionWidgt mExpressionWidgt;
    @Bind(R.id.comment_expression_btn)
    Button mExprBtn;
    @Bind(R.id.layout_content)
    InputViewSensitiveLinearLayout mRootContainer;
    @Bind(R.id.comment_editt_linear)
    RelativeLayout mCommentOperateLo;
    @Bind(R.id.toolbar_layout)
    View mToolBar;

    private boolean mIsExprShow = false;
    private boolean mIsSoftKeyboardShow = false;
    private InputMethodManager mImm;
    private Handler mUiHandler = new Handler();
    private int mSoftKeyboardHeight = 0;
    private int mOriginalHeight = 0;
    private int mRootContainerBottom = 0;
    private boolean isExprAreaResized = false;

    private Uri mPhotoUri;
	private String infoid;
	
	public static Intent newIntent(Context context, String infoid) {
		Intent intent = new Intent(context, CompanyFreshInfoActivity.class);
		intent.putExtra("infoid", infoid);
		return intent;
	}
	
	@Override
	protected String getScreenName() {
		return TAG;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_fresh_info);
		
		ButterKnife.bind(this);
		
		initActionBar();
		
		infoid = getIntent().getStringExtra("infoid");
		
		mImm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        WebSettingUtil.init(mWebView, null, null, null, false);
        mWebView.setWebViewClient(new MyWebViewClient());
        
        mRootContainer.setOnInputViewListener(this);
        
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (mIsExprShow) {
                        mIsExprShow = false;
                        mIsSoftKeyboardShow = true;
                        mExpressionWidgt.setVisibility(View.GONE);
                        mExprBtn.setBackgroundResource(R.drawable.selector_s_chat_expressions);
                    }
                }
                return false;
            }
        });
        mSoftKeyboardHeight = PreferencesManager.getSoftKeyboardHeight(this);
        if (mSoftKeyboardHeight > 0) {
            resizeExprArea(Constants.screenWidth);
        }
        
        /**
         * 测量软键盘所占区域高度
         */
        mRootContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
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
                    if (screenHeight != mOriginalHeight && !isExprAreaResized) {
                        mSoftKeyboardHeight = Math.abs(screenHeight - mOriginalHeight);
                        PreferencesManager.saveSoftKeyboardHeight(CompanyFreshInfoActivity.this, mSoftKeyboardHeight);
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
        mSoftKeyboardHeight = 300;
        resizeExprArea(Constants.screenWidth);
        String url = APIClient.getCompanyFreshInfoUrl() + "?infoid=" + infoid;
        Map<String, String> header = HttpClientConfig.getHeader();
        mWebView.loadUrl(url, header);
	}

	private void resizeExprArea(int viewWidth) {
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                mSoftKeyboardHeight);
        rlp.addRule(RelativeLayout.BELOW, R.id.comment_editt_linear);
        mExpressionWidgt.setLayoutParams(rlp);
        mExpressionWidgt.setKeyboardSize(viewWidth, mSoftKeyboardHeight);
    }

	@OnClick(R.id.comment_sendbtn)
    public void senAction() {
		EditUtil.hideMethod(this, editText);
        companyZoneCommentSub(editText.getText().toString());
        editText.setText("");
    }
	
	@OnClick(R.id.comment_expression_btn)
    public void onClickExprBtn() {
        if (mIsExprShow) {
            controlKeyboardOrExpr(true, true);
        } else {
            controlKeyboardOrExpr(false, true);
        }
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
            mExprBtn.setBackgroundResource(R.drawable.selector_s_chat_expressions);
        }
    }
    
    @Override
    public void onExprItemClick(String exprInfo, boolean isDel) {
        SpannableString ss;
        int textSize = (int) editText.getTextSize();
        if (isDel) {
            HashMap<String, Integer> mExprFilenameIdData = XWExpressionManager.getInstance().getmExprInfoIdValuesCN(this);
            ss = XWExpressionUtil.generateSpanComment(App.getContext(),
                    XWExpressionUtil.deleteOneWord(editText.getText().toString(), mExprFilenameIdData),
                    textSize);
        } else {
            String content = editText.getText() + exprInfo;
            ss = XWExpressionUtil.generateSpanComment(App.getContext(), content,
                    textSize);
        }
        editText.setText(ss);
        editText.setSelection(ss.length());
    }

    @Override
    public void onShowInputView() {
//        editText.setHint("");
//        mToolBar.setVisibility(View.VISIBLE);
//        KeyboardUtil.showSoftInput(editText);
        
//      LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(Actions.ACTION_HIDE_ENTERPRISE_BOTTOM_BAR));
    }

    @Override
    public void onHideInputView() {
//        if (!mIsExprShow) {
//            mExprBtn.setBackgroundResource(R.drawable.selector_s_chat_expressions);
//        }
//        mToolBar.setVisibility(View.GONE);
    	
//        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(Actions.ACTION_SHOW_ENTERPRISE_BOTTOM_BAR));
    }

    /**
     * 回复消息
     */
    private void companyZoneCommentSub(final String content) {
        if (mReplyDict == null || mReplyDict.optJSONObject("callback") == null || mReplyDict.optJSONObject("param") == null) {
            return;//{"callback":{"callback":"CompanyCommentDetailCBack","callbacktype":"jsonp","param":""},"action":"CompayZoneDetial","allow":{"filename":"upfile","allowcontent":"1","contentmax":"1000","allowEmoji":"1","allowFile":"0","isfilearray":"0","maxfilenum":"0"},"type":"input","param":{"commentid":"","infoid":"b660aac1ecccf14cf7d500e566960485","replyname":"","replyuid":""}}
        }
        final String callback = mReplyDict.optJSONObject("callback").optString("callback");
        JSONObject param = mReplyDict.optJSONObject("param");
        String infoid = param.optString("infoid"); // 新鲜事信息ID
        String userid = param.optString("replyuid");// 回复某条评论的发布人ID
        String commentid =  param.optString("commentid");//回复某条评论的ID
        APIClient.companyZoneCommentSub(new CompanyZoneCommentSubRequest(infoid, userid, commentid, content), new AsyncHttpResponseHandler() {
            @Override
            protected void onPreExecute() {
                HttpHanderUtil.cancel(mHttpHandler);
                mHttpHandler = this;
                showLoadingView();
            }

            @Override
            public void onSuccess(int statusCode, String result) {
                String js = "javascript:" + callback + "(" + mRelativeString + ",'" + content + "')";
                mWebView.loadUrl(js);
                mRelativeString = null;
                mReplyDict = null;
                mToolBar.setVisibility(View.GONE);
                editText.setText("");
            }

            @Override
            public void onFailure(Throwable error, String content) {
                showToast(R.string.load_server_failure);
            }

            @Override
            public void onFinish() {
                mHttpHandler = null;
                hideLoadingView();
            }
        });
    }
    
	 private class MyWebViewClient extends WebViewClient {

	        @Override
	        public boolean shouldOverrideUrlLoading(WebView view, String url) {
	            if (url.startsWith("saas://")) {
	                String relativeString = url.substring(7);//%7B%22action%22:%22CompayZoneDetial%22,%22type%22:%22input%22,%22param%22:%7B%22commentid%22:%22%22,%22replyuid%22:%22%22,%22infoid%22:%22b660aac1ecccf14cf7d500e566960485%22,%22replyname%22:%22%22%7D,%22allow%22:%7B%22allowEmoji%22:%221%22,%22allowFile%22:%220%22,%22allowcontent%22:%221%22,%22contentmax%22:%221000%22,%22filename%22:%22upfile%22,%22isfilearray%22:%220%22,%22maxfilenum%22:%220%22%7D,%22callback%22:%7B%22callbacktype%22:%22jsonp%22,%22callback%22:%22CompanyCommentDetailCBack%22,%22param%22:%22%22%7D%7D
	                try {
	                    mRelativeString = java.net.URLDecoder.decode(relativeString, "utf-8");
	                    mReplyDict = new JSONObject(mRelativeString);
	                    mToolBar.setVisibility(View.VISIBLE);
	                } catch (UnsupportedEncodingException e) {
	                    e.printStackTrace();
	                } catch (JSONException e) {
	                    e.printStackTrace();
	                }
	                return true;
	            }
	            view.loadUrl(url, HttpClientConfig.getHeader());
	            return true;
	        }

	        @Override
	        public void onPageStarted(WebView view, String url, Bitmap favicon) {
	            super.onPageStarted(view, url, favicon);

	            //开始加载网页
	            showLoadingView();
	        }

	        @Override
	        public void onPageFinished(WebView view, String url) {
	            super.onPageFinished(view, url);

	            //结束加载网页
	            hideLoadingView();
	        }
	    }
}
