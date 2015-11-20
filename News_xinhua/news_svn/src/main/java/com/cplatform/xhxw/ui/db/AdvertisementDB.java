package com.cplatform.xhxw.ui.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.cplatform.xhxw.ui.db.dao.AdvertisementDao;
import com.cplatform.xhxw.ui.ui.advertisement.AdvertiseUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import android.content.Context;

public class AdvertisementDB {
	private static final String TAG = AdvertisementDB.class.getSimpleName();
    private static Context mAppContext;

    private static void initContext(Context context) {
        if (mAppContext == null)
            mAppContext = context.getApplicationContext();
    }
    
    /**
     * 获取特定显示位置的所有有效广告
     * @param showPosition 1为loading页；2为焦点区； 3为角标区；4为详情页
     * @return
     */
    public static List<AdvertisementDao> getAdvertisementByShowPosition(Context context, int showPosition) {
    	List<AdvertisementDao> result = new ArrayList<AdvertisementDao>();
    	initContext(context);
    	DatabaseHelper dh;
    	try {
    		dh = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
			result = dh.getAdvertisementDao().queryBuilder()
					.orderBy(AdvertisementDao.ADVER_COLUMN_ADVERRANK, true)
					.where().eq(AdvertisementDao.ADVER_COLUMN_ADVERSHOWPOSITION, showPosition).and()
					.between(AdvertisementDao.ADVER_COLUMN_ADVERENDTIME, 
							System.currentTimeMillis() / 1000, Long.MAX_VALUE)
					.query();
		} catch (SQLException e) {
			LogUtil.e(TAG, e);
		} finally {
			OpenHelperManager.releaseHelper();
		}
    			
    	return result;
    }
    
    /**
     * 获取特定id的广告缓存信息
     * @param context
     * @param adverId
     * @return
     */
    public static AdvertisementDao getAdvertisementById(Context context, String adverId) {
    	AdvertisementDao result = null;
    	initContext(context);
    	DatabaseHelper dh;
    	try {
			dh = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
			List<AdvertisementDao> tmp = dh.getAdvertisementDao()
					.queryBuilder().where()
					.eq(AdvertisementDao.ADVER_COLUMN_ADVERID, adverId).and()
					.between(AdvertisementDao.ADVER_COLUMN_ADVERENDTIME, 1000, Long.MAX_VALUE)
					.query();
			if(tmp != null && tmp.size() > 0) {
				result = tmp.get(0);
			}
		} catch (Exception e) {
			LogUtil.e(TAG, e);
		} finally {
			OpenHelperManager.releaseHelper();
		}
    	
    	return result;
    }
    
    /**
     * 缓存广告到本地
     * @param context
     * @param data
     */
    public static void saveAdvertisements(Context context, AdvertisementDao data) {
    	initContext(context);
        DatabaseHelper dh;
        try {
            dh = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            AdvertisementDao result = dh.getAdvertisementDao().queryForId(data.getAdverId());
            if (result == null) {
                dh.getAdvertisementDao().create(data);
            } else {
                updateAdvertise(context, data);
            }
        } catch (Exception e) {
        	LogUtil.e(TAG, e);
        } finally {
            OpenHelperManager.releaseHelper();
        }
    }
    
    /**
     * 更新本地缓存的数据
     * @param context
     * @param data
     */
    public static void updateAdvertise(Context context, AdvertisementDao data) {
    	initContext(context);
        DatabaseHelper dh;
        try {
            dh = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            dh.getAdvertisementDao().update(data);
        } catch (Exception e) {
            LogUtil.e(TAG, e);
        } finally {
            OpenHelperManager.releaseHelper();
        }
    }
    
    /**
     * 删除特定id的新闻
     * @param context
     * @param id
     */
    public static void deleteAdverById(Context context, String id) {
    	initContext(context);
        DatabaseHelper dh;
        try {
            dh = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            dh.getAdvertisementDao().deleteById(id);
        } catch (Exception e) {
        	LogUtil.e(TAG, e);
        } finally {
            OpenHelperManager.releaseHelper();
        }
    }
    
    public static void deleteAdverByShowPosition(Context context, int showPos) {
    	List<AdvertisementDao> adversExist = getAdvertisementByShowPosition(context, showPos);
    	for(AdvertisementDao adver : adversExist) {
    		AdvertiseUtil.deleteCachedAdverImg(mAppContext, adver);
    	}
    	
    	initContext(context);
        DatabaseHelper dh;
        try {
            dh = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
            dh.getAdvertisementDao().deleteBuilder().where()
            .eq(AdvertisementDao.ADVER_COLUMN_ADVERSHOWPOSITION, String.valueOf(showPos));
        } catch (Exception e) {
        	LogUtil.e(TAG, e);
        } finally {
            OpenHelperManager.releaseHelper();
        }
    }
    
    /**
     * 设置脏数据
     * @param context
     * @param showPos
     */
    public static void setDirtAdverByShowPosition(Context context, int showPos) {
    	List<AdvertisementDao> adversExist = getAdvertisementByShowPosition(context, showPos);
    	for(AdvertisementDao adver : adversExist) {
    		adver.setAdverTypeId(AdvertisementDao.ADVER_TYPE_DIRT);
    		updateAdvertise(mAppContext, adver);
    	}
    }
    
    /**
     * 删除待删除广告数据
     * @param context
     */
    public static void deleteDirtAdver(Context context) {
    	initContext(context);
    	DatabaseHelper dh;
    	try {
			dh = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
			List<AdvertisementDao> dirts = dh.getAdvertisementDao()
					.queryForEq(AdvertisementDao.ADVER_COLUMN_ADVERTYPEID, AdvertisementDao.ADVER_TYPE_DIRT);
			if(dirts != null) {
				for(AdvertisementDao adver : dirts) {
					AdvertiseUtil.deleteCachedAdverImg(mAppContext, adver);
					dh.getAdvertisementDao().delete(adver);
				}
			}
		} catch (Exception e) {
			LogUtil.e(TAG, e);
		} finally {
			OpenHelperManager.releaseHelper();
		}
    }
    
    /**
     * 判断广告是否已在本地缓存
     * @param context
     * @param adver
     * @return
     */
    public static boolean isAdverExist(Context context, AdvertisementDao adver) {
    	initContext(context);
        DatabaseHelper dh;
        try {
        	dh = OpenHelperManager.getHelper(mAppContext, DatabaseHelper.class);
        	List<AdvertisementDao> result = dh.getAdvertisementDao()
        			.queryBuilder().where()
        			.eq(AdvertisementDao.ADVER_COLUMN_ADVERIMG, adver.getAdverImg()).and()
        			.eq(AdvertisementDao.ADVER_COLUMN_ADVERURL, adver.getAdverUrl()).and()
        			.eq(AdvertisementDao.ADVER_COLUMN_ADVERID, adver.getAdverId()).and()
        			.eq(AdvertisementDao.ADVER_COLUMN_ADVERSHOWPOSITION, adver.getAdverShowPosition())
        			.query();
        	if(result != null && result.size() > 0) {
        		return true;
        	} else {
        		return false;
        	}
		} catch (Exception e) {
			LogUtil.w(TAG, e);
		}
        return false;
    }
}
