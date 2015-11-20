package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.PreferencesManager;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.httputils.HttpHanderUtil;
import com.cplatform.xhxw.ui.http.net.DeleteFriendsFreshsMoodCommentRequest;
import com.cplatform.xhxw.ui.http.net.DeleteFriendsFreshsMoodCommentRsponse;
import com.cplatform.xhxw.ui.http.net.DeleteFriendsFreshsMoodRequest;
import com.cplatform.xhxw.ui.http.net.DeleteFriendsFreshsMoodResponse;
import com.cplatform.xhxw.ui.http.net.FriendsFreshsMoodInfoRequest;
import com.cplatform.xhxw.ui.http.net.FriendsFreshsMoodInfoResponse;
import com.cplatform.xhxw.ui.http.net.saas.FriendZoneCommentSubRequest;
import com.cplatform.xhxw.ui.http.net.saas.FriendZoneCommentSubResponse;
import com.cplatform.xhxw.ui.http.net.saas.FriendZoneParisaSubRequest;
import com.cplatform.xhxw.ui.http.net.saas.FriendZoneParisaSubResponse;
import com.cplatform.xhxw.ui.model.FriendsFreshsMoodInfoDataComment;
import com.cplatform.xhxw.ui.model.FriendsFreshsMoodInfoDataCommentReply;
import com.cplatform.xhxw.ui.model.FriendsFreshsMoodInfoDataCommentSender;
import com.cplatform.xhxw.ui.plugin.gallery.PicShowActivity;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.widget.CircleImageView;
import com.cplatform.xhxw.ui.ui.base.widget.InputViewSensitiveLinearLayout;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionManager;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionUtil;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionWidgt;
import com.cplatform.xhxw.ui.ui.main.saas.addressBook.FriendsFreshInfoNativeAdapter.onDeleteMoodCommentListener;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.cplatform.xhxw.ui.util.EditUtil;
import com.cplatform.xhxw.ui.util.FriendsFreshInfoNativePopu;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.cplatform.xhxw.ui.util.SelectNameUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 朋友圈新鲜事详情 --- 本地
 */
