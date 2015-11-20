package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.*;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.db.dao.ContactsDao;
import com.cplatform.xhxw.ui.provider.XwContentProvider;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.TextWatcher;
import com.cplatform.xhxw.ui.ui.base.adapter.AddressBookAdapter;
import com.cplatform.xhxw.ui.util.KeyboardUtil;

/**
 * 联系人搜索
 */
public class AddressBookSearchActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText mSearch;
    private ImageView mSearchClean;
    private ListView mListView;
    private AddressBookAdapter mAdapter;
    private boolean isFristDataLoad; // 是否为首次加载

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, AddressBookSearchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        return intent;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected String getScreenName() {
        return "AddressBookSearchActivity";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_book_search);
        setSwipeBackEnable(false);
        mSearchClean = (ImageView) findViewById(R.id.iv_search_clear);
        mSearchClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearch.setText(null);
            }
        });
        mListView = (ListView) findViewById(R.id.list_view);
        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mSearch = (EditText) findViewById(R.id.et_search);
        mSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mSearchClean.setVisibility(View.VISIBLE);
                } else {
                    mSearchClean.setVisibility(View.GONE);
                }
            }
        });
        mSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    KeyboardUtil.hideSoftInput(AddressBookSearchActivity.this);
                    String key = mSearch.getText().toString();
                    if (TextUtils.isEmpty(key)) {
                        showToast("请输入关键字");
                        return true;
                    }
                    if (isFristDataLoad) {
                        isFristDataLoad = false;
                        getSupportLoaderManager().initLoader(0, null, AddressBookSearchActivity.this);
                    } else {
                        getSupportLoaderManager().restartLoader(0, null, AddressBookSearchActivity.this);
                    }
                    return true;
                }
                return true;
            }
        });
        mAdapter = new AddressBookAdapter(this, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = mAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {
                    int iUserId = cursor.getColumnIndex(ContactsDao.USER_ID);
                    String userId = cursor.getString(iUserId);
                    startActivity(FriendInfoActivity.newIntent(AddressBookSearchActivity.this, userId, true));
                }
                finish();
            }
        });
        KeyboardUtil.showSoftInputDelay(mSearch);
    }

    /*
     * ------------------------------------------------
     * 加载本地数据
     * ------------------------------------------------
     */
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
        String selection = ContactsDao.MY_UID + " = ? AND " + ContactsDao.IS_MY_CONTACTS + " = ? AND (" + ContactsDao.NAME + " LIKE ? OR " + ContactsDao.NICK_NAME + " LIKE ? OR "+ContactsDao.COMMENT+" LIKE ? )";
        String key = mSearch.getText().toString();
        String[] selectionArgs = new String[]{Constants.getUid(), "1", "%" + key + "%", "%" + key + "%", "%" + key + "%"};
        return new CursorLoader(this, XwContentProvider.CONTACTS_URI,
                projection, selection, selectionArgs, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
        if (cursor == null || cursor.getCount() == 0) {
            showToast(R.string.no_search_results);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }
}
