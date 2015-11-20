package com.cplatform.xhxw.ui.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.PreferencesManager;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.db.dao.ContactsDao;
import com.cplatform.xhxw.ui.db.dao.NewFriendsDao;
import com.cplatform.xhxw.ui.model.Contact;
import com.cplatform.xhxw.ui.model.Friend;
import com.cplatform.xhxw.ui.provider.ContentProviderUtil;
import com.cplatform.xhxw.ui.provider.XwContentProvider;
import com.cplatform.xhxw.ui.ui.main.forelanguage.utils.LanguageUtil;
import com.cplatform.xhxw.ui.ui.main.saas.addressBook.NewFriends;
import com.cplatform.xhxw.ui.util.*;
import com.google.gson.Gson;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.db.ChanneDB;
import com.cplatform.xhxw.ui.db.dao.ChanneDao;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.*;
import com.cplatform.xhxw.ui.location.LocationClientController;
import com.cplatform.xhxw.ui.location.LocationUtil;
import com.cplatform.xhxw.ui.model.Channe;
import com.cplatform.xhxw.ui.model.Friend;
import com.cplatform.xhxw.ui.model.LocationPoint;
import com.cplatform.xhxw.ui.receiver.StartServiceReceiver;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by cy-love on 14-1-7.
 */
public class SyncService extends IntentService {

    private static final String TAG = SyncService.class.getSimpleName();

    public SyncService() {
        super("SyncService");
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String action = intent.getAction();
        if (Actions.ACTION_SYNC_CHANNEL.equals(action)) {
            syncChangeChanne();
//        sycnUserChanne();
            if(!PreferencesManager.getLanguageInfo(App.CONTEXT).equals(LanguageUtil.LANGUAGE_CH)) {
            	syncForeignLanguageChannel(PreferencesManager.getLanguageInfo(App.CONTEXT));
            } else {
            	sycnServerChanne();
            }
        }
        StartServiceReceiver.completeWakefulIntent(intent);
    }

    /**
     * 同步用户更改栏目数据
     */
    private void syncChangeChanne() {
        List<ChanneDao> list = ChanneDB.getDirtyChanne(this);
        if (!ListUtil.isEmpty(list)) {
            for (ChanneDao item : list) {
                String result = null;
                if(StringUtil.parseIntegerFromString(item.getChannelCreateType())
                		== ChanneDao.CHANNEL_TYPE_KEY_WORDS) {
                	continue;
                }
                // 如果没有登录，或者登录用户的id与需要同步的用户id不同，则停止此脏数据的同步，防止http header的用户信息错了
                switch (item.getShow()) {
                    case 0: // 显示
                    {
                        result = APIClient.syncAddChannel(new AddChannelRequest(item.getChannelID(), item.getListorder(), item.getOperatetime()));
                    }
                    break;
                    case 1: // 隐藏
                    {
                        result = APIClient.syncDelChannel(new DelChannelRequest(item.getChannelID(), item.getOperatetime()));
                    }
                    break;
                }
                if (!TextUtils.isEmpty(result)) {
                    BaseResponse response;
                    try {
                        ResponseUtil.checkResponse(result);
                        response = new Gson().fromJson(result, BaseResponse.class);
                    } catch (Exception e) {
                        LogUtil.e(TAG, e);
                        continue;
                    }
                    if (response.isSuccess()) {
                        item.setDirty(0);
                        ChanneDB.updateChanneById(this, item);
                        LogUtil.d(TAG, item.getChannelName() + " 提交成功!");
                    } else {
                        LogUtil.e(TAG, item.getChannelName() + " 提交失败! 失败信息::" + response.getMsg());
                    }
                }
            }
        }
    }
    
    private boolean syncForeignLanguageChannel(String language) {
    	List<Channe> serv;
		try {
			serv = getChannelList();
			String userId = Constants.hasLogin() ? Constants.getUid() : Constants.DB_DEFAULT_USER_ID;
	        for (Channe item : serv) {
	            item.setIsdisplay(0);
	        }
	        
	        boolean isChange = ChanneDB.sycnChannesByUserId(getBaseContext(), serv, userId, language);
            LogUtil.d(TAG, "外语频道同步完成");
            
            Intent intent = new Intent(Actions.ACTION_SYNC_SYSTEM_CHANNE_DONE);
            intent.putExtra("ischange", isChange);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            return true;
		} catch (Exception e) {
			LogUtil.w(TAG, "外语频道同步失败:" + e);
		}
    	return false;
    }

