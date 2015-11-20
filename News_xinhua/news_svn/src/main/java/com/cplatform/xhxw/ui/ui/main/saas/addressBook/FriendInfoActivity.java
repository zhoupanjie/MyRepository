package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.main.saas.SMessageChatActivity;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 详细资料
 * */
public class FriendInfoActivity extends BaseActivity implements OnClickListener {

	private static final String TAG = FriendInfoActivity.class.getSimpleName();

	private static final int UPDATE = 1;
	private static final int VERIFY = 2;

	private LinearLayout friendsLayout;
	private ImageView updateBtn;
	private LinearLayout remarkLayout;
	private ImageView userImage;
	private TextView userName;
	private ImageView userSex;
	private TextView userNickName;
	private TextView userRemark;
	private TextView userTel;
	private TextView userPhone;
	private TextView userEmail;
	private TextView userSign;
	private LinearLayout photoLayout;
	private ImageView firstImage;
	private ImageView secondImage;
	private ImageView thirdImage;
	private Button sendMessageBtn;

    private String mLogoUrl;
	private String friendsId;
	private String friendsName;
	 private boolean isFriend;
	private AsyncHttpResponseHandler mHttpResponseHandler;

	public static Intent newIntent(Context context, String friendsId,
			boolean isFriend) {
		Intent intent = new Intent();
		intent.setClass(context, FriendInfoActivity.class);
		intent.putExtra("friendsId", friendsId);
		// intent.putExtra("isFriend", isFriend);
		return intent;
	}

	@Override
	protected String getScreenName() {
		return TAG;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_frend_info);

		init();

		setListener();

