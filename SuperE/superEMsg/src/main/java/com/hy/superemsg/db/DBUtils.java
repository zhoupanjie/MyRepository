
package com.hy.superemsg.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.RawContacts.Entity;
import android.text.TextUtils;

import com.hy.superemsg.data.Contact;
import com.hy.superemsg.rsp.AbsContentDetail;
import com.hy.superemsg.rsp.AnimationContentDetail;
import com.hy.superemsg.rsp.Category;
import com.hy.superemsg.rsp.GameContentDetail;
import com.hy.superemsg.rsp.HolidayContentDetail;
import com.hy.superemsg.rsp.MmsContentDetail;
import com.hy.superemsg.rsp.RingContentDetail;
import com.hy.superemsg.rsp.SmsContentDetail;
import com.hy.superemsg.utils.CommonUtils;

public class DBUtils {
    private static DBUtils _instance;
    private Context ctx;
    private DBHelper helper;

    public static void initInstance(Context ctx) {
        _instance = new DBUtils();
        _instance.ctx = ctx;
        _instance.helper = _instance.new DBHelper(ctx);
    }

    private DBUtils() {

    }

    private String makeWhereClause() {
        return DBColumns.ID + " =? ";
    }

    private String[] makeWhereParams(String id) {
        return new String[] {
                id
        };
    }

    public long remove(String table, AbsContentDetail content) {
        return helper.getWritableDatabase().delete(table, makeWhereClause(),
                makeWhereParams(content.getId()));
    }

    public static DBUtils getInst() {
        return _instance;
    }

    public long insert(String table, String categoryid, AbsContentDetail content) {
        ContentValues values = content.toContentValues();
        if (table.indexOf(DBHelper.COLLECTION) == -1) {
            values.put(DBColumns.CATEGORY_ID, categoryid);
        }
        return helper.getWritableDatabase().replaceOrThrow(table, null, values);
    }

    public boolean checkIfExist(String table, String id) {
        Cursor c = null;
        c = helper.getReadableDatabase()
                .query(true, table, new String[] {
                        DBColumns.ID
                },
                        makeWhereClause(), makeWhereParams(id), null, null,
                        DBColumns.TIME + " desc", null);
        boolean ret = false;
        if (!c.isAfterLast()) {
            while (c.moveToNext()) {
                ret = true;
                break;
            }
        }
        if (null != c) {
            c.close();
        }
        return false;
    }

    public List<SmsContentDetail> querySms(boolean isCollect, String categoryid) {
        Cursor c = null;
        String whereClause = null;
        String[] whereParam = null;
        String table = isCollect ? DBHelper.TABLE_SMS_COLLECTION
                : DBHelper.TABLE_SMS;
        if (!isCollect && categoryid != null) {
            whereClause = DBColumns.CATEGORY_ID + "=?";
            whereParam = new String[] {
                    categoryid
            };
        }
        c = helper.getReadableDatabase().query(table, null, whereClause,
                whereParam, null, null, DBColumns.TIME + " desc", null);
        List<SmsContentDetail> ret = null;
        if (!c.isAfterLast()) {
            ret = new ArrayList<SmsContentDetail>();
            while (c.moveToNext()) {
                String id = c.getString(c.getColumnIndex(DBColumns.ID));
                String name = c.getString(c
                        .getColumnIndex(DBColumns.SmsColumns.SMS_CONTENT));
                SmsContentDetail sms = new SmsContentDetail();
                sms.smsid = id;
                sms.smscontent = name;
                ret.add(sms);
            }
        }
        if (null != c) {
            c.close();
        }

        return ret;
    }
    public List<HolidayContentDetail> queryHoliday(boolean isCollect, String categoryid) {
        Cursor c = null;
        String whereClause = null;
        String[] whereParam = null;
        String table = isCollect ? DBHelper.TABLE_HOLIDAY_COLLECTION
                : DBHelper.TABLE_HOLIDAY;
        if (!isCollect && categoryid != null) {
            whereClause = DBColumns.CATEGORY_ID + "=?";
            whereParam = new String[] {
                    categoryid
            };
        }
        c = helper.getReadableDatabase().query(table, null, whereClause,
                whereParam, null, null, DBColumns.TIME + " desc", null);
        List<HolidayContentDetail> ret = null;
        if (!c.isAfterLast()) {
            ret = new ArrayList<HolidayContentDetail>();
            while (c.moveToNext()) {
                String id = c.getString(c.getColumnIndex(DBColumns.ID));
                String name = c.getString(c
                        .getColumnIndex(DBColumns.HolidayColumns.HOLIDAY_CONTENT));
                HolidayContentDetail sms = new HolidayContentDetail();
                sms.smsid = id;
                sms.smscontent = name;
                ret.add(sms);
            }
        }
        if (null != c) {
            c.close();
        }

        return ret;
    }

