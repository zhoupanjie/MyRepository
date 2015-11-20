package com.cplatform.xhxw.ui.ui.search;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

import com.cplatform.xhxw.ui.PreferencesManager;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.StatisticalKey;
import com.cplatform.xhxw.ui.db.SearchDB;
import com.cplatform.xhxw.ui.db.dao.SearchDao;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.BaseRequest;
import com.cplatform.xhxw.ui.http.net.HotSearchWordResponse;
import com.cplatform.xhxw.ui.http.net.NewsSearchRequest;
import com.cplatform.xhxw.ui.http.net.NewsSearchResponse;
import com.cplatform.xhxw.ui.model.HotSearchWord;
import com.cplatform.xhxw.ui.model.Search;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.adapter.SearchAdapter;
import com.cplatform.xhxw.ui.ui.base.adapter.SearchRecordAdapter;
import com.cplatform.xhxw.ui.ui.base.view.HotWordsRectView;
import com.cplatform.xhxw.ui.ui.base.view.HotWordsRectView.onHotWordClickListener;
import com.cplatform.xhxw.ui.ui.base.widget.DefaultView;
import com.cplatform.xhxw.ui.ui.base.widget.PullRefreshListView;
import com.cplatform.xhxw.ui.ui.base.widget.PullRefreshListView.PullRefreshListener;
import com.cplatform.xhxw.ui.util.ActivityUtil;
import com.cplatform.xhxw.ui.util.DateUtil;
import com.cplatform.xhxw.ui.util.KeyboardUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.cplatform.xhxw.ui.util.StringUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.wbtech.ums.UmsAgent;