    /**
     * 同步服务器栏目
     *
     * @return
     */
    private boolean sycnServerChanne() {
        try {

            List<Channe> serv = getChannelList();
            List<Channe> def = getUserlChannelList();
            String userId = Constants.hasLogin() ? Constants.getUid() : Constants.DB_DEFAULT_USER_ID;
            for (Channe item : serv) {
                item.setIsdisplay(1);
            }
            List<Channe> userChanneOutofAll = new ArrayList<Channe>();
            for (Channe item : def) {
            	boolean isItemExist = false;
                for (Channe tmp : serv) {
                    if (!item.getChannelid().equals(tmp.getChannelid())
                    		&& !item.getChannelname().equals(tmp.getChannelname())) {
                        continue;
                    }
                    if (!item.getChannelid().equals(tmp.getChannelid())
                    		&& item.getChannelname().equals(tmp.getChannelname())) {
                        tmp.setChannelid(item.getChannelid());
                    }
                    isItemExist = true;
                    tmp.setListorder(item.getListorder());
                    tmp.setIsdisplay(0);
                    break;
                }
                if(!isItemExist) {
                	item.setIsdisplay(0);
                	userChanneOutofAll.add(item);
                }
            }
            serv.addAll(userChanneOutofAll);

            boolean isChange = ChanneDB.sycnChannesByUserId(getBaseContext(), serv, userId, LanguageUtil.LANGUAGE_CH);
            LogUtil.d(TAG, "服务器数据同步完成");

            if (Constants.DB_DEFAULT_USER_ID.equals(userId)) {
                Intent intent1 = new Intent(Actions.ACTION_SYNC_USER_CHANNE_DONE);
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
            }

            Intent intent = new Intent(Actions.ACTION_SYNC_SYSTEM_CHANNE_DONE);
            intent.putExtra("ischange", isChange);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            return true;

        } catch (Exception e) {
            LogUtil.w(TAG, "服务器栏目同步失败:" + e);
        }
        if (Constants.hasLogin()) {
            LocalBroadcastManager.getInstance(this)
                    .sendBroadcast(new Intent(Actions.ACTION_SYNC_SYSTEM_CHANNE_FAILURE));
        }
        return false;
    }

    private List<Channe> getChannelList() throws Exception {
        String result = APIClient.syncChannelList(new BaseRequest());
        ResponseUtil.checkResponse(result);
        ChannelListResponse response = new Gson().fromJson(result, ChannelListResponse.class);
        if (response.isSuccess()) {
            return response.getData();
        }
        return null;
    }

    private List<Channe> getUserlChannelList() throws Exception {
        GetUserChannelRequest request = new GetUserChannelRequest();
        LocationPoint lp = LocationUtil.getAPosition(this, new LocationClientController(this));
        request.setLnt(lp.getLongitude());
        request.setLat(lp.getLatitude());
        String result = APIClient.syncUserchannel(request);
        ResponseUtil.checkResponse(result);
        ChannelListResponse response = new Gson().fromJson(result, ChannelListResponse.class);
        if (response.isSuccess()) {
            return response.getData();
        }
        return null;
    }

    /**
     * 同步用户栏目
     */
//    private boolean sycnUserChanne() {
//        String result = APIClient.syncUserchannel();
//        LogUtil.d(TAG, "用户当前栏目:"+result);
//        if (!TextUtils.isEmpty(result)) {
//            try {
//            	ResponseUtil.checkResponse(result);
//                ChannelListResponse response = new Gson().fromJson(result, ChannelListResponse.class);
//                if (response.isSuccess()) {
//                    List<Channe> list = response.getData();
//                    if (ListUtil.isEmpty(list)) {
//                        ChanneDB.addUserIdChannesByDefault(this, Constants.userInfo.getUserId());
//                        syncChangeChanne();
//                    } else {
//                        ChanneDB.sycnChannesByUserId(getBaseContext(), list, Constants.getUid());
//                    }
//                    LogUtil.d(TAG, "用户数据同步完成");
//                    return true;
//                }
//                if (Constants.hasLogin()) {
//                    LocalBroadcastManager.getInstance(this)
//                            .sendBroadcast(new Intent(Actions.ACTION_SYNC_USER_CHANNE_DONE));
//                }
//            } catch (Exception e) {
//                LogUtil.w(TAG, "用户数据同步失败:"+e);
//            }
//        }
//        if (Constants.hasLogin()) {
//            LocalBroadcastManager.getInstance(this)
//                    .sendBroadcast(new Intent(Actions.ACTION_SYNC_USER_CHANNE_DONE));
//        }
//        return false;
//    }

