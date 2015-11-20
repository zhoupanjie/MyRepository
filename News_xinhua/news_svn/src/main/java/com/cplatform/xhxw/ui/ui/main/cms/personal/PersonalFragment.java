package com.cplatform.xhxw.ui.ui.main.cms.personal;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.StatisticalKey;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.BaseRequest;
import com.cplatform.xhxw.ui.http.net.NewMsgAlertResponse;
import com.cplatform.xhxw.ui.model.UserNewMsgAlert;
import com.cplatform.xhxw.ui.ui.base.BaseFragment;
import com.cplatform.xhxw.ui.ui.collect.CollectActivity;
import com.cplatform.xhxw.ui.ui.comment.MyCommentActivity;
import com.cplatform.xhxw.ui.ui.feedback.FeedbackActivity;
import com.cplatform.xhxw.ui.ui.login.LoginActivity;
import com.cplatform.xhxw.ui.ui.login.RegisterCheckActivity;
import com.cplatform.xhxw.ui.ui.main.HomeActivity;
import com.cplatform.xhxw.ui.ui.main.cms.MyMsgActivity;
import com.cplatform.xhxw.ui.ui.main.cms.game.GameDownloadCenterActivity;
import com.cplatform.xhxw.ui.ui.main.saas.EnterpriseMainFragment;
import com.cplatform.xhxw.ui.ui.message.MessageActivity;
import com.cplatform.xhxw.ui.ui.search.SearchActivity;
import com.cplatform.xhxw.ui.ui.settings.SettingsActivity;
import com.cplatform.xhxw.ui.ui.settings.UserInfoActivity;
import com.cplatform.xhxw.ui.util.Actions;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;
import com.wbtech.ums.UmsAgent;

/**
 * @author zpj
 * @version 1.0
 */

public class PersonalFragment extends BaseFragment {

	@InjectView(R.id.iv_avatar)
	ImageView mAvatar;
	@InjectView(R.id.tv_nickname)
	TextView mNickName;
	@InjectView(R.id.personal_reg_login_lo)
	LinearLayout mRegLoginLo;
	@InjectView(R.id.personal_reg_tv)
	TextView mRegBtn;
	@InjectView(R.id.personal_login_tv)
	TextView mLoginBtn;
	@InjectView(R.id.iv_msg_new)
	ImageView mMsgNew;
	@InjectView(R.id.iv_msg_myinfo)
	ImageView ivNewMyInfo;
	private View rootView;
	