public class SearchActivity extends BaseActivity implements TextWatcher,
		PullRefreshListener, AdapterView.OnItemClickListener,
		onHotWordClickListener {

	private static final String TAG = SearchActivity.class.getSimpleName();
	private static final int COUNT = 10;

	@Bind(R.id.search_edit)
	EditText editText;
	@Bind(R.id.search_clear)
	ImageView clear;
	@Bind(R.id.listview)
	PullRefreshListView listView;
	@Bind(R.id.def_view)
	DefaultView mDefView;
	@Bind(R.id.search_hot_words)
	HotWordsRectView mHotWordsView;
	@Bind(R.id.search_activity_container)
	LinearLayout mRootContainer;

	private String mKey;
	private int mPage;
	private View mCleanHistory;
	private TextView mCleanTV;
	private TextView mCloseTV;
	private int mOriginalHeight = 0;
	private int mSoftKeyboardHeight = 0;

	private InputMethodManager mInputManager;
	private boolean mIsSoftKeyboardPoped = false;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				mHttpResponseHandler = null;
				listView.onRefreshComplete(null);
				listView.onLoadMoreComplete();
				mDefView.setStatus(DefaultView.Status.showData);
				break;
			default:
				break;
			}
		}
	};

	public static Intent getIntent(Context context) {
		Intent intent = new Intent(context, SearchActivity.class);
		return intent;
	}

	@Override
	protected String getScreenName() {
		return "SearchActivity";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_search);

		ButterKnife.bind(this);

		init();
		getHotSearchWords();
	}

	private void init() {
		initActionBar();
		editText.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (mHotWordsView.getVisibility() == View.VISIBLE) {
					showSearchRecordAdapter();
					mHotWordsView.setVisibility(View.GONE);
				}
				return false;
			}
		});
		editText.addTextChangedListener(this);
		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() > 0) {
					showSearchRecordAdapter();
					mHotWordsView.setVisibility(View.GONE);
				} else {
					listView.setAdapter(null);
					listView.setCanRefresh(false);
					listView.setCanLoadMore(false);
					listView.removeFooterView(mCleanHistory);
					mHotWordsView.setVisibility(View.VISIBLE);
				}
			}
		});
		editText.clearFocus();
		listView.setPullRefreshListener(this);
		listView.setCanRefresh(false);
		listView.setCanLoadMore(false);

		mCleanHistory = LayoutInflater.from(this).inflate(
				R.layout.view_search_clean, null);
		mCleanTV = (TextView) mCleanHistory
				.findViewById(R.id.search_hist_clear);
		mCloseTV = (TextView) mCleanHistory
				.findViewById(R.id.search_hist_close);
		mCleanTV.setTextColor(Color.rgb(0, 191, 255));
		mCloseTV.setTextColor(Color.rgb(0, 191, 255));
		mCleanTV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(SearchActivity.this)
						.setMessage(R.string.clear_history_verify)
						.setCancelable(true)
						.setPositiveButton(R.string.confirm,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										SearchDB.delAllSearch(SearchActivity.this);
										ListAdapter headerAdapter = listView
												.getAdapter();
										if (headerAdapter != null
												&& headerAdapter instanceof HeaderViewListAdapter) {
											ListAdapter adp = ((HeaderViewListAdapter) headerAdapter)
													.getWrappedAdapter();
											if (adp != null
													&& adp instanceof SearchRecordAdapter) {
												SearchRecordAdapter tmp = (SearchRecordAdapter) adp;
												tmp.clearData();
												tmp.notifyDataSetChanged();
												listView.removeFooterView(mCleanHistory);
											}
										}
									}
								})
						.setNegativeButton(R.string.cancel,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								}).create().show();
			}
		});
		mCloseTV.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (editText.getText().toString().trim().length() == 0) {
					mHotWordsView.setVisibility(View.VISIBLE);
				}
				listView.setAdapter(null);
				listView.removeFooterView(mCleanHistory);
			}
		});
		listView.setAdapter(null);

		listView.setOnItemClickListener(this);
		mDefView.setHidenOtherView(listView);
		mDefView.setStatus(DefaultView.Status.showData);

		mHotWordsView.setVisibility(View.VISIBLE);
		mHotWordsView.setmOnhotWordClickListener(this);

		/**
		 * 监测软键盘
		 */
		mRootContainer.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						Rect r = new Rect();
						mRootContainer.getWindowVisibleDisplayFrame(r);
						int screenHeight = mRootContainer.getHeight();
						if (mOriginalHeight == 0) {
							mOriginalHeight = screenHeight;
						} else {
							if ((screenHeight != mOriginalHeight)
									&& (mSoftKeyboardHeight == 0)) {
								mSoftKeyboardHeight = Math.abs(screenHeight
										- mOriginalHeight);
								PreferencesManager.saveSoftKeyboardHeight(
										SearchActivity.this,
										mSoftKeyboardHeight);
							}
							if (screenHeight < mOriginalHeight) {
								mIsSoftKeyboardPoped = true;
							} else {
								mIsSoftKeyboardPoped = false;
							}
						}
					}
				});

		mInputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	private void getHotSearchWords() {
		APIClient.getHotSearchWords(new BaseRequest(),
				new AsyncHttpResponseHandler() {

					@Override
					protected void onPreExecute() {
					}

					@Override
					public void onFinish() {
					}

					@Override
					public void onSuccess(String content) {
						HotSearchWordResponse response;
						try {
							ResponseUtil.checkResponse(content);
							response = new Gson().fromJson(content,
									HotSearchWordResponse.class);
						} catch (Exception e) {
							showToast(R.string.data_format_error);
							LogUtil.w(TAG, e);
							return;
						}
						if (response.isSuccess()) {
							List<HotSearchWord> result = response.getData();
							mHotWordsView.setmWords(result);
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
					}

				});
	}

	@OnClick(R.id.search_clear)
	public void clear() {
		editText.setText("");
	}

	@OnClick(R.id.search_ok)
	public void search() {
		String key = editText.getText().toString().trim();
		if (TextUtils.isEmpty(key)) {
			showToast(this.getResources().getString(R.string.search_key_demand));
			return;
		}
		mKey = key;
		SearchDao dao = new SearchDao();
		dao.setName(key);
		dao.setLastTime(DateUtil.getTime());
		SearchDB.saveSearch(this, dao);
		listView.triggerRefresh(true);
		UmsAgent.onEvent(this, StatisticalKey.search_news,
				new String[] { StatisticalKey.key_keywords },
				new String[] { mKey });
	}

	private void showSearchRecordAdapter() {
		listView.setCanRefresh(false);
		listView.setCanLoadMore(false);
		if (mHttpResponseHandler != null) {
			mHttpResponseHandler.cancle();
			mHttpResponseHandler = null;
		}
		ListAdapter headerAdapter = listView.getAdapter();
		if (headerAdapter != null
				&& headerAdapter instanceof HeaderViewListAdapter) {
			ListAdapter adp = ((HeaderViewListAdapter) headerAdapter)
					.getWrappedAdapter();
			SearchRecordAdapter mAdapter;
			if (adp != null && adp instanceof SearchRecordAdapter) {
				mAdapter = (SearchRecordAdapter) adp;
			} else {
				mAdapter = new SearchRecordAdapter(this);
				listView.setAdapter(mAdapter);
			}
			mAdapter.clearData();
			mAdapter.addAllData(SearchDB.getSearchs(this));
			mAdapter.notifyDataSetChanged();
			if (mAdapter.getCount() > 0) {
				listView.addFooterView(mCleanHistory);
			} else {
				listView.removeFooterView(mCleanHistory);
			}

		}
	}

	@Override
	public void onRefresh() {
		getSearchDate(1);
	}

	@Override
	public void onLoadMore() {
		getSearchDate(mPage + 1);
	}

	@Override
	public void afterTextChanged(Editable string) {
		if (string.length() > 0) {
			clear.setVisibility(View.VISIBLE);
		} else {
			clear.setVisibility(View.GONE);
		}
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	}

	/**
	 * 获取评论数据
	 */
	private AsyncHttpResponseHandler mHttpResponseHandler;

	private void getSearchDate(final int p) {
		KeyboardUtil.hideSoftInput(this);
		listView.setCanRefresh(false);
		APIClient.newsSearch(new NewsSearchRequest(COUNT, p, mKey),
				new AsyncHttpResponseHandler() {

					@Override
					public void onFinish() {
						mHandler.sendEmptyMessage(1);
					}

					@Override
					protected void onPreExecute() {
						if (mHttpResponseHandler != null)
							mHttpResponseHandler.cancle();
						mHttpResponseHandler = this;
						ListAdapter headerAdapter = listView.getAdapter();
						if (headerAdapter != null
								&& headerAdapter instanceof HeaderViewListAdapter) {
							ListAdapter adp = ((HeaderViewListAdapter) headerAdapter)
									.getWrappedAdapter();
							if (adp instanceof SearchRecordAdapter) {
								mDefView.setStatus(DefaultView.Status.loading);
							}
						}

					}

					@Override
					public void onSuccess(String content) {
						NewsSearchResponse response;
						try {
							ResponseUtil.checkResponse(content);
							response = new Gson().fromJson(content,
									NewsSearchResponse.class);
						} catch (Exception e) {
							showToast(R.string.data_format_error);
							LogUtil.w(TAG, e);
							return;
						}
						if (response.isSuccess()) {
							mPage = p;
							ListAdapter headerAdapter = listView.getAdapter();
							if (headerAdapter != null
									&& headerAdapter instanceof HeaderViewListAdapter) {
								ListAdapter adp = ((HeaderViewListAdapter) headerAdapter)
										.getWrappedAdapter();
								SearchAdapter adapter;
								if (adp != null && adp instanceof SearchAdapter) {
									adapter = (SearchAdapter) adp;
								} else {
									adapter = new SearchAdapter(
											SearchActivity.this);
									listView.setAdapter(adapter);
								}
								if (p == 1) {
									adapter.clearData();
								}
								adapter.addAllData(response.getList());
								try {
									if (adapter.getCount() < Long
											.valueOf(response.getData()
													.getCount())) {
										listView.setCanLoadMore(true);
									} else {
										listView.setCanLoadMore(false);
									}
								} catch (Exception e) {
									LogUtil.e(TAG, e);
									listView.setCanLoadMore(false);
								}
								listView.removeFooterView(mCleanHistory);
								adapter.notifyDataSetChanged();
								if (adapter.getCount() == 0) {
									showToast(R.string.no_search_results);
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Adapter headAdp = parent.getAdapter();
		if (headAdp instanceof HeaderViewListAdapter) {
			Adapter adp = ((HeaderViewListAdapter) headAdp).getWrappedAdapter();
			if (adp instanceof SearchAdapter) {
				SearchAdapter adapter = (SearchAdapter) adp;
				Search item = adapter.getItem(position - 1);
				int newsType = (item.getNewstype() == null) ? 0 : Integer
						.valueOf(item.getNewstype());
				ActivityUtil.goToNewsDetailPageByNewstype(SearchActivity.this,
						newsType, item.getId(), null, false, item.getNewsTitle());
			} else if (adp instanceof SearchRecordAdapter) {
				SearchRecordAdapter adapter = (SearchRecordAdapter) adp;
				SearchDao item = adapter.getItem(position - 1);
				item.setLastTime(DateUtil.getTime());
				SearchDB.updateSearch(this, item);
				editText.setText(item.getName());
				editText.setSelection(StringUtil.getLength(item.getName()));
				mKey = item.getName();
				listView.triggerRefresh(true);
			}
		}
	}

	@Override
	public void onHotWordClick(String word) {
		editText.setText(word);
		mKey = word;
		SearchDao dao = new SearchDao();
		dao.setName(mKey);
		dao.setLastTime(DateUtil.getTime());
		SearchDB.saveSearch(this, dao);
		mHotWordsView.setVisibility(View.GONE);
		listView.triggerRefresh(true);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			if ((ev.getY() < mOriginalHeight - mSoftKeyboardHeight)
					&& mIsSoftKeyboardPoped) {
				mIsSoftKeyboardPoped = false;
				mInputManager.hideSoftInputFromWindow(
						editText.getWindowToken(), 0);
				return true;
			}
		}
		return super.dispatchTouchEvent(ev);
	}
}
