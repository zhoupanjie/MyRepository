package com.cplatform.xhxw.ui.ui.main.saas;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.BaseRequest;
import com.cplatform.xhxw.ui.http.net.saas.SaasGetMsgCountResponse;
import com.cplatform.xhxw.ui.ui.base.BaseFragment;
import com.cplatform.xhxw.ui.ui.collect.CollectActivity;
import com.cplatform.xhxw.ui.ui.login.LoginActivity;
import com.cplatform.xhxw.ui.ui.main.HomeActivity;
import com.cplatform.xhxw.ui.ui.main.saas.addressBook.PersonalMoodActivity;
import com.cplatform.xhxw.ui.ui.main.saas.personalcenter.MyPushMessageActivity;
import com.cplatform.xhxw.ui.ui.main.saas.personalcenter.MyRecordActivity;
import com.cplatform.xhxw.ui.ui.main.saas.personalcenter.SaasSysSettingActivity;
import com.cplatform.xhxw.ui.ui.main.saas.personalcenter.SaasUserinfoActivity;
import com.cplatform.xhxw.ui.ui.search.SearchActivity;
import com.cplatform.xhxw.ui.ui.web.WebViewActivity;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * SAAS 个人中心
 */
public class PersonalCenterFragment extends BaseFragment implements
		OnClickListener {

    // 应用中心地址
	private static final String APP_URL_XW = "http://218.106.246.85/cms/wap/app/v_list.do?type=android";

	/** 头像 */
	private ImageView mAvatar;
	/** 姓名*/
	private TextView mName;
	/** 昵称 */
	private TextView mNickName;
	/** 用户信息 */
	private RelativeLayout ly_avatar;
	
	private RelativeLayout mRecordLayout;
	private RelativeLayout mCollectionLayout;
	private RelativeLayout mFreshEventLayout;
	private RelativeLayout mAppCenterLayout;
	private RelativeLayout mMsgCenterLayout;
	private RelativeLayout mSearchLayout;
	private RelativeLayout mSysSettingsLayout;
	private TextView mRecordAlertTv;
	private TextView mMsgCenterAlertTv;
	
	private onMsgCountChangeListener onMsgCountChangeLis;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_personal_center,
				container, false);
		initViews(rootView);
		return rootView;
	}

	private void initViews(View rootView) {
		mCollectionLayout = (RelativeLayout) rootView
				.findViewById(R.id.saas_personal_setting_my_collection_lo);
		mRecordLayout = (RelativeLayout) rootView
				.findViewById(R.id.saas_personal_setting_my_record_lo);
		mFreshEventLayout = (RelativeLayout) rootView
				.findViewById(R.id.saas_personal_setting_fresh_event);
		mAppCenterLayout = (RelativeLayout) rootView
				.findViewById(R.id.saas_personal_setting_app_center_lo);
		mMsgCenterLayout = (RelativeLayout) rootView
				.findViewById(R.id.saas_personal_setting_msg_center_lo);
		mSearchLayout = (RelativeLayout) rootView
				.findViewById(R.id.saas_personal_setting_search_lo);
		mSysSettingsLayout = (RelativeLayout) rootView
				.findViewById(R.id.saas_personal_setting_sys_settings_lo);
		mAvatar = (ImageView) rootView.findViewById(R.id.iv_avatar);
		mName = (TextView) rootView.findViewById(R.id.saas_personal_name_tv);
		mNickName = (TextView) rootView.findViewById(R.id.saas_personnal_nick_tv);
		ly_avatar = (RelativeLayout) rootView.findViewById(R.id.ly_avatar);
		mRecordAlertTv = (TextView) rootView.findViewById(R.id.saas_personal_setting_my_record_alert);
		mMsgCenterAlertTv = (TextView) rootView.findViewById(R.id.saas_personal_setting_sys_msg_alert);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initEvents();
		refreshUserInfo();
	}

	private void refreshUserInfo() {
		if (Constants.hasEnterpriseAccountLoggedIn()) {
			ImageLoader.getInstance().displayImage(
					Constants.userInfo.getLogo(), mAvatar,
					DisplayImageOptionsUtil.avatarSaasImagesOptions);
			mName.setText(Constants.userInfo.getEnterprise().getStaff_name());
			mNickName.setText(Constants.userInfo.getNickName());
		} else {
			mAvatar.setImageResource(R.drawable.ic_avatar_bg);
			mName.setText(R.string.not_login);
			mNickName.setText(null);
		}
	}
	
	private void initEvents() {
		mCollectionLayout.setOnClickListener(this);
		mRecordLayout.setOnClickListener(this);
		mFreshEventLayout.setOnClickListener(this);
		mAppCenterLayout.setOnClickListener(this);
		mMsgCenterLayout.setOnClickListener(this);
		mSearchLayout.setOnClickListener(this);
		mSysSettingsLayout.setOnClickListener(this);
		ly_avatar.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.saas_personal_setting_my_collection_lo:
			//我的收藏
			startActivity(CollectActivity.getIntent(getActivity()));
			break;
		case R.id.saas_personal_setting_my_record_lo:
			//我的记录
			startActivity(new Intent(mAct, MyRecordActivity.class));
			break;
		case R.id.ly_avatar:
			if (Constants.hasEnterpriseAccountLoggedIn()) {
				startActivity(SaasUserinfoActivity.getIntent(getActivity()));
			} else {
				startActivity(LoginActivity.getIntent(getActivity()));
			}
			break;
		case R.id.saas_personal_setting_app_center_lo:
			//应用中心
			mAct.startActivity(WebViewActivity.getIntentByURL(mAct, APP_URL_XW, 
					getString(R.string.saas_setting_app_center)));
			break;
		case R.id.saas_personal_setting_msg_center_lo:
			//消息中心
			mAct.startActivity(new Intent(mAct, MyPushMessageActivity.class));
			break;
		case R.id.saas_personal_setting_search_lo:
			//搜索
			mAct.startActivity(SearchActivity.getIntent(mAct));
			break;
		case R.id.saas_personal_setting_sys_settings_lo:
			//系统设置
			mAct.startActivityForResult(new Intent(mAct, SaasSysSettingActivity.class),
					EnterpriseMainFragment.REQUEST_CODE_SAAS_QUIT);
			break;
		case R.id.saas_personal_setting_fresh_event:
			//新鲜事
			mAct.startActivity(PersonalMoodActivity.newIntent(getActivity(), Constants.getUid()));
			break;
		default:
			break;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		refreshUserInfo();
		refreshMsgCount();
	}

	private void refreshMsgCount() {
		BaseRequest request = new BaseRequest();
		request.setSaasRequest(true);
		APIClient.saasGetMsgCount(request, new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, String content) {
				SaasGetMsgCountResponse response;
				try {
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content, SaasGetMsgCountResponse.class);
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				
				if(response != null && response.isSuccess()) {
					int commentCount = response.getData().getBackcomment();
					int msgCount = response.getData().getUsermsg();
					
					if(commentCount > 0) {
						mRecordAlertTv.setVisibility(View.VISIBLE);
						mRecordAlertTv.setText("" + (commentCount > 99 ? 99 : commentCount));
					} else {
						mRecordAlertTv.setVisibility(View.GONE);
					}
					
					if(msgCount > 0) {
						mMsgCenterAlertTv.setVisibility(View.VISIBLE);
						mMsgCenterAlertTv.setText("" + (msgCount > 99 ? 99 : msgCount));
					} else {
						mMsgCenterAlertTv.setVisibility(View.GONE);
					}
					
					if(onMsgCountChangeLis != null) {
						onMsgCountChangeLis.onMsgCountChange(msgCount + commentCount);
					}
				}
			}
		});
	}
	
	public onMsgCountChangeListener getOnMsgCountChangeLis() {
		return onMsgCountChangeLis;
	}

	public void setOnMsgCountChangeLis(onMsgCountChangeListener onMsgCountChangeLis) {
		this.onMsgCountChangeLis = onMsgCountChangeLis;
	}

	public interface onMsgCountChangeListener{
		void onMsgCountChange(int count);
	}
}
