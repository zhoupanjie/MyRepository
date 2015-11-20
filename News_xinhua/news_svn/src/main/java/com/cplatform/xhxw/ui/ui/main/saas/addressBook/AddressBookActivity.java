package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import java.util.HashMap;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
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
import android.widget.Button;
import android.widget.ListView;
import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.db.dao.ContactsDao;
import com.cplatform.xhxw.ui.provider.XwContentProvider;
import com.cplatform.xhxw.ui.receiver.StartServiceReceiver;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.adapter.AddressBookAdapter;
import com.cplatform.xhxw.ui.ui.base.view.MyLetterListView;
import com.cplatform.xhxw.ui.ui.base.view.MyLetterListView.OnTouchingLetterChangedListener;
import com.cplatform.xhxw.ui.util.Actions;


/**
 * 通讯录
 * Created by cy-love on 14-8-3.
 */
public class AddressBookActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Receiver mReceiver;
    private ListView mListView;
    private AddressBookAdapter mAdapter;
    private AddressBookHeader mHeader;
	private MyLetterListView myLetterListView; // 首字母索引
	private HashMap<String, Integer> alphaIndexer;//存放存在的汉语拼音首字母和与之对应的列表位置
	
	public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, AddressBookActivity.class);
        return intent;
    }

    @Override
    protected String getScreenName() {
        return "AddressBookActivity";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_book);
        initActionBar();
        myLetterListView = (MyLetterListView) findViewById(R.id.MyLetterListView01);
        myLetterListView.setOnTouchingLetterChangedListener(new LetterListViewListener());
        alphaIndexer = new HashMap<String, Integer>();
        
        mListView = (ListView) findViewById(R.id.list_view);
        mHeader = new AddressBookHeader(this);
        mHeader.setSearchOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNoAnimActivity(AddressBookSearchActivity.newIntent(AddressBookActivity.this));
            }
        });
        mHeader.findViewById(R.id.ly_new_friend).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startActivity(NewFriendsActivity.newIntent(AddressBookActivity.this));
                App.getPreferenceManager().setNewFriendNewCount(0);
                App.getPreferenceManager().setNewFriendsLimit(0);
                updateNewCount();
            }
        });

        mListView.addHeaderView(mHeader);
        mAdapter = new AddressBookAdapter(this, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = mAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position - 1)) {
                    int iUserId = cursor.getColumnIndex(ContactsDao.USER_ID);
                    String userId = cursor.getString(iUserId);
                    startActivity(FriendInfoActivity.newIntent(AddressBookActivity.this, userId, true));
                }
            }
        });

        findViewById(R.id.add_new_friends).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startActivity(SearchFriendActivity.newIntent(AddressBookActivity.this));
            }
        });

        showLoadingView();
        getSupportLoaderManager().initLoader(0, null, this);
        Intent intent = new Intent(AddressBookActivity.this, StartServiceReceiver.class);
        intent.setAction(StartServiceReceiver.ACTION_START_DOWNLOAD_CONTACTS);
        sendBroadcast(intent);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = new String[]{
                ContactsDao._ID,
                ContactsDao.USER_ID,
                ContactsDao.LOGO,
                ContactsDao.NAME,
                ContactsDao.NICK_NAME,
                ContactsDao.COMMENT,
                ContactsDao.INDEX_KEY,
        };
        String selection = ContactsDao.MY_UID + " = ? AND " + ContactsDao.IS_MY_CONTACTS + " = ? ";
        String[] selectionArgs = new String[]{Constants.getUid(), "1"};
        String sortOrder = ContactsDao.ORDER_KEY;
        return new CursorLoader(AddressBookActivity.this, XwContentProvider.CONTACTS_URI,
                projection, selection, selectionArgs, sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
        alphaIndexer.clear();
        if (cursor != null) {
        cursor.moveToFirst();
		while (cursor.moveToNext()) {
				try {
                    //当前汉语拼音首字母
                    String currentStr = cursor.getString(cursor
                            .getColumnIndex(ContactsDao.INDEX_KEY));
                    //判断是否包含首字母
                   if (!alphaIndexer.containsKey(currentStr)) {
                        alphaIndexer.put(currentStr, cursor.getPosition());
//                       	sections[cursor.getPosition()] = currentStr; 
                   }
				} catch (Exception e) {}
		}
        }
        
        hideLoadingView();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (Constants.hasEnterpriseAccountLoggedIn()) {
//            Button backBtn = (Button)findViewById(R.id.btn_back);
//            backBtn.setText(Constants.userInfo.getEnterprise().getAliases().getIm_alias());
//        }
        updateNewCount();
        getSupportLoaderManager().restartLoader(0, null, AddressBookActivity.this);
        mReceiver = new Receiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Actions.ACTION_START_DOWNLOAD_CONTACTS_END);
        intentFilter.addAction(Actions.ACTION_START_DOWNLOAD_CONTACTS_ERROR);
        intentFilter.addAction(Actions.ACTION_ADDRESS_BOOK_NEW);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, intentFilter);
        getContentResolver().registerContentObserver(XwContentProvider.CONTACTS_URI,true, mDbObs);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        getContentResolver().unregisterContentObserver(mDbObs);
        mReceiver = null;
    }

    /*
     * ------------------------------------------------
     * 数据库更改监听
     * ------------------------------------------------
     */
    private ContentObserver mDbObs = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            getSupportLoaderManager().restartLoader(0, null, AddressBookActivity.this);
        }
    };

    /**
     * 更新新消息
     */
    private void updateNewCount() {
        int count = App.getPreferenceManager().getNewFriendNewCount() + App.getPreferenceManager().getNewFriendsLimit();
        mHeader.setNewFriendNum(count);
    }

    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Actions.ACTION_START_DOWNLOAD_CONTACTS_END.equals(action)
                    || Actions.ACTION_UPLOAD_CONTACT_ERROR.equals(action)) {
                hideLoadingView();
            } else if (Actions.ACTION_ADDRESS_BOOK_NEW.equals(action)) {
                int count = mHeader.getNewFriendNum();
                count ++;
                mHeader.setNewFriendNum(count);
            }
        }
    }

    /**显示屏幕中间的字母标签*/
    private class LetterListViewListener implements OnTouchingLetterChangedListener{

		@Override
		public void onTouchingLetterChanged(final String s) {

			if(alphaIndexer.get(s) != null) {
				int position = alphaIndexer.get(s);
				mListView.setSelection(position);
			}
		}
    	
    }
}