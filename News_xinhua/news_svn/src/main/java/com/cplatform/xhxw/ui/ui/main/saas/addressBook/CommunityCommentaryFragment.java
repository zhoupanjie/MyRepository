package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import android.content.*;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.PreferencesManager;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.httputils.HttpHanderUtil;
import com.cplatform.xhxw.ui.http.net.saas.CompanyZoneCommentSubRequest;
import com.cplatform.xhxw.ui.http.net.saas.CompanyZoneCommentSubResponse;
import com.cplatform.xhxw.ui.http.net.saas.FriendZoneCommentSubRequest;
import com.cplatform.xhxw.ui.http.net.saas.FriendZoneCommentSubResponse;
import com.cplatform.xhxw.ui.model.saas.CommentSubData;
import com.cplatform.xhxw.ui.ui.base.BaseFragment;
import com.cplatform.xhxw.ui.ui.base.widget.InputViewSensitiveLinearLayout;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionManager;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionUtil;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionWidgt;
import com.cplatform.xhxw.ui.util.*;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.HashMap;

/**
 * 社区评论
 * Created by cy-love on 14-8-27.
 */
public class CommunityCommentaryFragment extends BaseFragment implements InputViewSensitiveLinearLayout.OnInputViewListener, XWExpressionWidgt.onExprItemClickListener {

    private AsyncHttpResponseHandler mHttpHandler;
    @InjectView(R.id.comment_editt)
    EditText editText;
    @InjectView(R.id.comment_expression_widgt)
    XWExpressionWidgt mExpressionWidgt;
    @InjectView(R.id.comment_expression_btn)
    Button mExprBtn;
    @InjectView(R.id.layout_content)
    InputViewSensitiveLinearLayout mRootContainer;

    private boolean mIsExprShow = false;
    private boolean mIsSoftKeyboardShow = false;
    private InputMethodManager mImm;
    private Handler mUiHandler = new Handler();
    private int mSoftKeyboardHeight = 0;
    private int mOriginalHeight = 0;
    private boolean isExprAreaResized = false;

    private boolean isCompanyCircle; // true为公司圈评论 否则为朋友圈
    private OnCommunityCommentaryListener mComLis;

