package com.cplatform.xhxw.ui.ui.main.saas;

import android.app.AlertDialog;
import android.content.*;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.db.dao.SMessageChatDao;
import com.cplatform.xhxw.ui.db.dao.SMessageDao;
import com.cplatform.xhxw.ui.provider.XwContentProvider;
import com.cplatform.xhxw.ui.receiver.StartServiceReceiver;
import com.cplatform.xhxw.ui.ui.base.BaseFragment;
import com.cplatform.xhxw.ui.ui.main.saas.addressBook.AddressBookActivity;
import com.cplatform.xhxw.ui.ui.main.saas.addressBook.SearchHeader;
import com.cplatform.xhxw.ui.util.Actions;
import com.cplatform.xhxw.ui.util.SelectNameUtil;

/**
 * S信主界面
 */
public class SMessageFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private TextView mNum;
    private TextView mTitle;
    private View mNavRightBtn;
    private ListView mListView;
    private SMessageAdapter mAdapter;
    private SyncSMessageDataTask mSycnLocalTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_s_message,
                container, false);
        mNavRightBtn = view.findViewById(R.id.btn_contacts);
        mListView = (ListView) view.findViewById(R.id.list_view);
        mNum = (TextView) view.findViewById(R.id.tv_num);
        mTitle = (TextView) view.findViewById(R.id.tv_title);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mNavRightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(AddressBookActivity.newIntent(getActivity()));
            }
        });
        SearchHeader header = new SearchHeader(getActivity());
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNoAnimActivit(SMessageSearchActivity.newIntent(getActivity()));
            }
        });
        mAdapter = new SMessageAdapter(getActivity(), null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mListView.addHeaderView(header);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = mAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position - 1)) {
                    int iRoomId = cursor.getColumnIndex(SMessageDao.ROOM_ID);
                    int iName = cursor.getColumnIndex(SMessageDao.NAME);
                    int iNickName = cursor.getColumnIndex(SMessageDao.NICK_NAME);
                    int iComment = cursor.getColumnIndex(SMessageDao.COMMENT);
                    int iUnReadCount = cursor.getColumnIndex(SMessageDao.UNREAD_COUNT);
                    int iLogo = cursor.getColumnIndex(SMessageDao.LOGO);
                    String roomId = cursor.getString(iRoomId);
                    String name = cursor.getString(iName);
                    String nickName = cursor.getString(iNickName);
                    String comment = cursor.getString(iComment);
                    String logo = cursor.getString(iLogo);
                    int unReadCount = cursor.getInt(iUnReadCount);
                    subtractNewMsgCount(unReadCount);
                    ContentValues values = new ContentValues();
                    values.put(SMessageDao.UNREAD_COUNT, 0);
                    getActivity().getContentResolver().update(XwContentProvider.S_MESSAGE_URI, values, SMessageDao.ROOM_ID + " = ? ", new String[]{roomId});
                    String backTitle;
                    if (Constants.hasEnterpriseAccountLoggedIn()) {
                        backTitle = Constants.userInfo.getEnterprise().getAliases().getIm_alias();
                    } else {
                        backTitle = "S信";
                    }
                    startActivity(SMessageChatActivity.newIntent(getActivity(), roomId, comment, nickName, name, logo, backTitle));
                }
            }
        });
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor = mAdapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position - 1)) {
                    int iId = cursor.getColumnIndex(SMessageDao._ID);
                    int iName = cursor.getColumnIndex(SMessageDao.NAME);
                    int iNickName = cursor.getColumnIndex(SMessageDao.NICK_NAME);
                    int iComment = cursor.getColumnIndex(SMessageDao.COMMENT);
                    int iRoomId = cursor.getColumnIndex(SMessageDao.ROOM_ID);
                    int iUnReadCount = cursor.getColumnIndex(SMessageDao.UNREAD_COUNT);
                    final String dataId = cursor.getString(iId);
                    final String roomId = cursor.getString(iRoomId);
                    final int unReadCount = cursor.getInt(iUnReadCount);
                    String name = cursor.getString(iName);
                    String nickName = cursor.getString(iNickName);
                    String comment = cursor.getString(iComment);
                    String nameStr = SelectNameUtil.getName(comment, nickName, name);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    if (!TextUtils.isEmpty(nameStr)) {
                        builder.setTitle(nameStr);
                    }
                    builder.setItems(new String[]{getString(R.string.delete)}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    subtractNewMsgCount(unReadCount);
                                    getActivity().getContentResolver().delete(XwContentProvider.S_MESSAGE_URI,
                                            SMessageDao._ID + " = ? ", new String[]{dataId});
                                    getActivity().getContentResolver().delete(XwContentProvider.S_MESSAGE_CHAT_URI,
                                            SMessageChatDao.ROOM_ID + " = ? AND " + SMessageChatDao.MY_UID + " = ? ", new String[]{roomId, Constants.getUid()});

                                    break;
                            }
                        }
                    });
                    builder.create().show();

                }
                return true;
            }
        });
        getLoaderManager().initLoader(0, null, this);

