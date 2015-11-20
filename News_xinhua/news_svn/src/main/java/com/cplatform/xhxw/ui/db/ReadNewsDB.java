package com.cplatform.xhxw.ui.db;

import android.content.Context;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.cplatform.xhxw.ui.db.dao.ReadNewsDao;

import java.util.List;

/**
 * 已读新闻
 */
public class ReadNewsDB {

    private static final String TAG = ReadNewsDB.class.getSimpleName();
    private static Context mAppContext;

    private static void initContext(Context context) {
        if (mAppContext == null)
            mAppContext = context.getApplicationContext();

    }

    /**
     * 已读
     * */
    public static void saveNews(Context context, List<ReadNewsDao> news) {
        if (null == news || news.size() == 0) {
            return;
        }
        for (ReadNewsDao item : news) {
            saveNews(context, item);
        }
    }

    public static void saveNews(Context context, ReadNewsDao news) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);

            ReadNewsDao result = mHelper.getReadNewsDao().queryForId(news.getNewsId());
            if (result == null) {
                mHelper.getReadNewsDao().create(news);
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
    public static void delReadNewsById(Context context, String newsId) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            mHelper.getReadNewsDao().deleteById(newsId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }
    }

    /**
     * 删除小于某个阅读时间的所有数据
     * @param context
     * @param time
     * @return
     */
    public static int delReadNewsByLtTime(Context context, long time) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            DeleteBuilder<ReadNewsDao, String> builder = mHelper.getReadNewsDao().deleteBuilder();
            builder.where().lt("readTime", time);
            return builder.delete();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }
        return 0;
    }

    /**
     * 根据newsId查询
     * @param context
     * @param newsId
     * @return
     */
    public static ReadNewsDao getReadNewsByNewsId(Context context, String newsId) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);

            return mHelper.getReadNewsDao().queryForId(newsId);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }

        return null;
    }
}
