package com.cplatform.xhxw.ui.ui.main.saas;

import android.app.AlertDialog;
import android.content.*;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.CursorAdapter;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.PreferencesManager;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.db.dao.ContactsDao;
import com.cplatform.xhxw.ui.db.dao.SMessageChatDao;
import com.cplatform.xhxw.ui.db.dao.SMessageDao;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.httputils.HttpHanderUtil;
import com.cplatform.xhxw.ui.http.net.GetSChatRequest;
import com.cplatform.xhxw.ui.http.net.GetSChatResponse;
import com.cplatform.xhxw.ui.http.net.SChatSendRequest;
import com.cplatform.xhxw.ui.http.net.SChatSendResponse;
import com.cplatform.xhxw.ui.model.SChat;
import com.cplatform.xhxw.ui.model.SChatUserInfo;
import com.cplatform.xhxw.ui.provider.ContentProviderUtil;
import com.cplatform.xhxw.ui.provider.XwContentProvider;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.widget.CommentListView;
import com.cplatform.xhxw.ui.ui.base.widget.InputViewSensitiveLinearLayout;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionManager;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionUtil;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionWidgt;
import com.cplatform.xhxw.ui.ui.main.saas.addressBook.FriendInfoActivity;
import com.cplatform.xhxw.ui.util.*;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.HashMap;
import java.util.List;

/**
 * S信息对话
 */
public class SMessageChatActivity extends BaseActivity implements InputViewSensitiveLinearLayout.OnInputViewListener, XWExpressionWidgt.onExprItemClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = SMessageChatActivity.class.getSimpleName();
    private static final String KEY_ROOM_ID = "roomId";
    private static final String KEY_NAME = "name";
    private static final String KEY_NICK_NAME = "nickName";
    private static final String KEY_COMMENT = "comment";
    private static final String KEY_LOGO = "logo";
    private static final String KEY_BACK_TITLE = "backTitle";

    @InjectView(R.id.list_view)
    CommentListView mListView;
    @InjectView(R.id.comment_editt)
    EditText editText;
    @InjectView(R.id.comment_expression_widgt)
    XWExpressionWidgt mExpressionWidgt;
    @InjectView(R.id.comment_expression_btn)
    Button mExprBtn;
    @InjectView(R.id.layout_content)
    InputViewSensitiveLinearLayout mRootContainer;
    @InjectView(R.id.comment_editt_linear)
    RelativeLayout mCommentOperateLo;

    private boolean mIsExprShow = false;
    private boolean mIsSoftKeyboardShow = false;
    private InputMethodManager mImm;
    private Handler mUiHandler = new Handler();
    private int mSoftKeyboardHeight = 0;
    private int mOriginalHeight = 0;
    private int mRootContainerBottom = 0;
    private boolean isExprAreaResized = false;

    private TextView mTitle;
    private SMessageChatAdapter mAdapter;
    private String mRoomId;
    private AsyncHttpResponseHandler mHttpHandler;
    private SyncLocalChatDataTask mSycnLocalChatTask;
    private String mLastactionTime; // 最后一次获取时间
    private boolean isScrollBottom = true; // 是否滑动到底部判断

    public static Intent newIntent(Context context, String roomId, String comment, String nickName, String name, String logo, String backTitle) {
        Intent intent = new Intent(context, SMessageChatActivity.class);
        intent.putExtra(KEY_NAME, name);
        intent.putExtra(KEY_NICK_NAME, nickName);
        intent.putExtra(KEY_COMMENT, comment);
        intent.putExtra(KEY_ROOM_ID, roomId);
        intent.putExtra(KEY_BACK_TITLE, backTitle);
        intent.putExtra(KEY_LOGO, logo);
        return intent;
    }

    @Override
    protected String getScreenName() {
        return "SMessageChatActivity";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_message_chat);
        ButterKnife.inject(this);
        if (!Constants.hasLogin()) {
            finish();
            return;
        }
        mImm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        mRoomId = getIntent().getStringExtra(KEY_ROOM_ID);
        mLastactionTime = SMessageUtil.getSMessageLastactionTime(this, mRoomId);
        initActionBar();
        mTitle = (TextView) findViewById(R.id.tv_title);
        String name = getIntent().getStringExtra(KEY_NAME);
        String nickName = getIntent().getStringExtra(KEY_NICK_NAME);
        String comment = getIntent().getStringExtra(KEY_COMMENT);
        String nameStr = SelectNameUtil.getName(comment, nickName, name);
        if (TextUtils.isEmpty(nameStr)) {
            mTitle.setText(XWExpressionUtil.getUserName(this, mRoomId));
        } else {
            mTitle.setText(nameStr);
        }

