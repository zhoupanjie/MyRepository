package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.db.dao.CompanyMessageDao;
import com.cplatform.xhxw.ui.db.dao.FriendsMessageDao;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.httputils.HttpHanderUtil;
import com.cplatform.xhxw.ui.http.net.CompanyMessageRequest;
import com.cplatform.xhxw.ui.http.net.CompanyMessageResponse;
import com.cplatform.xhxw.ui.provider.XwContentProvider;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 公司圈评论列和回复列表
 */
public class CompanyMessageActivity extends BaseActivity implements
		OnItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

	private static final String TAG = FriendsMessageActivity.class
			.getSimpleName();

	private ImageView clearBtn;
	private ListView mNetListView;
	private ListView mNativeListView; 
	private FriendsMessageFooter footerLayout;
	private CompanyMessageNetAdapter mNetAdapter;
	private CompanyMessageNativeAdapter mNativeAdapter;
	private AsyncHttpResponseHandler mHttpResponseHandler;
	private SyncLocalMessageDataTask mSycnLocalTask;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, CompanyMessageActivity.class);
        return intent;
    }

	@Override
	protected String getScreenName() {
		return TAG;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_company_message);

		init();

		setListener();

		getCompanyMessage();
	}

	private void init() {
		initActionBar();
		clearBtn = (ImageView) findViewById(R.id.message_clear_btn);
		mNetListView = (ListView) findViewById(R.id.company_message_net_listview);
		mNativeListView = (ListView) findViewById(R.id.company_message_native_listview);
		
		mNetAdapter = new CompanyMessageNetAdapter(this);
		mNativeAdapter = new CompanyMessageNativeAdapter(this, null,
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		
		mNetListView.setAdapter(mNetAdapter);
		mNativeListView.setAdapter(mNativeAdapter);
		
		footerLayout = new FriendsMessageFooter(this);
		
//		View view = LayoutInflater.from(this).inflate(R.layout.activity_friends_message_bottom_layout, null);
		mNetListView.addFooterView(footerLayout);
	}

	private void setListener() {
		mNetListView.setOnItemClickListener(this);
		mNativeListView.setOnItemClickListener(this);
		
//		footerLayout.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View arg0) {
//				/** 查看更多---从数据库获取数据 */
//				clearBtn.setVisibility(View.VISIBLE);
//				mNetListView.setVisibility(View.GONE);
//				mNativeListView.setVisibility(View.VISIBLE);
//
//				getSupportLoaderManager().initLoader(0, null,
//						CompanyMessageActivity.this);
//			}
//		});
		
		clearBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				/**清除数据库里面的消息*/
				String selectionFriend = CompanyMessageDao.MY_UID + "=?";
				String[] projectionFriend = new String[] { Constants.getUid() };
				getContentResolver().delete(XwContentProvider.COMPANY_MESSAGE_URI, selectionFriend, projectionFriend);
				
				getSupportLoaderManager().restartLoader(0, null,
						CompanyMessageActivity.this);
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> view, View arg1, int position, long arg3) {
		switch (view.getId()) {
		case R.id.company_message_net_listview:
			if (mNetAdapter.getCount() == position) {
				/** 查看更多---从数据库获取数据 */
				clearBtn.setVisibility(View.VISIBLE);
//				mNetListView.setVisibility(View.GONE);
//				mNativeListView.setVisibility(View.VISIBLE);

				getSupportLoaderManager().initLoader(0, null,
						CompanyMessageActivity.this);
			}else {
				startActivity(CompanyFreshInfoActivity.newIntent(this, mNetAdapter
						.getItem(position).getInfoid()));
			}
			break;
		case R.id.company_message_native_listview:
			Cursor cursor = mNativeAdapter.getCursor();
			if (cursor != null && cursor.moveToPosition(position)) {
				startActivity(CompanyFreshInfoActivity.newIntent(this, cursor
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
		String[] projection = new String[] { CompanyMessageDao._ID,
				CompanyMessageDao.USER_ID, CompanyMessageDao.LOGO,
				CompanyMessageDao.NAME, CompanyMessageDao.NICK_NAME,
				CompanyMessageDao.COMMENT, CompanyMessageDao.BACKINFO,
				CompanyMessageDao.INFODATA, CompanyMessageDao.INFOID,
				CompanyMessageDao.CTIME, CompanyMessageDao.FRIENDSTIME };
		return new CursorLoader(CompanyMessageActivity.this,
				XwContentProvider.COMPANY_MESSAGE_URI, projection, null, null,
				CompanyMessageDao._ID + " desc ");
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor cursor) {
//		mNativeAdapter.swapCursor(cursor);
		
		cursor.moveToFirst();
		cursor.moveToPrevious();

		List<CompanyMessage> list = new ArrayList<CompanyMessage>();
		while (cursor.moveToNext()) {
			CompanyMessage companyMessage = new CompanyMessage();
			CompanyMessageUserInfo userInfo = new CompanyMessageUserInfo();
				try {
					userInfo.setLogo(cursor.getString(cursor
							.getColumnIndex(CompanyMessageDao.LOGO)));
					userInfo.setName(cursor.getString(cursor
							.getColumnIndex(CompanyMessageDao.NAME)));
					userInfo.setNickname(cursor.getString(cursor
							.getColumnIndex(CompanyMessageDao.NICK_NAME)));
					userInfo.setComment(cursor.getString(cursor
							.getColumnIndex(CompanyMessageDao.COMMENT)));
					userInfo.setUserid(cursor.getString(cursor
							.getColumnIndex(CompanyMessageDao.USER_ID)));
					
					companyMessage.setCtimel(cursor.getString(cursor
							.getColumnIndex(CompanyMessageDao.CTIME)));
					companyMessage.setFriendtime(cursor.getString(cursor
							.getColumnIndex(CompanyMessageDao.FRIENDSTIME)));
					companyMessage.setInfodata(cursor.getString(cursor
							.getColumnIndex(CompanyMessageDao.INFODATA)));
					companyMessage.setInfoid(cursor.getString(cursor
							.getColumnIndex(CompanyMessageDao.INFOID)));
					companyMessage.setBackinfo(cursor.getString(cursor
							.getColumnIndex(CompanyMessageDao.BACKINFO)));
					list.add(companyMessage);
				} catch (Exception e) {

				}
			}
		
		mNetAdapter.clearData();
		mNetAdapter.addAllData(list);
		mNetAdapter.notifyDataSetChanged();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
//		mNativeAdapter.swapCursor(null);
	}

	/** 获取新的消息 */
	private void getCompanyMessage() {

		CompanyMessageRequest request = new CompanyMessageRequest();
		request.setDevRequest(true);

		APIClient.getCompanyMessage(request, new AsyncHttpResponseHandler() {

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
                showLoadingView();
			}

			@Override
			public void onSuccess(String content) {
				CompanyMessageResponse response;
				try {
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content,
							CompanyMessageResponse.class);
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

	private void newSycnLocalTask(List<CompanyMessage> messages) {
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

		private List<CompanyMessage> _messages;
		private Context _context;

		public SyncLocalMessageDataTask(Context context,
				List<CompanyMessage> messages) {
			_messages = messages;
			_context = context;
		}

		@Override
		protected Void doInBackground(Void... params) {
			List<CompanyMessage> messages = _messages;
			int size = _messages.size();
			ContentValues[] arrayValues = new ContentValues[size];
			long sycnTime = System.currentTimeMillis();
			for (int i = 0; i < size; i++) {
				if (isCancelled()) {
					return null;
				}
				CompanyMessage item = _messages.get(i);
				ContentValues values = new ContentValues();
				values.put(CompanyMessageDao.MY_UID, Constants.getUid());
				values.put(CompanyMessageDao.USER_ID, item.getUserinfo()
						.getUserid());
				values.put(CompanyMessageDao.LOGO, item.getUserinfo().getLogo());
				values.put(CompanyMessageDao.NAME, item.getUserinfo().getName());
				values.put(CompanyMessageDao.NICK_NAME, item.getUserinfo()
						.getNickname());
				values.put(CompanyMessageDao.COMMENT, item.getUserinfo()
						.getComment());
				values.put(CompanyMessageDao.CTIME, item.getCtimel());
				values.put(CompanyMessageDao.FRIENDSTIME, item.getFriendtime());
				values.put(CompanyMessageDao.COMPANYID, item.getCompanyid());
				values.put(CompanyMessageDao.ACTIONTYPE, item.getActiontype());
				values.put(CompanyMessageDao.BACKINFO, item.getBackinfo());
				values.put(CompanyMessageDao.INFODATA, item.getInfodata());
				values.put(CompanyMessageDao.INFOID, item.getInfoid());
				values.put(CompanyMessageDao.SYNC_TIME, sycnTime);
				arrayValues[i] = values;
			}

			int count = _context.getContentResolver().bulkInsert(
					XwContentProvider.COMPANY_MESSAGE_URI, arrayValues);
			LogUtil.d("testdata", "company_message_book : " + count);

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
