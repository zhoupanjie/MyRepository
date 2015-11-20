package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.db.dao.ContactsDao;
import com.cplatform.xhxw.ui.db.dao.NewFriendsDao;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.httputils.HttpHanderUtil;
import com.cplatform.xhxw.ui.provider.XwContentProvider;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.widget.PullRefreshListView;
import com.cplatform.xhxw.ui.ui.main.saas.addressBook.NewAdapter.OnAddFriendsListener;
import com.cplatform.xhxw.ui.util.Actions;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 新的朋友
 * */
public class NewFriendsActivity extends BaseActivity implements
		OnAddFriendsListener, OnItemClickListener,
		LoaderManager.LoaderCallbacks<Cursor> {

	private static final String TAG = NewFriendsActivity.class.getSimpleName();

	private PullRefreshListView mlistView;
	private NewAdapter mAdapter;
	private static final int VERIFY = 1;

	private int mPage = 1;
	private int position;
	private boolean isFirst = true;
	private AsyncHttpResponseHandler mHttpResponseHandler;
	private SyncLocalUserDataTask mSycnLocalTask;

	private NewFriendsReceiver mReceiver;

	public static Intent newIntent(Context context) {
		Intent intent = new Intent(context, NewFriendsActivity.class);
		return intent;
	}

	@Override
	protected String getScreenName() {
		return TAG;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_new_friends);

		init();

	}

	private void init() {
		initActionBar();
		mlistView = (PullRefreshListView) findViewById(R.id.listview);

		mAdapter = new NewAdapter(this, null,
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		mAdapter.setOnAddFriendsListener(this);
		mlistView.setAdapter(mAdapter);

		mlistView.setCanRefresh(false);
		mlistView.setCanLoadMore(false);
		findViewById(R.id.add_new_friends).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						startActivity(SearchFriendActivity
								.newIntent(NewFriendsActivity.this));
					}
				});

		/** 从数据库获取数据 */
		getSupportLoaderManager().initLoader(0, null, this);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {
		this.position = position - 1;
		Cursor cursor = mAdapter.getCursor();
		if (cursor != null && cursor.moveToPosition(position - 1)) {
			int iUserId = cursor.getColumnIndex(NewFriendsDao.USER_ID);
			int iStatus = cursor.getColumnIndex(NewFriendsDao.STATUS);
			String userId = cursor.getString(iUserId);
			String status = cursor.getString(iStatus);
			if ("3".equals(iStatus)) {
				startActivity(FriendInfoActivity.newIntent(
						NewFriendsActivity.this, userId, true));
			} else {
				startActivityForResult(FriendInfoActivity.newIntent(
						NewFriendsActivity.this, userId, false), VERIFY);
			}
		}
	}

	/** 接受好友 */
	@Override
	public void receiveFriends(String friendId, String infoId, int position) {
		receiveNewFriends(friendId, infoId, position);
	}

	/** 添加 */
	@Override
	public void addFriends(String friendId, int position) {
		this.position = position;
		startActivityForResult(FriendsVerifyActivity.newIntent(this, friendId),
				VERIFY);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode != Activity.RESULT_OK) {
			return;
		}

		switch (requestCode) {
		case VERIFY:
			/** 刷新数据库 */

			Cursor cursor = mAdapter.getCursor();
			if (cursor != null && cursor.moveToPosition(position)) {
				int iLogo = cursor.getColumnIndex(NewFriendsDao.LOGO);
				int iName = cursor.getColumnIndex(NewFriendsDao.NAME);
				int iNickName = cursor.getColumnIndex(NewFriendsDao.NICK_NAME);
				int iComment = cursor.getColumnIndex(NewFriendsDao.COMMENT);
				int iinfoId = cursor.getColumnIndex(NewFriendsDao.INFO_ID);
				int friendId = cursor.getColumnIndex(NewFriendsDao.USER_ID);

				ContentValues[] arrayValues = new ContentValues[1];
				ContentValues values = new ContentValues();
				values.put(NewFriendsDao.MY_UID, Constants.getUid());
				values.put(NewFriendsDao.USER_ID, cursor.getString(friendId));
				values.put(NewFriendsDao.INFO_ID, cursor.getString(iinfoId));
				values.put(NewFriendsDao.STATUS, "2");
				values.put(NewFriendsDao.LOGO, cursor.getString(iLogo));
				values.put(NewFriendsDao.NAME, cursor.getString(iName));
				values.put(NewFriendsDao.NICK_NAME, cursor.getString(iNickName));
				values.put(NewFriendsDao.COMMENT, cursor.getString(iComment));
				arrayValues[0] = values;
				int count = getContentResolver().bulkInsert(
						XwContentProvider.NEW_FRIENDS_URL, arrayValues);
			}
			break;
		default:
			break;
		}
	}

	/** 获取新的朋友列表 */
	private void getNewFriends(final int page) {

		NewFriendsRequest request = new NewFriendsRequest();
		request.setPage(String.valueOf(page));
		request.setDevRequest(true);

		APIClient.getNewFriends(request, new AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				mHttpResponseHandler = null;
				mlistView.onRefreshComplete(null);
				mlistView.onLoadMoreComplete();

				hideLoadingView();
			}

			@Override
			protected void onPreExecute() {
				if (mHttpResponseHandler != null) {
					mHttpResponseHandler.cancle();
				}
				mHttpResponseHandler = this;

				if (isFirst) {
					showLoadingView();
					isFirst = false;
				}

			}

			@Override
			public void onSuccess(String content) {
				NewFriendsResponse response;
				try {
					
					String string = Constants.getUid();
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content,
							NewFriendsResponse.class);
				} catch (Exception e) {
					showToast(R.string.data_format_error);
					LogUtil.w(TAG, e);
					return;
				}

				if (response.isSuccess()) {
					mPage = page;

					if (page == 1) {
						mPage = page;
					}

					if (response != null) {
						if (response.getData() != null
								&& response.getData().size() > 0) {

							newSycnLocalTask(response.getData());
						}
					}
				} else {
					showToast(response.getMsg());
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				if (mAdapter.getCount() == 0) {

				} else {
					showToast(R.string.load_server_failure);
				}
			}
		});
	}

	/** 接受新的朋友 */
	private void receiveNewFriends(final String friendId, final String infoId,
			final int position) {

		ReceiveNewFriendsRequest request = new ReceiveNewFriendsRequest();
		request.setDevRequest(true);
		request.setId(infoId);
		request.setUserid(friendId);
		request.setTypeid("1");

		APIClient.receiveNewFriends(request, new AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				mHttpResponseHandler = null;
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
				ReceiveNewFriendsResponse response;
				try {
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content,
							ReceiveNewFriendsResponse.class);
				} catch (Exception e) {
					showToast(R.string.data_format_error);
					LogUtil.w(TAG, e);
					return;
				}
				if (response.isSuccess()) {
					/** 同意---刷新数据库 */
					// List<NewFriends> list = new ArrayList<NewFriends>();
					// NewFriends item = new NewFriends();
					// item.setUserid(friendId);
					// item.setStatues("3");
					// list.add(item);
					//
					// newSycnLocalTask(list);

					Cursor cursor = mAdapter.getCursor();
					if (cursor != null && cursor.moveToPosition(position)) {
						/**新的好友*/
						int iLogo = cursor.getColumnIndex(NewFriendsDao.LOGO);
						int iName = cursor.getColumnIndex(NewFriendsDao.NAME);
						int iNickName = cursor
								.getColumnIndex(NewFriendsDao.NICK_NAME);
						int iRename = cursor
								.getColumnIndex(NewFriendsDao.RENAME);
						int iComment = cursor
								.getColumnIndex(NewFriendsDao.COMMENT);

						ContentValues[] arrayValues = new ContentValues[1];
						ContentValues values = new ContentValues();
						values.put(NewFriendsDao.MY_UID, Constants.getUid());
						values.put(NewFriendsDao.INFO_ID, infoId);
						values.put(NewFriendsDao.USER_ID, friendId);
						values.put(NewFriendsDao.STATUS, "3");
						values.put(NewFriendsDao.LOGO, cursor.getString(iLogo));
						values.put(NewFriendsDao.NAME, cursor.getString(iName));
						values.put(NewFriendsDao.NICK_NAME,
								cursor.getString(iNickName));
						values.put(NewFriendsDao.RENAME,
								cursor.getString(iRename));
						values.put(NewFriendsDao.COMMENT,
								cursor.getString(iComment));
						arrayValues[0] = values;
						int count = getContentResolver().bulkInsert(
								XwContentProvider.NEW_FRIENDS_URL, arrayValues);
						
						/**通讯录*/
						ContentValues[] cantactArrayValues = new ContentValues[1];
						ContentValues cantactValues = new ContentValues();
						cantactValues.put(ContactsDao.MY_UID, Constants.getUid());
//						values.put(ContactsDao.INFO_ID, infoId);
						cantactValues.put(ContactsDao.USER_ID, friendId);
						cantactValues.put(ContactsDao.IS_MY_CONTACTS, "1");
						cantactValues.put(ContactsDao.LOGO, cursor.getString(iLogo));
						cantactValues.put(ContactsDao.NAME, cursor.getString(iName));
						cantactValues.put(ContactsDao.NICK_NAME,
								cursor.getString(iNickName));
						cantactValues.put(ContactsDao.COMMENT,
								cursor.getString(iRename));
						cantactArrayValues[0] = cantactValues;
						int cantactCount = getContentResolver().bulkInsert(
								XwContentProvider.CONTACTS_URI, cantactArrayValues);
					}
					
					getSupportLoaderManager().restartLoader(0, null,
							NewFriendsActivity.this);
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

	private void newSycnLocalTask(List<NewFriends> friends) {
		if (mSycnLocalTask != null) {
			mSycnLocalTask.cancel(true);
			mSycnLocalTask = null;
		}
		mSycnLocalTask = new SyncLocalUserDataTask(this, friends);
		mSycnLocalTask.execute();
	}

	/**
	 * 同步新的好友
	 */
	private class SyncLocalUserDataTask extends AsyncTask<Void, Void, Void> {

		private List<NewFriends> _friends;
		private Context _context;

		public SyncLocalUserDataTask(Context context, List<NewFriends> friends) {
			_friends = friends;
			_context = context;
		}

		@Override
		protected Void doInBackground(Void... params) {
			List<NewFriends> friends = _friends;
			int size = _friends.size();
			ContentValues[] arrayValues = new ContentValues[size];
			long sycnTime = System.currentTimeMillis();
			for (int i = 0; i < size; i++) {
				if (isCancelled()) {
					return null;
				}
				NewFriends item = _friends.get(i);
				ContentValues values = new ContentValues();
				values.put(NewFriendsDao.MY_UID, Constants.getUid());
				values.put(NewFriendsDao.USER_ID, item.getUserid());
				values.put(NewFriendsDao.INFO_ID, item.getInfoid());
				values.put(NewFriendsDao.LOGO, item.getLogo());
				values.put(NewFriendsDao.NAME, item.getName());
				values.put(NewFriendsDao.NICK_NAME, item.getNickname());
				values.put(NewFriendsDao.STATUS, "4");
				values.put(NewFriendsDao.SYNC_TIME, sycnTime);
				values.put(NewFriendsDao.COMMENT, item.getMsg());
				arrayValues[i] = values;
			}

			int count = _context.getContentResolver().bulkInsert(
					XwContentProvider.NEW_FRIENDS_URL, arrayValues);
			LogUtil.d("testdata", "new_friends_book : " + count);

			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			getSupportLoaderManager().restartLoader(0, null,
					NewFriendsActivity.this);
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

	@Override
	protected void onResume() {
		super.onResume();
		mReceiver = new NewFriendsReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Actions.ACTION_NEW_FRIEND_RELOAD);
		LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver,
				intentFilter);

		if (isFirst) {
			isFirst = false;
		}else {
			App.getCurrentApp().setScreen(App.RunScreen.NEW_FRIENDS);
			getSupportLoaderManager().restartLoader(0, null,
					NewFriendsActivity.this);
			getContentResolver().registerContentObserver(
					XwContentProvider.NEW_FRIENDS_URL, false, mDbObs);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
        App.getCurrentApp().setScreen(App.RunScreen.NONE);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        getContentResolver().unregisterContentObserver(mDbObs);
		mReceiver = null;
	}

	private class NewFriendsReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (Actions.ACTION_NEW_FRIEND_RELOAD.equals(intent.getAction())) {
				getNewFriends(1);
			}
		}
	}

	/*
	 * ------------------------------------------------ 数据库更改监听
	 * ------------------------------------------------
	 */
	private ContentObserver mDbObs = new ContentObserver(new Handler()) {
		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			getSupportLoaderManager().restartLoader(0, null,
					NewFriendsActivity.this);
		}
	};

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		String[] projection = new String[] { NewFriendsDao._ID,
				NewFriendsDao.USER_ID, NewFriendsDao.INFO_ID,
				NewFriendsDao.LOGO, NewFriendsDao.NAME,
				NewFriendsDao.NICK_NAME, NewFriendsDao.RENAME, NewFriendsDao.COMMENT,
				NewFriendsDao.STATUS };
        String selection = NewFriendsDao.MY_UID + " = ? ";
        String[] selectionArgs = {Constants.getUid()};
		return new CursorLoader(NewFriendsActivity.this,
				XwContentProvider.NEW_FRIENDS_URL, projection, selection, selectionArgs, NewFriendsDao._ID +" desc ");//" order by " +
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
		mAdapter.swapCursor(cursor);
		getNewFriends(1);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		mAdapter.swapCursor(null);
	}

	@Override
	public void goFriendsInfo(String friendId, int position) {
		this.position = position;
		Cursor cursor = mAdapter.getCursor();
		if (cursor != null && cursor.moveToPosition(position)) {
			String userId = cursor.getString(cursor.getColumnIndex(NewFriendsDao.USER_ID));
			if ("3".equals(cursor.getString(cursor.getColumnIndex(NewFriendsDao.STATUS)))) {
				startActivity(FriendInfoActivity.newIntent(
						NewFriendsActivity.this, userId, true));
			} else {
				startActivityForResult(FriendInfoActivity.newIntent(
						NewFriendsActivity.this, userId, false), VERIFY);
			}
		}
	}

}