//        Button backTitle = (Button) findViewById(R.id.btn_back);
//        String back = getIntent().getStringExtra(KEY_BACK_TITLE);
//        if (TextUtils.isEmpty(back)) {
//            back = "返回";
//        }
//        backTitle.setText(back);
        findViewById(R.id.btn_view_details).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SMessageChatUserActivity.newIntent(SMessageChatActivity.this, mRoomId));
            }
        });
        mAdapter = new SMessageChatAdapter(this, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer posi = (Integer) v.getTag();
                Cursor cursor = mAdapter.getCursor();
                cursor.moveToPosition(posi);
                int iInfoId = cursor.getColumnIndex(SMessageChatDao.INFO_ID);
                int iMsg = cursor.getColumnIndex(SMessageChatDao.MSG);
                int iStatus = cursor.getColumnIndex(SMessageChatDao.STATUS);
                String infoId = cursor.getString(iInfoId);
                String msg = cursor.getString(iMsg);
                int status = cursor.getInt(iStatus);
                if (!mAdapter.checkSending(infoId) && status == 1) {
                    senMsgNetTask(infoId, msg);
                }
            }
        }, new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Integer posi = (Integer) v.getTag();
                Cursor cursor = mAdapter.getCursor();
                cursor.moveToPosition(posi);
                int iMsg = cursor.getColumnIndex(SMessageChatDao.MSG);
                final String msg = cursor.getString(iMsg);
                int iId = cursor.getColumnIndex(SMessageChatDao._ID);
                final String id = cursor.getString(iId);

                new AlertDialog.Builder(SMessageChatActivity.this)
                        .setItems(new String[]{"复制", "删除"}, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                switch (which) {
                                    case 0:
                                        Util.copy(SMessageChatActivity.this, msg);
                                        showToast("已复制到剪切板");
                                        break;
                                    case 1:
                                        getContentResolver().delete(XwContentProvider.S_MESSAGE_CHAT_URI, SMessageChatDao._ID + " = ? ", new String[]{id});
                                        break;
                                }

                            }
                        }).create().show();
                return true;
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer posi = (Integer) v.getTag();
                Cursor cursor = mAdapter.getCursor();
                cursor.moveToPosition(posi);
                int iUserId = cursor.getColumnIndex(SMessageChatDao.USER_ID);
                String friendsId = cursor.getString(iUserId);
                startActivity(FriendInfoActivity.newIntent(SMessageChatActivity.this, friendsId, true));
            }
        });
        mListView.setAdapter(mAdapter);
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastItem = firstVisibleItem + visibleItemCount;
                isScrollBottom = (lastItem == totalItemCount);
            }
        });
        initUserAvatar();
        getSupportLoaderManager().initLoader(0, null, this);


        mRootContainer.setOnInputViewListener(this);

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (mIsExprShow) {
                        mIsExprShow = false;
                        mIsSoftKeyboardShow = true;
                        mExpressionWidgt.setVisibility(View.GONE);
                        mExprBtn.setBackgroundResource(R.drawable.selector_s_chat_expressions);
                    }
                }
                return false;
            }
        });
        mSoftKeyboardHeight = PreferencesManager.getSoftKeyboardHeight(this);
        if (mSoftKeyboardHeight <= 0) {
            // 转为DP单位
            mSoftKeyboardHeight = Constants.screenHeight / 3;
        }
        resizeExprArea(Constants.screenWidth);

        /**
         * 测量软键盘所占区域高度
         */
        mRootContainer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                mRootContainer.getWindowVisibleDisplayFrame(r);
                int screenHeight = mRootContainer.getHeight();
                final int screenWidth = mRootContainer.getWidth();
                mRootContainerBottom = screenHeight;
                if (mOriginalHeight == 0) {
                    mOriginalHeight = screenHeight;
                } else {
                    if (screenHeight != mOriginalHeight && !isExprAreaResized) {
                        mSoftKeyboardHeight = Math.abs(screenHeight - mOriginalHeight);
                        PreferencesManager.saveSoftKeyboardHeight(SMessageChatActivity.this, mSoftKeyboardHeight);
                        isExprAreaResized = true;
                        mUiHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                resizeExprArea(screenWidth);
                            }
                        });
                    }
                }
            }
        });
        mExpressionWidgt.setmOnExprItemClickListener(this);
        resizeExprArea(Constants.screenWidth);
        loadData(false);
    }

    /**
     * 初始化头像
     */
    private void initUserAvatar() {
        String[] projection = {ContactsDao.USER_ID, ContactsDao.LOGO};
        String selection = ContactsDao.MY_UID + " = ? AND " + ContactsDao.USER_ID + " = ? ";
        String[] selectionArgs = {Constants.getUid(), mRoomId};
        Cursor cursor = getContentResolver().query(XwContentProvider.CONTACTS_URI, projection, selection, selectionArgs, null);
        if (cursor == null) {
            return;
        }
        if (cursor.getCount() == 0) {
            cursor.close();
            return;
        }
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int iLogo = cursor.getColumnIndex(ContactsDao.LOGO);
            int iUserId = cursor.getColumnIndex(ContactsDao.USER_ID);
            String logo = cursor.getString(iLogo);
            String userId = cursor.getString(iUserId);
            mAdapter.putLogo(userId, logo);
        }
        cursor.close();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = new String[]{
                SMessageChatDao._ID, // id
                SMessageChatDao.USER_ID, // 用户id
                SMessageChatDao.MSG, // 内容
                SMessageChatDao.MSG_TYPE, // 内容类型
                SMessageChatDao.STATUS, // 类型
                SMessageChatDao.ISSELF,
                SMessageChatDao.INFO_ID,
                SMessageChatDao.TIME
        };
        String selection = SMessageChatDao.ROOM_ID + " = ? AND " + SMessageChatDao.MY_UID + " = ? ";
        String[] selectionArgs = new String[]{mRoomId, Constants.getUid()};
        return new CursorLoader(SMessageChatActivity.this, XwContentProvider.S_MESSAGE_CHAT_URI,
                projection, selection, selectionArgs, SMessageChatDao.TIME);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
