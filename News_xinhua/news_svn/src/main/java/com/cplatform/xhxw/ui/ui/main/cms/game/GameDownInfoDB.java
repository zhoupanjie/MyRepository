package com.cplatform.xhxw.ui.ui.main.cms.game;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.google.gson.Gson;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.cplatform.xhxw.ui.db.DatabaseHelper;
import com.cplatform.xhxw.ui.db.dao.NewsCashDao;
import com.cplatform.xhxw.ui.util.DateUtil;
import com.cplatform.xhxw.ui.util.LogUtil;

/**
 * 栏目缓存
 */
public class GameDownInfoDB {

	private static final String TAG = GameDownInfoDB.class.getSimpleName();
    private static Context mAppContext;

    private static void initContext(Context context) {
        if (mAppContext == null)
            mAppContext = context.getApplicationContext();
    }

    /**
     * 保存数据
     */
    public static void saveData(Context context, List<DownloadInfo> infos) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            if (infos != null && (false == infos.isEmpty())) {
    			for (DownloadInfo di : infos) {
    				GameDownInfoDao gdid = new GameDownInfoDao();
    				gdid.setAppId(di.getUrl());
    				String jsonContent = new Gson().toJson(di);
    				gdid.setJson(jsonContent);
    				gdid.setSaveTime(DateUtil.getTime());
    				GameDownInfoDao result = mHelper.getGameDownInfoDao().queryForId(gdid.getAppId());
    	            if (result == null) {
    	                mHelper.getGameDownInfoDao().create(gdid);
    	            } else {
    	            	mHelper.getGameDownInfoDao().update(gdid);
    	            }
    			}
    		}
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }
    }
    public static void delGameCashByAppId(Context context, String url) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            List<GameDownInfoDao> lgdid = GameDownInfoDB.getGameCacheList(context);
    		if (lgdid != null && (false == lgdid.isEmpty())) {

    			List<GameDownInfoDao> ldi = new ArrayList<GameDownInfoDao>();
    			for (GameDownInfoDao gdid : lgdid) {
    				if (gdid.getAppId().equals(url)) {
    					ldi.add(gdid);
    				}
    			}
    			if (ldi != null && (false == ldi.isEmpty())) {
    				mHelper.getGameDownInfoDao().delete(ldi);
    			}
    		}
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
    public static GameDownInfoDao getGameCacheByAppId(Context context, String columnId) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            return mHelper.getGameDownInfoDao().queryForId(columnId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }

        return null;
    }
    public static List<GameDownInfoDao> getGameCacheList(Context context) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            return mHelper.getGameDownInfoDao().queryForAll();
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
    public static int updateGameCache(Context context, GameDownInfoDao GameDownInfoDao) {
        initContext(context);
        DatabaseHelper helper;
        try {
            helper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            return helper.getGameDownInfoDao().update(GameDownInfoDao);
        } catch (Exception e) {
            LogUtil.w(TAG, e);
        } finally {
            OpenHelperManager.releaseHelper();
        }
        return -1;
    }


}
