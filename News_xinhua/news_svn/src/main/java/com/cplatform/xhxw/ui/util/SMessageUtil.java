package com.cplatform.xhxw.ui.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.db.dao.SMessageDao;
import com.cplatform.xhxw.ui.provider.XwContentProvider;

/**
 * S信工具类
 */
public class SMessageUtil {

    /**
     * 获取最后加载时间
     */
    public static String getSMessageLastactionTime(Context context, String roomId) {
        String[] projection = {SMessageDao.LASTCTIONTIME};
        String selection = SMessageDao.MY_UID + " = ? AND " + SMessageDao.ROOM_ID + " = ? ";
        String[] selectionArgs = {Constants.getUid(), roomId};
        Cursor cursor = context.getContentResolver().query(XwContentProvider.S_MESSAGE_URI, projection, selection, selectionArgs, null);
        String time = "0";
        if (cursor != null) {
            if (cursor.getCount() > 0 && cursor.moveToFirst()) {
                String timeTmp = cursor.getString(0);
                if (!TextUtils.isEmpty(timeTmp)) {
                    time = timeTmp;
                }
            }
            cursor.close();
        }
        return time;
    }

    /**
     * 修改最后加载时间
     * @param context
     * @param roomId
     * @param time 获取时服务器的返回时间
     */
    public static void updateSMessageLastactionTime(Context context, String roomId, String time) {
        String where = SMessageDao.MY_UID + " = ? AND " + SMessageDao.ROOM_ID + " = ? ";
        String[] whereArgs = {Constants.getUid(), roomId};
        ContentValues values = new ContentValues();
        values.put(SMessageDao.LASTCTIONTIME, time);
        context.getContentResolver().update(XwContentProvider.S_MESSAGE_URI, values, where, whereArgs);
    }
}