public class FriendsFreshInfoNativeActivity extends BaseActivity implements
		OnItemClickListener, OnClickListener, onDeleteMoodCommentListener, InputViewSensitiveLinearLayout.OnInputViewListener, XWExpressionWidgt.onExprItemClickListener {

	private static final String TAG = FriendsFreshInfoNativeActivity.class
			.getSimpleName();

	private AsyncHttpResponseHandler mHttpResponseHandler;

	private CircleImageView userLogoImage;
	private TextView userNameText;
	private TextView contentText;
	private ImageView firstImage;
	private ImageView secondImage;
	private ImageView thirdImage;
	private ImageView forthImage;
	private ImageView fiveImage;
	private ImageView sixImage;
	private TextView timeText;
	private TextView deleteText;
	private ImageView managerImage;
	private LinearLayout parisaLayout;
	private LinearLayout parisaPersonLayout;
	private ImageView arrowView;
	private View lineView;
	private ListView mListView;

	private EditText mEditText;
	private XWExpressionWidgt mExpressionWidgt;
	private Button mExprBtn;
	private Button sendBtn;
	private InputViewSensitiveLinearLayout mRootContainer;
    private RelativeLayout mCommentOperateLo;
    private View mToolBar;
    
	private View headView;
	
	private FriendsFreshInfoNativeAdapter mAdapter;
	
	private boolean mIsExprShow = false;
    private boolean mIsSoftKeyboardShow = false;
    private InputMethodManager mImm;
    private Handler mUiHandler = new Handler();
    private int mSoftKeyboardHeight = 0;
    private int mOriginalHeight = 0;
    private int mRootContainerBottom = 0;
    private boolean isExprAreaResized = false;
    
	private String infoid;
	private String createUserId;
	private String parisa;//'		: 0,//是否已赞 1是 0否
	private String isparisa;//'	        : '1',//是否可点赞 1是 2否
	private String iscomment;//'      :  '1', // 是否可评论，1是，2否
	
	/**被回复者id*/
	private String replyerId;
	/**被回复者昵称*/
	private String replyerName;
	/**评论id*/
	private String commentId;
	
	private List<String> images = new ArrayList<String>();

	public static Intent newIntent(Context context, String infoid) {
		Intent intent = new Intent(context,
				FriendsFreshInfoNativeActivity.class);
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

		setContentView(R.layout.activity_friends_fresh_mood_native_info);

		init();

		initView();
		
		setListener();

		getFriendsFreshsMoodInfo(infoid);
	}

	private void init() {
		infoid = getIntent().getStringExtra("infoid");
		
		initActionBar();

		headView = LayoutInflater.from(this).inflate(R.layout.activity_friends_fresh_mood_native_info_head, null);
		
		userLogoImage = (CircleImageView) headView.findViewById(R.id.fresh_mood_info_user_logo);
		userNameText = (TextView) headView.findViewById(R.id.fresh_mood_info_user_name);
		contentText = (TextView) headView.findViewById(R.id.fresh_mood_info_user_content);
		firstImage = (ImageView) headView.findViewById(R.id.fresh_mood_info_first);
		secondImage = (ImageView) headView.findViewById(R.id.fresh_mood_info_second);
		thirdImage = (ImageView) headView.findViewById(R.id.fresh_mood_info_third);
		forthImage = (ImageView) headView.findViewById(R.id.fresh_mood_info_forth);
		fiveImage = (ImageView) headView.findViewById(R.id.fresh_mood_info_five);
		sixImage = (ImageView) headView.findViewById(R.id.fresh_mood_info_six);
		timeText = (TextView) headView.findViewById(R.id.fresh_mood_info_time);
		deleteText = (TextView) headView.findViewById(R.id.fresh_mood_info_delete);
		managerImage = (ImageView) headView.findViewById(R.id.fresh_mood_info_commentary_option);
		parisaLayout = (LinearLayout) headView.findViewById(R.id.parisa_layout);
		parisaPersonLayout = (LinearLayout) headView.findViewById(R.id.parisa_person_layout);
		arrowView = (ImageView) headView.findViewById(R.id.arrow);
		lineView = (View) headView.findViewById(R.id.ly_like_line);
		
		mEditText = (EditText) findViewById(R.id.comment_editt);
		mExpressionWidgt = (XWExpressionWidgt) findViewById(R.id.comment_expression_widgt);
		mExprBtn = (Button) findViewById(R.id.comment_expression_btn);
		sendBtn = (Button) findViewById(R.id.comment_sendbtn);
		mRootContainer = (InputViewSensitiveLinearLayout)findViewById(R.id.layout_content);
		mCommentOperateLo = (RelativeLayout) findViewById(R.id.comment_editt_linear);
		mToolBar = (View) findViewById(R.id.toolbar_layout);
		
		mListView = (ListView) findViewById(R.id.fresh_mood_info_comment_listview);
		mAdapter = new FriendsFreshInfoNativeAdapter(this);
		
		mListView.addHeaderView(headView);
		mAdapter.setOnDeleteMoodListener(this);
		mListView.setAdapter(mAdapter);

	}

	private void setListener() {
		firstImage.setOnClickListener(this);
		secondImage.setOnClickListener(this);
		thirdImage.setOnClickListener(this);
		forthImage.setOnClickListener(this);
		fiveImage.setOnClickListener(this);
		sixImage.setOnClickListener(this);
		deleteText.setOnClickListener(this);
		managerImage.setOnClickListener(this);
		mListView.setOnItemClickListener(this);
		
		sendBtn.setOnClickListener(this);
		mExprBtn.setOnClickListener(this);
	}
	
	private void initView() {
		mImm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
		mRootContainer.setOnInputViewListener(this);
		mEditText.setOnTouchListener(new View.OnTouchListener() {
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
                        PreferencesManager.saveSoftKeyboardHeight(FriendsFreshInfoNativeActivity.this, mSoftKeyboardHeight);
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

	/**加载不图片*/
	private void imageLoader(ImageView imageView, String url) {
		ImageLoader.getInstance().displayImage(url, imageView,
				DisplayImageOptionsUtil.newsMultiHorImgOptions);
	}

	/**
	 * 根据屏幕的宽度，设置小图片的大小
	 */
	private void setShape(ImageView imageView) {
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView
				.getLayoutParams();
		params.width = getSmallWight();
		params.height = getSmallWight();
		imageView.setLayoutParams(params);
	}

	/**
	 * 根据屏幕的宽度，获得小图片的宽度
	 * <p/>
	 * 屏幕的宽度 - 左右两测的距离(margin) - 左侧个人头像所占的宽度 - 图片之间的宽度
	 */
	private int getSmallWight() {
		/** 获得屏幕的宽度 */
		int leftMargin = this.getResources().getDimensionPixelOffset(
				R.dimen.event_meeting_record_left_margin);
		int rightMargin = this.getResources().getDimensionPixelOffset(
				R.dimen.event_meeting_record_right_margin);
		int leftLayoutDistance = this.getResources().getDimensionPixelOffset(
				R.dimen.event_meeting_record_left_layout);
		int imageBetweenDistance = this.getResources().getDimensionPixelOffset(
				R.dimen.hds_small_margin);
		return (Constants.screenWidth - leftMargin - rightMargin
				- leftLayoutDistance - imageBetweenDistance * 2) / 3;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		HttpHanderUtil.cancel(mHttpResponseHandler);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {

		if (position == 0) {
			return;
		}
		
		mToolBar.setVisibility(View.VISIBLE);
		
		/**回复这条评论的发起人*/
		if (!TextUtils.isEmpty(mAdapter.getData().get(position - 1).getSenduser().getUserid())) {
			/**判断“被回复的人”是否是“自己”,如果是，则相当于发起一条新的评论，不是，则回复这个评论的发起人*/
			if (mAdapter.getData().get(position - 1).getSenduser().getUserid().equals(Constants.getUid())) {
				replyerId = "";
				replyerName = "";
           	 	commentId = "";
           	 	
           	    mEditText.setHint("");
			}else {
				replyerId = mAdapter.getData().get(position - 1).getSenduser().getUserid();
				replyerName = SelectNameUtil.getName("", mAdapter.getData().get(position - 1).getSenduser().getNickname(), mAdapter.getData().get(position - 1).getSenduser().getName());
           	 	commentId = mAdapter.getData().get(position - 1).getCommentid();
           	 	
           	    mEditText.setHint("回复 " + replyerName);
			}
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.fresh_mood_info_first:
			startActivity(0);
			break;
		case R.id.fresh_mood_info_second:
			startActivity(1);
			break;
		case R.id.fresh_mood_info_third:
			startActivity(2);
			break;
		case R.id.fresh_mood_info_forth:
			startActivity(3);
			break;
		case R.id.fresh_mood_info_five:
			startActivity(4);
			break;
		case R.id.fresh_mood_info_six:
			startActivity(5);
			break;
		case R.id.fresh_mood_info_delete:
			/**调用删除接口*/
			deleteFriendsFreshsMood(infoid);
			break;
		case R.id.fresh_mood_info_commentary_option:
			 final FriendsFreshInfoNativePopu pop = new FriendsFreshInfoNativePopu(this, "1".equals(parisa));
			 pop.setListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     pop.dismiss();
                     /**判断此新鲜事是否允许赞*/
                     if ("1".equals(isparisa)) {
                    	 friendZoneParisaSub(infoid, createUserId);
                     } else {
                         showToast(R.string.disable_parisa);
                     }
                 }
             }, new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     pop.dismiss();
                     /**判断此新鲜事是否允许评论*/
                     if ("1".equals(iscomment)) {
                    	 replyerId = "";
                    	 commentId = "";
                    	 replyerName = "";
                    	 
                    	 mEditText.setHint("");
                    	 
                    	 mEditText.requestFocus();
             			InputMethodManager imm = (InputMethodManager) mEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
             			imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                     } else {
                         showToast(R.string.disable_comments);
                     }
                 }
             });
             pop.show(managerImage);
			break;
		case R.id.comment_sendbtn:
			if (!TextUtils.isEmpty(mEditText
					.getText().toString().trim())) {
				friendZoneCommentSub(infoid, replyerId, commentId, mEditText.getText().toString(), createUserId);
			}
			break;
		case R.id.comment_expression_btn:
			 if (mIsExprShow) {
		         controlKeyboardOrExpr(true, true);
		     } else {
		         controlKeyboardOrExpr(false, true);
		     }
			break;
		}
	}

	/**跳转图片预览页面*/
	private void startActivity(int position) {
		startActivity(PicShowActivity.newIntent(this, images, position, false));
	}
	
	/** 获取新鲜事详情 */
	private void getFriendsFreshsMoodInfo(String infoid) {

		FriendsFreshsMoodInfoRequest request = new FriendsFreshsMoodInfoRequest();
		request.setInfoid(infoid);
		request.setDevRequest(true);

		APIClient.getFriendsFreshsMoodInfo(request,
				new AsyncHttpResponseHandler() {

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

						showLoadingView("正在加载...");
					}

					@Override
					public void onSuccess(String content) {
						LogUtil.d(content);
						FriendsFreshsMoodInfoResponse response;
						try {
							response = new Gson().fromJson(content,
									FriendsFreshsMoodInfoResponse.class);
							ResponseUtil.checkObjResponse(response);
						} catch (Exception e) {
							showToast(R.string.data_format_error);
							LogUtil.w(TAG, e);
							return;
						}

						if (response.isSuccess()) {
							if (response.getData() != null) {
								
								managerImage.setVisibility(View.VISIBLE);
								
								if (response.getData().getUserinfo() != null) {
									imageLoader(userLogoImage, response
											.getData().getUserinfo().getLogo());
								}
								if (response.getData().getUserinfo() != null) {
									userNameText.setText(SelectNameUtil.getName(
											response.getData().getUserinfo()
													.getComment(), response
													.getData().getUserinfo()
													.getNickname(), response
													.getData().getUserinfo()
													.getName()));
									
									if (!TextUtils.isEmpty(response.getData().getUserinfo().getUserid())) {
										if (response.getData().getUserinfo().getUserid().equals(Constants.getUid())) {
											/**这条新鲜事 ，是“我”发布的*/
											createUserId = response.getData().getUserinfo().getUserid();
											deleteText.setText("删除");
											deleteText.setVisibility(View.VISIBLE);
										}
									}
								}
								
								parisa = response.getData().getParisa();
								isparisa = response.getData().getIsparisa();
								iscomment = response.getData().getIscomment();  
								
								contentText.setText(response.getData()
										.getContent());
								timeText.setText(response.getData().getFtime());
								
								/**赞和评论*/
								if (response.getData().getCommentdata() != null
										&& response.getData().getCommentdata()
												.size() > 0) {//有评论
									
									arrowView.setVisibility(View.VISIBLE);
									
									/**判断是否有“赞”*/
									if (response.getData().getParisadata().getCount() > 0) {//有赞
										lineView.setVisibility(View.VISIBLE);
										parisaLayout.setVisibility(View.VISIBLE);
										parisaLayout.setBackgroundResource(R.drawable.freshs_mood_info_top_round);
										mAdapter.setHaveParis(true);
										
										/**显示赞的人员*/
										if (response.getData().getParisadata().getList() != null && response.getData().getParisadata().getList().size() > 0) {
											/**初始化“赞”布局的总大小， 屏幕的宽度 - 左右两侧的距离 - "赞"图标两侧的距离 - "赞"图标的大小*/
											int residueWidth = Constants.screenWidth - getResources().getDimensionPixelOffset(R.dimen.freshs_mood_info_margin) * 2
													                                    - getResources().getDimensionPixelOffset(R.dimen.parisa_margin_left) 
													                                    - getResources().getDimensionPixelOffset(R.dimen.parisa_margin_right) 
													                                    - getResources().getDimensionPixelOffset(R.dimen.parisa_tubiao_comment_tubiao_size);
											parisaPersonLayout.removeAllViews();
											for (int i = 0; i < response.getData().getParisadata().getList().size(); i++) {
												/**判断父布局的剩余空间是否可以继续添加*/
												if (residueWidth > getResources().getDimensionPixelOffset(R.dimen.parisa_user_logo_size) + getResources().getDimensionPixelOffset(R.dimen.parisa_user_logo_distance)) {
													CircleImageView imageView = new CircleImageView(FriendsFreshInfoNativeActivity.this);
													imageView.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDimensionPixelOffset(R.dimen.parisa_user_logo_size),
															 getResources().getDimensionPixelOffset(R.dimen.parisa_user_logo_size)));
													ImageLoader.getInstance().displayImage(response.getData().getParisadata().getList().get(i).getLogo(),
															imageView, DisplayImageOptionsUtil.avatarSaasImagesOptions);
													parisaPersonLayout.addView(imageView);
													/**设置图片的大小*/
													LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
													params.rightMargin = getResources().getDimensionPixelOffset(R.dimen.parisa_user_logo_distance);
													imageView.setLayoutParams(params);
													
													/**赞”布局的剩余大小*/
													residueWidth = residueWidth - getResources().getDimensionPixelOffset(R.dimen.parisa_user_logo_size) -
															 getResources().getDimensionPixelOffset(R.dimen.parisa_user_logo_distance);
												}else {
													break;
												}
											}
										}
									}else {//没有赞
										parisaLayout.setVisibility(View.GONE);
										mAdapter.setHaveParis(false);
									}
									mAdapter.clearData();
									mAdapter.addAllData(response.getData()
											.getCommentdata());
									mAdapter.notifyDataSetChanged();
								}else {//没有评论
									/**判断是否有“赞”*/
									if (response.getData().getParisadata().getCount() > 0) {//有赞
										
										arrowView.setVisibility(View.VISIBLE);
										
										parisaLayout.setVisibility(View.VISIBLE);
										parisaLayout.setBackgroundResource(R.drawable.freshs_mood_info_round);
										mAdapter.setHaveParis(true);
										
										/**显示赞的人员*/
										if (response.getData().getParisadata().getList() != null && response.getData().getParisadata().getList().size() > 0) {
											/**父布局的总大小*/
											int parentWidth = Constants.screenWidth - getResources().getDimensionPixelOffset(R.dimen.parisa_margin_left) + getResources().getDimensionPixelOffset(R.dimen.parisa_margin_right);
											/**父布局剩余的大小， 初始化的时候等于父布局的大小*/
											int residueWidth = parentWidth;
											parisaPersonLayout.removeAllViews();
											for (int i = 0; i < response.getData().getParisadata().getList().size(); i++) {
												/**判断父布局的剩余空间是否可以继续添加*/
												if (residueWidth > getResources().getDimensionPixelOffset(R.dimen.parisa_user_logo_size) + getResources().getDimensionPixelOffset(R.dimen.parisa_user_logo_distance)) {
													CircleImageView imageView = new CircleImageView(FriendsFreshInfoNativeActivity.this);
													imageView.setLayoutParams(new LinearLayout.LayoutParams(getResources().getDimensionPixelOffset(R.dimen.parisa_user_logo_size),
															 getResources().getDimensionPixelOffset(R.dimen.parisa_user_logo_size)));
													ImageLoader.getInstance().displayImage(response.getData().getParisadata().getList().get(i).getLogo(),
															imageView, DisplayImageOptionsUtil.avatarSaasImagesOptions);
													parisaPersonLayout.addView(imageView);
													
													/**设置图片的大小*/
													LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
													params.leftMargin = getResources().getDimensionPixelOffset(R.dimen.parisa_user_logo_distance);
													imageView.setLayoutParams(params);
													
													parentWidth = parentWidth - getResources().getDimensionPixelOffset(R.dimen.parisa_user_logo_size) +
															 getResources().getDimensionPixelOffset(R.dimen.parisa_user_logo_distance);
												}else {
													break;
												}
											}
										}
									}else {//没有赞
										parisaLayout.setVisibility(View.GONE);
										mAdapter.setHaveParis(false);
									}
								}
								
								/**发布的图片*/
								if (response.getData().getExrta() != null
										&& response.getData().getExrta().size() > 0) {

									for (int i = 0; i < response.getData().getExrta().size(); i++) {
										images.add(response.getData().getExrta().get(i).getThumb());
									}
									
									switch (response.getData().getExrta()
											.size()) {
									case 1:
										firstImage.setVisibility(View.VISIBLE);
										setShape(firstImage);
										imageLoader(firstImage, response
												.getData().getExrta().get(0)
												.getThumb());
										break;
									case 2:
										firstImage.setVisibility(View.VISIBLE);
										secondImage.setVisibility(View.VISIBLE);
										setShape(firstImage);
										setShape(secondImage);
										imageLoader(firstImage, response
												.getData().getExrta().get(0)
												.getThumb());
										imageLoader(secondImage, response
												.getData().getExrta().get(1)
												.getThumb());
										break;
									case 3:
										firstImage.setVisibility(View.VISIBLE);
										secondImage.setVisibility(View.VISIBLE);
										thirdImage.setVisibility(View.VISIBLE);
										setShape(firstImage);
										setShape(secondImage);
										setShape(thirdImage);
										imageLoader(firstImage, response
												.getData().getExrta().get(0)
												.getThumb());
										imageLoader(secondImage, response
												.getData().getExrta().get(1)
												.getThumb());
										imageLoader(thirdImage, response
												.getData().getExrta().get(2)
												.getThumb());
										break;
									case 4:
										firstImage.setVisibility(View.VISIBLE);
										secondImage.setVisibility(View.VISIBLE);
										thirdImage.setVisibility(View.VISIBLE);
										forthImage.setVisibility(View.VISIBLE);
										setShape(firstImage);
										setShape(secondImage);
										setShape(thirdImage);
										setShape(forthImage);
										imageLoader(firstImage, response
												.getData().getExrta().get(0)
												.getThumb());
										imageLoader(secondImage, response
												.getData().getExrta().get(1)
												.getThumb());
										imageLoader(thirdImage, response
												.getData().getExrta().get(2)
												.getThumb());
										imageLoader(forthImage, response
												.getData().getExrta().get(3)
												.getThumb());
										break;
									case 5:
										firstImage.setVisibility(View.VISIBLE);
										secondImage.setVisibility(View.VISIBLE);
										thirdImage.setVisibility(View.VISIBLE);
										forthImage.setVisibility(View.VISIBLE);
										fiveImage.setVisibility(View.VISIBLE);
										setShape(firstImage);
										setShape(secondImage);
										setShape(thirdImage);
										setShape(forthImage);
										setShape(fiveImage);
										imageLoader(firstImage, response
												.getData().getExrta().get(0)
												.getThumb());
										imageLoader(secondImage, response
												.getData().getExrta().get(1)
												.getThumb());
										imageLoader(thirdImage, response
												.getData().getExrta().get(2)
												.getThumb());
										imageLoader(forthImage, response
												.getData().getExrta().get(3)
												.getThumb());
										imageLoader(fiveImage, response
												.getData().getExrta().get(4)
												.getThumb());
										break;
									case 6:
										firstImage.setVisibility(View.VISIBLE);
										secondImage.setVisibility(View.VISIBLE);
										thirdImage.setVisibility(View.VISIBLE);
										forthImage.setVisibility(View.VISIBLE);
										fiveImage.setVisibility(View.VISIBLE);
										sixImage.setVisibility(View.VISIBLE);
										setShape(firstImage);
										setShape(secondImage);
										setShape(thirdImage);
										setShape(forthImage);
										setShape(fiveImage);
										setShape(sixImage);
										imageLoader(firstImage, response
												.getData().getExrta().get(0)
												.getThumb());
										imageLoader(secondImage, response
												.getData().getExrta().get(1)
												.getThumb());
										imageLoader(thirdImage, response
												.getData().getExrta().get(2)
												.getThumb());
										imageLoader(forthImage, response
												.getData().getExrta().get(3)
												.getThumb());
										imageLoader(fiveImage, response
												.getData().getExrta().get(4)
												.getThumb());
										imageLoader(sixImage, response
												.getData().getExrta().get(5)
												.getThumb());
										break;
									}
								}

							}
						} else {
							showToast(response.getMsg());
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						LogUtil.d(content);
						showToast(R.string.load_server_failure);
					}
				});
	}
	

	/** 删除新鲜事 */
	private void deleteFriendsFreshsMood(String infoid) {

		DeleteFriendsFreshsMoodRequest request = new DeleteFriendsFreshsMoodRequest();
		request.setInfoid(infoid);
		request.setInfouserid(createUserId);
		request.setDevRequest(true);

		APIClient.deleteFriendsFreshsMood(request,
				new AsyncHttpResponseHandler() {

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

						showLoadingView("正在删除...");
					}

					@Override
					public void onSuccess(String content) {
						LogUtil.d(content);
						DeleteFriendsFreshsMoodResponse response;
						try {
							response = new Gson().fromJson(content,
									DeleteFriendsFreshsMoodResponse.class);
							ResponseUtil.checkObjResponse(response);
						} catch (Exception e) {
							showToast(R.string.data_format_error);
							LogUtil.w(TAG, e);
							return;
						}

						if (response.isSuccess()) {
							setResult(Activity.RESULT_OK);
							finish();
						} else {
							showToast(response.getMsg());
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						LogUtil.d(content);
						showToast(R.string.load_server_failure);
					}
				});
	}
	
	/** 朋友圈删除评论 */
	private void deleteFriendsFreshsComment(String infoid, String userId, String commentId, final int position) {

		DeleteFriendsFreshsMoodCommentRequest request = new DeleteFriendsFreshsMoodCommentRequest();
		request.setInfoid(infoid);
		request.setInfouserid(userId);
		request.setCommentid(commentId);
		request.setDevRequest(true);

		APIClient.deleteFriendsFreshsComment(request,
				new AsyncHttpResponseHandler() {

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

						showLoadingView("正在删除...");
					}

					@Override
					public void onSuccess(String content) {
						LogUtil.d(content);
						DeleteFriendsFreshsMoodCommentRsponse response;
						try {
							response = new Gson().fromJson(content,
									DeleteFriendsFreshsMoodCommentRsponse.class);
							ResponseUtil.checkObjResponse(response);
						} catch (Exception e) {
							showToast(R.string.data_format_error);
							LogUtil.w(TAG, e);
							return;
						}

						if (response.isSuccess()) {
							/**刷新适配器*/
							mAdapter.getData().remove(position);
							mAdapter.notifyDataSetChanged();
						} else {
							showToast(response.getMsg());
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						LogUtil.d(content);
						showToast(R.string.load_server_failure);
					}
				});
	}

	 /**
     * 赞和取消赞接口
     * @param item
     */
    private void friendZoneParisaSub(final String infoid, String infouserid) {

        APIClient.friendZoneParisaSub(new FriendZoneParisaSubRequest(infoid, infouserid), new AsyncHttpResponseHandler() {

            @Override
            protected void onPreExecute() {
                HttpHanderUtil.cancel(mHttpResponseHandler);
                mHttpResponseHandler = null;
                showLoadingView("正在处理...");
            }

            @Override
            public void onFinish() {
            	mHttpResponseHandler = null;
                hideLoadingView();
            }

            @Override
            public void onSuccess(int statusCode, String content) {
                FriendZoneParisaSubResponse response;
                try {
                    response = new Gson().fromJson(content, FriendZoneParisaSubResponse.class);
                    ResponseUtil.checkObjResponse(response);
                } catch (Exception e) {
                    showToast(R.string.data_format_error);
                    return;
                }
                if (response.isSuccess()) {
                	if (response.getData() != null) {
						if ("0".equals(response.getData().getAllowtype())) {
							showToast("此新鲜事不可赞");
						}else if ("1".equals(response.getData().getAllowtype())) {
							parisa = "0";
							showToast("取消成功");
						}else if ("2".equals(response.getData().getAllowtype())) {
							parisa = "1";
							showToast("赞成功");
						}
						getFriendsFreshsMoodInfo(infoid);
					}
                }else {
					showToast(response.getMsg());
				}
            }

            @Override
            public void onFailure(Throwable error, String content) {
                showToast(R.string.load_server_failure);
            }
        });
    }
    
	/** 
	 * 好友圈评论，这个方法也是回复方法 
	 * 
     * @param infoid 新鲜事信息ID
     * @param userid 回复某条评论的发布人ID
     * @param commentid 回复某条评论的ID
     * @param content 评论内容
     * @param infouserid 新鲜事创建者ID
     */
	private void friendZoneCommentSub(String infoid, String userid, String commentid, String content, String createUserid) {

//		FriendZoneCommentSubRequest request = new FriendZoneCommentSubRequest(infoid, userid, commentid, content, createUserid);
		FriendZoneCommentSubRequest request = new FriendZoneCommentSubRequest();
		request.setInfoid(infoid);
		request.setUserid(userid);
		request.setCommentid(commentid);
		request.setContent(content);
		request.setInfouserid(createUserid);
		request.setDevRequest(true);

		APIClient.friendZoneCommentSub(request,
				new AsyncHttpResponseHandler() {

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

						showLoadingView("正在处理...");
					}

					@Override
					public void onSuccess(String content) {
						LogUtil.d(content);
						FriendZoneCommentSubResponse response;
						try {
							response = new Gson().fromJson(content,
									FriendZoneCommentSubResponse.class);
							ResponseUtil.checkObjResponse(response);
						} catch (Exception e) {
							showToast(R.string.data_format_error);
							LogUtil.w(TAG, e);
							return;
						}

						if (response.isSuccess()) {
							if (response.getData() != null) {
								FriendsFreshsMoodInfoDataComment comment = new FriendsFreshsMoodInfoDataComment();
								comment.setCommentid(response.getData().getCommentid());
								comment.setCtime(response.getData().getCtime());
								comment.setContent(mEditText.getText().toString().trim());
								/**发送者*/
								FriendsFreshsMoodInfoDataCommentSender sender = new FriendsFreshsMoodInfoDataCommentSender();
								sender.setLogo(Constants.userInfo.getLogo());
								sender.setUserid(Constants.userInfo.getUserId());
								sender.setNickname(Constants.userInfo.getNickName());
								comment.setSenduser(sender);
								
								/**回复者*/
								FriendsFreshsMoodInfoDataCommentReply replyuser = new FriendsFreshsMoodInfoDataCommentReply();
								replyuser.setUserid(replyerId);
								replyuser.setNickname(replyerName);
								comment.setReplyuser(replyuser);
								
								mAdapter.getData().add(comment);
								mAdapter.notifyDataSetChanged();
								
								close();
							}
							
						} else {
							showToast(response.getMsg());
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						LogUtil.d(content);
						showToast(R.string.load_server_failure);
					}
				});
	}
	
	@Override
	public void onDeleteMoodComment(String commentId, int position) {
		deleteFriendsFreshsComment(infoid, createUserId, commentId, position);
	}

	 @Override
	    public void onExprItemClick(String exprInfo, boolean isDel) {
	        SpannableString ss;
	        int textSize = (int) mEditText.getTextSize();
	        if (isDel) {
	            HashMap<String, Integer> mExprFilenameIdData = XWExpressionManager.getInstance().getmExprInfoIdValuesCN(this);
	            ss = XWExpressionUtil.generateSpanComment(App.getContext(),
	                    XWExpressionUtil.deleteOneWord(mEditText.getText().toString(), mExprFilenameIdData),
	                    textSize);
	        } else {
	            String content = mEditText.getText() + exprInfo;
	            ss = XWExpressionUtil.generateSpanComment(App.getContext(), content,
	                    textSize);
	        }
	        mEditText.setText(ss);
	        mEditText.setSelection(ss.length());
	    }

	@Override
	public void onShowInputView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onHideInputView() {
		
	}
	
	private void resizeExprArea(int viewWidth) {
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                mSoftKeyboardHeight);
        rlp.addRule(RelativeLayout.BELOW, R.id.comment_editt_linear);
        mExpressionWidgt.setLayoutParams(rlp);
        mExpressionWidgt.setKeyboardSize(viewWidth, mSoftKeyboardHeight);
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
                    mImm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
                } else {
                    mIsExprShow = false;
                    mExpressionWidgt.setVisibility(View.GONE);
                }
            }
            mExprBtn.setBackgroundResource(R.drawable.selector_s_chat_expressions);
        }
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
			}else {
//				mEditText.setText("");
//				EditUtil.hideMethod(this, mEditText);
//				
//				mToolBar.setVisibility(View.GONE);
			}
		}
		return super.dispatchTouchEvent(ev);
	}
	
	private void close() {
		mEditText.setText("");
		EditUtil.hideMethod(this, mEditText);
		controlKeyboardOrExpr(false, false);
		
		/**根据最新需要临时改为VISIBLE*/
		mToolBar.setVisibility(View.VISIBLE);
	}
	
	/**获取头像布局的宽度-空隙的宽度， 除以设定的个数，得到头像的宽度和高度
	 * 
	 * 判断头像总数，执行循环代码，动态加载第一行布局，如果i % 7 = 0, 在次加载新的布局
	 * */
}