    public void saveCategory(String table_as_key, List<Category> data) {
        if (CommonUtils.isNotEmpty(data)) {
            removeCategory(table_as_key);
            for (Category cate : data) {
                ContentValues values = new ContentValues();
                values.put(DBColumns.ID, table_as_key);
                values.put(DBColumns.CATEGORY_ID, cate.categoryid);
                values.put(DBColumns.CATEGORY_NAME, cate.categoryname);
                helper.getWritableDatabase().insert(DBHelper.TABLE_CATEGORY,
                        null, values);
            }
        }
    }

    private void removeCategory(String table_as_key) {
        helper.getWritableDatabase().delete(DBHelper.TABLE_CATEGORY,
                makeWhereClause(), new String[] {
                    table_as_key
                });
    }

    public List<Category> queryCategory(String table_as_key) {

        Cursor c = null;

        c = helper.getReadableDatabase().query(DBHelper.TABLE_CATEGORY, null,
                makeWhereClause(), new String[] {
                    table_as_key
                }, null, null,
                null);
        List<Category> ret = null;
        if (!c.isAfterLast()) {
            ret = new ArrayList<Category>();
            while (c.moveToNext()) {
                Category cate = new Category();
                String id = c
                        .getString(c.getColumnIndex(DBColumns.CATEGORY_ID));
                String name = c.getString(c
                        .getColumnIndex(DBColumns.CATEGORY_NAME));
                cate.categoryid = id;
                cate.categoryname = name;
                ret.add(cate);
            }
        }
        if (null != c) {
            c.close();
        }

        return ret;

    }

    public List<MmsContentDetail> queryMms(boolean isCollect, String categoryid) {
        Cursor c = null;
        String whereClause = null;
        String[] whereParam = null;
        String table = isCollect ? DBHelper.TABLE_MMS_COLLECTION
                : DBHelper.TABLE_MMS;
        if (!isCollect && categoryid != null) {
            whereClause = DBColumns.CATEGORY_ID + "=?";
            whereParam = new String[] {
                    categoryid
            };
        }
        c = helper.getReadableDatabase().query(table, null, whereClause,
                whereParam, null, null, DBColumns.TIME + " desc", null);
        List<MmsContentDetail> ret = null;
        if (!c.isAfterLast()) {
            ret = new ArrayList<MmsContentDetail>();
            while (c.moveToNext()) {
                String id = c.getString(c.getColumnIndex(DBColumns.ID));
                String mmscontent = c.getString(c
                        .getColumnIndex(DBColumns.MmsColumns.MMS_CONTENT));
                String mmsname = c.getString(c
                        .getColumnIndex(DBColumns.MmsColumns.MMS_NAME));
                String mmspicurl = c.getString(c
                        .getColumnIndex(DBColumns.MmsColumns.MMS_PIC_URL));
                MmsContentDetail mms = new MmsContentDetail();
                mms.mmsid = id;
                mms.mmscontent = mmscontent;
                mms.mmsname = mmsname;
                mms.mmspicurl = mmspicurl;
                ret.add(mms);
            }
        }
        if (null != c) {
            c.close();
        }

        return ret;
    }