		initView();
	}

	private void init() {
		initActionBar();
		updateBtn = (ImageView) findViewById(R.id.friends_more_layout);
		remarkLayout = (LinearLayout) findViewById(R.id.friend_info_remark_layout);
		friendsLayout = (LinearLayout) findViewById(R.id.friends_layout);
		userImage = (ImageView) findViewById(R.id.friend_info_image);
		userName = (TextView) findViewById(R.id.friend_info_name);
		userSex = (ImageView) findViewById(R.id.friend_info_sex);
		userNickName = (TextView) findViewById(R.id.friend_info_nickname);
		userRemark = (TextView) findViewById(R.id.friend_info_remark);
		userTel = (TextView) findViewById(R.id.friend_info_tel);
		userPhone = (TextView) findViewById(R.id.friend_info_phone);
		userEmail = (TextView) findViewById(R.id.friend_info_email);
		userSign = (TextView) findViewById(R.id.friend_info_sign);
		photoLayout = (LinearLayout) findViewById(R.id.friend_photo_layout);
		firstImage = (ImageView) findViewById(R.id.friend_info_first_photo);
		secondImage = (ImageView) findViewById(R.id.friend_info_second_photo);
		thirdImage = (ImageView) findViewById(R.id.friend_info_third_photo);
		sendMessageBtn = (Button) findViewById(R.id.friend_info_send_message);
	}

	private void setListener() {
		updateBtn.setOnClickListener(this);
		sendMessageBtn.setOnClickListener(this);
		friendsLayout.setOnClickListener(this);
		photoLayout.setOnClickListener(this);
	}

	private void initView() {
		friendsId = getIntent().getStringExtra("friendsId");
		// isFriend = getIntent().getBooleanExtra("isFriend", false);

		getFriendsInfo(friendsId);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.friends_more_layout:
			if (!TextUtils.isEmpty(userRemark.getText().toString().trim())) {
				friendsName = userRemark.getText().toString().trim();
			}else if (!TextUtils.isEmpty(userNickName.getText().toString().trim())) {
				friendsName = userNickName.getText().toString().trim();
			}else {
				friendsName = userName.getText().toString().trim();
			}
			startActivityForResult(
					UpdateInfoActivity.newIntent(this, friendsId, userRemark.getText().toString().trim()), UPDATE);
			break;
		case R.id.friend_info_send_message:
			 if (isFriend) {//好友  发消息
                 String name = userName.getText().toString();
                 String nickName = userNickName.getText().toString();
                 String comment = userRemark.getText().toString();
				 startActivity(SMessageChatActivity.newIntent(FriendInfoActivity.this,
						 friendsId, comment, nickName, name, mLogoUrl, "通讯录"));
			 }else {
				 startActivityForResult(FriendsVerifyActivity.newIntent(this,
						 friendsId), VERIFY);
			 }
			break;
		case R.id.friend_photo_layout:
			startActivity(PersonalMoodActivity.newIntent(this, friendsId));
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode != Activity.RESULT_OK) {
			return;
		}

		switch (requestCode) {
		case UPDATE:
			if (data != null && data.getStringExtra("remark") != null) {
				if (TextUtils.isEmpty(data.getStringExtra("remark"))) {//删除
						isFriend = false;
						updateBtn.setVisibility(View.INVISIBLE);
						friendsLayout.setVisibility(View.GONE);
						sendMessageBtn.setText("添加好友");
						remarkLayout.setVisibility(View.INVISIBLE);
						
						finish();
				}else {//更改备注
					userRemark.setText(data.getStringExtra("remark"));
				}
			}
			break;
		case VERIFY:
//			setResult(Activity.RESULT_OK);
//			finish();
			break;
		default:
			break;
		}
	}

	private void getFriendsInfo(String friendsId) {

		FriendsInfoRequest request = new FriendsInfoRequest();
		request.setUserid(friendsId);
		request.setDevRequest(true);

		APIClient.getFriendsInfo(request, new AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				hideLoadingView();
				
				mHttpResponseHandler = null;
			}

			@Override
			protected void onPreExecute() {
				if (mHttpResponseHandler != null) {
					mHttpResponseHandler.cancle();
				}
				mHttpResponseHandler = this;
				
				showLoadingView();
			}

			@Override
			public void onSuccess(String content) {
				FriendsInfoResponse response;
				try {
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content,
							FriendsInfoResponse.class);
				} catch (Exception e) {
					showToast(R.string.data_format_error);
					LogUtil.w(TAG, e);
					return;
				}
				if (response.isSuccess()) {
					if (response != null && response.getData() != null) {
                        mLogoUrl = response.getData().getLogo();
						ImageLoader
								.getInstance()
								.displayImage(
                                        mLogoUrl,
										userImage,
										DisplayImageOptionsUtil.avatarFriendsInfoImagesOptions);
//						userName.setText(SelectNameUtil.getName("", response.getData().getNickname(), response.getData().getName()));
						userName.setText(response.getData().getName());
//						friendsName = SelectNameUtil.getName(response.getData().getComment(), response.getData().getNickname(), response.getData().getName());
						if ("1".equals(response.getData().getGender())) {// 男
							userSex.setBackgroundResource(R.drawable.friends_man);
						} else if ("2".equals(response.getData().getGender())) { // 女
							userSex.setBackgroundResource(R.drawable.friends_man);
						} else {// 未定义

						}
						userNickName.setText(response.getData().getNickname());
						userRemark.setText(response.getData().getComment());
						userTel.setText(response.getData().getMobile());
						userPhone.setText(response.getData().getPhone());
						userEmail.setText(response.getData().getEmail());
						userSign.setText(response.getData().getSign());
//						photoLayout.setData(response.getData().getThumbpics());
						
						if (response.getData().getThumbpics() != null && response.getData().getThumbpics().size() > 0) {
							int count = response.getData().getThumbpics().size();
							if (count >= 1) {
								ImageLoader.getInstance().displayImage(response.getData().getThumbpics().get(0), firstImage, DisplayImageOptionsUtil.avatarFriendsInfoImagesOptions);
							}
							
							if (count >= 2) {
								ImageLoader.getInstance().displayImage(response.getData().getThumbpics().get(1), secondImage, DisplayImageOptionsUtil.avatarFriendsInfoImagesOptions);
							}
							
							if (count >= 3) {
								ImageLoader.getInstance().displayImage(response.getData().getThumbpics().get(2), thirdImage, DisplayImageOptionsUtil.avatarFriendsInfoImagesOptions);
							}
						}

						if ("1".equals(response.getData().getIsfriend())) {// 是好友
							updateBtn.setVisibility(View.VISIBLE);
							friendsLayout.setVisibility(View.VISIBLE);
							sendMessageBtn.setText("发送消息");
							remarkLayout.setVisibility(View.VISIBLE);
							isFriend = true;
						} else {
							updateBtn.setVisibility(View.INVISIBLE);
							friendsLayout.setVisibility(View.GONE);
							sendMessageBtn.setText("添加好友");
							remarkLayout.setVisibility(View.INVISIBLE);
							isFriend = false;
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
		});
	}

}
