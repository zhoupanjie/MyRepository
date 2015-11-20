package com.cplatform.xhxw.ui.ui.main.cms.game;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.cplatform.xhxw.ui.db.DatabaseHelper;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.j256.ormlite.android.apptools.OpenHelperManager;

/**
 * 栏目缓存
 */
public class GameCashDB {

	private static final String TAG = GameCashDB.class.getSimpleName();
    private static Context mAppContext;

    private static void initContext(Context context) {
        if (mAppContext == null)
            mAppContext = context.getApplicationContext();
    }

    /**
     * 保存数据
     */
    public static void saveData(Context context, GameCashDao gameCashDao) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            GameCashDao result = mHelper.getGameCashDao().queryForId(gameCashDao.getAppId());
            if (result == null) {
                mHelper.getGameCashDao().create(gameCashDao);
            } else {
                updateGameCache(context, gameCashDao);
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
    public static void delGameCashByAppId(Context context, String columnId) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            mHelper.getGameCashDao().deleteById(columnId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }
    }
    /**
     * 获得指定栏目id的缓存
     * @param context
     * @param columnId 游戏id
     * @return
     */
    public static GameCashDao getGameCacheByAppId(Context context, String columnId) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            return mHelper.getGameCashDao().queryForId(columnId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }

        return null;
    }
//    public static GameCashDao getGameCacheByDown(Context context, String columnId) {
//        initContext(context);
//        DatabaseHelper mHelper;
//        try {
//            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
//            return mHelper.getGameCashDao().queryForEq(arg0, arg1).queryForId(columnId);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            OpenHelperManager.releaseHelper();
//        }
//
//        return null;
//    }
    public static List<GameCashDao> getGameCacheList(Context context) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            return mHelper.getGameCashDao().queryForAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }
        return null;
    }
    public static List<GameCashDao> getGameCacheDownList(Context context) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            return mHelper.getGameCashDao().queryForEq("stateDown", GameUtil.GAME_DOWN_WAIT);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }
        return null;
    }
//
    /**
     * 更新缓存
     * @param context
     * @param newsCashDao 更新内容
     * @return
     */
    public static int updateGameCache(Context context, GameCashDao gameCashDao) {
    	Log.e("GameCashDB", "updateGameCache1");
        initContext(context);
        DatabaseHelper helper;
        try {
            helper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            Log.e("GameCashDB", "updateGameCache3");
            return helper.getGameCashDao().update(gameCashDao);
        } catch (Exception e) {
            LogUtil.w(TAG, e);
        } finally {
            OpenHelperManager.releaseHelper();
        }
        Log.e("GameCashDB", "updateGameCache2");
        return -1;
    }


}