    public void removeCategoryBasedContents(String table, String categoryid) {
        helper.getWritableDatabase().delete(table,
                DBColumns.CATEGORY_ID + " =? ", new String[] {
                    categoryid
                });
    }

    public List<RingContentDetail> queryRing(boolean isCollect,
            String categoryid) {
        Cursor c = null;
        String whereClause = null;
        String[] whereParam = null;
        String table = isCollect ? DBHelper.TABLE_RING_COLLECTION
                : DBHelper.TABLE_RING;
        if (!isCollect && categoryid != null) {
            whereClause = DBColumns.CATEGORY_ID + "=?";
            whereParam = new String[] {
                    categoryid
            };
        }
        c = helper.getReadableDatabase().query(table, null, whereClause,
                whereParam, null, null, DBColumns.TIME + " desc", null);
        List<RingContentDetail> ret = null;
        if (!c.isAfterLast()) {
            ret = new ArrayList<RingContentDetail>();
            while (c.moveToNext()) {
                String id = c.getString(c.getColumnIndex(DBColumns.ID));
                String ringname = c.getString(c
                        .getColumnIndex(DBColumns.RingColumns.RING_NAME));
                String ringprice = c.getString(c
                        .getColumnIndex(DBColumns.RingColumns.RING_PRICE));
                String ringsinger = c.getString(c
                        .getColumnIndex(DBColumns.RingColumns.RING_SINGER));
                int ringusecount = c.getInt(c
                        .getColumnIndex(DBColumns.RingColumns.RING_USE_COUNT));
                RingContentDetail ring = new RingContentDetail();
                ring.ringid = id;
                ring.ringname = ringname;
                ring.ringprice = ringprice;
                ring.ringsinger = ringsinger;
                ring.ringusecount = ringusecount;
                ret.add(ring);
            }
        }
        if (null != c) {
            c.close();
        }

        return ret;
    }

    public List<GameContentDetail> queryGame(boolean isCollect,
            String categoryid) {
        Cursor c = null;
        String whereClause = null;
        String[] whereParam = null;
        String table = isCollect ? DBHelper.TABLE_GAME_COLLECTION
                : DBHelper.TABLE_GAME;
        if (!isCollect && categoryid != null) {
            whereClause = DBColumns.CATEGORY_ID + "=?";
            whereParam = new String[] {
                    categoryid
            };
        }
        c = helper.getReadableDatabase().query(table, null, whereClause,
                whereParam, null, null, DBColumns.TIME + " desc", null);
        List<GameContentDetail> ret = null;
        if (!c.isAfterLast()) {
            ret = new ArrayList<GameContentDetail>();
            while (c.moveToNext()) {
                String id = c.getString(c.getColumnIndex(DBColumns.ID));
                int gamedownload = c.getInt(c
                        .getColumnIndex(DBColumns.GameColumns.GAME_DOWNLOAD));
                String gamefilesize = c.getString(c
                        .getColumnIndex(DBColumns.GameColumns.GAME_FILE_SIZE));
                String gamefileurl = c.getString(c
                        .getColumnIndex(DBColumns.GameColumns.GAME_FILE_URL));
                String gameiconurl = c.getString(c
                        .getColumnIndex(DBColumns.GameColumns.GAME_ICON_URL));
                String gameintroduce = c.getString(c
                        .getColumnIndex(DBColumns.GameColumns.GAME_INTRODUCE));
                String gamename = c.getString(c
                        .getColumnIndex(DBColumns.GameColumns.GAME_NAME));
                String gamepublisher = c.getString(c
                        .getColumnIndex(DBColumns.GameColumns.GAME_PUBLISHER));
                String gamescreenshotslist = c
                        .getString(c
                                .getColumnIndex(DBColumns.GameColumns.GAME_SCREEN_SHOT_LIST));
                String gamestyle = c.getString(c
                        .getColumnIndex(DBColumns.GameColumns.GAME_STYLE));
                String gameversion = c.getString(c
                        .getColumnIndex(DBColumns.GameColumns.GAME_VERSION));
                GameContentDetail game = new GameContentDetail();
                game.gameid = id;
                game.gamedownload = gamedownload;
                game.gamefilesize = gamefilesize;
                game.gamefileurl = gamefileurl;
                game.gameiconurl = gameiconurl;
                game.gameintroduce = gameintroduce;
                game.gamename = gamename;
                game.gamepublisher = gamepublisher;
                if (!TextUtils.isEmpty(gamescreenshotslist)
                        && gamescreenshotslist.indexOf(";") != -1) {
                    game.gamescreenshotslist = Arrays
                            .asList(gamescreenshotslist.split(";"));
                }
                game.gamestyle = gamestyle;
                game.gameversion = gameversion;
                ret.add(game);
            }
        }
        if (null != c) {
            c.close();
        }

        return ret;
    }