    /**
     * 同步通讯录
     */
    private void uploadContacts() {
        if (!Constants.hasLogin()) {
            return;
        }
        String myId = Constants.getUid();
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(Actions.ACTION_UPLOAD_CONTACT_START));
        String json = null;
        try {
            json = ContactsUtil.getPhoneContacts(getApplicationContext(), getContentResolver());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(json)) {
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(Actions.ACTION_UPLOAD_CONTACT_END));
            return;
        }

        String result = APIClient.syncFriendsCompare(new FriendsCompareRequest(json));
        FriendsCompareResponse response;
        try {
            ResponseUtil.checkResponse(result);
            response = new Gson().fromJson(result, FriendsCompareResponse.class);
            if (response.isSuccess()) {
                App.getPreferenceManager().setLastUploadContactsTime(DateUtil.getTime());
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(Actions.ACTION_UPLOAD_CONTACT_END));
                if (!ListUtil.isEmpty(response.getData())) {
                    boolean isUpdateSMessage = false;
                    List<Friend> friends = response.getData();
                    ContentValues[] valueses = new ContentValues[friends.size()];
                    int newFriend = 0;
                    for (int i = 0; i < friends.size(); i++) {
                        Friend friend = friends.get(i);
                        String name = SelectNameUtil.getName(friend.getSname(), friend.getName(), friend.getPhone());
                        String[] projection = {NewFriendsDao._ID};
                        String selection = NewFriendsDao.USER_ID + " = ? AND " + NewFriendsDao.MY_UID +" = ? ";
                        String[] selectionArgs = {friend.getUserid(), myId};
                        Cursor cursor = getContentResolver().query(XwContentProvider.NEW_FRIENDS_URL, projection, selection, selectionArgs, null);
                        boolean isExist = false;
                        if (cursor != null) {
                            isExist =  (cursor.getCount() > 0);
                            cursor.close();
                        }
                        if (isExist) {
                            continue;
                        }
                        ContentValues values = new ContentValues();
                        values.put(NewFriendsDao.MY_UID, myId);
                        values.put(NewFriendsDao.USER_ID, friend.getUserid());
                        values.put(NewFriendsDao.NAME, name);
                        values.put(NewFriendsDao.COMMENT, getString(R.string.recommend_friends));
                        values.put(NewFriendsDao.STATUS, friend.getStatus());
                        // 手机号字段未入库
                        valueses[i] = values;
                        int updateRow = ContentProviderUtil.updateSMessageListUserInfo(getApplicationContext(), myId, friend.getUserid(), null, friend.getName(),friend.getSname(), null);
                        if (!isUpdateSMessage && updateRow > 0) {
                            isUpdateSMessage = true;
                        }
                        boolean isExtends = ContentProviderUtil.isExistNewFriendsByUserId(getApplicationContext(), friend.getUserid(), myId);
                        if (!isExtends) {
                            newFriend++;
                        }
                    }

                    int tmp = getContentResolver().bulkInsert(XwContentProvider.NEW_FRIENDS_URL, valueses);

                    switch (App.getCurrentApp().getScreen()) {
                        case NEW_FRIENDS:
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(Actions.ACTION_NEW_FRIEND_RELOAD));
                            break;
                        case ADDRESS_BOOK: {
                            newFriend = newFriend + App.getPreferenceManager().getNewFriendNewCount();
                            App.getPreferenceManager().setNewFriendNewCount(newFriend);
                            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(Actions.ACTION_ADDRESS_BOOK_NEW));
                        }
                        break;
                        default:
                        {
                            newFriend = newFriend + App.getPreferenceManager().getNewFriendNewCount();
                            App.getPreferenceManager().setNewFriendNewCount(newFriend);
                        }
                            break;
                    }
                    LogUtil.d(TAG, "入库推荐好友个人："+tmp);

                    // 更新聊天记录列表
                    if (isUpdateSMessage) {
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(Actions.ACTION_SMESSAGE_RELOAD));
                    }
                }
            } else {
                LogUtil.w(TAG, response.getMsg());
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(Actions.ACTION_UPLOAD_CONTACT_ERROR));
            }
        } catch (Exception e) {
            LogUtil.w(TAG, result);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(Actions.ACTION_UPLOAD_CONTACT_ERROR));
        }
    }

    /**
     * 下载联系人数据
     */
    private void downContacts() {
        if (!Constants.hasLogin() || !"1".equals(Constants.userInfo.getType())) {
            LogUtil.w(TAG, "用户非企业用户，不能执行同步");
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(Actions.ACTION_START_DOWNLOAD_CONTACTS_END));
            return;
        }
        String myId = Constants.getUid();
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(Actions.ACTION_START_DOWNLOAD_CONTACTS_START));
        String content = APIClient.syncContactList();
        ContactListResponse response;
        try {
            ResponseUtil.checkResponse(content);
            response = new Gson().fromJson(content, ContactListResponse.class);
        } catch (Exception e) {
            LogUtil.e(TAG, "联系人下载数据异常：" + content);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(Actions.ACTION_START_DOWNLOAD_CONTACTS_ERROR));
            return;
        }
        if (!response.isSuccess()) {
            LogUtil.e(TAG, "联系人下载数据异常：" + response.getMsg());
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(Actions.ACTION_START_DOWNLOAD_CONTACTS_ERROR));
            return;
        }
        List<Contact> contacts = response.getData();
        if (contacts == null) {
            contacts = new LinkedList<Contact>();
        }
        int size = contacts.size();
        ContentValues[] arrayValues = new ContentValues[size];
        long sycnTime = System.currentTimeMillis();
        boolean isUpdateSMessage = false;
        for (int i = 0; i < size; i++) {
            Contact item = contacts.get(i);
            ContentValues values = new ContentValues();
            values.put(ContactsDao.USER_ID, item.getUserid());
            values.put(ContactsDao.LOGO, item.getLogo());
            values.put(ContactsDao.NAME, item.getName());
            values.put(ContactsDao.NICK_NAME, item.getNickname());
            values.put(ContactsDao.COMMENT, item.getComment());
            values.put(ContactsDao.SIGN, item.getSign());
            values.put(ContactsDao.SYNC_TIME, sycnTime);
            values.put(ContactsDao.IS_MY_CONTACTS, 1);
            values.put(ContactsDao.MY_UID, myId);
            String orderKey = Cn2SpellUtil.converterToSpellToUpperCase(item.getComment(), item.getNickname(), item.getName());
            String indexKey = orderKey.substring(0, 1);
            if (!RegexUtil.isEnglish(indexKey)) {
                indexKey = "#";
            }
            values.put(ContactsDao.ORDER_KEY, orderKey);
            values.put(ContactsDao.INDEX_KEY, indexKey);
            arrayValues[i] = values;

            int updateRow = ContentProviderUtil.updateSMessageListUserInfo(getApplicationContext(), myId, item.getUserid(), item.getLogo(), item.getName(), item.getNickname(), item.getComment());
            if (!isUpdateSMessage && updateRow > 0) {
                isUpdateSMessage = true;
            }
        }

        // 更新聊天记录列表
        if (isUpdateSMessage) {
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(Actions.ACTION_SMESSAGE_RELOAD));
        }

        int count = getContentResolver().bulkInsert(XwContentProvider.CONTACTS_URI, arrayValues);
        LogUtil.d("testdata", "add address_book : " + count);
        count = getContentResolver().delete(XwContentProvider.CONTACTS_URI,
                ContactsDao.SYNC_TIME + " < ? AND " + ContactsDao.MY_UID + " = ? AND " + ContactsDao.IS_MY_CONTACTS + " = ? ", new String[]{String.valueOf(sycnTime), myId, "1"});
        LogUtil.d("testdata", "del address_book : " + count);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(new Intent(Actions.ACTION_START_DOWNLOAD_CONTACTS_END));
    }
}
