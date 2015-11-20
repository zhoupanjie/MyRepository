package com.cplatform.xhxw.ui.ui.main.saas;

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
import com.cplatform.xhxw.ui.db.dao.SMessageDao;
import com.cplatform.xhxw.ui.provider.XwContentProvider;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.TextWatcher;
import com.cplatform.xhxw.ui.util.KeyboardUtil;

/**
 * S信搜索
 */
public class SMessageSearchActivity extends BaseActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private EditText mSearch;
    private ListView mListView;
    private SMessageAdapter mAdapter;
    private ImageView mSearchClean;
    private boolean isFristDataLoad; // 是否为首次加载

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SMessageSearchActivity.class);
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
        return "SMessageSearchActivity";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_message_search);
        setSwipeBackEnable(false);
        mSearchClean = (ImageView) findViewById(R.id.iv_search_clear);
        mSearchClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearch.setText(null);
            }
        });
        mListView = (ListView) findViewById(R.id.list_view);
        mSearch = (EditText) findViewById(R.id.et_search);
        mSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    KeyboardUtil.hideSoftInput(SMessageSearchActivity.this);
                    String key = mSearch.getText().toString();
                    if (TextUtils.isEmpty(key)) {
                        showToast("请输入关键字");
                        return true;
                    }
                    if (isFristDataLoad) {
                        isFristDataLoad = false;
                        getSupportLoaderManager().initLoader(0, null, SMessageSearchActivity.this);
                    } else {
                        getSupportLoaderManager().restartLoader(0, null, SMessageSearchActivity.this);
                    }
                    return true;
                }
                return true;
            }
        });
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
        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mAdapter = new SMessageAdapter(this, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = mAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {
                    int iRoomId = cursor.getColumnIndex(SMessageDao.ROOM_ID);
                    int iName = cursor.getColumnIndex(SMessageDao.NAME);
                    int iNickName = cursor.getColumnIndex(SMessageDao.NICK_NAME);
                    int iComment = cursor.getColumnIndex(SMessageDao.COMMENT);
                    int iLogo = cursor.getColumnIndex(SMessageDao.LOGO);
                    String roomId = cursor.getString(iRoomId);
                    String name = cursor.getString(iName);
                    String nickName = cursor.getString(iNickName);
                    String comment = cursor.getString(iComment);
                    String logo = cursor.getString(iLogo);
                    String backTitle;
                    if (Constants.hasEnterpriseAccountLoggedIn()) {
                        backTitle = Constants.userInfo.getEnterprise().getAliases().getIm_alias();
                    } else {
                        backTitle = "S信";
                    }
                    startActivity(SMessageChatActivity.newIntent(SMessageSearchActivity.this, roomId, comment, nickName, name,logo, backTitle));
                }
                finish();
            }
        });
        KeyboardUtil.showSoftInputDelay(mSearch);
//        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Cursor cursor = mAdapter.getCursor();
//                if (cursor != null && cursor.moveToPosition(position)) {
//                    int iId = cursor.getColumnIndex(SMessageDao._ID);
//                    int iName = cursor.getColumnIndex(SMessageDao.NAME);
//                    final String dataId = cursor.getString(iId);
//                    String dataName = cursor.getString(iName);
//
//                    AlertDialog.Builder builder = new AlertDialog.Builder(SMessageSearchActivity.this);
//                    if (!TextUtils.isEmpty(dataName)) {
//                        builder.setTitle(dataName);
//                    }
//                    builder.setItems(new String[]{getString(R.string.delete)}, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            switch (which) {
//                                case 0:
//                                    getContentResolver().delete(XwContentProvider.S_MESSAGE_URI,
//                                            SMessageDao._ID + " = ?", new String[]{dataId});
//                                    break;
//                            }
//                        }
//                    });
//                    builder.create().show();
//
//                }
//                return true;
//            }
//        });

    }

    /*
     * ------------------------------------------------
     * 加载本地数据
     * ------------------------------------------------
     */
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = new String[]{
                SMessageDao._ID,
                SMessageDao.ROOM_ID,
                SMessageDao.LOGO,
                SMessageDao.NAME,
                SMessageDao.NICK_NAME,
                SMessageDao.COMMENT,
                SMessageDao.UNREAD_COUNT,
                SMessageDao.CTIME,
                SMessageDao.LASTCTIONTIME,
                SMessageDao.LAST_MSG
        };

        String selection = SMessageDao.MY_UID + " = ? AND (" + SMessageDao.NAME + " LIKE ? OR " + SMessageDao.NICK_NAME + " LIKE ? OR "+SMessageDao.COMMENT+" LIKE ? )";
        String key = mSearch.getText().toString();
        String[] selectionArgs = new String[]{Constants.getUid(), "%" + key + "%", "%" + key + "%", "%" + key + "%"};

        return new CursorLoader(this, XwContentProvider.S_MESSAGE_URI,
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
