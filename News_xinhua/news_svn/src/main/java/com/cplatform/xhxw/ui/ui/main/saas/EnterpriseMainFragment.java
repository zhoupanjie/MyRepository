package com.cplatform.xhxw.ui.ui.main.saas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.BaseRequest;
import com.cplatform.xhxw.ui.http.net.BaseResponse;
import com.cplatform.xhxw.ui.http.net.PushOnlineRequest;
import com.cplatform.xhxw.ui.http.net.saas.SaasGetMsgCountResponse;
import com.cplatform.xhxw.ui.model.PushInfoTmp;
import com.cplatform.xhxw.ui.model.saas.CommentSubData;
import com.cplatform.xhxw.ui.model.saas.CompanyZoneList;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.view.HomeOperationsBar;
import com.cplatform.xhxw.ui.ui.base.view.HomeOperationsBar.onHomeBottomBarClickListener;
import com.cplatform.xhxw.ui.ui.base.widget.ScrollLayout;
import com.cplatform.xhxw.ui.ui.base.widget.ScrollLayout.OnFlagAreaExpandListener;
import com.cplatform.xhxw.ui.ui.main.HomeActivity;
import com.cplatform.xhxw.ui.ui.main.cms.HomeFragment;
import com.cplatform.xhxw.ui.ui.main.saas.PersonalCenterFragment.onMsgCountChangeListener;
import com.cplatform.xhxw.ui.ui.main.saas.addressBook.CircleCreateType;
import com.cplatform.xhxw.ui.ui.main.saas.addressBook.CommunityCommentaryFragment;
import com.cplatform.xhxw.ui.ui.main.saas.addressBook.OnCommunityCommentaryListener;
import com.cplatform.xhxw.ui.ui.main.saas.addressBook.SendImageTextActivity;
import com.cplatform.xhxw.ui.util.Actions;
import com.cplatform.xhxw.ui.util.AppCoomentUtil;
import com.cplatform.xhxw.ui.util.DateUtil;
import com.cplatform.xhxw.ui.util.ImageUtil;
import com.cplatform.xhxw.ui.util.ListUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * SAAS主界面
 */
