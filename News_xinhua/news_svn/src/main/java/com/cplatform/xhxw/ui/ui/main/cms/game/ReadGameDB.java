package com.cplatform.xhxw.ui.ui.main.cms.game;

import android.content.Context;

import com.cplatform.xhxw.ui.db.DatabaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;

/**
 * 
 * @ClassName ReadGameDB 
 * @Description TODO 已读游戏
 * @Version 1.0
 * @Author zxe
 * @Creation 2015年6月26日 上午10:58:43 
 * @Mender zxe
 * @Modification 2015年6月26日 上午10:58:43 
 * @Copyright Copyright © 2013 - 2015 Channelsoft (Beijing) Technology Co., Ltd.All Rights Reserved.
*
 */
public class ReadGameDB {

//    private static final String TAG = ReadGameDB.class.getSimpleName();
    private static Context mAppContext;

    private static void initContext(Context context) {
        if (mAppContext == null)
            mAppContext = context.getApplicationContext();
    }

    /**
     * 根据newsId查询
     * @param context
     * @param newsId
     * @return
     */
    public static ReadGameDao getReadGameById(Context context, String id) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            if(mHelper.getReadGameDao()!=null){
            	return mHelper.getReadGameDao().queryForId(id);
            }
        } catch (Exception e) {
//            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }

        return null;
    }
}
