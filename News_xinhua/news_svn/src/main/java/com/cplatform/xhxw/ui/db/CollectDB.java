package com.cplatform.xhxw.ui.db;

import java.util.List;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.db.dao.CollectDao;
import com.cplatform.xhxw.ui.db.dao.CollectFlag;
import com.cplatform.xhxw.ui.util.StringUtil;

import android.content.Context;

public class CollectDB {

	private static final String TAG = CollectDB.class.getSimpleName();
    private static Context mAppContext;

    private static void initContext(Context context) {
        if (mAppContext == null)
            mAppContext = context.getApplicationContext();

    }
    
    /**
     * 收藏新闻
     * */
    public static void saveNews(Context context, List<CollectDao> news) {
        if (null == news || news.size() == 0) {
            return;
        }
        for (CollectDao item : news) {
            saveNews(context, item);
        }
    }
    
    public static void saveNews(Context context, CollectDao news) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            CollectDao result = mHelper.getCollectDao().queryForId(news.getNewsId());
            if (result == null) {
                mHelper.getCollectDao().create(news);
            } else {
                /*for (CollectImagesDao tmp : result){
                    tmp.setListorder(collectImage.getListorder());
                    tmp.setChannelName(collectImage.getChannelName());
                    tmp.setOperatetime(collectImage.getOperatetime());
                    updateChanneById(context, tmp);
                }*/

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }
    }

    /**
     * 根据id删除栏目
     *
     * @param newsId
     */
    public static void delCollectByNewsId(Context context, String newsId) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            mHelper.getCollectDao().deleteById(newsId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }
    }

    /**
     * 获得收藏内容
     * @param context 上下文
     * @return
     */
    public static List<CollectDao> getCollects(Context context) {
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
            QueryBuilder<CollectDao, String> que = mHelper.getCollectDao().queryBuilder();
            que.orderBy("operatetime", false);
            return que.query();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }

        return null;
    }

    /**
     * 根据newsId查询
     * @param context
     * @param newsId
     * @return
     */
    public static CollectDao getCollectByNewsId(Context context, String newsId) {
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
            return mHelper.getCollectDao().queryForId(newsId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }

        return null;
    }

    /**
     * 根据当前用户企业ID获取收藏内容
     * @param context
     * @return
     */
    public static List<CollectDao> getCollectsByEnterPriseId (Context context) {
    	DatabaseHelper mHelper;
        try {
        	int enterPriseId = 0;
        	if(Constants.hasEnterpriseAccountLoggedIn()) {
        		enterPriseId = StringUtil.parseIntegerFromString(Constants.getEnterpriseId());
        	}
            mHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
            QueryBuilder<CollectDao, String> que = mHelper.getCollectDao().queryBuilder();
            que.orderBy("operatetime", false);
            if(enterPriseId > 0) {
            	que.where().eq("flag", CollectFlag.COLLECT_NEWS_TYPE_NORMAL_NORM)
        			.or().eq("flag", CollectFlag.COLLECT_NEWS_TYPE_NORMAL_PICS)
        			.or().eq("flag", CollectFlag.COLLECT_NEWS_TYPE_ENTERPRISE_NORM + enterPriseId)
        			.or().eq("flag", CollectFlag.COLLECT_NEWS_TYPE_ENTERPRISE_PICS + enterPriseId);
            } else {
            	que.where().eq("flag", CollectFlag.COLLECT_NEWS_TYPE_NORMAL_NORM)
        			.or().eq("flag", CollectFlag.COLLECT_NEWS_TYPE_NORMAL_PICS);
            }
            
            return que.query();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }

        return null;
    }
}