	private NewMsgReceiver mReceiver;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_personal, container,
				false);
		ButterKnife.inject(this, rootView);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		mReceiver = new NewMsgReceiver();
		IntentFilter ift = new IntentFilter();
		ift.addAction(Actions.ACTION_MESSAGE_NEW_COUNT_CHANGE);
		getActivity().registerReceiver(mReceiver, ift);
	}

	/**
	 * 点击头像
	 */
	@OnClick(R.id.iv_avatar)
	public void personalLogin() {
		UmsAgent.onEvent(getActivity(), StatisticalKey.menu_head);
		personalContentRelative();
	}

	/**
	 * 点击注册
	 */
	@OnClick(R.id.personal_reg_tv)
	public void personalReg() {
		UmsAgent.onEvent(getActivity(), StatisticalKey.menu_register);
		getActivity().startActivityForResult(
				RegisterCheckActivity.getIntent(getActivity()), 1);
	}

	/**
	 * 点击登录
	 */
	@OnClick(R.id.personal_login_tv)
	public void personalLog() {
		UmsAgent.onEvent(getActivity(), StatisticalKey.menu_login);
		getActivity().startActivityForResult(
				LoginActivity.getIntent(getActivity()),
				HomeActivity.REQUEST_CODE_LOGIN);
	}

	/**
	 * 我的消息
	 */
	@OnClick(R.id.btn_personal_myinfo)
	public void personalMyInfo() {
		UmsAgent.onEvent(getActivity(), StatisticalKey.menu_sysmsg);
		Intent it = new Intent();
		it.setClass(getActivity(), MyMsgActivity.class);
		getActivity().startActivity(it);
	}

	/**
	 * 设置
	 */
	@OnClick(R.id.btn_personal_setting)
	public void personalSetting() {
		MobclickAgent.onEvent(getActivity(), StatisticalKey.menu_setting);
		UmsAgent.onEvent(getActivity(), StatisticalKey.menu_setting);
		getActivity().startActivityForResult(
				SettingsActivity.getIntent(getActivity()),
				EnterpriseMainFragment.REQUEST_CODE_SETTING);
	}

	/**
	 * 下载中心
	 */
	@OnClick(R.id.btn_personal_download)
	public void openGameDownList() {// 下载列表
		Intent intent = new Intent(getActivity(),
				GameDownloadCenterActivity.class);
		getActivity().startActivity(intent);
	}

	/**
	 * 多语种
	 */
	@OnClick(R.id.btn_personal_mutilanguage)
	public void personalSwitchLanguage(){
		((HomeActivity)getActivity()).showSwitchLanguageAlert();
	}
	/**
	 * 要闻消息
	 */
	@OnClick(R.id.btn_personal_alert)
	public void personalAlertAction() {
		MobclickAgent.onEvent(getActivity(), StatisticalKey.menu_message);
		UmsAgent.onEvent(getActivity(), StatisticalKey.menu_message);
		App.getPreferenceManager().setMessageNewCount(0); // 清空消息数量
		getActivity().startActivity(MessageActivity.getIntent(getActivity()));
	}

	/**
	 * 我的评论
	 */
	@OnClick(R.id.btn_personal_mycomments)
	public void personalCommentAction() {
		MobclickAgent.onEvent(getActivity(), StatisticalKey.menu_comment);
		UmsAgent.onEvent(getActivity(), StatisticalKey.menu_comment);
		getActivity().startActivity(MyCommentActivity.getIntent(getActivity()));
	}

	/**
	 * 新闻搜索
	 */
	@OnClick(R.id.btn_personal_search_news)
	public void personalSearchAction() {
		MobclickAgent.onEvent(getActivity(), StatisticalKey.menu_search);
		UmsAgent.onEvent(getActivity(), StatisticalKey.menu_search);
		getActivity().startActivity(SearchActivity.getIntent(getActivity()));
	}

	/**
	 * 收藏
	 */
	@OnClick(R.id.btn_personal_collection)
	public void personalCollectAction() {
		MobclickAgent.onEvent(getActivity(), StatisticalKey.menu_favorit);
		UmsAgent.onEvent(getActivity(), StatisticalKey.menu_favorit);
		getActivity().startActivity(CollectActivity.getIntent(getActivity()));
	}

	/**
	 * 意见反馈
	 */
	@OnClick(R.id.btn_personal_feedback)
	public void personalSettingAction() {
		UmsAgent.onEvent(getActivity(), StatisticalKey.menu_feedback);
		getActivity().startActivity(FeedbackActivity.getIntent(getActivity()));
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			displayAccount();
		}
		super.onHiddenChanged(hidden);
	}

	@Override
	public void onResume() {
		displayAccount();
		super.onResume();
	}

	@Override
	public void onDestroyView() {
		getActivity().unregisterReceiver(mReceiver);
		super.onDestroyView();
	}

	public void displayAccount() {
		if (Constants.hasLogin()) {
			ImageLoader.getInstance().displayImage(
					Constants.userInfo.getLogo(), mAvatar,
					DisplayImageOptionsUtil.avatarImagesOptions);
			mNickName.setText(Constants.userInfo.getNickName());
		} else {
			mAvatar.setImageResource(R.drawable.personal_avatar);

			mNickName.setText("");
		}
		setViewDisplayByLogStatus();
		updateMessageNewStatus();
	}

	/**
	 * 更新新消息提示
	 */
	public void updateMessageNewStatus() {
		int msgVis = (App.getPreferenceManager().getMessageNewCount() == 0) ? View.GONE
				: View.VISIBLE;
		if(getActivity() != null) {
			mMsgNew.setVisibility(msgVis);
		}
	}

	private void setViewDisplayByLogStatus() {
		if (Constants.hasLogin()) {
			mRegLoginLo.setVisibility(View.GONE);
		} else {
			mRegLoginLo.setVisibility(View.VISIBLE);
		}
	}

	public void UmsTextMode() {
		UmsAgent.onEvent(getActivity(), StatisticalKey.menu_textmode,
				new String[] { StatisticalKey.key_type },
				new String[] { Constants.getNewsDetTextSize() + "" });
	}

	/**
	 * 跳转到登陆界面
	 */
	public void personalContentRelative() {
		if (!Constants.hasLogin()) {
			getActivity().startActivityForResult(
					LoginActivity.getIntent(getActivity()),
					HomeActivity.REQUEST_CODE_LOGIN);
		} else {
			getActivity().startActivity(
					UserInfoActivity.getIntent(getActivity()));
		}
	}

	/**
	 * 获取未读系统消息数目
	 */
	public void getNewMsgCount() {
		APIClient.getIsHaveNewMsg(new BaseRequest(),
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, String content) {
						NewMsgAlertResponse response = null;
						try {
							ResponseUtil.checkResponse(content);
							response = new Gson().fromJson(content,
									NewMsgAlertResponse.class);
						} catch (Exception e) {
							e.printStackTrace();
						}
						if (response == null)
							return;
						if (response.isSuccess()) {
							UserNewMsgAlert data = response.getData();
							if (data.getNewMsgNum() > 0) {
								ivNewMyInfo.setVisibility(View.VISIBLE);
							} else {
								ivNewMyInfo.setVisibility(View.GONE);
							}
						}
					}
				});
	}

	class NewMsgReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(Actions.ACTION_MESSAGE_NEW_COUNT_CHANGE)) {
				updateMessageNewStatus();
			}
		}
	}
}
