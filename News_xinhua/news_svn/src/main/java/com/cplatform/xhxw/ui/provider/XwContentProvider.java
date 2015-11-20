package com.cplatform.xhxw.ui.provider;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import com.cplatform.xhxw.ui.db.DatabaseHelper;
import com.cplatform.xhxw.ui.db.dao.CompanyMessageDao;
import com.cplatform.xhxw.ui.db.dao.ContactsDao;
import com.cplatform.xhxw.ui.db.dao.FriendsMessageDao;
import com.cplatform.xhxw.ui.db.dao.NewFriendsDao;
import com.cplatform.xhxw.ui.db.dao.SMessageChatDao;
import com.cplatform.xhxw.ui.db.dao.SMessageDao;
import com.cplatform.xhxw.ui.util.LogUtil;

/**
 * Created by cy-love on 14-8-3.
 */
public class XwContentProvider extends ContentProvider {

    private static final String TAG = XwContentProvider.class.getSimpleName();
    private DatabaseHelper mOpenHelper;

    private static final String AUTHORITY = "com.cplatform.xhxw.ui.provider.XwContentProvider";

    /**
     * 公司消息
     */
    public static final Uri COMPANY_MESSAGE_URI = Uri.parse("content://" + AUTHORITY + "/company_message_book");
    
    /**
     * 朋友消息
     */
    public static final Uri FRIENDS_MESSAGE_URL = Uri.parse("content://" + AUTHORITY + "/friends_message_book");
    
    /**
    * 新的好友
    */
   public static final Uri NEW_FRIENDS_URL = Uri.parse("content://" + AUTHORITY + "/new_friends_book");
    
    /**
     * 通讯录
     */
    public static final Uri CONTACTS_URI = Uri.parse("content://" + AUTHORITY + "/address_book");
    /**
     * S信列表
     */
    public static final Uri S_MESSAGE_URI = Uri.parse("content://" + AUTHORITY + "/s_message");
    /**
     * S信对话内容
     */
    public static final Uri S_MESSAGE_CHAT_URI = Uri.parse("content://" + AUTHORITY + "/s_message_content");

    // 标记
    private static final int ADDRESS_BOOKS = 1;
    private static final int S_MESSAGES = 2;
    private static final int S_MESSAGE_CONTENTS = 3;
    private static final int NEW_FRIENDS = 4;
    private static final int FRIENDS_MESSAGE = 5;
    private static final int COMPANYS_MESSAGE = 6;
    
