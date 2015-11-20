package com.cplatform.xhxw.ui.db;

import android.content.Context;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.cplatform.xhxw.ui.db.dao.NewsDetailCashDao;
import com.cplatform.xhxw.ui.util.LogUtil;

/**
 * 新闻内容缓存
 */
public class NewsDetailCashDB {

	private static final String TAG = NewsDetailCashDB.class.getSimpleName();
    private static Context mAppContext;

    private static void initContext(Context context) {
        if (mAppContext == null)
            mAppContext = context.getApplicationContext();
    }

    /**
     * 保存数据
     */
    public static void saveData(Context context, NewsDetailCashDao dao) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            NewsDetailCashDao result = mHelper.getNewsDetailCashDao().queryForId(dao.getNewsId());
            if (result == null) {
                mHelper.getNewsDetailCashDao().create(dao);
            } else {
                updateNewsDetailCash(context, dao);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }
    }

    /**
     * 根据新闻id删除数据
     * @param context
     * @param newsId 新闻id
     */
    public static void delNewsDetailCashByNewsId(Context context, String newsId) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            mHelper.getNewsDetailCashDao().deleteById(newsId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }
    }

    /**
     * 获得指定新闻的缓存
     * @param context
     * @param newsId 新闻id
     * @return
     */
    public static NewsDetailCashDao getNewsCashByColumnId(Context context, String newsId) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            return mHelper.getNewsDetailCashDao().queryForId(newsId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }

        return null;
    }

    /**
     * 更新缓存
     * @param context
     * @param dao 更新内容
     * @return
     */
    public static int updateNewsDetailCash(Context context, NewsDetailCashDao dao) {
        initContext(context);
        DatabaseHelper helper;
        try {
            helper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            return helper.getNewsDetailCashDao().update(dao);
        } catch (Exception e) {
            LogUtil.w(TAG, e);
        } finally {
            OpenHelperManager.releaseHelper();
        }
        return -1;
    }


}
