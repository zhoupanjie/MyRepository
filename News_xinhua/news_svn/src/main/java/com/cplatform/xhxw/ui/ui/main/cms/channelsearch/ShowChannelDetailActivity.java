package com.cplatform.xhxw.ui.ui.main.cms.channelsearch;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.PreferencesManager;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.db.ChanneDB;
import com.cplatform.xhxw.ui.db.dao.ChanneDao;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.AddChannelRequest;
import com.cplatform.xhxw.ui.http.net.BaseResponse;
import com.cplatform.xhxw.ui.http.net.ChannelSearchAddRequest;
import com.cplatform.xhxw.ui.http.net.ChannelSearchAddResponse;
import com.cplatform.xhxw.ui.http.net.ChannelSearchCancelRequest;
import com.cplatform.xhxw.ui.http.net.DelChannelRequest;
import com.cplatform.xhxw.ui.model.Channe;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.main.cms.NewsFragment;
import com.cplatform.xhxw.ui.ui.main.cms.game.GameFragment;
import com.cplatform.xhxw.ui.ui.main.cms.game.GameUtil;
import com.cplatform.xhxw.ui.ui.main.forelanguage.utils.LanguageUtil;
import com.cplatform.xhxw.ui.util.StringUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class ShowChannelDetailActivity extends BaseActivity {

	public static Intent getThisIntent(Context con, String channelId,
			String channelName, int channelType, int isAdd, long listorder) {
		Intent it = new Intent();
		it.setClass(con, ShowChannelDetailActivity.class);
		it.putExtra("channel_id", channelId);
		it.putExtra("channel_name", channelName);
		it.putExtra("channel_type", channelType);
		it.putExtra("is_add", isAdd);
		it.putExtra("channel_listorder", listorder);
		return it;
	}

	private String mChannelId;
	private String mChannelName;
	private int mChannelType;
	private int isAdd;
	private long mChannelOrder;
	private boolean isStatusChange = false;

	private TextView mTitleTv;
	private ImageView mAddBtn;
	private Button mBackBtn;
	private ProgressDialog mProgressDialog;

	@Override
	protected String getScreenName() {
		return null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_search_channel_detail);
		inits();
	}

	private void inits() {
		mChannelId = getIntent().getStringExtra("channel_id");
		mChannelName = getIntent().getStringExtra("channel_name");
		mChannelType = getIntent().getIntExtra("channel_type",
				ChanneDao.CHANNEL_TYPE_KEY_WORDS);
		isAdd = getIntent().getIntExtra("is_add", 0);
		mChannelOrder = getIntent().getLongExtra("channel_listorder", 1);
		mTitleTv = (TextView) findViewById(R.id.channel_detail_title_tv);
		mTitleTv.setText(mChannelName);
		mAddBtn = (ImageView) findViewById(R.id.channel_detail_add_btn);
		mAddBtn.setImageResource(isAdd == 1 ? R.drawable.icon_search_delete
				: R.drawable.icon_search_add);
		mBackBtn = (Button) findViewById(R.id.btn_back);
		mAddBtn.setOnClickListener(mOnclick);
		mBackBtn.setOnClickListener(mOnclick);

		// 判断切换普通新闻频道还是游戏频道
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		Bundle bundle = new Bundle();
		bundle.putString("channelid", mChannelId);
		bundle.putString("channel_keyword", mChannelName);
		NewsFragment fragm = new NewsFragment();
		fragm.setArguments(bundle);
		t.replace(R.id.channel_detail_fg_lo, fragm).commit();
	}

	OnClickListener mOnclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.channel_detail_add_btn) {
				subscribeChannel(isAdd == 0 ? true : false);
			} else if (v.getId() == R.id.btn_back) {
				finish();
			}
		}
	};

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	private void subscribeChannel(final boolean isToSub) {
		new AlertDialog.Builder(this)
				.setTitle(R.string.edit_channel_manage)
				.setMessage(
						isToSub ? R.string.channel_sub_verify
								: R.string.channel_sub_cancel_verify)
				.setPositiveButton(R.string.confirm,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								commitRequest(isToSub);
							}
						}).setNegativeButton(R.string.cancel, null).show();
	}

	private void commitRequest(boolean isToSub) {
		mProgressDialog = ProgressDialog.show(this,
				getString(R.string.edit_channel_manage),
				getString(R.string.commit_dialog_msg));
		if (isToSub) {
			if (mChannelType == ChanneDao.CHANNEL_TYPE_KEY_WORDS) {
				subscribeRequest();
			} else {
				subscribeCmsChannelReqeust();
			}
		} else {
			if (mChannelType == ChanneDao.CHANNEL_TYPE_KEY_WORDS) {
				cancelSubRequest();
			} else {
				cancelSubCmsChannel();
			}
		}
	}

	/**
	 * 订阅普通栏目
	 * 
	 * @param channe
	 */
	private void subscribeCmsChannelReqeust() {
		AddChannelRequest request = new AddChannelRequest(mChannelId,
				mChannelOrder, 0);
		APIClient.asyncAddChannel(request, new AsyncHttpResponseHandler() {
			@Override
			public void onFinish() {
				super.onFinish();
				mProgressDialog.dismiss();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				BaseResponse response = null;
				try {
					response = new Gson().fromJson(content, BaseResponse.class);
					ResponseUtil.checkObjResponse(response);
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				if (response != null && response.isSuccess()) {
					Channe channel = new Channe();
					channel.setChannelid(mChannelId);
					channel.setChannelname(mChannelName);
					channel.setChanneltype(mChannelType);
					channel.setListorder(100);
					channel.setIsadd(1);
					isAdd = 1;
					updateChannelToDb(channel);
					mAddBtn.setImageResource(R.drawable.icon_search_delete);
					showToast(R.string.channel_sub_success);
					isStatusChange = true;
				} else {
					showToast(R.string.channel_sub_fail);
				}
				mProgressDialog.dismiss();
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				showToast(R.string.channel_sub_fail);
				mProgressDialog.dismiss();
			}
		});
	}

	/**
	 * 订阅关键字栏目
	 */
	private void subscribeRequest() {
		ChannelSearchAddRequest reqeust = new ChannelSearchAddRequest();
		reqeust.setChannelid(StringUtil.parseIntegerFromString(mChannelId));
		reqeust.setChannelname(mChannelName);
		reqeust.setType(mChannelType);

		APIClient.subscribeSearchedChannel(reqeust,
				new AsyncHttpResponseHandler() {

					@Override
					public void onFinish() {
						super.onFinish();
						mProgressDialog.dismiss();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						ChannelSearchAddResponse response = null;
						try {
							response = new Gson().fromJson(content,
									ChannelSearchAddResponse.class);
							ResponseUtil.checkObjResponse(response);
						} catch (Exception e) {
							e.printStackTrace();
							return;
						}
						if (response != null && response.isSuccess()) {
							Channe channel = response.getData().getList();
							mChannelId = channel.getChannelid();
							isAdd = 1;
							updateChannelToDb(channel);
							mAddBtn.setImageResource(R.drawable.icon_search_delete);
							showToast(R.string.channel_sub_success);
							isStatusChange = true;
						} else {
							showToast(R.string.channel_sub_fail);
						}
						mProgressDialog.dismiss();
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						showToast(R.string.channel_sub_fail);
						mProgressDialog.dismiss();
					}
				});
	}

	/**
	 * 取消订阅普通栏目
	 * 
	 * @param channel
	 */
	private void cancelSubCmsChannel() {
		DelChannelRequest request = new DelChannelRequest(mChannelId, 0);
		APIClient.asyncDelChannel(request, new AsyncHttpResponseHandler() {
			@Override
			public void onFinish() {
				super.onFinish();
				mProgressDialog.dismiss();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				BaseResponse response = null;
				try {
					response = new Gson().fromJson(content, BaseResponse.class);
					ResponseUtil.checkObjResponse(response);
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				if (response != null && response.isSuccess()) {
					isAdd = 0;
					mAddBtn.setImageResource(R.drawable.icon_search_add);
					isStatusChange = true;
					dealCancelSubData();
					showToast(R.string.channel_sub_success);
				} else {
					showToast(R.string.channel_sub_fail);
				}
				mProgressDialog.dismiss();
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				showToast(R.string.channel_sub_fail);
				mProgressDialog.dismiss();
			}
		});
	}

	/**
	 * 取消订阅关键字栏目
	 */
	private void cancelSubRequest() {
		ChannelSearchCancelRequest request = new ChannelSearchCancelRequest();
		request.setChannelid(StringUtil.parseIntegerFromString(mChannelId));
		request.setOperatetime("" + 0);

		APIClient.cancelSubChannel(request, new AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				super.onFinish();
				mProgressDialog.dismiss();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				BaseResponse response = null;
				try {
					response = new Gson().fromJson(content, BaseResponse.class);
					ResponseUtil.checkObjResponse(response);
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				if (response != null && response.isSuccess()) {
					isAdd = 0;
					mAddBtn.setImageResource(R.drawable.icon_search_add);
					isStatusChange = true;
					dealCancelSubData();
					showToast(R.string.channel_sub_success);
				} else {
					showToast(R.string.channel_sub_fail);
				}
				mProgressDialog.dismiss();
			}

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				mProgressDialog.dismiss();
				showToast(R.string.channel_sub_fail);
			}
		});
	}

	/**
	 * 保存订阅频道到数据库
	 * 
	 * @param channel
	 */
	private void updateChannelToDb(Channe channel) {
		ChanneDao cd = new ChanneDao();
		cd.setChannelID(channel.getChannelid());
		cd.setUserId(Constants.getDbUserId());
		cd.setChannelName(channel.getChannelname());
		String channLang = PreferencesManager.getLanguageInfo(this) == null ? LanguageUtil.LANGUAGE_CH
				: PreferencesManager.getLanguageInfo(this);
		cd.setChannelLanguage(channLang);
		cd.setChannelCreateType(String.valueOf(channel.getChanneltype()));
		cd.setEnterpriseId(0);
		cd.setListorder(100);
		cd.setShow(0);
		cd.setEnterpriseId(0);
		ChanneDB.updateChanneById(this, cd);
	}

	private void dealCancelSubData() {
		ChanneDao cd = new ChanneDao();
		cd.setChannelID(mChannelId);
		cd.setUserId(Constants.getDbUserId());
		cd.setChannelName(mChannelName);
		cd.setChannelCreateType(String.valueOf(mChannelType));
		cd.setEnterpriseId(0);
		cd.setShow(1);
		if (mChannelType == ChanneDao.CHANNEL_TYPE_KEY_WORDS) {
			ChanneDB.delChanneByChannelId(this, mChannelId);
		} else {
			ChanneDB.updateChanneById(this, cd);
		}
	}

	@Override
	public void finish() {
		if (isStatusChange) {
			Intent it = new Intent();
			it.putExtra("channel_id", mChannelId);
			it.putExtra("channel_name", mChannelName);
			it.putExtra("is_add", isAdd);
			setResult(RESULT_OK, it);
		} else {
			setResult(RESULT_CANCELED);
		}
		super.finish();
	}
}