    private static final UriMatcher sUriMatcher;

    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(AUTHORITY, "address_book",
                ADDRESS_BOOKS);
        sUriMatcher.addURI(AUTHORITY, "s_message",
                S_MESSAGES);
        sUriMatcher.addURI(AUTHORITY, "s_message_content",
                S_MESSAGE_CONTENTS);
        sUriMatcher.addURI(AUTHORITY, "new_friends_book",
                NEW_FRIENDS);
        sUriMatcher.addURI(AUTHORITY, "friends_message_book",
        		FRIENDS_MESSAGE);
        		sUriMatcher.addURI(AUTHORITY, "company_message_book",
        				COMPANYS_MESSAGE);
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String tableName;
        switch (sUriMatcher.match(uri)) {
            case ADDRESS_BOOKS:
                tableName = ContactsDao.TABLE_NAME;
                break;
            case S_MESSAGES:
                tableName = SMessageDao.TABLE_NAME;
                break;
            case S_MESSAGE_CONTENTS:
                tableName = SMessageChatDao.TABLE_NAME;
                break;
            case NEW_FRIENDS:
                tableName = NewFriendsDao.TABLE_NAME;
                break;
            case FRIENDS_MESSAGE:
                tableName = FriendsMessageDao.TABLE_NAME;
                break;
            case COMPANYS_MESSAGE:
                tableName = CompanyMessageDao.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(tableName);
        return qb.query(mOpenHelper.getReadableDatabase(), projection, selection,
                selectionArgs, null, null, sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case ADDRESS_BOOKS:
                return "vnd.android.cursor.dir/vnd.android_address_book.address_book";
            case S_MESSAGES:
                return "vnd.android.cursor.dir/vnd.android_s_message.s_message";
            case S_MESSAGE_CONTENTS:
                return "vnd.android.cursor.dir/vnd.android_s_message_content.s_message_content";
            case NEW_FRIENDS:
                return "vnd.android.cursor.dir/vnd.android_new_friends_book.new_friends_book";
            case FRIENDS_MESSAGE:
                return "vnd.android.cursor.dir/vnd.android_friends_message_book.friends_message_book";
            case COMPANYS_MESSAGE:
                return "vnd.android.cursor.dir/vnd.android_company_message_book.company_message_book";
        }
        return null;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        // 栏目批量插入
        int numValues = 0;
        switch (sUriMatcher.match(uri)) {
            case ADDRESS_BOOKS: // 批量插入联系人
            case S_MESSAGES: // S信列表
            case S_MESSAGE_CONTENTS: // S信对话内容
            case NEW_FRIENDS: // 新的好友
            case FRIENDS_MESSAGE: // 好友消息
            case COMPANYS_MESSAGE: // 公司消息
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        db.beginTransaction(); //开始事务
        try {
            //数据库操作
            numValues = values.length;
            for (int i = 0; i < numValues; i++) {
                insert(uri, values[i]);
            }
            db.setTransactionSuccessful(); // Commit
        } finally {
            db.endTransaction(); //结束事务
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return numValues;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowId;
        switch (sUriMatcher.match(uri)) {
            case ADDRESS_BOOKS:
                rowId = tableInsert(mOpenHelper, values, ContactsDao.TABLE_NAME, ContactsDao.USER_ID, true);
                break;
            case S_MESSAGES:
                rowId = tableInsert(mOpenHelper, values, SMessageDao.TABLE_NAME, SMessageDao.ROOM_ID, true);
                break;
            case S_MESSAGE_CONTENTS:
                rowId = tableInsert(mOpenHelper, values, SMessageChatDao.TABLE_NAME, null, false);
                break;
            case NEW_FRIENDS:
                rowId = tableInsert(mOpenHelper, values, NewFriendsDao.TABLE_NAME, NewFriendsDao.USER_ID, true);
                break;
            case FRIENDS_MESSAGE:
                rowId = tableInsert(mOpenHelper, values, FriendsMessageDao.TABLE_NAME, FriendsMessageDao.USER_ID, true);
                break;
            case COMPANYS_MESSAGE:
                rowId = tableInsert(mOpenHelper, values, CompanyMessageDao.TABLE_NAME, CompanyMessageDao.USER_ID, true);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (rowId > 0) {
            Uri insertedBookUri =
                    ContentUris.withAppendedId(uri, rowId);
            getContext().getContentResolver().notifyChange(insertedBookUri, null);
            return insertedBookUri;
        }
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count;
        String table;
        switch (sUriMatcher.match(uri)) {
            case ADDRESS_BOOKS:
                table = ContactsDao.TABLE_NAME;
                break;
            case S_MESSAGES:
                table = SMessageDao.TABLE_NAME;
                break;
            case S_MESSAGE_CONTENTS:
                table = SMessageChatDao.TABLE_NAME;
                break;
            case NEW_FRIENDS:
                table = NewFriendsDao.TABLE_NAME;
                break;
            case FRIENDS_MESSAGE:
                table = FriendsMessageDao.TABLE_NAME;
                break;
            case COMPANYS_MESSAGE:
                table = CompanyMessageDao.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        count = mOpenHelper.getWritableDatabase().delete(table, selection, selectionArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count;
        String table;
        switch (sUriMatcher.match(uri)) {
            case ADDRESS_BOOKS:
                table = ContactsDao.TABLE_NAME;
                break;
            case S_MESSAGES:
                table = SMessageDao.TABLE_NAME;
                break;
            case S_MESSAGE_CONTENTS:
                table = SMessageChatDao.TABLE_NAME;
                break;
            case NEW_FRIENDS:
                table = NewFriendsDao.TABLE_NAME;
                break;
            case FRIENDS_MESSAGE:
                table = FriendsMessageDao.TABLE_NAME;
                break;
            case COMPANYS_MESSAGE:
                table = CompanyMessageDao.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        count = mOpenHelper.getWritableDatabase().update(table, values, selection, selectionArgs);
        if (count > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    /**
     * 插入数据
     * @param db
     * @param initialValues 需要插入的内容
     * @param tableName 表名
     * @param checkKey 重复内容数据检查的字段
     * @param checkUpdate true 若存在则执行更新， false不执行任何操作
     * @return
     */
    public static long tableInsert(SQLiteOpenHelper db, ContentValues initialValues, String tableName, String checkKey, boolean checkUpdate) {
        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        // 判断是否需要检查源数据
        if (!TextUtils.isEmpty(checkKey)) {
            if (values.containsKey(checkKey) == false) {
                LogUtil.e(TAG, "插入操作无检查字段");
                return -1;
            }
            String checkValue = values.getAsString(checkKey);
            SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
            qb.setTables(tableName);
            String whereClause = checkKey + "=?";
            String[] whereArgs = new String[]{checkValue};
            Cursor cursor = qb.query(db.getReadableDatabase(), new String[]{checkKey}, whereClause,
                    whereArgs, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    LogUtil.d(TAG, "data数据已存在");
                    cursor.close();
                    int count = 0;
                    if (checkUpdate) {
                        count = db.getWritableDatabase().update(tableName,
                                values, whereClause, whereArgs);

                        LogUtil.d(TAG, "更新数据:" + count);
                    }
                    return count;
                }
                cursor.close();
            }
        }
        return db.getWritableDatabase().insert(tableName, tableName, values);
    }

}