    public List<AnimationContentDetail> queryCartoon(boolean isCollect,
            String categoryid) {
        Cursor c = null;
        String whereClause = null;
        String[] whereParam = null;
        String table = isCollect ? DBHelper.TABLE_CARTOON_COLLECTION
                : DBHelper.TABLE_CARTOON;
        if (!isCollect && categoryid != null) {
            whereClause = DBColumns.CATEGORY_ID + "=?";
            whereParam = new String[] {
                    categoryid
            };
        }
        c = helper.getReadableDatabase().query(table, null, whereClause,
                whereParam, null, null, DBColumns.TIME + " desc", null);
        List<AnimationContentDetail> ret = null;
        if (!c.isAfterLast()) {
            ret = new ArrayList<AnimationContentDetail>();
            while (c.moveToNext()) {
                String id = c.getString(c.getColumnIndex(DBColumns.ID));
                String amauthor = c
                        .getString(c
                                .getColumnIndex(DBColumns.CartoonColumns.CARTOON_AUTHOR));
                String amcoverpicurl = c
                        .getString(c
                                .getColumnIndex(DBColumns.CartoonColumns.CARTOON_COVER_PIC_URL));
                String amintroduce = c
                        .getString(c
                                .getColumnIndex(DBColumns.CartoonColumns.CARTOON_INTRODUCE));
                String amname = c.getString(c
                        .getColumnIndex(DBColumns.CartoonColumns.CARTOON_NAME));
                AnimationContentDetail cartoon = new AnimationContentDetail();
                cartoon.amid = id;
                cartoon.amauthor = amauthor;
                cartoon.amcoverpicurl = amcoverpicurl;
                cartoon.amintroduce = amintroduce;
                cartoon.amname = amname;
                ret.add(cartoon);
            }
        }
        if (null != c) {
            c.close();
        }

        return ret;
    }

