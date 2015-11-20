package com.cplatform.xhxw.ui.ui.main.cms.channelsearch;

import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.PreferencesManager;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.StatisticalKey;
import com.cplatform.xhxw.ui.db.ChanneDB;
import com.cplatform.xhxw.ui.db.dao.ChanneDao;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.AddChannelRequest;
import com.cplatform.xhxw.ui.http.net.BaseResponse;
import com.cplatform.xhxw.ui.http.net.ChannelSearchAddRequest;
import com.cplatform.xhxw.ui.http.net.ChannelSearchAddResponse;
import com.cplatform.xhxw.ui.http.net.ChannelSearchCancelRequest;
import com.cplatform.xhxw.ui.http.net.ChannelSearchRequest;
import com.cplatform.xhxw.ui.http.net.ChannelSearchResponse;
import com.cplatform.xhxw.ui.http.net.ChannelSearchResponse.ChannelSearchData;
import com.cplatform.xhxw.ui.http.net.DelChannelRequest;
import com.cplatform.xhxw.ui.model.Channe;
import com.cplatform.xhxw.ui.ui.apptips.TipsActivity;
import com.cplatform.xhxw.ui.ui.apptips.TipsUtil;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.main.forelanguage.utils.LanguageUtil;
import com.cplatform.xhxw.ui.util.NetUtils;
import com.cplatform.xhxw.ui.util.StringUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.wbtech.ums.UmsAgent;

