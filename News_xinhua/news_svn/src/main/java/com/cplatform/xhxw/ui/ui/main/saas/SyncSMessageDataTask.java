package com.cplatform.xhxw.ui.ui.main.saas;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.db.dao.SMessageDao;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.GetNotreadListResponse;
import com.cplatform.xhxw.ui.model.GetNotreadItem;
import com.cplatform.xhxw.ui.model.SChat;
import com.cplatform.xhxw.ui.model.SChatUserInfo;
import com.cplatform.xhxw.ui.provider.ContentProviderUtil;
import com.cplatform.xhxw.ui.provider.XwContentProvider;
import com.cplatform.xhxw.ui.util.Actions;
import com.cplatform.xhxw.ui.util.ListUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.google.gson.Gson;
import java.util.List;

/**
 * 同步S信息内容
 * Created by cy-love on 14-8-17.
 */
public class SyncSMessageDataTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = SyncSMessageDataTask.class.getSimpleName();
    private Context _context;
    private String _errorNsg;

    public SyncSMessageDataTask(Context context) {
        _context = context;
    }

    @Override
    protected Void doInBackground(Void... params) {
        if (!Constants.hasLogin()) {
            return null;
        }
        String result = APIClient.getSyncNotreadList();
        String myId = Constants.getUid();
        GetNotreadListResponse response;
        try {
            ResponseUtil.checkResponse(result);
            response = new Gson().fromJson(result, GetNotreadListResponse.class);
        } catch (Exception e) {
            _errorNsg = _context.getString(R.string.data_format_error);
            LogUtil.w(TAG, _errorNsg);
            return null;
        }
        List<GetNotreadItem> notreads;
        if (response.isSuccess() && response.getData() != null) {

            notreads = response.getData().getList();
        } else {
            _errorNsg = _context.getString(R.string.data_format_error);
            LogUtil.w(TAG, _errorNsg);
            return null;
        }
        if (ListUtil.isEmpty(notreads)) {
            return null;
        }

        int size = notreads.size();
        ContentValues[] arrayValues = new ContentValues[size];
        int unReadCount = 0;
        for (int i = 0; i < size; i++) {
            GetNotreadItem item = notreads.get(i);
            if (item.getUserinfo() != null) {
                boolean isExist = ContentProviderUtil.isExistContactsDaoByUserId(_context, item.getUserinfo().getUserid(), myId);
                if (!isExist) {
                    // TODO 此处需要查询详情下的内容
                    // 添加到通讯录并更新新的好友列表状态为已添加
                    ContentProviderUtil.addUserInfo(_context, item.getUserinfo(), myId);
                    ContentProviderUtil.updateNewFriendsInfoStatus(_context, item.getUserinfo().getUserid(), myId, 3);
                }
            }
            ContentValues values = new ContentValues();
            if (item.getUserinfo() != null) {
                SChatUserInfo userInfo = item.getUserinfo();
                boolean isExits = ContentProviderUtil.isExistSMessageDaoByRoomId(_context, userInfo.getUserid(), myId);

                values.put(SMessageDao.ROOM_ID, userInfo.getUserid());
                if (!isExits) {
                    values.put(SMessageDao.NAME, userInfo.getName());
                    values.put(SMessageDao.NICK_NAME, userInfo.getNickname());
                    values.put(SMessageDao.COMMENT, userInfo.getComment());
                    values.put(SMessageDao.LOGO, userInfo.getLogo());
                }
                values.put(SMessageDao.UNREAD_COUNT, item.getCount());
                values.put(SMessageDao.MY_UID, myId);
                unReadCount += item.getCount();
                if (!ListUtil.isEmpty(item.getList())) {
                    SChat chat = item.getList().get(0);
                    values.put(SMessageDao.CTIME, chat.getCtime());
                    switch (chat.getChattype()) {
                        case 1:
                            values.put(SMessageDao.LAST_MSG, chat.getContent());
                            break;
                        case 2:
                            values.put(SMessageDao.LAST_MSG, "图片消息");
                            break;
                        case 3:
                            values.put(SMessageDao.LAST_MSG, "语音消息");
                            break;
                        default:
                            values.put(SMessageDao.LAST_MSG, "未知类型");
                            break;
                    }
                }

            }
            arrayValues[i] = values;
        }

        int count = _context.getContentResolver().bulkInsert(XwContentProvider.S_MESSAGE_URI, arrayValues);
        LogUtil.d("testdata", "add s_message : " + count);
        App.getPreferenceManager().setMsgNewSMessage(unReadCount);
        LocalBroadcastManager.getInstance(_context).sendBroadcast(new Intent(Actions.ACTION_MSG_NEW_UPDATE));
        return null;
    }

}
