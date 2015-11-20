package com.cplatform.xhxw.ui.test;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.*;

import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;

/**
 * 活动详情 Created by liang on 14-1-20.
 */
public class TsActDetailActivity extends BaseActivity {

	@Override
	protected String getScreenName() {
		return null;
	}
//
//    private static final int APPLY_NORMAL = 0;
//    private static final int APPLY_OVER = 1;
//    private static final int APPLY_ALREADY = 2;
//
//    @InjectView(R.id.layout_parent)
//    RelativeLayout mLayoutParent;
//    @InjectView(R.id.layout_bottom)
//    ViewFlipper mViewFlipper;
//    @InjectView(R.id.btn_back)
//    ImageView mBackBtn;
//    @InjectView(R.id.station_title)
//    TextView mTitleView;
//    @InjectView(R.id.btn_share)
//    TextView mMoreBtn;
//    //
//    @InjectView(R.id.tv_activity_name)
//    TextView mNameView;
//    @InjectView(R.id.tv_fqr)
//    TextView mFqrView;
//    @InjectView(R.id.tv_join_num)
//    TextView mJoinNumView;
//    @InjectView(R.id.btn_favor)
//    Button mFavorBtn;
//    @InjectView(R.id.tv_act_time)
//    TextView mTimeView;
//    @InjectView(R.id.tv_act_location)
//    TextView mLocationView;
//    @InjectView(R.id.tv_act_cost)
//    TextView mCostView;
//    @InjectView(R.id.tv_act_apply)
//    TextView mApplyView;
//    @InjectView(R.id.tv_act_html_info)
//    TextView mDetailView;
//    @InjectView(R.id.mWebView)
//    WebView mWebView;
//    @InjectView(R.id.btn_call)
//    Button mCallBtn;
//    @InjectView(R.id.btn_net)
//    Button mNetBtn;
//    @InjectView(R.id.mScrollView)
//    ScrollView mScrollView;
//    @InjectView(R.id.iv_activity)
//    ImageView mImageView;
//
//    Downloader mDownloader;
//    SharePopupHelper mShareHelper;
//    // URLImageParser mImageParser;
//
//    // param
//    String mStationId;
//    String mActivityId;
//    TsActivity mTsActivity;
//
//    boolean isActivityFavor = false;
//
//    DisplayImageOptions options;
//    /**
//     * @param context
//     * @param station_id
//     * @param activity_id
//     * @param mAct
//     * @return
//     */
//    public static Intent newIntent(Context context, String station_id,String activity_id, TsActivity mAct) {
//        Intent i = new Intent(context, TsActDetailActivity.class);
//        i.putExtra("station_id", station_id);
//        i.putExtra("activity_id", activity_id);
//        i.putExtra("activity_detail", mAct);
//        return i;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        mStationId = getIntent().getStringExtra("station_id");
//        mActivityId = getIntent().getStringExtra("activity_id");
//        mTsActivity = (TsActivity) getIntent().getSerializableExtra("activity_detail");
//        setContentView(R.layout.activity_ts_act);
//        initView();
//
//        options = new DisplayImageOptions.Builder()
//                .showStubImage(R.drawable.default_icon)
//                .showImageForEmptyUri(R.drawable.default_icon).cacheInMemory()
//                .cacheOnDisc().build();
//
//        if (mTsActivity != null) {
//            fillActivityInfo();
//        }
//        mDownloader = new Downloader();
//        doFetchActDetailList();
//    }
//
//    private void initView() {
//        mShareHelper = new SharePopupHelper(this, mLayoutParent);
//        mShareHelper.setController(mController);
//
//        mBackBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TsActDetailActivity.this.finish();
//            }
//        });
//        mMoreBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            	String activityId = (mTsActivity == null) ? null :mTsActivity.getId();
//                mShareHelper.showPopup(UMengEventId.POSITION_SHARE_ACTIVITY,null,activityId);
//            }
//        });
//        mTitleView.setText("活动");
//        mFavorBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doAddFavor();
//            }
//        });
//
//        mCallBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String mobile = mTsActivity.getApply_phone();
//                if (TextUtils.isEmpty(mobile))
//                    return;
//                doPhoneSign();
//                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"  + mobile));
//                TsActDetailActivity.this.startActivity(intent);
//            }
//        });
//        mNetBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doNetSign();
//            }
//        });
//
//        // mImageParser = new URLImageParser(mDetailView,this);
//
//        // WebSettings mWebSettings = mWebView.getSettings();
//        // mWebSettings.setUseWideViewPort(true);
//        // mWebSettings.setLoadWithOverviewMode(true);
//        mWebView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(event.getAction() == MotionEvent.ACTION_UP){
//                    mScrollView.requestDisallowInterceptTouchEvent(false);
//                }else{
//                    mScrollView.requestDisallowInterceptTouchEvent(true);
//                }
//
//                return false;
//            }
//        });
//    }
//
//    private void fillActivityInfo() {
//        mNameView.setText(mTsActivity.getTitle());
//        mFqrView.setText(mTsActivity.getStation_name());
//        if(mTsActivity.getTotal_num().equals("0")){
//        	mJoinNumView.setVisibility(View.INVISIBLE);
//        }else{
//        	mJoinNumView.setVisibility(View.VISIBLE);
//        	mJoinNumView.setText(mTsActivity.getTotal_num() + "人");
//        }
//        String applyTime = mTsActivity.getApply_time();
//        if (applyTime.equals(TsActivity.FOREVER_ACTIVITY)) {
//            mTimeView.setText("长期有效");
//        } else {
//            mTimeView.setText(mTsActivity.getStart_time() + " 至 "+ mTsActivity.getEnd_time());
//        }
//
//        StringBuilder sb = new StringBuilder();
//        sb.append(mTsActivity.getProvince_adcode()).append(mTsActivity.getCity_adcode()).append(mTsActivity.getAddress());
//        mLocationView.setText(sb.toString());
//
//        String payWay = mTsActivity.getPayment_way();
//        if (payWay.equals(TsActivity.PAY_WAY_FREE)) {
//            mCostView.setText("免费");
//        } else {
//            mCostView.setText(mTsActivity.getPayment_user_money());
//        }
//
//        String applyWay = mTsActivity.getApply_way();
//        if (!TextUtils.isEmpty(applyWay)) {
//            String[] applys = applyWay.split(",");
//            sb = new StringBuilder();
//            if (applys.length > 1) {
//                sb.append(Constants.APPLY_WAY_MAP.get(applys[0])).append("/").append(Constants.APPLY_WAY_MAP.get(applys[1]));
//            } else {
//                sb.append(Constants.APPLY_WAY_MAP.get(applys[0]));
//                if (applys[0].equals(Constants.APPLY_ONLINE)) {
//                    mNetBtn.setVisibility(View.VISIBLE);
//                    mCallBtn.setVisibility(View.GONE);
//                } else {
//                    mCallBtn.setVisibility(View.VISIBLE);
//                    mNetBtn.setVisibility(View.GONE);
//                }
//            }
//            mApplyView.setText(sb.toString());
//        }
//        //pics
//        String pics = mTsActivity.getPics();
//        if(!TextUtils.isEmpty(pics)){
//            mImageView.setVisibility(View.VISIBLE);
//            ImageLoader.getInstance().displayImage(pics,mImageView,options);
//        }
//
//        String content = mTsActivity.getContent();
//        // content = "<HTML><BODY><H3>HAHAHAHAH</H3></BODY></HTML>";
//        mWebView.loadDataWithBaseURL(null, content, "text/html", "utf-8", null);
//
//        // 判断收藏情况
//        if (mTsActivity.getIs_collect() == Constants.HAS_DONE) {
//            isActivityFavor = true;
//            mFavorBtn.setText("已收藏");
//        } else {
//            isActivityFavor = false;
//            mFavorBtn.setText("收藏");
//        }
//        // 判断是否已经报名，活动是否已经结束
//        // test
//        // mTsActivity.setStatus("0");
//        //mTsActivity.setIs_apply(0);
//        //判断是否报名是走is_applay是否支付的状态来判断
//        if (mTsActivity.getStatus().equals("2")) {
//            if (mTsActivity.getIs_apply() == Constants.HAS_DONE) {
//                mViewFlipper.setDisplayedChild(APPLY_ALREADY);
//            } else {
//                mViewFlipper.setDisplayedChild(APPLY_NORMAL);
//            }
//        } else {
//            mViewFlipper.setDisplayedChild(APPLY_OVER);
//        }
//        //我参与了“发起者”的“活动名称”活动。超有趣好好玩！快来看看吧！+下载地址@听说交通
//        StringBuilder shareContent = new StringBuilder("我参与了\"");
//        shareContent.append(mTsActivity.getTitle()).append("\"活动").append("。超有趣好好玩！快来看看吧！").append(DownloaderUtil.SHARE_DOWN_ADDRESS).append("@交通电台");
//        mShareHelper.setController(mController, shareContent.toString(), R.drawable.share_activity_logo);
//    }
//
//    private void doPhoneSign() {
//        AjaxParams params = new AjaxParams();
//        params.put("user_id", RuntimeUser.getInstance().getUserId());
//        params.put("activity_id", mTsActivity.getId());
//        FinalHttp mFinal = new FinalHttp();
//        mFinal.post(DownloaderUtil.PHONE_SIGN_URL, params,
//                new AjaxCallBack<String>() {
//                    @Override
//                    public void onSuccess(String o) {
//                        LogUtil.e("Liang", "doPhoneSign:" + o);
//                    }
//                });
//    }
//
//    private void doNetSign() {
//        if (RuntimeUser.getInstance().isLogin()) {
//            if (RuntimeUser.getInstance().getDBUser().getIs_verify() != 1) {
//                Intent intent = new Intent(this, VerifyPhoneActivity.class);
//                startActivity(intent);
//            } else {
//                Intent mIntent = ApplyActivity.newInstance(this, mTsActivity);
////                startActivity(mIntent);
//                startActivityForResult(mIntent, 0);
//            }
//        } else {
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);
//        }
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        /*
//		 * if(resultCode == RESULT_OK){
//		 * mViewFlipper.setDisplayedChild(APPLY_ALREADY); }
//		 */
//        /** 使用SSO授权必须添加如下代码 */
//        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(requestCode);
//        if (ssoHandler != null) {
//            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
//        }
//
//    }
//
//    FetchActDetailTask mInfoTask;
//
//    private void doFetchActDetailList() {
//        if (mInfoTask != null && !mInfoTask.isCancelled()) {
//            mInfoTask.cancel(true);
//            mInfoTask = null;
//        }
//        mInfoTask = new FetchActDetailTask();
//        mInfoTask.execute();
//    }
//
//    class FetchActDetailTask extends AsyncTask<Void, Void, Integer> {
//
//        @Override
//        protected void onPreExecute() {
//        }
//
//        @Override
//        protected Integer doInBackground(Void... params) {
//            try {
//                ZHResponse<TsActivity> mResp = mDownloader.getActivity(mStationId, mActivityId);
//                if (mResp.getCode() == Constants.STATUS_OK) {
//                    mTsActivity = mResp.getData();
//                    return Constants.RETURN_SUCCESS;
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//                return Constants.RETURN_EXCEPTION;
//            }
//            return Constants.RETURN_FAILED;
//        }
//
//        @Override
//        protected void onPostExecute(Integer result) {
//            if (result == Constants.RETURN_SUCCESS) {
//                fillActivityInfo();
//            }
//        }
//    }
//
//    AddFavorTask mFavorTask;
//
//    private void doAddFavor() {
//        if (mFavorTask != null && !mFavorTask.isCancelled()) {
//            mFavorTask.cancel(true);
//            mFavorTask = null;
//        }
//
//        mFavorTask = new AddFavorTask(isActivityFavor);
//        mFavorTask.execute();
//    }
//
//    /**
//     * 添加/取消收藏
//     */
//    class AddFavorTask extends AsyncTask<String, Void, Integer> {
//
//        boolean isFavor;
//
//        public AddFavorTask(boolean isFavor) {
//            this.isFavor = isFavor;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            showDialog();
//        }
//
//        @Override
//        protected Integer doInBackground(String... params) {
//            try {
//                SimpleResponse mResp = null;
//                if (!isFavor) {
//                    mResp = mDownloader.addFavor(mStationId, mActivityId,Constants.FAVOR_ACTIVITY);
//                } else {
//                    mResp = mDownloader.cancelFavor(mStationId, mActivityId,Constants.FAVOR_ACTIVITY);
//                }
//
//                if (mResp.getCode() == Constants.STATUS_OK) {
//                    return Constants.RETURN_SUCCESS;
//                }
//            } catch (Exception e) {
//                return Constants.RETURN_EXCEPTION;
//            }
//            return Constants.RETURN_FAILED;
//        }
//
//        @Override
//        protected void onPostExecute(Integer result) {
//            dismissDialog();
//            if (result == Constants.RETURN_SUCCESS) {
//                if (isFavor) {
//                    ToastUtil.show(TsActDetailActivity.this, "取消收藏成功");
//                    mFavorBtn.setText("收藏");
//                    isActivityFavor = false;
//                 } else {
//                    ToastUtil.show(TsActDetailActivity.this, "收藏成功");
//                    mFavorBtn.setText("已收藏");
//                    isActivityFavor = true;
//                }
//            } else {
//                if (isFavor) {
//                    ToastUtil.show(TsActDetailActivity.this, "取消收藏失败");
//                } else {
//                    ToastUtil.show(TsActDetailActivity.this, "收藏失败");
//                }
//            }
//        }
//    }
//    public UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share", RequestType.SOCIAL);
}
