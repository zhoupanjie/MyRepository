package com.cplatform.xhxw.ui.test;

import android.content.ContentValues;
import android.content.Context;
import com.cplatform.xhxw.ui.db.dao.ContactsDao;
import com.cplatform.xhxw.ui.provider.XwContentProvider;
import com.cplatform.xhxw.ui.util.LogUtil;

/**
 * Created by cy-love on 14-8-3.
 */
public class TestData {


    public static void addAddressBookData(Context context) {
        ContentValues[] arrayValues = new ContentValues[50];
        String modifiedDate = String.valueOf(System.currentTimeMillis());
        for (int i = 0; i < 50; i++) {
            ContentValues values = new ContentValues();
            values.put(ContactsDao.USER_ID,i);
            values.put(ContactsDao.INDEX_KEY, i);
            values.put(ContactsDao.LOGO, "http://su.bdimg.com/static/superplus/img/logo_white.png");
            values.put(ContactsDao.NAME, "test"+i);
            values.put(ContactsDao.SYNC_TIME, modifiedDate);
            arrayValues[i] = values;
        }
        int count = context.getContentResolver().bulkInsert(XwContentProvider.CONTACTS_URI, arrayValues);
        LogUtil.d("testdata", "add address_book : "+ count);
    }


}
