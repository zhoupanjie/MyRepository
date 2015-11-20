package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.db.dao.FriendsMessageDao;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.httputils.HttpHanderUtil;
import com.cplatform.xhxw.ui.provider.XwContentProvider;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 朋友圈消息
 */
public class FriendsMessageActivity extends BaseActivity implements
		OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

	private static final String TAG = FriendsMessageActivity.class
			.getSimpleName();

	private ImageView clearBtn;
	private ListView mNetListView;
	private ListView mNativeListView;
	private FriendsMessageFooter footerLayout;
	private FriendsMessageNetAdapter mNetAdapter;
	private FriendsMessageNativeAdapter mNativeAdapter;
	private AsyncHttpResponseHandler mHttpResponseHandler;
	private SyncLocalMessageDataTask mSycnLocalTask;

	public static Intent newIntent(Context context) {
		Intent intent = new Intent(context, FriendsMessageActivity.class);
		return intent;
	}

	@Override
	protected String getScreenName() {
		return TAG;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_friends_message);

		init();

		setListener();

		getFriendsMessage();
	}

	private void init() {
		initActionBar();
		clearBtn = (ImageView) findViewById(R.id.message_clear_btn);
		mNetListView = (ListView) findViewById(R.id.friends_message_net_listview);
		mNativeListView = (ListView) findViewById(R.id.friends_message_native_listview);

		mNetAdapter = new FriendsMessageNetAdapter(this);
		mNativeAdapter = new FriendsMessageNativeAdapter(this, null,
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

		mNetListView.setAdapter(mNetAdapter);
		mNativeListView.setAdapter(mNativeAdapter);

		footerLayout = new FriendsMessageFooter(this);
		mNetListView.addFooterView(footerLayout);
	}

	private void setListener() {
		mNetListView.setOnItemClickListener(this);
		mNativeListView.setOnItemClickListener(this);

		// footerLayout.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// /** 查看更多---从数据库获取数据 */
		// clearBtn.setVisibility(View.VISIBLE);
		// mNetListView.setVisibility(View.GONE);
		// mNativeListView.setVisibility(View.VISIBLE);
		//
		// getSupportLoaderManager().initLoader(0, null,
		// FriendsMessageActivity.this);
		// }
		// });

		clearBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				/** 清除数据库里面的消息 */
				String selectionFriend = FriendsMessageDao.MY_UID + "=?";
				String[] projectionFriend = new String[] { Constants.getUid() };
				getContentResolver().delete(
						XwContentProvider.FRIENDS_MESSAGE_URL, selectionFriend,
						projectionFriend);

				getSupportLoaderManager().restartLoader(0, null,
						FriendsMessageActivity.this);
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> view, View arg1, int position,
			long arg3) {
		switch (view.getId()) {
		case R.id.friends_message_net_listview:
			if (mNetAdapter.getCount() == position) {
				/** 查看更多---从数据库获取数据 */
				clearBtn.setVisibility(View.VISIBLE);
				mNetListView.setVisibility(View.GONE);
				mNativeListView.setVisibility(View.VISIBLE);

				getSupportLoaderManager().initLoader(0, null,
						FriendsMessageActivity.this);
			} else {
				startActivity(FriendsFreshInfoActivity.newIntent(this,
						mNetAdapter.getItem(position).getInfoid()));
			}
			break;
		case R.id.friends_message_native_listview:
			Cursor cursor = mNativeAdapter.getCursor();
			if (cursor != null && cursor.moveToPosition(position)) {
				startActivity(FriendsFreshInfoActivity.newIntent(this, cursor
						.getString(cursor
								.getColumnIndex(FriendsMessageDao.INFOID))));
			}
			break;
		default:
			break;
		}
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String[] projection = new String[] { FriendsMessageDao._ID,
				FriendsMessageDao.USER_ID, FriendsMessageDao.LOGO,
				FriendsMessageDao.NAME, FriendsMessageDao.NICK_NAME,
				FriendsMessageDao.COMMENT, FriendsMessageDao.BACKINFO,
				FriendsMessageDao.INFODATA, FriendsMessageDao.INFOID };
		return new CursorLoader(FriendsMessageActivity.this,
				XwContentProvider.FRIENDS_MESSAGE_URL, projection, null, null,
				FriendsMessageDao._ID + " desc ");// " order by " +
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		mNativeAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mNativeAdapter.swapCursor(null);
	}

	/** 获取新的消息 */
	private void getFriendsMessage() {

		FriendsMessageRequest request = new FriendsMessageRequest();
		request.setDevRequest(true);

		APIClient.getFriendsMessage(request, new AsyncHttpResponseHandler() {

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
			}

			@Override
			public void onSuccess(String content) {
				FriendsMessageResponse response;
				try {
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content,
							FriendsMessageResponse.class);
				} catch (Exception e) {
					showToast(R.string.data_format_error);
					LogUtil.w(TAG, e);
					return;
				}

				if (response.isSuccess()) {
					if (response != null) {
						if (response.getData() != null
								&& response.getData().size() > 0) {

							mNetAdapter.addAllData(response.getData());
							mNetAdapter.notifyDataSetChanged();

							newSycnLocalTask(response.getData());
						}
					}
				} else {
					showToast(response.getMsg());
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				if (mNetAdapter.getCount() == 0) {

				} else {
					showToast(R.string.load_server_failure);
				}
			}
		});
	}

	private void newSycnLocalTask(List<FriendsMessage> messages) {
		if (mSycnLocalTask != null) {
			mSycnLocalTask.cancel(true);
			mSycnLocalTask = null;
		}
		mSycnLocalTask = new SyncLocalMessageDataTask(this, messages);
		mSycnLocalTask.execute();
	}

	/**
	 * 同步新的消息
	 */
	private class SyncLocalMessageDataTask extends AsyncTask<Void, Void, Void> {

		private List<FriendsMessage> _messages;
		private Context _context;

		public SyncLocalMessageDataTask(Context context,
				List<FriendsMessage> messages) {
			_messages = messages;
			_context = context;
		}

		@Override
		protected Void doInBackground(Void... params) {
			List<FriendsMessage> messages = _messages;
			int size = _messages.size();
			ContentValues[] arrayValues = new ContentValues[size];
			long sycnTime = System.currentTimeMillis();
			for (int i = 0; i < size; i++) {
				if (isCancelled()) {
					return null;
				}
				FriendsMessage item = _messages.get(i);
				ContentValues values = new ContentValues();
				values.put(FriendsMessageDao.MY_UID, Constants.getUid());
				values.put(FriendsMessageDao.USER_ID, item.getUserinfo()
						.getUserid());
				values.put(FriendsMessageDao.LOGO, item.getUserinfo().getLogo());
				values.put(FriendsMessageDao.NAME, item.getUserinfo().getName());
				values.put(FriendsMessageDao.NICK_NAME, item.getUserinfo()
						.getNickname());
				values.put(FriendsMessageDao.COMMENT, item.getUserinfo()
						.getComment());
				values.put(FriendsMessageDao.CTIME, item.getCtimel());
				values.put(FriendsMessageDao.FRIENDSTIME, item.getFriendtime());
				values.put(FriendsMessageDao.COMPANYID, item.getCompanyid());
				values.put(FriendsMessageDao.ACTIONTYPE, item.getActiontype());
				values.put(FriendsMessageDao.BACKINFO, item.getBackinfo());
				values.put(FriendsMessageDao.INFODATA, item.getInfodata());
				values.put(FriendsMessageDao.INFOID, item.getInfoid());
				values.put(FriendsMessageDao.SYNC_TIME, sycnTime);
				arrayValues[i] = values;
			}

			int count = _context.getContentResolver().bulkInsert(
					XwContentProvider.FRIENDS_MESSAGE_URL, arrayValues);
			LogUtil.d("testdata", "friends_message_book : " + count);

			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {

		}
	}

	@Override
	protected void onDestroy() {

		if (mSycnLocalTask != null) {
			mSycnLocalTask.cancel(true);
			mSycnLocalTask = null;
		}
		HttpHanderUtil.cancel(mHttpResponseHandler);
		super.onDestroy();
	}
}
