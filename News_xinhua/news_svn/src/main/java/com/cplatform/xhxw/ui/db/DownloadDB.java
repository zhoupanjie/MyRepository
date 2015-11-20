package com.cplatform.xhxw.ui.db;

import android.content.Context;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.cplatform.xhxw.ui.db.dao.DownloadDao;

import java.util.List;

public class DownloadDB {

	private static final String TAG = DownloadDB.class.getSimpleName();
    private static Context mAppContext;

    private static void initContext(Context context) {
        if (mAppContext == null)
            mAppContext = context.getApplicationContext();

    }

    /**
     * 获得所有下载
     * @return
     */
    public static List<DownloadDao> getAllDownload(Context context) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            return mHelper.getDownloadDao().queryForAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }

        return null;
    }

    /**
     * 获得下一个下载
     * @return
     */
    public static DownloadDao getNextDownload(Context context) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            QueryBuilder<DownloadDao, Long> query = mHelper.getDownloadDao().queryBuilder();
            return query.queryForFirst();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }

        return null;
    }

    /**
     * 删除指定id的下载任务
     * @param downId
     * @return
     */
    public static int removeDownload(Context context, long downId) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            return mHelper.getDownloadDao().deleteById(downId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }
        return 0;
    }

    /**
     * 根据url查询
     * @param url
     * @return
     */
    public static DownloadDao getDownloadByUrl(Context context, String url){
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            QueryBuilder<DownloadDao, Long> query = mHelper.getDownloadDao().queryBuilder();
            query.where().eq("url",url);
            return query.queryForFirst();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }
        return null;
    }

    /**
     * 更新任务
     * @param downloadDao
     */
    public static int updateDownload(Context context, DownloadDao downloadDao) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            return mHelper.getDownloadDao().update(downloadDao);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }
        return 0;
    }

    /**
     * 添加任务
     * @param downloadDao
     * @return
     */
    public static boolean addDownload(Context context, DownloadDao downloadDao) {
        DownloadDao dao = getDownloadByUrl(context, downloadDao.getUrl());
        if (dao != null) {
            return false;
        }
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            int i = mHelper.getDownloadDao().create(downloadDao);
            if (i > 0) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }
        return false;
    }


}
