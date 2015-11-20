package com.cplatform.xhxw.ui.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import android.content.Context;
import android.text.TextUtils;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.PreferencesManager;
import com.cplatform.xhxw.ui.db.dao.ChanneDao;
import com.cplatform.xhxw.ui.ui.main.forelanguage.utils.LanguageUtil;
import com.cplatform.xhxw.ui.util.DateUtil;
import com.cplatform.xhxw.ui.util.ListUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.j256.ormlite.stmt.QueryBuilder;

public class ChanneDB {

    private static final String TAG = ChanneDB.class.getSimpleName();
    private static Context mAppContext;

    private static void initContext(Context context) {
        if (mAppContext == null)
            mAppContext = context.getApplicationContext();

    }

    /**
     * 同步用户栏目
     * @param context
     * @param channes
     * @param userId
     * @throws Exception
     */
    private static boolean sIsChangeTmp = false;
    public static boolean sycnChannesByUserId(Context context, final List<com.cplatform.xhxw.ui.model.Channe> channes, 
    		final String userId, final String language) throws Exception {
        if (null == channes || channes.size() == 0) {
            return false;
        }
        sIsChangeTmp = false;
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);

            final Dao<ChanneDao, Integer> dao = mHelper.getChanneDao();
            dao.callBatchTasks(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    long syncTime = DateUtil.getTime();
                    HashMap<String, Boolean> showIds = new HashMap<String, Boolean>();
                    if (!ListUtil.isEmpty(channes)) {
                        for (com.cplatform.xhxw.ui.model.Channe channe : channes) {
                            showIds.put(channe.getChannelid(), true);
                            ChanneDao item = new ChanneDao();
                            item.setSyncTime(syncTime);
                            item.setChannelName(channe.getChannelname());
                            item.setListorder(channe.getListorder());
                            item.setChannelID(channe.getChannelid());
                            item.setShow(channe.getIsdisplay());
                            item.setUserId(userId);
                            item.setChannelLanguage(language);
                            item.setChannelCreateType("" + channe.getChanneltype());
                            boolean change = sycnChannesByUserId(dao, item, userId, language);
                            sIsChangeTmp = change ? true : sIsChangeTmp;
                        }
                    }
//                    for (ChanneDao channe : allChanne) {
//                        Boolean isShow = showIds.get(channe.getChannelID());
//                        if (isShow != null && isShow) {
//                            continue;
//                        }
//                        ChanneDao tmp = new ChanneDao();
//                        tmp.setSyncTime(syncTime);
//                        tmp.setChannelID(channe.getChannelID());
//                        tmp.setChannelName(channe.getChannelName());
//                        tmp.setListorder(channe.getListorder());
//                        tmp.setUserId(userId);
//                        tmp.setShow(1);
//                        int row = sycnChannesByUserId(dao, tmp, userId);
//                        LogUtil.d(TAG, "用户id:: "+userId+"  隐藏数据::"+row);
//                    }
                    // 删除冗余数据
                    DeleteBuilder<ChanneDao, Integer> del = dao.deleteBuilder();
                    del.where().eq("userId", userId).and().lt("syncTime", syncTime)
                    			.and().eq(ChanneDao.CHANNEL_ENTERPRISE, 0)
                    			.and().eq(ChanneDao.CHANNEL_LANGUAGE, language)
                    			.and().not().eq(ChanneDao.CHANNEL_TYPE, ChanneDao.CHANNEL_TYPE_KEY_WORDS).prepare();
                    int row = del.delete();
                    LogUtil.d(TAG, "用户id:: "+userId+"  清空数据 row::"+row + " delwhere---" + del.where().toString());
                    return null;
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();

        }
        return sIsChangeTmp;
    }

    public static void addUserIdChannesByDefault(Context context, final String userId) throws Exception {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            final Dao<ChanneDao, Integer> dao = mHelper.getChanneDao();
            dao.callBatchTasks(new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    long syncTime = DateUtil.getTime();
                    List<ChanneDao> allChanne = getDefaultAllChanne(mAppContext);
                    if (!ListUtil.isEmpty(allChanne)) {
                        for (ChanneDao channe : allChanne) {
                            channe.setUserId(userId);
                            channe.setDirty(1);
                            channe.setChannelLanguage(LanguageUtil.LANGUAGE_CH);
                            channe.setSyncTime(syncTime);
                            sycnChannesByUserId(dao, channe, userId, LanguageUtil.LANGUAGE_CH);
                        }
                    }
                    return null;
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();

        }
    }

    /**
     * 用户栏目同步修改
     * @return row 新增条数
     */
    public static boolean sycnChannesByUserId(Dao<ChanneDao, Integer> dao, ChanneDao channe, 
    		String userId, String language) throws SQLException {
        boolean isAdd = false;
        if (channe != null && TextUtils.isEmpty(channe.getChannelName())) {
            return false;
        }
        List<ChanneDao> result = dao.queryBuilder().where().eq("channelId", channe.getChannelID()).and()
            	.eq("userId", userId).and().eq(ChanneDao.CHANNEL_ENTERPRISE, 0)
            	.and().eq(ChanneDao.CHANNEL_LANGUAGE, language).query();
        if (result == null || result.size() == 0) {
        	if(channe.getChannelLanguage() == null 
        			|| channe.getChannelLanguage().length() < 1) {
        		channe.setChannelLanguage(LanguageUtil.LANGUAGE_CH);
        	}
            int row = dao.create(channe);
            isAdd = row == 1 ? true : false;
        } else {
            for (ChanneDao tmp : result) {
                tmp.setListorder(channe.getListorder());
                tmp.setChannelName(channe.getChannelName());
                tmp.setOperatetime(channe.getOperatetime());
                tmp.setSyncTime(channe.getSyncTime());
                tmp.setChannelCreateType(channe.getChannelCreateType());
                if(!Constants.DB_DEFAULT_USER_ID.equals(tmp.getUserId())
                        && tmp.getDirty() != 1) {
                    tmp.setShow(channe.getShow());
                }
                if(tmp.getChannelLanguage() == null 
            			|| tmp.getChannelLanguage().length() < 1) {
                	tmp.setChannelLanguage(LanguageUtil.LANGUAGE_CH);
            	}
                dao.update(tmp);
                break;
            }
        }
        return isAdd;
    }

    /**
     * 获得默认用户的所有栏目
     *
     * @param context
     * @return
     */
    public static List<ChanneDao> getDefaultAllChanne(Context context) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", Constants.DB_DEFAULT_USER_ID);
            List<ChanneDao> result = mHelper.getChanneDao()
            		.queryBuilder().where().eq("userId", Constants.DB_DEFAULT_USER_ID)
            		.and().eq(ChanneDao.CHANNEL_ENTERPRISE, 0)
            		.and().eq(ChanneDao.CHANNEL_LANGUAGE, LanguageUtil.LANGUAGE_CH).query();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }
        return null;
    }

    /**
     * 获得默认用户的所有栏目
     *
     * @param context
     * @return
     */
    public static List<ChanneDao> getAllChanne(Context context) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("userId", Constants.DB_DEFAULT_USER_ID);
            List<ChanneDao> result = mHelper.getChanneDao().queryForFieldValues(map);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }
        return null;
    }
    
    public static void gAllChanne(Context context) {
    	initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            List<ChanneDao> result = mHelper.getChanneDao().queryForAll();
            for(ChanneDao cd : result) {
            	LogUtil.e(TAG, "id---" + cd.getId() + " userid---" + cd.getUserId() 
            			+ " channelName---" + cd.getChannelName()
            			+ " channelId---" + cd.getChannelID() + " enId---" + cd.getEnterpriseId()
            			+ " operTime---" + cd.getOperatetime() + " order---" + cd.getListorder()
            			+ " isShow----" + cd.getShow() + " language---" + cd.getChannelLanguage()
            			+ " enterprise----" + cd.getEnterpriseId() + " syncTime----" + cd.getSyncTime()
            			+ " isDirty----" + cd.getDirty() + " channelType----" + cd.getChannelCreateType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }
    }

    /**
     * 获得显示的栏目
     *
     * @param context
     * @param userId
     * @return
     */
    public static List<ChanneDao> getShowChanne(Context context, String userId) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            QueryBuilder<ChanneDao, Integer> que = mHelper.getChanneDao().queryBuilder();
            que.where().eq("userId", userId).and().eq("show", "0")
            .and().eq(ChanneDao.CHANNEL_ENTERPRISE, 0)
            .and().eq(ChanneDao.CHANNEL_LANGUAGE, PreferencesManager.getLanguageInfo(context));
            List<ChanneDao> quResult = que.orderBy("listorder", true).query();
            
            if(quResult == null || quResult.size() < 1) {
            	QueryBuilder<ChanneDao, Integer> que1 = mHelper.getChanneDao().queryBuilder();
                que1.where().eq("userId", userId).and().eq("show", "0")
                .and().eq(ChanneDao.CHANNEL_ENTERPRISE, 0);
                quResult = que1.query();
            }
            List<ChanneDao> result = new ArrayList<ChanneDao>();
            StringBuilder channeNameString = new StringBuilder();
            channeNameString.append(",");
            if(quResult != null) {
            	for(ChanneDao cd : quResult) {
            		if(channeNameString.indexOf("," + cd.getChannelName() + ",") < 0) {
            			result.add(cd);
            			channeNameString.append(cd.getChannelName() + ",");
            		}
            	}
            }
            channeNameString = null;
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }

        return null;
    }
    
    public static List<ChanneDao> getSssssss(Context context, String userId) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            QueryBuilder<ChanneDao, Integer> que = mHelper.getChanneDao().queryBuilder();
            que.where().eq("userId", userId).and().eq("show", "0");
            List<ChanneDao> result = que.orderBy("listorder", true).query();
            for(ChanneDao cd : result) {
            	LogUtil.e(TAG, "0----" + cd.getChannelID() + " name---" + cd.getChannelName() +
            			" listOrder------" + cd.getListorder() + " uid-----" + cd.getUserId() +
            			" ct---" + cd.getChannelCreateType() + " eid-----" + cd.getEnterpriseId() +
            			" chLan----" + cd.getChannelLanguage());
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }

        return null;
    }

    /**
     * 获得显示的栏目
     *
     * @param context
     * @param userId
     * @return
     */
    public static List<ChanneDao> getShowChannelNoOrder(Context context, String userId) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            QueryBuilder<ChanneDao, Integer> que = mHelper.getChanneDao().queryBuilder();
            que.where().eq("userId", userId).and().eq("show", "0")
            .and().eq(ChanneDao.CHANNEL_ENTERPRISE, 0)
            .and().eq(ChanneDao.CHANNEL_LANGUAGE, PreferencesManager.getLanguageInfo(context));
            return que.query();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }

        return null;
    }

    /**
     * 获得脏数据的栏目
     */
    public static List<ChanneDao> getDirtyChanne(Context context) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);

            List<ChanneDao> result = mHelper.getChanneDao().queryBuilder()
                    .where().eq("dirty", 1)
                    .query();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }

        return null;
    }

    /**
     * 获得隐藏的栏目
     *
     * @param context
     * @param userId
     * @return
     */
    public static List<ChanneDao> getHideChanne(Context context, String userId) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            QueryBuilder<ChanneDao, Integer> que = mHelper.getChanneDao().queryBuilder();
            que.where().eq("userId", userId).and().eq("show", "1")
            .and().eq(ChanneDao.CHANNEL_ENTERPRISE, 0)
            .and().eq(ChanneDao.CHANNEL_LANGUAGE, LanguageUtil.LANGUAGE_CH);
            return que.orderBy("listorder", true).query();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }

        return null;
    }

    /**
     * 根据id删除栏目
     *
     * @param id
     */
    public static void delChanneById(Context context, int id) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            mHelper.getChanneDao().deleteById(id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }
    }
    
    /**
     * 根据栏目id删除栏目
     *
     * @param id
     */
    public static void delChanneByChannelId(Context context, String id) {
        initContext(context);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            DeleteBuilder<ChanneDao, Integer> delb = mHelper.getChanneDao().deleteBuilder();
            delb.where().eq("channelID", id).prepare();
            delb.delete();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }
    }
    
    /**
     * 删除一个企业用户下的所有企业频道
     * @param con
     * @param userId
     */
    public static void delChannelByEnterpriseId(Context con, String enterpriseId) {
    	initContext(con);
        DatabaseHelper mHelper;
        try {
            mHelper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            DeleteBuilder<ChanneDao, Integer> del = mHelper.getChanneDao().deleteBuilder();
            del.where().eq(ChanneDao.CHANNEL_ENTERPRISE, enterpriseId);
            del.delete();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            OpenHelperManager.releaseHelper();
        }
    }

    /**
     * 根据id更新单个栏目
     *
     * @param channe
     * @return
     */
    public static int updateChanneById(Context context, ChanneDao channe) {
        initContext(context);
        DatabaseHelper helper;
        try {
            channe.setOperatetime(DateUtil.getTime());
            helper = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            Dao<ChanneDao, Integer> dao = helper.getChanneDao();
            List<ChanneDao> que = dao.queryBuilder().where()
            		.eq("userId", channe.getUserId()).and()
            		.eq("channelID", channe.getChannelID()).and()
            		.eq(ChanneDao.CHANNEL_ENTERPRISE, 0).query();
            int row = 0;
            if (!ListUtil.isEmpty(que)) {
                for (ChanneDao item : que) {
                    item.setShow(channe.getShow());
                    item.setDirty(channe.getDirty());
                    item.setListorder(channe.getListorder());
                    item.setOperatetime(channe.getOperatetime());
                    item.setChannelCreateType(channe.getChannelCreateType());
                    if(item.getChannelLanguage() == null 
                			|| item.getChannelLanguage().length() < 1) {
                    	item.setChannelLanguage(LanguageUtil.LANGUAGE_CH);
                	}
                    int r = dao.update(item);
                    row += r;
                }
            } else {
            	LogUtil.e(TAG, " channel lang---------------" + channe.getChannelLanguage());
            	if(channe.getChannelLanguage() == null 
            			|| channe.getChannelLanguage().length() < 1) {
            		channe.setChannelLanguage(LanguageUtil.LANGUAGE_CH);
            	}
            	dao.create(channe);
            }
            return row;
        } catch (Exception e) {
            LogUtil.w(TAG, e);
        } finally {
            OpenHelperManager.releaseHelper();
        }
        return -1;
    }
    
    /**
     * 存储频道
     * @param context
     * @param data
     */
    public static void saveChannel(Context context, ChanneDao data) {
    	initContext(context);
        DatabaseHelper dh;
        try {
            dh = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            ChanneDao result = dh.getChanneDao().queryForId(Integer.valueOf(data.getChannelID()));
            if (result == null) {
                dh.getChanneDao().create(data);
            }
        } catch (Exception e) {
        	LogUtil.e(TAG, e);
        } finally {
            OpenHelperManager.releaseHelper();
        }
    }
    
    /**
     * 根据企业id获取其下的频道列表
     * @param con
     * @param enterpriseId
     * @return
     */
    public static List<ChanneDao> getChannelsByEnterpriseId(Context con, String enterpriseId) {
    	initContext(con);
        DatabaseHelper dh;
        List<ChanneDao> result = null;
        try {
            dh = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            result = dh.getChanneDao().queryBuilder().orderBy("listorder", true).where()
            		.eq(ChanneDao.CHANNEL_ENTERPRISE, enterpriseId).query();
        } catch (Exception e) {
        	LogUtil.e(TAG, e);
        } finally {
            OpenHelperManager.releaseHelper();
        }
        
        return result;
    }
    
    /**
     * 清除取消订阅的关键字频道
     * @param con
     */
    public static void clearUselessKeywordChannel(Context con) {
    	initContext(con);
        DatabaseHelper dh;
        try {
            dh = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            DeleteBuilder<ChanneDao, Integer> deleteBuilder = dh.getChanneDao().deleteBuilder();
            deleteBuilder.where().eq(ChanneDao.CHANNEL_TYPE, ChanneDao.CHANNEL_TYPE_KEY_WORDS)
            		.and().eq("show", 1).prepare();
            deleteBuilder.delete();
        } catch (Exception e) {
        	LogUtil.e(TAG, e);
        } finally {
            OpenHelperManager.releaseHelper();
        }
    }
}