    public List<Contact> getContacts(Context ctx) {
        String select = "((" + Contacts.DISPLAY_NAME + " NOTNULL) AND ("
                + Contacts.HAS_PHONE_NUMBER + "=1) AND ("
                + Contacts.DISPLAY_NAME + " != '' ))";
        Cursor c = ctx.getContentResolver().query(Contacts.CONTENT_URI,
                new String[] {
                        Contacts._ID, Contacts.SORT_KEY_PRIMARY
                },
                select, null, Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
        List<Contact> contacts = new ArrayList<Contact>();
        while (c.moveToNext()) {
            Contact contact = new Contact();
            contact.id = c.getString(0);
            contact.pinyin = c.getString(1);
            contacts.add(contact);
            char first = contact.pinyin.charAt(0);
            if (first < 'A' || first > 'Z') {
                contact.pinyin = "#" + contact.pinyin;
            }
        }
        c.close();
        if (contacts.size() > 0) {
            for (int i = 0; i < contacts.size(); i++) {
                Contact contact = contacts.get(i);
                Uri uri = Uri.parse("content://com.android.contacts/contacts/"
                        + contact.id + "/data");
                c = ctx.getContentResolver().query(uri,
                        new String[] {
                                Entity.MIMETYPE, Entity.DATA1
                        }, null,
                        null, null);
                try {
                    while (c.moveToNext()) {
                        String mimeType = c.getString(0);
                        String data = c.getString(1);
                        // decide here based on mimeType, see comment later
                        if (mimeType
                                .equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {
                            contact.phone = data;
                        } else if (mimeType
                                .equals(ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)) {
                            contact.name = data;
                        }
                    }
                } finally {
                    c.close();
                }
            }
        }
        return contacts;
    }

    public class DBHelper extends SQLiteOpenHelper {
        // 初步想法是这样的，每个表有个字段，iscollected,选中的表示是收藏的，不能被删除
        public static final String DB_NAME = "superemsg";

        public static final String TABLE_SMS = "tbl_sms";
        public static final String TABLE_MMS = "tbl_mms";
        public static final String TABLE_RING = "tbl_ring";
        public static final String TABLE_HOLIDAY = "tbl_holiday";
        public static final String TABLE_GAME = "tbl_game";
        public static final String TABLE_NEWS = "tbl_news";
        public static final String TABLE_CARTOON = "tbl_cartoon";
        public static final String TABLE_DRAMA = "tbl_drama";

        public static final String COLLECTION = "collection";
        public static final String TABLE_SMS_COLLECTION = "tbl_sms_collection";
        public static final String TABLE_MMS_COLLECTION = "tbl_mms_collection";
        public static final String TABLE_RING_COLLECTION = "tbl_ring_collection";
        public static final String TABLE_HOLIDAY_COLLECTION = "tbl_holiday_collection";
        public static final String TABLE_GAME_COLLECTION = "tbl_game_collection";
        public static final String TABLE_NEWS_COLLECTION = "tbl_news_collection";
        public static final String TABLE_CARTOON_COLLECTION = "tbl_cartoon_collection";
        public static final String TABLE_DRAMA_COLLECTION = "tbl_drama_collection";

        public static final String TABLE_CATEGORY = "tbl_category";

        public static final int VERSION = 1;

        public static final String BLANK = " ";
        public static final String TEXT_NOT_NULL = "TEXT NOT NULL,";
        public static final String BLOB_NOT_NULL = "BLOB NOT NULL,";
        public static final String INTEGER_NOT_NULL = "INTEGER NOT NULL,";
        public static final String CREATE_TABLE_SMS_COLLECTION = "CREATE TABLE IF NOT EXISTS"
                + BLANK
                + TABLE_SMS_COLLECTION
                + BLANK
                + "("
                + DBColumns.ID
                + " CHAR UNIQUE, "
                + DBColumns.SmsColumns.SMS_CONTENT
                + " TEXT, "
                + DBColumns.TIME
                + "  TIMESTAMP NOT NULL ON CONFLICT REPLACE DEFAULT (datetime('now')))";
        public static final String CREATE_TABLE_SMS = "CREATE TABLE IF NOT EXISTS"
                + BLANK
                + TABLE_SMS
                + BLANK
                + "("
                + DBColumns.ID
                + " CHAR UNIQUE, "
                + DBColumns.SmsColumns.SMS_CONTENT
                + " TEXT, "
                + DBColumns.CATEGORY_ID
                + " TEXT, "
                + DBColumns.TIME
                + "  TIMESTAMP NOT NULL ON CONFLICT REPLACE DEFAULT (datetime('now')))";
        public static final String CREATE_TABLE_HOLIDAY_COLLECTION = "CREATE TABLE IF NOT EXISTS"
                + BLANK
                + TABLE_HOLIDAY_COLLECTION
                + BLANK
                + "("
                + DBColumns.ID
                + " CHAR UNIQUE, "
                + DBColumns.HolidayColumns.HOLIDAY_CONTENT
                + " TEXT, "
                + DBColumns.TIME
                + "  TIMESTAMP NOT NULL ON CONFLICT REPLACE DEFAULT (datetime('now')))";
        public static final String CREATE_TABLE_HOLIDAY = "CREATE TABLE IF NOT EXISTS"
                + BLANK
                + TABLE_HOLIDAY
                + BLANK
                + "("
                + DBColumns.ID
                + " CHAR UNIQUE, "
                + DBColumns.HolidayColumns.HOLIDAY_CONTENT
                + " TEXT, "
                + DBColumns.CATEGORY_ID
                + " TEXT, "
                + DBColumns.TIME
                + "  TIMESTAMP NOT NULL ON CONFLICT REPLACE DEFAULT (datetime('now')))";
        public static final String CREATE_TABLE_MMS_COLLECTION = "CREATE TABLE IF NOT EXISTS"
                + BLANK
                + TABLE_MMS_COLLECTION
                + BLANK
                + "("
                + DBColumns.ID
                + " CHAR UNIQUE, "
                + DBColumns.MmsColumns.MMS_NAME
                + " TEXT, "
                + DBColumns.MmsColumns.MMS_CONTENT
                + " TEXT, "
                + DBColumns.MmsColumns.MMS_PIC_URL
                + " TEXT, "
                + DBColumns.TIME
                + "  TIMESTAMP NOT NULL ON CONFLICT REPLACE DEFAULT (datetime('now')))";
        public static final String CREATE_TABLE_MMS = "CREATE TABLE IF NOT EXISTS"
                + BLANK
                + TABLE_MMS
                + BLANK
                + "("
                + DBColumns.ID
                + " CHAR UNIQUE, "
                + DBColumns.MmsColumns.MMS_NAME
                + " TEXT, "
                + DBColumns.MmsColumns.MMS_CONTENT
                + " TEXT, "
                + DBColumns.MmsColumns.MMS_PIC_URL
                + " TEXT, "
                + DBColumns.CATEGORY_ID
                + " TEXT, "
                + DBColumns.TIME
                + "  TIMESTAMP NOT NULL ON CONFLICT REPLACE DEFAULT (datetime('now')))";
        public static final String CREATE_TABLE_RING_COLLECTION = "CREATE TABLE IF NOT EXISTS"
                + BLANK
                + TABLE_RING_COLLECTION
                + BLANK
                + "("
                + DBColumns.ID
                + " CHAR UNIQUE, "
                + DBColumns.RingColumns.RING_NAME
                + " TEXT, "
                + DBColumns.RingColumns.RING_PRICE
                + " TEXT, "
                + DBColumns.RingColumns.RING_SINGER
                + " TEXT, "
                + DBColumns.RingColumns.RING_USE_COUNT
                + " INTEGER, "
                + DBColumns.TIME
                + "  TIMESTAMP NOT NULL ON CONFLICT REPLACE DEFAULT (datetime('now')))";
        public static final String CREATE_TABLE_RING = "CREATE TABLE IF NOT EXISTS"
                + BLANK
                + TABLE_RING
                + BLANK
                + "("
                + DBColumns.ID
                + " CHAR UNIQUE, "
                + DBColumns.RingColumns.RING_NAME
                + " TEXT, "
                + DBColumns.RingColumns.RING_PRICE
                + " TEXT, "
                + DBColumns.RingColumns.RING_SINGER
                + " TEXT, "
                + DBColumns.CATEGORY_ID
                + " TEXT, "
                + DBColumns.RingColumns.RING_USE_COUNT
                + " INTEGER, "
                + DBColumns.TIME
                + "  TIMESTAMP NOT NULL ON CONFLICT REPLACE DEFAULT (datetime('now')))";
        public static final String CREATE_TABLE_GAME_COLLECTION = "CREATE TABLE IF NOT EXISTS"
                + BLANK
                + TABLE_GAME_COLLECTION
                + BLANK
                + "("
                + DBColumns.ID
                + " CHAR UNIQUE, "
                + DBColumns.GameColumns.GAME_NAME
                + " TEXT, "
                + DBColumns.GameColumns.GAME_ICON_URL
                + " TEXT, "
                + DBColumns.GameColumns.GAME_INTRODUCE
                + " TEXT, "
                + DBColumns.GameColumns.GAME_STYLE
                + " TEXT, "
                + DBColumns.GameColumns.GAME_VERSION
                + " TEXT, "
                + DBColumns.GameColumns.GAME_FILE_SIZE
                + " TEXT, "
                + DBColumns.GameColumns.GAME_PUBLISHER
                + " TEXT, "
                + DBColumns.GameColumns.GAME_FILE_URL
                + " TEXT, "
                + DBColumns.GameColumns.GAME_SCREEN_SHOT_LIST
                + " TEXT, "
                + DBColumns.GameColumns.GAME_DOWNLOAD
                + " INTEGER, "
                + DBColumns.CATEGORY_ID
                + " TEXT, "
                + DBColumns.TIME
                + "  TIMESTAMP NOT NULL ON CONFLICT REPLACE DEFAULT (datetime('now')))";
        public static final String CREATE_TABLE_GAME = "CREATE TABLE IF NOT EXISTS"
                + BLANK
                + TABLE_GAME
                + BLANK
                + "("
                + DBColumns.ID
                + " CHAR UNIQUE, "
                + DBColumns.GameColumns.GAME_NAME
                + " TEXT, "
                + DBColumns.GameColumns.GAME_ICON_URL
                + " TEXT, "
                + DBColumns.GameColumns.GAME_INTRODUCE
                + " TEXT, "
                + DBColumns.GameColumns.GAME_STYLE
                + " TEXT, "
                + DBColumns.GameColumns.GAME_VERSION
                + " TEXT, "
                + DBColumns.GameColumns.GAME_FILE_SIZE
                + " TEXT, "
                + DBColumns.GameColumns.GAME_PUBLISHER
                + " TEXT, "
                + DBColumns.GameColumns.GAME_FILE_URL
                + " TEXT, "
                + DBColumns.GameColumns.GAME_SCREEN_SHOT_LIST
                + " TEXT, "
                + DBColumns.GameColumns.GAME_DOWNLOAD
                + " INTEGER, "
                + DBColumns.CATEGORY_ID
                + " TEXT, "
                + DBColumns.TIME
                + "  TIMESTAMP NOT NULL ON CONFLICT REPLACE DEFAULT (datetime('now')))";
        public static final String CREATE_TABLE_NEWS_COLLECTION = "CREATE TABLE IF NOT EXISTS"
                + BLANK
                + TABLE_NEWS_COLLECTION
                + BLANK
                + "("
                + DBColumns.ID
                + " CHAR UNIQUE, "
                + DBColumns.TIME
                + "  TIMESTAMP NOT NULL ON CONFLICT REPLACE DEFAULT (datetime('now')))";
        public static final String CREATE_TABLE_NEWS = "CREATE TABLE IF NOT EXISTS"
                + BLANK
                + TABLE_NEWS
                + BLANK
                + "("
                + DBColumns.ID
                + " CHAR UNIQUE, "
                + DBColumns.TIME
                + "  TIMESTAMP NOT NULL ON CONFLICT REPLACE DEFAULT (datetime('now')))";
        public static final String CREATE_TABLE_CARTOON_COLLECTION = "CREATE TABLE IF NOT EXISTS"
                + BLANK
                + TABLE_CARTOON_COLLECTION
                + BLANK
                + "("
                + DBColumns.ID
                + " CHAR UNIQUE, "
                + DBColumns.CartoonColumns.CARTOON_NAME
                + " TEXT, "
                + DBColumns.CartoonColumns.CARTOON_AUTHOR
                + " TEXT, "
                + DBColumns.CartoonColumns.CARTOON_INTRODUCE
                + " TEXT, "
                + DBColumns.CartoonColumns.CARTOON_COVER_PIC_URL
                + " TEXT, "
                + DBColumns.TIME
                + "  TIMESTAMP NOT NULL ON CONFLICT REPLACE DEFAULT (datetime('now')))";
        public static final String CREATE_TABLE_CARTOON = "CREATE TABLE IF NOT EXISTS"
                + BLANK
                + TABLE_CARTOON
                + BLANK
                + "("
                + DBColumns.ID
                + " CHAR UNIQUE, "
                + DBColumns.CartoonColumns.CARTOON_NAME
                + " TEXT, "
                + DBColumns.CartoonColumns.CARTOON_AUTHOR
                + " TEXT, "
                + DBColumns.CartoonColumns.CARTOON_INTRODUCE
                + " TEXT, "
                + DBColumns.CartoonColumns.CARTOON_COVER_PIC_URL
                + " TEXT, "
                + DBColumns.CATEGORY_ID
                + " TEXT, "
                + DBColumns.TIME
                + "  TIMESTAMP NOT NULL ON CONFLICT REPLACE DEFAULT (datetime('now')))";
        public static final String CREATE_TABLE_DRAMA_COLLECTION = "CREATE TABLE IF NOT EXISTS"
                + BLANK
                + TABLE_DRAMA_COLLECTION
                + BLANK
                + "("
                + DBColumns.ID
                + " CHAR UNIQUE, "
                + DBColumns.CartoonDramaColumns.DRAMA_NAME
                + " TEXT, "
                + DBColumns.CartoonDramaColumns.DRAMA_FILE_URL
                + " TEXT, "
                + DBColumns.CartoonDramaColumns.DRAMA_PIC_COUNT
                + " INTEGER, "
                + DBColumns.TIME
                + "  TIMESTAMP NOT NULL ON CONFLICT REPLACE DEFAULT (datetime('now')))";
        public static final String CREATE_TABLE_DRAMA = "CREATE TABLE IF NOT EXISTS"
                + BLANK
                + TABLE_DRAMA
                + BLANK
                + "("
                + DBColumns.ID
                + " CHAR UNIQUE, "
                + DBColumns.CartoonDramaColumns.DRAMA_NAME
                + " TEXT, "
                + DBColumns.CartoonDramaColumns.DRAMA_FILE_URL
                + " TEXT, "
                + DBColumns.CartoonDramaColumns.DRAMA_PIC_COUNT
                + " INTEGER, "
                + DBColumns.TIME
                + "  TIMESTAMP NOT NULL ON CONFLICT REPLACE DEFAULT (datetime('now')))";

        public static final String CREATE_TABLE_CATEGORY = "CREATE TABLE IF NOT EXISTS"
                + BLANK
                + TABLE_CATEGORY
                + BLANK
                + "("
                + DBColumns.ID
                + " TEXT, "
                + DBColumns.CATEGORY_NAME
                + " TEXT, "
                + DBColumns.CATEGORY_ID + " TEXT)";

        public DBHelper(Context context) {
            this(context, DB_NAME, null, VERSION);
        }

        protected DBHelper(Context context, String name, CursorFactory factory,
                int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE_SMS);
            db.execSQL(CREATE_TABLE_MMS);
            db.execSQL(CREATE_TABLE_RING);
            db.execSQL(CREATE_TABLE_HOLIDAY);
            db.execSQL(CREATE_TABLE_GAME);
            db.execSQL(CREATE_TABLE_NEWS);
            db.execSQL(CREATE_TABLE_CARTOON);
            db.execSQL(CREATE_TABLE_SMS_COLLECTION);
            db.execSQL(CREATE_TABLE_MMS_COLLECTION);
            db.execSQL(CREATE_TABLE_RING_COLLECTION);
            db.execSQL(CREATE_TABLE_HOLIDAY_COLLECTION);
            db.execSQL(CREATE_TABLE_GAME_COLLECTION);
            db.execSQL(CREATE_TABLE_NEWS_COLLECTION);
            db.execSQL(CREATE_TABLE_CARTOON_COLLECTION);
            db.execSQL(CREATE_TABLE_CATEGORY);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

    }

}