//        long time = DateUtil.getTime() - App.getPreferenceManager().getLastUploadContactsTime();
//        if (time > 20 * 60 * 60 * 1000) { // 同步时间超过一天则执行同步
        if (App.getPreferenceManager().isAgreeReadContacts()) {
            Intent intent = new Intent(getActivity(), StartServiceReceiver.class);
            intent.setAction(StartServiceReceiver.ACTION_START_SYNC_CONTACTS);
            getActivity().sendBroadcast(intent);
        } else {
            new AlertDialog.Builder(getActivity())
                    .setTitle("提示")
                    .setMessage("S信想访问您的通讯录,以便推荐给您推荐好友")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            App.getPreferenceManager().setAgreeReadContacts(true);
                            Intent intent = new Intent(getActivity(), StartServiceReceiver.class);
                            intent.setAction(StartServiceReceiver.ACTION_START_SYNC_CONTACTS);
                            getActivity().sendBroadcast(intent);
                        }
                    }).setNegativeButton("取消", null).create().show();
        }
//        }
//        loadData(false);
        newSycnLocalTask();
    }

    /**
     * 减去底部角标未读消息数量
     *
     * @param unReadCount 待减去数量
     */
    private void subtractNewMsgCount(int unReadCount) {
        int oldReadCount = App.getPreferenceManager().getMsgNewSMessage();
        oldReadCount -= unReadCount;
        App.getPreferenceManager().setMsgNewSMessage(oldReadCount);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(Actions.ACTION_MSG_NEW_UPDATE));
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
                SMessageDao.LAST_MSG,
        };

        String selection = SMessageDao.MY_UID + " = ? ";
        String[] selectionArgs = new String[]{Constants.getUid()};
        return new CursorLoader(getActivity(), XwContentProvider.S_MESSAGE_URI,
                projection, selection, selectionArgs, SMessageDao.CTIME+" DESC ");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }

    /**
     * 同步本地数据
     */
    private void newSycnLocalTask() {
        if (mSycnLocalTask != null) {
            mSycnLocalTask.cancel(true);
            mSycnLocalTask = null;
        }
        mSycnLocalTask = new SyncSMessageDataTask(getActivity());
        mSycnLocalTask.execute();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Constants.hasEnterpriseAccountLoggedIn()) {
            mTitle.setText(Constants.userInfo.getEnterprise().getAliases().getIm_alias());
        }
        updateNewMsgCount();
        App.getCurrentApp().setScreen(App.RunScreen.SMESSAGE);
        getLoaderManager().restartLoader(0, null, SMessageFragment.this);
        getActivity().getContentResolver().registerContentObserver(XwContentProvider.S_MESSAGE_URI, false, mDbObs);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Actions.ACTION_SMESSAGE_RELOAD);
        intentFilter.addAction(Actions.ACTION_ADDRESS_BOOK_NEW);
        intentFilter.addAction(Actions.ACTION_MSG_NEW_UPDATE);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBro, intentFilter);
    }

    /**
     * 更新角标提示
     */
    private void updateNewMsgCount() {
        int msgFriends = App.getPreferenceManager().getNewFriendNewCount() + App.getPreferenceManager().getNewFriendsLimit();
        if (msgFriends == 0) {
            mNum.setVisibility(View.GONE);
        } else if (msgFriends < 100){
            mNum.setVisibility(View.VISIBLE);
            mNum.setText(String.valueOf(msgFriends));
        } else {
            mNum.setVisibility(View.VISIBLE);
            mNum.setText("99");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        App.getCurrentApp().setScreen(App.RunScreen.NONE);
        getActivity().getContentResolver().unregisterContentObserver(mDbObs);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBro);
    }

    private BroadcastReceiver mBro = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Actions.ACTION_SMESSAGE_RELOAD.equals(intent.getAction())) {
                newSycnLocalTask();
            } else if (Actions.ACTION_ADDRESS_BOOK_NEW.equals(intent.getAction())) {
                updateNewMsgCount();
            } else if (Actions.ACTION_MSG_NEW_UPDATE.equals(intent.getAction())) {
                updateNewMsgCount();
            }
        }
    };

    /*
     * ------------------------------------------------
     * 数据库更改监听
     * ------------------------------------------------
     */
    private ContentObserver mDbObs = new ContentObserver(new Handler()) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            getLoaderManager().restartLoader(0, null, SMessageFragment.this);
        }
    };

    @Override
    public void onDestroy() {
        if (mSycnLocalTask != null) {
            mSycnLocalTask.cancel(true);
            mSycnLocalTask = null;
        }
        super.onDestroy();
    }

}
