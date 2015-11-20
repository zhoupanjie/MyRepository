package com.cplatform.xhxw.ui.provider;

import android.content.*;
import android.database.Cursor;
import com.cplatform.xhxw.ui.db.dao.ContactsDao;
import com.cplatform.xhxw.ui.db.dao.NewFriendsDao;
import com.cplatform.xhxw.ui.db.dao.SMessageDao;
import com.cplatform.xhxw.ui.model.SChatUserInfo;
import com.cplatform.xhxw.ui.util.Cn2SpellUtil;
import com.cplatform.xhxw.ui.util.RegexUtil;

/**
 * Created by cy-love on 14-8-15.
 */
public class ContentProviderUtil {

    /**
     * 查询S信聊天记录列表是否存在此消息是否存在
     *
     * @param roomId 为用户id
     */
    public static boolean isExistSMessageDaoByRoomId(Context context, String roomId, String myId) {
        String[] projection = {SMessageDao._ID};
        String selection = SMessageDao.ROOM_ID + " = ? AND " + SMessageDao.MY_UID + " = ? ";
        String[] selectionArgs = {roomId, myId};
        Cursor cursor = context.getContentResolver().query(XwContentProvider.S_MESSAGE_URI, projection, selection, selectionArgs, null);
        boolean isExist = false;
        if (cursor != null) {
            isExist = cursor.getCount() > 1;
            cursor.close();
        }
        return isExist;
    }

    /**
     * 查询用户是否存在
     */
    public static boolean isExistContactsDaoByUserId(Context context, String userId, String myId) {
        String[] projection = {ContactsDao._ID};
        String selection = ContactsDao.USER_ID + " = ? AND " + ContactsDao.MY_UID + " = ? AND " + ContactsDao.IS_MY_CONTACTS + " = 1 ";
        String[] selectionArgs = {userId, myId};
        Cursor cursor = context.getContentResolver().query(XwContentProvider.CONTACTS_URI, projection, selection, selectionArgs, null);
        boolean isExist = false;
        if (cursor != null) {
            isExist = cursor.getCount() > 1;
            cursor.close();
        }
        return isExist;
    }

    /**
     * 查询推荐好友是否存在
     */
    public static boolean isExistNewFriendsByUserId(Context context, String userId, String myId) {
        String[] projection = {NewFriendsDao._ID};
        String selection = NewFriendsDao.USER_ID + " = ? AND " + NewFriendsDao.MY_UID + " = ? ";
        String[] selectionArgs = {userId, myId};
        Cursor cursor = context.getContentResolver().query(XwContentProvider.NEW_FRIENDS_URL, projection, selection, selectionArgs, null);
        boolean isExist = false;
        if (cursor != null) {
            isExist = cursor.getCount() > 1;
            cursor.close();
        }
        return isExist;
    }

    /**
     * 添加到通讯录列列表
     */
    public static void addUserInfo(Context context, SChatUserInfo info, String myId) {
        ContentValues values = new ContentValues();
        values.put(ContactsDao.LOGO, info.getLogo());
        values.put(ContactsDao.NAME, info.getName());
        values.put(ContactsDao.COMMENT, info.getComment());
        values.put(ContactsDao.USER_ID, info.getUserid());
        values.put(ContactsDao.IS_MY_CONTACTS, 1);
        values.put(ContactsDao.MY_UID, myId);
        String orderKey = Cn2SpellUtil.converterToSpellToUpperCase(info.getComment(), info.getNickname(), info.getName());
        String indexKey = orderKey.substring(0, 1);
        if (!RegexUtil.isEnglish(indexKey)) {
            indexKey = "#";
        }
        values.put(ContactsDao.ORDER_KEY, orderKey);
        values.put(ContactsDao.INDEX_KEY, indexKey);
        context.getContentResolver().insert(XwContentProvider.CONTACTS_URI, values);
    }

    /**
     * 更新新的好友的当前状态
     *
     * @param status 1 添加  2 等待验证  3 已添加  4 接受
     */
    public static void updateNewFriendsInfoStatus(Context context, String userId, String myId, int status) {
        ContentValues values = new ContentValues();
        values.put(NewFriendsDao.STATUS, status); // 更改状态为已添加
        String where = NewFriendsDao.USER_ID + " = ? AND " + NewFriendsDao.MY_UID + " = ? ";
        String[] selectionArgs = {userId, myId};
        context.getContentResolver().update(XwContentProvider.NEW_FRIENDS_URL, values, where, selectionArgs);
    }

    /**
     * 更新S信列表内的用户信息
     *
     * @param context
     * @param myId    本机用户id
     * @param userId  更改用户id
     * @param logo    用户logo
     * @param name    用户名
     * @return 更新行数
     */
    public static int updateSMessageListUserInfo(Context context, String myId, String userId, String logo, String name, String nickName, String comment) {
        if (userId == null && name == null) {
            return -1;
        }
        ContentResolver resolver = context.getContentResolver();
        String where = SMessageDao.ROOM_ID + " = ? AND " + SMessageDao.MY_UID + " = ? ";
        String[] selectionArgs = {userId, myId};
        ContentValues values = new ContentValues();
        if (logo != null) {
            values.put(SMessageDao.LOGO, logo);
        }
        if (name != null) {
            values.put(SMessageDao.NAME, name);
        }
        if (nickName != null) {
            values.put(SMessageDao.NICK_NAME, nickName);
        }
        if (comment != null) {
            values.put(SMessageDao.COMMENT, comment);
        }
        return resolver.update(XwContentProvider.S_MESSAGE_URI, values, where, selectionArgs);
    }

    /**
     * 更新S信列表内的用户信息
     *
     * @param context
     * @param myId        登录用户id
     * @param userId      用户id
     * @param unReadCount 未读消息数量
     * @return
     */
    public static int updateSMessageListUnReadMsgCount(Context context, String myId, String userId, int unReadCount) {

        ContentResolver resolver = context.getContentResolver();
        String where = SMessageDao.ROOM_ID + " = ? AND " + SMessageDao.MY_UID + " = ? ";
        String[] selectionArgs = {userId, myId};
        ContentValues values = new ContentValues();

        values.put(SMessageDao.UNREAD_COUNT, unReadCount);
        return resolver.update(XwContentProvider.S_MESSAGE_URI, values, where, selectionArgs);
    }

    /**
     * 获取S信列表内的用户信息
     *
     * @param context
     * @param myId        登录用户id
     * @param userId      用户id
     * @return 未读消息数量
     */
    public static int getSMessageListUnReadMsgCount(Context context, String myId, String userId) {
        String[] projection = {SMessageDao.UNREAD_COUNT};
        ContentResolver resolver = context.getContentResolver();
        String selection = SMessageDao.ROOM_ID + " = ? AND " + SMessageDao.MY_UID + " = ? ";
        String[] selectionArgs = {userId, myId};
        Cursor cursor = resolver.query(XwContentProvider.S_MESSAGE_URI, projection, selection, selectionArgs, null);
        int unReadCount = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                unReadCount = cursor.getInt(0);
            }
            cursor.close();
        }
        return unReadCount;
    }
}