//        if (isScrollBottom) {
        mListView.setSelection(cursor.getCount());
//        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mAdapter.swapCursor(null);
    }

    @OnClick(R.id.comment_sendbtn)
    public void senAction() {
        String text = editText.getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            showToast("请输入内容");
            return;
        }
        isScrollBottom = true;
        String tmpId = "tmp_" + DateUtil.getTime();
        addSChatDB(tmpId, text, String.valueOf(DateUtil.getUnixTime()));
        senMsgNetTask(tmpId, text);
        editText.setText("");
    }

    @OnClick(R.id.comment_expression_btn)
    public void onClickExprBtn() {
        if (mIsExprShow) {
            controlKeyboardOrExpr(true, true);
        } else {
            controlKeyboardOrExpr(false, true);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIsExprShow || mIsSoftKeyboardShow) {
                controlKeyboardOrExpr(false, false);
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    /**
     * 控制表情区和软键盘的显示
     *
     * @param isShowKeyboard
     * @param isSwitch
     */
    private void controlKeyboardOrExpr(boolean isShowKeyboard, boolean isSwitch) {
        if (isSwitch) {
            if (!isShowKeyboard) {
                mIsExprShow = true;
                mIsSoftKeyboardShow = false;
                mImm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                mUiHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mExpressionWidgt.setVisibility(View.VISIBLE);
                    }
                }, 100);
                mExprBtn.setBackgroundResource(R.drawable.selector_s_chat_keyboard);
            } else {
                mIsExprShow = false;
                mIsSoftKeyboardShow = true;
                mExpressionWidgt.setVisibility(View.GONE);
                mExprBtn.setBackgroundResource(R.drawable.selector_s_chat_expressions);
                mImm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        } else {
            if (isShowKeyboard) {
                mIsExprShow = false;
                mIsSoftKeyboardShow = true;
                mExpressionWidgt.setVisibility(View.GONE);
                mImm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            } else {
                if (mIsSoftKeyboardShow) {
                    mIsSoftKeyboardShow = false;
                    mImm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                } else {
                    mIsExprShow = false;
                    mExpressionWidgt.setVisibility(View.GONE);
                }
            }
            mExprBtn.setBackgroundResource(R.drawable.selector_s_chat_expressions);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (mIsExprShow || mIsSoftKeyboardShow) {
                int rootTop = 0;
                if (mIsExprShow) {
                    rootTop = mRootContainerBottom - mSoftKeyboardHeight -
                            mCommentOperateLo.getHeight();
                } else {
                    rootTop = mRootContainerBottom - mCommentOperateLo.getHeight();
                }
                if (ev.getY() < rootTop) {
                    controlKeyboardOrExpr(false, false);
                    return true;
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void resizeExprArea(int viewWidth) {
        RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                mSoftKeyboardHeight);
        rlp.addRule(RelativeLayout.BELOW, R.id.comment_editt_linear);
        mExpressionWidgt.setLayoutParams(rlp);
        mExpressionWidgt.setKeyboardSize(viewWidth, mSoftKeyboardHeight);
    }

    @Override
    public void onExprItemClick(String exprInfo, boolean isDel) {
        SpannableString ss;
        int textSize = (int) editText.getTextSize();
        if (isDel) {
            HashMap<String, Integer> mExprFilenameIdData = XWExpressionManager.getInstance().getmExprInfoIdValuesCN(this);
            ss = XWExpressionUtil.generateSpanComment(getApplicationContext(),
                    XWExpressionUtil.deleteOneWord(editText.getText().toString(), mExprFilenameIdData),
                    textSize);
        } else {
            String content = editText.getText() + exprInfo;
            ss = XWExpressionUtil.generateSpanComment(getApplicationContext(), content,
                    textSize);
        }
        editText.setText(ss);
        editText.setSelection(ss.length());
    }

    @Override
    public void onShowInputView() {
        editText.setHint("");
    }

    @Override
    public void onHideInputView() {
        if (!mIsExprShow) {
            mExprBtn.setBackgroundResource(R.drawable.selector_s_chat_expressions);
        }
    }

    private void loadData(final boolean isShowLoading) {
        APIClient.getSChat(new GetSChatRequest(mRoomId, mLastactionTime), new AsyncHttpResponseHandler() {
            private boolean _isSuccess;

            @Override
            protected void onPreExecute() {
                HttpHanderUtil.cancel(mHttpHandler);
                mHttpHandler = this;
                if (isShowLoading) {
                    showLoadingView();
                }
            }

            @Override
            public void onFinish() {
                mHttpHandler = null;
                if (isShowLoading && !_isSuccess) {
                    hideLoadingView();
                }
            }

            @Override
            public void onSuccess(int statusCode, String content) {
                GetSChatResponse response;
                try {
                    ResponseUtil.checkResponse(content);
                    response = new Gson().fromJson(content, GetSChatResponse.class);
                } catch (Exception e) {
                    showToast(R.string.data_format_error);
                    return;
                }
                if (response.isSuccess()) {

                    if (response.getData() != null) {
                        GetSChatResponse.Data data = response.getData();
                        mLastactionTime = data.getLastactiontime();
                        SMessageUtil.updateSMessageLastactionTime(SMessageChatActivity.this, mRoomId, mLastactionTime);
                        List<SChat> sChats = data.getList();
                        if (!ListUtil.isEmpty(sChats)) {
                            _isSuccess = true;
                            newSycnLocalTask(data.getUserinfo(), sChats, isShowLoading);
                        }
                    }

                } else {
                    showToast(response.getMsg());
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
//                showToast(R.string.load_server_failure);
                LogUtil.d(TAG, "S Chat  网络连接失败");
            }
        });
    }

    /**
     * 同步本地聊天数据
     *
     * @param info
     * @param sChats
     * @param isShowLoading
     */
    private void newSycnLocalTask(SChatUserInfo info, List<SChat> sChats, boolean isShowLoading) {
        if (mSycnLocalChatTask != null) {
            mSycnLocalChatTask.cancel(true);
            mSycnLocalChatTask = null;
        }
        mSycnLocalChatTask = new SyncLocalChatDataTask(this, mRoomId, info, sChats, isShowLoading);
        mSycnLocalChatTask.execute();
    }

    /**
     * 同步本地数据
     */
    private class SyncLocalChatDataTask extends AsyncTask<Void, Void, Void> {

        private List<SChat> _sChats;
        private boolean _isShowLoading;
        private Context _context;
        private SChatUserInfo _userInfo;
        private String _roomId;

        public SyncLocalChatDataTask(Context context, String roomId, SChatUserInfo userInfo, List<SChat> sChats, boolean isShowLoading) {
            _sChats = sChats;
            _isShowLoading = isShowLoading;
            _context = context;
            _userInfo = userInfo;
            _roomId = roomId;
        }


        @Override
        protected Void doInBackground(Void... params) {
            List<SChat> sChats = _sChats;
            int size = sChats.size();
            ContentValues[] arrayValues = new ContentValues[size];
            String myId = Constants.getUid();
            // 1.保存用户数据
            addUserInfo(_userInfo);
            // 2保存聊天记录
            for (int i = 0; i < size; i++) {
                if (isCancelled()) {
                    return null;
                }
                SChat item = sChats.get(i);
                ContentValues values = new ContentValues();
                values.put(SMessageChatDao.USER_ID, _userInfo.getUserid());
                values.put(SMessageChatDao.INFO_ID, item.getInfoid());
                values.put(SMessageChatDao.MY_UID, myId);
                values.put(SMessageChatDao.IS_READ, 0);
                values.put(SMessageChatDao.ISSELF, item.getIsself());
                values.put(SMessageChatDao.ROOM_ID, _roomId);
                values.put(SMessageChatDao.MSG, item.getContent());
                values.put(SMessageChatDao.MSG_TYPE, item.getChattype());
                if (item.getExrta() != null) {
                    values.put(SMessageChatDao.EXRTA_THUMB, item.getExrta().getThumb());
                    values.put(SMessageChatDao.EXRTA_FILE, item.getExrta().getFile());
                }
                values.put(SMessageChatDao.TIME, item.getCtime());
                arrayValues[i] = values;
            }
            if (size > 0) {
                SChat item = sChats.get(size - 1);
                updateSMessageList(item.getContent(), item.getCtime());
            }
            int count = _context.getContentResolver().bulkInsert(XwContentProvider.S_MESSAGE_CHAT_URI, arrayValues);
            LogUtil.d(TAG, "add 聊天记录 : " + count);
            return null;
        }

        /**
         * 更新对话列表
         *
         * @param msg
         * @param time
         */
        private void updateSMessageList(String msg, long time) {
            ContentValues values = new ContentValues();
            values.put(SMessageDao.ROOM_ID, mRoomId);
            values.put(SMessageDao.LAST_MSG, msg);
            values.put(SMessageDao.MY_UID, Constants.getUid());
            values.put(SMessageDao.CTIME, time);
            getContentResolver().insert(XwContentProvider.S_MESSAGE_URI, values);
        }

        private void addUserInfo(SChatUserInfo info) {
            if (info == null) {
                return;
            }
            ContentValues values = new ContentValues();
            values.put(ContactsDao.LOGO, info.getLogo());
            values.put(ContactsDao.NAME, info.getName());
            values.put(ContactsDao.COMMENT, info.getComment());
            values.put(ContactsDao.USER_ID, info.getUserid());
            values.put(ContactsDao.MY_UID, Constants.getUid());
            String orderKey = Cn2SpellUtil.converterToSpellToUpperCase(info.getComment(), info.getNickname(), info.getName());
            String indexKey = orderKey.substring(0, 1);
            if (!RegexUtil.isEnglish(indexKey)) {
                indexKey = "#";
            }
            values.put(ContactsDao.ORDER_KEY, orderKey);
            values.put(ContactsDao.INDEX_KEY, indexKey);
            _context.getContentResolver().insert(XwContentProvider.CONTACTS_URI, values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (!isCancelled() && _isShowLoading) {
                hideLoadingView();
            }
        }
    }

    /**
     * 发送
     */
    private void senMsgNetTask(final String infoId, final String msg) {

        APIClient.sChatSend(new SChatSendRequest(mRoomId, 1, msg, null), new AsyncHttpResponseHandler() {
            @Override
            protected void onPreExecute() {
                if (mAdapter != null) {
                    mAdapter.putSending(infoId);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFinish() {
                if (mAdapter != null) {
                    mAdapter.removeSending(infoId);
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                showToast(R.string.load_server_failure);
            }

            @Override
            public void onSuccess(int statusCode, String content) {
                SChatSendResponse response;
                try {
                    response = new Gson().fromJson(content, SChatSendResponse.class);
                    ResponseUtil.checkObjResponse(response);
                } catch (Exception e) {
                    LogUtil.e(TAG, "json解析数据异常：" + content);
                    return;
                }
                if (response.isSuccess()) {
                    if (response.getData() != null) {
                        SChatSendResponse.SChatSendInfo info = response.getData();
                        updateSchatDB(infoId, info.getInfoid(), info.getCtime());
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    new AlertDialog.Builder(SMessageChatActivity.this).setMessage(response.getMsg())
                            .setPositiveButton("添加好友", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(FriendInfoActivity.newIntent(getApplicationContext(), mRoomId, false));
                                }
                            })
                            .setNegativeButton("取消", null)
                            .create().show();
                    LogUtil.e(TAG, "消息发送失败：" + content);
                }
            }
        });
    }

    /**
     * 添加到数据库
     *
     * @param infoId  消息id  未发送成功id为tmp开头
     * @param content 内容
     * @param time    时间  单位秒
     */
    private void addSChatDB(String infoId, String content, String time) {
        ContentValues values = new ContentValues();
        values.put(SMessageChatDao.ROOM_ID, mRoomId);
        values.put(SMessageChatDao.USER_ID, mRoomId);
        values.put(SMessageChatDao.ISSELF, 1);
        values.put(SMessageChatDao.INFO_ID, infoId);
        values.put(SMessageChatDao.MSG, content);
        values.put(SMessageChatDao.MSG_TYPE, 1);
        values.put(SMessageChatDao.STATUS, 1); // 未发送成功
        values.put(SMessageChatDao.MY_UID, Constants.getUid());
        values.put(SMessageChatDao.TIME, time);
        getContentResolver().insert(XwContentProvider.S_MESSAGE_CHAT_URI, values);
        getSupportLoaderManager().restartLoader(0, null, this);

        // 添加到首页对话列表
        addSMessageList(content, time);
    }

    private void addSMessageList(String msg, String time) {
        String name = getIntent().getStringExtra(KEY_NAME);
        String nickName = getIntent().getStringExtra(KEY_NICK_NAME);
        String comment = getIntent().getStringExtra(KEY_COMMENT);
        String logoUrl = getIntent().getStringExtra(KEY_LOGO);
        ContentValues values = new ContentValues();
        values.put(SMessageDao.ROOM_ID, mRoomId);
        values.put(SMessageDao.LAST_MSG, msg);
        values.put(SMessageDao.COMMENT, comment);
        values.put(SMessageDao.NICK_NAME, nickName);
        values.put(SMessageDao.NAME, name);
        values.put(SMessageDao.LOGO, logoUrl);
//        values.put(SMessageDao.LASTCTIONTIME, time);
        values.put(SMessageDao.MY_UID, Constants.getUid());
        values.put(SMessageDao.CTIME, time);
        getContentResolver().insert(XwContentProvider.S_MESSAGE_URI, values);
    }

    /**
     * 消息发送成功后修改数据库
     *
     * @param oldInfoId 旧数据id
     * @param newInfoId 新数据id
     * @param time      发送时间
     */
    private void updateSchatDB(String oldInfoId, String newInfoId, String time) {
        ContentValues values = new ContentValues();
        values.put(SMessageChatDao.INFO_ID, newInfoId);
        values.put(SMessageChatDao.STATUS, 0); // 发送成功
        values.put(SMessageChatDao.TIME, time);
        String where = SMessageChatDao.INFO_ID + " = ? AND " + SMessageChatDao.MY_UID + " = ? ";
        String[] whereArgs = {oldInfoId, Constants.getUid()};
        values.put(SMessageChatDao.MY_UID, Constants.getUid());
        values.put(SMessageChatDao.TIME, time);

        getContentResolver().update(XwContentProvider.S_MESSAGE_CHAT_URI, values, where, whereArgs);
        getSupportLoaderManager().restartLoader(0, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.getCurrentApp().setsChatRoomId(mRoomId);
        App.getCurrentApp().setScreen(App.RunScreen.SCHAT);
        int count = ContentProviderUtil.getSMessageListUnReadMsgCount(this, Constants.getUid(), mRoomId);
        int newMsg = App.getPreferenceManager().getMsgNewSMessage();
        newMsg = newMsg - count;
        App.getPreferenceManager().setMsgNewSMessage(newMsg);
        ContentProviderUtil.updateSMessageListUnReadMsgCount(this, Constants.getUid(), mRoomId, 0);
        getSupportLoaderManager().restartLoader(0, null, this);
        getContentResolver().registerContentObserver(XwContentProvider.S_MESSAGE_CHAT_URI, true, mBor);
        LocalBroadcastManager.getInstance(this).registerReceiver(mBro, new IntentFilter(Actions.ACTION_SMESSAGE_CHAT_RELOAD));
    }

    @Override
    protected void onPause() {
        super.onPause();
        App.getCurrentApp().setScreen(App.RunScreen.NONE);
        App.getCurrentApp().setsChatRoomId(null);
        getContentResolver().unregisterContentObserver(mBor);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBro);
    }

    private BroadcastReceiver mBro = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Actions.ACTION_SMESSAGE_CHAT_RELOAD.equals(intent.getAction())) {
                loadData(false);
            }
        }
    };

    /**
     * 数据更改监听
     */
    private ContentObserver mBor = new ContentObserver(new Handler()) {

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            getSupportLoaderManager().restartLoader(0, null, SMessageChatActivity.this);
        }
    };

    @Override
    protected void onDestroy() {
        if (mSycnLocalChatTask != null) {
            mSycnLocalChatTask.cancel(true);
            mSycnLocalChatTask = null;
        }
        HttpHanderUtil.cancel(mHttpHandler);
        super.onDestroy();
    }
}