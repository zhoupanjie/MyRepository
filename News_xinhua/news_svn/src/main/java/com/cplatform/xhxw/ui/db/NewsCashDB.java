package com.cplatform.xhxw.ui.db;

import android.content.Context;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.cplatform.xhxw.ui.db.dao.NewsCashDao;
import com.cplatform.xhxw.ui.util.LogUtil;

/**
 * 栏目缓存
 */
public class NewsCashDB {

	private static final String TAG = NewsCashDB.class.getSimpleName();
    private static Context mAppContext;

    private static void initContext(Context context) {
        if (mAppContext == null)
            mAppContext = context.getApplicationContext();
    }

    /**
     * 保存数据
     */
    public static void saveData(Context context, NewsCashDao newsCashDao) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            NewsCashDao result = mHelper.getNewsCashDao().queryForId(newsCashDao.getColumnId());
            if (result == null) {
                mHelper.getNewsCashDao().create(newsCashDao);
            } else {
                updateNewsCash(context, newsCashDao);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }
    }

    /**
     * 根据名称删除搜索
     * @param context
     * @param columnId 栏目id
     */
    public static void delNewsCashByColumnId(Context context, String columnId) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            mHelper.getNewsCashDao().deleteById(columnId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }
    }

    /**
     * 获得指定栏目id的缓存
     * @param context
     * @param columnId 栏目id
     * @return
     */
    public static NewsCashDao getNewsCashByColumnId(Context context, String columnId) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            return mHelper.getNewsCashDao().queryForId(columnId);
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
     * @param newsCashDao 更新内容
     * @return
     */
    public static int updateNewsCash(Context context, NewsCashDao newsCashDao) {
        initContext(context);
        DatabaseHelper helper;
        try {
            helper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            return helper.getNewsCashDao().update(newsCashDao);
        } catch (Exception e) {
            LogUtil.w(TAG, e);
        } finally {
            OpenHelperManager.releaseHelper();
        }
        return -1;
    }


}