public class EnterpriseMainFragment extends BaseActivity implements
		onHomeBottomBarClickListener, onMsgCountChangeListener,
		OnCommunityCommentaryListener, OnFlagAreaExpandListener {
	private HomeOperationsBar mBottomBar;
	private EnterpriseHomeFragment mEnterNewsFragment;
	private PersonalCenterFragment mPersonalCenterFrag;
	private SMessageFragment mSMessageFragment;
	private CommunityFragment mCompanyCircleFragment;

	private Fragment mCurrentDisplayedFragment;
	private int mCurrentBtnType;
	private Receiver mReceiver;
	// 界面旋转所需属性
//	public RelativeLayout enterprise_main_root_ly;
	public static final String Translate_tag = "EnterpriseMainFragment";
	private HomeFragment mCMSMainFrangment;
	private ImageView view_header;
	private ScrollLayout mFlagManager;
	/**
	 * 用户设置界面（判断用户是否登录或者退出）
	 */
	public static final int REQUEST_CODE_SETTING = 2;
	/**
	 * saas账户退出登录
	 */
	public static final int REQUEST_CODE_SAAS_QUIT = 3;

	/**
	 * 公司圈--发布新鲜事--照相
	 */
	public static final int REQUEST_CODE_COMPANT_CAMERA = 100;
	public static final int REQUEST_CODE_FRIEND_CAMERA = 103;
	/** 相册 */
	public static final int REQUEST_CODE_COMPANT_ALBUMGROUP = 101;
	public static final int CREATE = 102;
	public static final int REQUEST_CODE_FRIEND_ALBUMGROUP = 104;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_enterprise_main);
		setSwipeBackEnable(false);
		init();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		// here we can use getIntent() to get the extra data.
	}

	private void init() {
		initViews();
		initFragment();

		mFlagManager.setOnFlagAreaExpandLis(this);
	}

	private void initViews() {
//		enterprise_main_root_ly = (RelativeLayout) findViewById(R.id.enterprise_main_root_ly);
		mBottomBar = (HomeOperationsBar) findViewById(R.id.enterprise_bottom_bar);
		mBottomBar.setmOnHomeBottomBarClickLs(this);
		mBottomBar.setMenterpriseName(Constants.userInfo.getEnterprise()
				.getSimplename());
		mFlagManager = (ScrollLayout) findViewById(R.id.ly_flag_manager);
		view_header = (ImageView) findViewById(R.id.view_header);
	}

	private void initFragment() {
		// 添加fragment

		FragmentTransaction t = getSupportFragmentManager().beginTransaction();

		// if(mCMSMainFrangment == null) {
		// mCMSMainFrangment= new HomeFragment();
		// }
		// if(mCMSMainFrangment.isAdded()) {
		// t.show(mCMSMainFrangment);
		// } else {
		// t.replace(R.id.fg_home, mCMSMainFrangment);
		// }

		if (mEnterNewsFragment == null) {
			mEnterNewsFragment = new EnterpriseHomeFragment();
		}
		if (mEnterNewsFragment.isAdded()) {
			t.show(mEnterNewsFragment);
		} else {
			t.replace(R.id.fg_enterprise_main, mEnterNewsFragment);
		}

		t.commit();
		mCurrentDisplayedFragment = mEnterNewsFragment;
		mCurrentBtnType = HomeOperationsBar.BOTTOM_BUTTON_TYPE_NEWS;
	}

	/**
	 * 切换fragment
	 * 
	 * @param from
	 *            当前显示的fragment
	 * @param to
	 *            切换的目标fragment
	 */
	public void switchContent(Fragment from, Fragment to) {
		if (from != to) {
			mCurrentDisplayedFragment = to;
			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();

			if (!to.isAdded()) {
				transaction.hide(from).add(R.id.fg_enterprise_main, to)
						.commit();
			} else {
				transaction.hide(from).show(to).commit();
			}
		}
	}

	@Override
	public void onHomeBottomBarClick(int buttonType) {
		switchChildFragments(buttonType);
	}

	/**
	 * 根据点击的导航按钮类型，切换显示不同界面
	 * 
	 * @param buttonType
	 */
	private void switchChildFragments(int buttonType) {
		if (buttonType == mCurrentBtnType) {
			return;
		}
		Fragment switchTo = null;

		if (buttonType == HomeOperationsBar.BOTTOM_BUTTON_TYPE_NEWS) {
			switchTo = mEnterNewsFragment;
			mCurrentBtnType = HomeOperationsBar.BOTTOM_BUTTON_TYPE_NEWS;
		} else if (buttonType == HomeOperationsBar.BOTTOM_BUTTON_TYPE_BROADCAST) {
			if (mSMessageFragment == null) {
				mSMessageFragment = new SMessageFragment();
			}
			switchTo = mSMessageFragment;
			mCurrentBtnType = HomeOperationsBar.BOTTOM_BUTTON_TYPE_BROADCAST;
			pushOnline();
		} else if (buttonType == HomeOperationsBar.BOTTOM_BUTTON_TYPE_VIDEO) {
			if (mCompanyCircleFragment == null) {
				mCompanyCircleFragment = new CommunityFragment();
				mCompanyCircleFragment.setCommunityCommentaryListener(this);
			}
			switchTo = mCompanyCircleFragment;
			mCurrentBtnType = HomeOperationsBar.BOTTOM_BUTTON_TYPE_VIDEO;
		} else if (buttonType == HomeOperationsBar.BOTTOM_BUTTON_TYPE_PERSONAL) {
			if (mPersonalCenterFrag == null) {
				mPersonalCenterFrag = new PersonalCenterFragment();
				mPersonalCenterFrag.setOnMsgCountChangeLis(this);
			}
			switchTo = mPersonalCenterFrag;
			mCurrentBtnType = HomeOperationsBar.BOTTOM_BUTTON_TYPE_PERSONAL;
		}

		if (switchTo != null) {
			switchContent(mCurrentDisplayedFragment, switchTo);
		}
		mCurrentDisplayedFragment = switchTo;
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshMsgCount();
		updateNewMsgCount();
		mReceiver = new Receiver();
		LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
				new IntentFilter(Actions.ACTION_MSG_NEW_UPDATE));
	}

	@Override
	public void onPause() {
		super.onPause();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
		mReceiver = null;
	}

	/**
	 * 显示评论布局
	 */
	@Override
	public void onShowCommunityCommentary(boolean isCompanyCircle,
			String infoId, String infoUserId, String userId, String commentId,
			String hint) {
		mBottomBar.setVisibility(View.GONE);
		CommunityCommentaryFragment fg = CommunityCommentaryFragment.createFragment(isCompanyCircle, infoId,
		infoUserId, userId, commentId, hint, this);
		getSupportFragmentManager().beginTransaction()
		.add(R.id.fg_my_comment, fg)
		.addToBackStack("myComment")
		.commit();
	}

	@Override
	public void onHideCommunityCommentary() {
		mBottomBar.setVisibility(View.VISIBLE);
	}

	@Override
	public void onCommentarySutmit(CommentSubData result) {
		if (mCompanyCircleFragment != null) {
			mCompanyCircleFragment.setCompanyCircleCommentary(result);
		}
	}

	private void refreshMsgCount() {
		BaseRequest request = new BaseRequest();
		request.setSaasRequest(true);
		APIClient.saasGetMsgCount(request, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, String content) {
				SaasGetMsgCountResponse response;
				try {
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content,
							SaasGetMsgCountResponse.class);
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}

				if (response != null && response.isSuccess()) {
					int commentCount = response.getData().getBackcomment();
					int msgCount = response.getData().getUsermsg();
					mBottomBar.setMsgCount(commentCount + msgCount);
				}
			}
		});
	}

	public Uri getPhotoUri() {
		if (mCompanyCircleFragment != null) {
			return mCompanyCircleFragment.getPhotoUri();
		}
		return null;
	}

	@Override
	public void onMsgCountChange(int count) {
		mBottomBar.setMsgCount(count);
	}

	private class Receiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (Actions.ACTION_MSG_NEW_UPDATE.equals(intent.getAction())) {
				updateNewMsgCount();
			}
		}
	}

	/**
	 * 更新消息角标
	 */
	private void updateNewMsgCount() {
		int homeOperAppCount = App.getPreferenceManager()
				.getMsgNewCompanyCircleCount()
				+ App.getPreferenceManager().getMsgNewFriendsCircleCount();
		mBottomBar.setMsgHomeOperAppCount(homeOperAppCount);

		int smessage = App.getPreferenceManager().getMsgNewSMessage()
				+ App.getPreferenceManager().getNewFriendNewCount()
				+ App.getPreferenceManager().getNewFriendsLimit();
		mBottomBar.setMsgEnterpriseCount(smessage);
	}

	/**
	 * 社区发布内容
	 */
	public void sendCompanyZoneListItem(boolean isFriends, CompanyZoneList item) {
		if (mCompanyCircleFragment != null) {
			mCompanyCircleFragment.sendCompanyZoneListItem(isFriends, item);
		}
	}

	public void onFetchChannelsDone() {
		if (mEnterNewsFragment != null) {
			mEnterNewsFragment.loadContent();
		}
		if (mBottomBar != null) {
			mBottomBar.upgradeBottomBtnName();
		}
	}

	/**
	 * push在线注册
	 */
	private void pushOnline() {
		PushInfoTmp tmp = Constants.sPushInfoTmp;
		if (tmp == null) {
			return;
		}

		final String userId = tmp.getUserId();
		final String deviceToken = tmp.getDeviceToken();
		final String channelId = tmp.getChannelId();
		String push_channel = tmp.getPush_channel();
		APIClient.pushOnline(new PushOnlineRequest(this, userId, deviceToken,
				channelId, push_channel), new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, String content) {
				BaseResponse response;
				try {
					response = new Gson().fromJson(content, BaseResponse.class);
					ResponseUtil.checkObjResponse(response);
				} catch (Exception e) {
					LogUtil.w(e);
					return;
				}
				if (response.isSuccess()) {
					LogUtil.d("服务器绑定push成功！ userId=" + userId
							+ "  deviceToken=" + deviceToken + "  channelId="
							+ channelId);
					App.getPreferenceManager().setPushDeviceToken(deviceToken);
				} else {
					LogUtil.w("服务器绑定push失败！msg:" + content);
				}
			}
		});
	}

	@Override
	protected String getScreenName() {
		// TODO Auto-generated method stub
		return null;
	}

	public void jumpToFirst() {
		Intent in = new Intent();
		in.setClass(EnterpriseMainFragment.this, HomeActivity.class);
		setActivityAnim();
		startActivity(in,false);
	}
	
    @Override
    public void startActivity(Intent intent,boolean isDefaultAnim) {
        super.startActivity(intent,false);
    }
    protected void setActivityAnim() {
        overridePendingTransition(R.anim.activity_scale_translate_rotate, R.anim.activity_alpha_action_exit);
    }

	@Override
	public void onFlagAreaExpand() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSAASCMSSwitch() {
		jumpToFirst();
		// TODO Auto-generated method stub
//		rotateHelper = new RotationHelper(EnterpriseMainFragment.this,
//				TranslateConstants.KEY_SECOND_CLOCKWISE);
//		rotateHelper.applyFirstRotation(enterprise_main_root_ly, 0, 90);
	}

	@Override
	public void onSwitchLanguage(String language) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onSAASCMSSwitch();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		LogUtil.e("", "-----" + requestCode + " resultCode---" + resultCode);
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case REQUEST_CODE_SAAS_QUIT:
			onSAASCMSSwitch();
			// mFlagManager.onResume(mIsUnderEnterprise);
			break;
		// case REQUEST_CODE_LOGIN:
		// if(Constants.hasEnterpriseAccountLoggedIn()) {
		// mFlagManager.maximize();
		// return;
		// }
		case REQUEST_CODE_SETTING:
			return;
		case REQUEST_CODE_COMPANT_CAMERA:
		case REQUEST_CODE_FRIEND_CAMERA:
			/** 提取照相机传过来的数据 */
			if (Constants.hasEnterpriseAccountLoggedIn()) {
				String path = AppCoomentUtil.getUriPath(this, getPhotoUri());
				if (ImageUtil.needRotate(path)) {
					String tmpPath = Constants.Directorys.TEMP
							+ DateUtil.getTime() + ".jpg";
					ImageUtil.rotatedBitmap(this, path, tmpPath);
					path = tmpPath;
				}
				List<String> imageUrls = new ArrayList<String>();
				if (!TextUtils.isEmpty(AppCoomentUtil.getUriPath(this,
						getPhotoUri()))) {
					imageUrls.add(path);
				}
				// startActivityForResult(SendImageTextActivity.newIntent(this,
				// imageUrls, AppCoomentUtil.getUriPath(this,
				// mSAASMainFragment.getPhotoUri())), CREATE);
				CircleCreateType type = requestCode == REQUEST_CODE_COMPANT_CAMERA ? CircleCreateType.company
						: CircleCreateType.friend;
				startActivityForResult(
						SendImageTextActivity.newIntent(this, imageUrls, type),
						CREATE);
			}
			return;
		case REQUEST_CODE_COMPANT_ALBUMGROUP:
		case REQUEST_CODE_FRIEND_ALBUMGROUP:
			/** 相册传过来的数据 */
			if (data != null) {
				List<String> result = (List<String>) data
						.getSerializableExtra("result");
				if (!ListUtil.isEmpty(result)) {
					Intent intent = new Intent();
					intent.putExtra("result", (Serializable) result);
					// startActivityForResult(SendImageTextActivity.newIntent(this,
					// result, ""), CREATE);
					CircleCreateType type = requestCode == REQUEST_CODE_COMPANT_ALBUMGROUP ? CircleCreateType.company
							: CircleCreateType.friend;
					startActivityForResult(
							SendImageTextActivity.newIntent(this, result, type),
							CREATE);
				}
			}
			return;
		case CREATE:
			if (data != null) {
				CompanyZoneList item = (CompanyZoneList) data
						.getSerializableExtra("data");
				boolean isFriends = data.getBooleanExtra("isFriends", false);
				if (Constants.hasEnterpriseAccountLoggedIn()) {
					sendCompanyZoneListItem(isFriends, item);
				}

			}
			break;
		}
	}
}