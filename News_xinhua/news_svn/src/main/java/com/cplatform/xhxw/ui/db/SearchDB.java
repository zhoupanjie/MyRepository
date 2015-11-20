package com.cplatform.xhxw.ui.db;

import android.content.Context;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.cplatform.xhxw.ui.db.dao.SearchDao;
import com.cplatform.xhxw.ui.util.LogUtil;

import java.util.List;

public class SearchDB {

	private static final String TAG = SearchDB.class.getSimpleName();
    private static Context mAppContext;

    private static void initContext(Context context) {
        if (mAppContext == null)
            mAppContext = context.getApplicationContext();

    }
    
    public static void saveSearch(Context context, SearchDao search) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            SearchDao result = mHelper.getSearchDao().queryForId(search.getName());
            if (result == null) {
                mHelper.getSearchDao().create(search);
            } else {
                updateSearch(context, search);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }
    }

    /**
     * 根据名称删除搜索
     */
    public static void delSearchByName(Context context, String name) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            mHelper.getSearchDao().deleteById(name);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }
    }

    /**
     * 删除所有搜索记录
     */
    public static void delAllSearch(Context context) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            mHelper.getSearchDao().deleteBuilder().delete();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }
    }

    /**
     * 获得搜索搜索记录
     */
    public static List<SearchDao> getSearchs(Context context) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            QueryBuilder<SearchDao, String> que = mHelper.getSearchDao().queryBuilder();
            que.orderBy("lastTime", false);
            return que.query();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }

        return null;
    }

    /**
     * 更新搜索
     */
    public static int updateSearch(Context context, SearchDao search) {
        initContext(context);
        DatabaseHelper helper;
        try {
            helper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            return helper.getSearchDao().update(search);
        } catch (Exception e) {
            LogUtil.w(TAG, e);
        } finally {
            OpenHelperManager.releaseHelper();
        }
        return -1;
    }


}