public class ChannelSearchActivity extends BaseActivity implements
		OnChannelItemClickLisenter {
	private static final String TAG = ChannelSearchActivity.class.getName();

	private static final int REQUESTCODE_SUB_CHANNEL = 1001;

	private EditText mSearchET;
	private Button mSearchBtn;
	private ImageView mBackBtn;
	private TextView mSearchResultHint;
	private ListView mSearchResultList;
	private ProgressBar mSearchPb;
	private ProgressDialog mProgressDialog;

	private String mCurrentSearchStr = "";
	private ChannelSearchResultAdapter mAdapter;
	private List<Channe> mSearchResult;
	private boolean isChannelChange = false;
	private boolean isTipShown = TipsUtil.isTipShown(TipsUtil.TIP_CHANNEL_ADD);

	@Override
	protected String getScreenName() {
		return null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_channel_search);
		initViews();
	}

	private void initViews() {
		mSearchBtn = (Button) findViewById(R.id.channel_search_search_btn);
		mBackBtn = (ImageView) findViewById(R.id.btn_back);
		mSearchET = (EditText) findViewById(R.id.channel_search_edittext);
		mSearchPb = (ProgressBar) findViewById(R.id.channel_search_pb);
		mSearchPb.setVisibility(View.GONE);
		mSearchResultHint = (TextView) findViewById(R.id.channel_search_result_hint_tv);
		mSearchResultHint.setText(getString(R.string.channel_sub_search_hint,
				""));
		mSearchResultList = (ListView) findViewById(R.id.channel_search_result_listv);
		mAdapter = new ChannelSearchResultAdapter(this);
		mAdapter.setmItemClickLisenter(this);
		mSearchResultList.setAdapter(mAdapter);
		mSearchET.addTextChangedListener(mTextWatcher);

		mSearchBtn.setOnClickListener(mOnclickLis);
		mBackBtn.setOnClickListener(mOnclickLis);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	/**
	 * 搜索框内容变化监听
	 */
	TextWatcher mTextWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			searchChannel(s.toString().trim());
		}
	};

	OnClickListener mOnclickLis = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.channel_search_search_btn) {
				onSearchBtnClick();
			} else if (v.getId() == R.id.btn_back) {
				backToPrePage();
			}
		}
	};

	private void onSearchBtnClick() {
		String searchedWords = mSearchET.getText().toString().trim();
		if (searchedWords == null || searchedWords.length() < 1) {
			return;
		}
		if (mSearchResult == null) {
			searchChannel(searchedWords);
			return;
		}
		Channe searchedChanne = null;
		for (Channe ch : mSearchResult) {
			if (ch.getChannelname().equals(searchedWords)) {
				searchedChanne = ch;
				break;
			}
		}
		UmsAgent.onEvent(this, StatisticalKey.search_channel,
				new String[] { StatisticalKey.key_keywords },
				new String[] { searchedWords });
		if (searchedChanne == null) {
			searchChannel(searchedWords);
		} else {
			startActivityForResult(ShowChannelDetailActivity.getThisIntent(
					this, searchedChanne.getChannelid(),
					searchedChanne.getChannelname(),
					searchedChanne.getChanneltype(), searchedChanne.getIsadd(),
					searchedChanne.getListorder()), REQUESTCODE_SUB_CHANNEL);
		}
	}

	/**
	 * 按关键词搜索栏目
	 * 
	 * @param keyword
	 */
	private void searchChannel(String keyword) {
		if (keyword == null || keyword.equals(mCurrentSearchStr)) {
			return;
		}
		if (keyword.length() == 0 && mCurrentSearchStr.length() > 0) {
			mAdapter.setmData(null);
			mCurrentSearchStr = "";
			mSearchResultHint.setText(getString(
					R.string.channel_sub_search_hint, ""));
			return;
		}
		if (!NetUtils.isNetworkAvailable()) {
			showToast(R.string.network_invalid);
			return;
		}
		mCurrentSearchStr = keyword;
		mSearchPb.setVisibility(View.VISIBLE);
		ChannelSearchRequest request = new ChannelSearchRequest();
		request.setKeyword(keyword);
		APIClient.searchChannelsByKeyword(request,
				new AsyncHttpResponseHandler() {

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						super.onFinish();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						ChannelSearchResponse response;
						try {
							response = new Gson().fromJson(content,
									ChannelSearchResponse.class);
							ResponseUtil.checkObjResponse(response);
						} catch (Exception e) {
							e.printStackTrace();
							return;
						}
						if (response != null && response.isSuccess()) {
							ChannelSearchData data = response.getData();
							if (data != null
									&& data.getKeyword() != null
									&& data.getKeyword().equals(
											mCurrentSearchStr)) {
								mSearchResult = data.getList();
								mAdapter.setmData(mSearchResult);
								mSearchResultHint.setText(getString(
										R.string.channel_sub_search_hint,
										mCurrentSearchStr + "  "));
								mSearchPb.setVisibility(View.GONE);
								if (!isTipShown) {
									startActivity(TipsActivity.getTipIntent(
											ChannelSearchActivity.this,
											TipsUtil.TIP_CHANNEL_ADD));
									isTipShown = true;
								}
							}
						} else if (response != null) {
							mSearchPb.setVisibility(View.GONE);
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						// TODO Auto-generated method stub
						super.onFailure(error, content);
					}
				});
	}

	@Override
	public void onItemClick(Channe item) {
		startActivityForResult(
				ShowChannelDetailActivity.getThisIntent(this,
						item.getChannelid(), item.getChannelname(),
						item.getChanneltype(), item.getIsadd(),
						item.getListorder()), REQUESTCODE_SUB_CHANNEL);
	}

	@Override
	public void onSubscribeChanne(Channe item) {
		subscribeChannel(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUESTCODE_SUB_CHANNEL) {
				int isAdd = data.getIntExtra("is_add", 0);
				String channelId = data.getStringExtra("channel_id");
				String channelName = data.getStringExtra("channel_name");
				updateListData(channelName, channelId, isAdd);
				isChannelChange = true;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void updateListData(String channeName, String channelId, int isAdd) {
		if (mSearchResult != null) {
			for (Channe ch : mSearchResult) {
				if (ch.getChannelname().equals(channeName)) {
					ch.setIsadd(isAdd);
					ch.setChannelid(channelId);
					break;
				}
			}
			mAdapter.notifyDataSetChanged();
		}
	}

	private void subscribeChannel(final Channe channe) {
		new AlertDialog.Builder(this)
				.setTitle(R.string.edit_channel_manage)
				.setMessage(
						channe.getIsadd() == 0 ? R.string.channel_sub_verify
								: R.string.channel_sub_cancel_verify)
				.setPositiveButton(R.string.confirm,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								commitRequest(channe);
							}
						}).setNegativeButton(R.string.cancel, null).show();
	}

	private void commitRequest(Channe channe) {
		mProgressDialog = ProgressDialog.show(this,
				getString(R.string.edit_channel_manage),
				getString(R.string.commit_dialog_msg));
		if (channe.getIsadd() == 0) {
			if (channe.getChanneltype() == ChanneDao.CHANNEL_TYPE_KEY_WORDS) {
				subscribeKeywodRequest(channe);
			} else {
				subscribeCmsChannelReqeust(channe);
			}
		} else {
			if (channe.getChanneltype() == ChanneDao.CHANNEL_TYPE_KEY_WORDS) {
				cancelSubRequest(channe);
			} else {
				cancelSubCmsChannel(channe);
			}
		}
	}

	/**
	 * 订阅普通栏目
	 * 
	 * @param channe
	 */
	private void subscribeCmsChannelReqeust(final Channe channe) {
		AddChannelRequest request = new AddChannelRequest(
				channe.getChannelid(), channe.getListorder(), 0);
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
					channe.setIsadd(1);
					updateChannelToDb(channe);
					isChannelChange = true;
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
	 * 订阅关键字栏目
	 * 
	 * @param channe
	 */
	private void subscribeKeywodRequest(Channe channe) {
		ChannelSearchAddRequest reqeust = new ChannelSearchAddRequest();
		reqeust.setChannelid(StringUtil.parseIntegerFromString(channe
				.getChannelid()));
		reqeust.setChannelname(channe.getChannelname());
		reqeust.setType(channe.getType());

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
							channel.setIsadd(1);
							updateChannelToDb(channel);
							isChannelChange = true;
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
	 * 取消订阅普通栏目
	 * 
	 * @param channel
	 */
	private void cancelSubCmsChannel(final Channe channel) {
		DelChannelRequest request = new DelChannelRequest(
				channel.getChannelid(), 0);
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
					dealCancelSubData(channel);
					showToast(R.string.channel_sub_success);
					isChannelChange = true;
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
	 * 
	 * @param channel
	 */
	private void cancelSubRequest(final Channe channel) {
		ChannelSearchCancelRequest request = new ChannelSearchCancelRequest();
		request.setChannelid(StringUtil.parseIntegerFromString(channel
				.getChannelid()));
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
					dealCancelSubData(channel);
					showToast(R.string.channel_sub_success);
					isChannelChange = true;
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
		updateListData(channel.getChannelname(), channel.getChannelid(),
				channel.getIsadd());
	}

	private void dealCancelSubData(Channe channel) {
		ChanneDao cd = new ChanneDao();
		cd.setChannelID(channel.getChannelid());
		cd.setUserId(Constants.getDbUserId());
		cd.setChannelName(channel.getChannelname());
		cd.setChannelCreateType(String.valueOf(channel.getChanneltype()));
		cd.setShow(1);
		cd.setEnterpriseId(0);
		if (channel.getType() == ChanneDao.CHANNEL_TYPE_KEY_WORDS) {
			ChanneDB.delChanneByChannelId(this, channel.getChannelid());
		} else {
			ChanneDB.updateChanneById(this, cd);
		}
		updateListData(channel.getChannelname(), channel.getChannelid(), 0);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			backToPrePage();
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}

	private void backToPrePage() {
		if (isChannelChange) {
			setResult(RESULT_OK);
		} else {
			setResult(RESULT_CANCELED);
		}
		finish();
	}
}