    /**
     * 好友圈评论
     *
     * @param isCompanyCircle
     * @param infoid
     * @param userid
     * @param commentid
     * @param hint            默认显示内容
     * @return
     */
    public static CommunityCommentaryFragment createFragment(boolean isCompanyCircle, String infoid, String infouserid, String userid, String commentid, String hint, OnCommunityCommentaryListener commentaryListener) {
        Bundle bundle = new Bundle();
        bundle.putString("infoid", infoid);
        bundle.putString("userid", userid);
        bundle.putString("commentid", commentid);
        bundle.putString("infouserid", infouserid);
        bundle.putString("hint", hint);
        bundle.putBoolean("isCompanyCircle", isCompanyCircle);
        CommunityCommentaryFragment fragment = new CommunityCommentaryFragment();
        fragment.setArguments(bundle);
        fragment.setOnCommunityCommentaryListener(commentaryListener);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_community_commentary, null);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        editText.setHint(getArguments().getString("hint"));
        isCompanyCircle = getArguments().getBoolean("isCompanyCircle");
        mImm = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        getView().findViewById(R.id.ly_other).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    finish();
                    KeyboardUtil.hideSoftInput(getActivity());
                    if (mComLis != null) {
                        mComLis.onHideCommunityCommentary();
                    }
                    getFragmentManager().beginTransaction().remove(CommunityCommentaryFragment.this).commit();
                    return true;
                }
                return false;
            }
        });
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
        } else {
            mSoftKeyboardHeight = Constants.screenHeight / 3;
        }

        /**
         * 测量软键盘所占区域高度
         */
        mRootContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                if (mRootContainer != null) {
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
            }
        });
        mExpressionWidgt.setmOnExprItemClickListener(this);
        resizeExprArea(Constants.screenWidth);
        KeyboardUtil.showSoftInput(editText);
    }

    @OnClick(R.id.comment_sendbtn)
    public void senAction() {
        String infoid = getArguments().getString("infoid");
        String userid = getArguments().getString("userid");
        String commentid = getArguments().getString("commentid");
        String content = editText.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            showToast("请输入内容");
            return;
        }
        KeyboardUtil.hideSoftInput(getActivity());

        if (isCompanyCircle) {
            companyZoneCommentSub(infoid, userid, commentid, content);
        } else {
            String infouserid = getArguments().getString("infouserid");
            friendZoneCommentSub(infoid, userid, commentid, content, infouserid);
        }
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
                mExprBtn.setBackgroundResource(R.drawable.selector_s_chat_keyboard);
            } else {
                mIsExprShow = false;
                mIsSoftKeyboardShow = true;
                mExpressionWidgt.setVisibility(View.GONE);
                mExprBtn.setBackgroundResource(R.drawable.selector_s_chat_expressions);
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
            ss = XWExpressionUtil.generateSpanComment(getActivity().getApplicationContext(),
                    XWExpressionUtil.deleteOneWord(editText.getText().toString(), mExprFilenameIdData),
                    textSize);
        } else {
            String content = editText.getText() + exprInfo;
            ss = XWExpressionUtil.generateSpanComment(getActivity().getApplicationContext(), content,
                    textSize);
        }
        editText.setText(ss);
        editText.setSelection(ss.length());
    }

    @Override
    public void onShowInputView() {
        editText.setHint("");
    }

    @Override
    public void onHideInputView() {
        if (!mIsExprShow && mExprBtn != null) {
            mExprBtn.setBackgroundResource(R.drawable.selector_s_chat_expressions);
        }
    }

    /**
     * 回复公司圈消息
     */
    private void companyZoneCommentSub(String infoId, String userId, String commentId, final String content) {
        APIClient.companyZoneCommentSub(new CompanyZoneCommentSubRequest(infoId, userId, commentId, content), new AsyncHttpResponseHandler() {
            @Override
            protected void onPreExecute() {
                HttpHanderUtil.cancel(mHttpHandler);
                mHttpHandler = this;
                showLoadingView();
            }

            @Override
            public void onSuccess(int statusCode, String result) {
                CompanyZoneCommentSubResponse response;
                try {
                    ResponseUtil.checkResponse(result);
                    response = new Gson().fromJson(result, CompanyZoneCommentSubResponse.class);
                } catch (Exception e) {
                    showToast(R.string.data_format_error);
                    return;
                }
                if (response.isSuccess() && response.getData() != null) {
                    if (mComLis != null) {
                        CompanyZoneCommentSubResponse.Data dataRes = response.getData();
                        setResult(dataRes.getCommentid(), content, dataRes.getCtime());
                    }
                    hideLoadingView();
                    getFragmentManager().beginTransaction().remove(CommunityCommentaryFragment.this).commit();
                } else {
                    showToast(response.getMsg());
                }
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

    /**
     * 回复公司圈消息
     */
    private void friendZoneCommentSub(String infoId, String userId, String commentId, final String content, String infouserid) {
        APIClient.friendZoneCommentSub(new FriendZoneCommentSubRequest(infoId, userId, commentId, content, infouserid), new AsyncHttpResponseHandler() {
            @Override
            protected void onPreExecute() {
                HttpHanderUtil.cancel(mHttpHandler);
                mHttpHandler = this;
                showLoadingView();
            }

            @Override
            public void onSuccess(int statusCode, String result) {
                FriendZoneCommentSubResponse response;
                try {
                    ResponseUtil.checkResponse(result);
                    response = new Gson().fromJson(result, FriendZoneCommentSubResponse.class);
                } catch (Exception e) {
                    showToast(R.string.data_format_error);
                    return;
                }
                if (response.isSuccess() && response.getData() != null) {
                    if (mComLis != null) {
                        FriendZoneCommentSubResponse.Data dataRes = response.getData();
                        setResult(dataRes.getCommentid(), content, dataRes.getCtime());
                        mComLis.onHideCommunityCommentary();
                    }
                    hideLoadingView();
                    getFragmentManager().beginTransaction().remove(CommunityCommentaryFragment.this).commit();
                } else {
                    showToast(response.getMsg());
                }
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

    /**
     * 设置返回内容
     *
     * @param commentId 评论id
     * @param content   评论内容
     * @param cTime     评论时间
     */
    private void setResult(String commentId, String content, String cTime) {
        CommentSubData data = new CommentSubData();
        data.setCompanyCircle(isCompanyCircle);
        data.setCommentid(commentId);
        data.setContent(content);
        data.setCtime(cTime);
        mComLis.onCommentarySutmit(data);
        mComLis.onHideCommunityCommentary();
    }

    @Override
    public void onDestroyView() {
        hideLoadingView();
        HttpHanderUtil.cancel(mHttpHandler);
        super.onDestroyView();
    }

    /**
     * 添加回调监听
     *
     * @param mComLis
     */
    public void setOnCommunityCommentaryListener(OnCommunityCommentaryListener mComLis) {
        this.mComLis = mComLis;
    }
}