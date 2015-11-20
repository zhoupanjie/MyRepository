package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.PreferencesManager;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.ui.base.BaseFragment;
import com.cplatform.xhxw.ui.ui.base.widget.InputViewSensitiveLinearLayout;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionManager;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionUtil;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionWidgt;
import com.cplatform.xhxw.ui.util.KeyboardUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import java.util.HashMap;

/**
 * 评论
 */
public class CommunityCommentFragment extends BaseFragment implements InputViewSensitiveLinearLayout.OnInputViewListener, XWExpressionWidgt.onExprItemClickListener {

    private AsyncHttpResponseHandler mHttpHandler;
    // 编辑框
    @InjectView(R.id.comment_editt)
    EditText editText;
    // 表情库
    @InjectView(R.id.comment_expression_widgt)
    XWExpressionWidgt mExpressionWidgt;
    // 表情按钮
    @InjectView(R.id.comment_expression_btn)
    Button mExprBtn;

    @InjectView(R.id.layout_content)
    InputViewSensitiveLinearLayout mRootContainer;
    @InjectView(R.id.toolbar_layout)
    View mToolBar;

    private boolean mIsExprShow = false;
    private boolean mIsSoftKeyboardShow = false;
    private InputMethodManager mImm;
    private Handler mUiHandler = new Handler();
    private int mSoftKeyboardHeight = 0;
    private int mOriginalHeight = 0;
    private boolean isExprAreaResized = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_community_comment,
                container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mImm = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
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
        mSoftKeyboardHeight = PreferencesManager.getSoftKeyboardHeight(getActivity());
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
                if (mOriginalHeight == 0) {
                    mOriginalHeight = screenHeight;
                } else {
                    if (screenHeight != mOriginalHeight && !isExprAreaResized) {
                        mSoftKeyboardHeight = Math.abs(screenHeight - mOriginalHeight);
                        PreferencesManager.saveSoftKeyboardHeight(getActivity(), mSoftKeyboardHeight);
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
    }

    @OnClick(R.id.comment_sendbtn)
    public void senAction() {
        // TODO 发送
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

//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (mIsExprShow || mIsSoftKeyboardShow) {
//                controlKeyboardOrExpr(false, false);
//                return true;
//            } else {
//                finish();
//            }
//        }
//        return super.onKeyUp(keyCode, event);
//    }

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

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            if (mIsExprShow || mIsSoftKeyboardShow) {
//                int rootTop = 0;
//                if (mIsExprShow) {
//                    rootTop = mRootContainerBottom - mSoftKeyboardHeight -
//                            mCommentOperateLo.getHeight();
//                } else {
//                    rootTop = mRootContainerBottom - mCommentOperateLo.getHeight();
//                }
//                if (ev.getY() < rootTop) {
//                    controlKeyboardOrExpr(false, false);
//                    return true;
//                }
//            }
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    private void resizeExprArea(int viewWidth) {
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                mSoftKeyboardHeight);
        rlp.addRule(RelativeLayout.BELOW, R.id.comment_editt_linear);
        mExpressionWidgt.setLayoutParams(rlp);
        mExpressionWidgt.setKeyboardSize(viewWidth, mSoftKeyboardHeight);
    }

    @Override
    public void onExprItemClick(String exprInfo, boolean isDel) {
        SpannableString ss;
        int textSize = (int) editText.getTextSize();
        if (isDel) {
            HashMap<String, Integer> mExprFilenameIdData = XWExpressionManager.getInstance().getmExprInfoIdValuesCN(getActivity());
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
        editText.setHint("");
        mToolBar.setVisibility(View.VISIBLE);
        KeyboardUtil.showSoftInput(editText);

    }

    @Override
    public void onHideInputView() {
        if (!mIsExprShow) {
            mExprBtn.setBackgroundResource(R.drawable.selector_s_chat_expressions);
        }
        mToolBar.setVisibility(View.GONE);
    }

    /**
     * 回复消息
     */
    private void companyZoneCommentSub(final String content) {
//        if (mReplyDict == null || mReplyDict.optJSONObject("callback") == null || mReplyDict.optJSONObject("param") == null) {
//            return;
//        }
//        final String callback = mReplyDict.optJSONObject("callback").optString("callback");
//        JSONObject param = mReplyDict.optJSONObject("param");
//        String infoid = param.optString("infoid"); // 新鲜事信息ID
//        String userid = param.optString("replyuid");// 回复某条评论的发布人ID
//        String commentid =  param.optString("commentid");//回复某条评论的ID
//        APIClient.companyZoneCommentSub(new CompanyZoneCommentSubRequest(infoid, userid, commentid, content), new AsyncHttpResponseHandler() {
//            @Override
//            protected void onPreExecute() {
//                HttpHanderUtil.cancel(mHttpHandler);
//                mHttpHandler = this;
//                showLoadingView();
//            }
//
//            @Override
//            public void onSuccess(int statusCode, String result) {
//                String js = "javascript:" + callback + "(" + mRelativeString + ",'" + content + "')";
//                mWebView.loadUrl(js);
//                mRelativeString = null;
//                mReplyDict = null;
//                mToolBar.setVisibility(View.GONE);
//                editText.setText("");
//            }
//
//            @Override
//            public void onFailure(Throwable error, String content) {
//                showToast(R.string.load_server_failure);
//            }
//
//            @Override
//            public void onFinish() {
//                mHttpHandler = null;
//                hideLoadingView();
//            }
//        });
    }

}
