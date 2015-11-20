package com.cplatform.xhxw.ui.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cplatform.xhxw.ui.db.dao.AdvertisementDao;
import com.cplatform.xhxw.ui.db.dao.ChanneDao;
import com.cplatform.xhxw.ui.db.dao.CollectDao;
import com.cplatform.xhxw.ui.db.dao.CompanyMessageDao;
import com.cplatform.xhxw.ui.db.dao.ContactsDao;
import com.cplatform.xhxw.ui.db.dao.DownloadDao;
import com.cplatform.xhxw.ui.db.dao.FriendsMessageDao;
import com.cplatform.xhxw.ui.db.dao.NewFriendsDao;
import com.cplatform.xhxw.ui.db.dao.NewsCashDao;
import com.cplatform.xhxw.ui.db.dao.NewsDetailCashDao;
import com.cplatform.xhxw.ui.db.dao.ReadNewsDao;
import com.cplatform.xhxw.ui.db.dao.SMessageChatDao;
import com.cplatform.xhxw.ui.db.dao.SMessageDao;
import com.cplatform.xhxw.ui.db.dao.SearchDao;
import com.cplatform.xhxw.ui.ui.main.cms.game.GameCashDao;
import com.cplatform.xhxw.ui.ui.main.cms.game.GameDownInfoDao;
import com.cplatform.xhxw.ui.ui.main.cms.game.ReadGameDao;
import com.cplatform.xhxw.ui.ui.main.forelanguage.utils.LanguageUtil;
import com.cplatform.xhxw.ui.util.ChanneXmlResolve;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();
	private static final String DATABASE_NAME = "xuanwen.db";
	private static final int DATABASE_VERSION = 6;
	private Dao<ChanneDao, Integer> channeDao = null;
	private Dao<CollectDao, String> imagesDao = null;
    private Dao<ReadNewsDao, String> readNewsDao = null;
	private Dao<ReadGameDao, String> readGameDao = null;
    private Dao<SearchDao, String> searchDao = null;
    private Dao<DownloadDao,Long> downloadDao = null;
    private Dao<NewsCashDao, String> newsCashDao = null;
    private Dao<NewsDetailCashDao, String> newsDetailCashDaos = null;
    private Dao<AdvertisementDao, String> advertisementDao = null;
    private Context mContext;
	
    private Dao<GameCashDao, String> gameCashDao = null;
    private Dao<GameDownInfoDao, String> gameDownInfoDao = null;
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
	}


	@Override
	public void close() {
		super.close();
        channeDao = null;
		imagesDao = null;
        readNewsDao = null;
        readGameDao = null;
        searchDao = null;
        downloadDao = null;
        newsCashDao = null;
        gameCashDao = null;
        gameDownInfoDao=null;
        advertisementDao = null;
	}

	@Override
	public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
		initTables();
	}
	
	private void initTables() {
		Log.e("DatabaseHelper", "initTables1");
		try {
			TableUtils.createTableIfNotExists(connectionSource, ChanneDao.class);
			TableUtils.createTableIfNotExists(connectionSource, CollectDao.class);
            TableUtils.createTableIfNotExists(connectionSource, ReadNewsDao.class);
            TableUtils.createTableIfNotExists(connectionSource, SearchDao.class);
            TableUtils.createTableIfNotExists(connectionSource, DownloadDao.class);
            TableUtils.createTableIfNotExists(connectionSource, NewsCashDao.class);
            TableUtils.createTableIfNotExists(connectionSource, NewsDetailCashDao.class);
            TableUtils.createTableIfNotExists(connectionSource, AdvertisementDao.class);
            TableUtils.createTableIfNotExists(connectionSource, ContactsDao.class);
            TableUtils.createTableIfNotExists(connectionSource, SMessageDao.class);
            TableUtils.createTableIfNotExists(connectionSource, SMessageChatDao.class);
            TableUtils.createTableIfNotExists(connectionSource, NewFriendsDao.class);
            TableUtils.createTableIfNotExists(connectionSource, FriendsMessageDao.class);
            TableUtils.createTableIfNotExists(connectionSource, CompanyMessageDao.class);
            TableUtils.createTableIfNotExists(connectionSource, GameCashDao.class);
            TableUtils.createTableIfNotExists(connectionSource, GameDownInfoDao.class);
            initChanne();
		} catch (SQLException e) {
			Log.e("DatabaseHelper", "initTables2");
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource arg1, int arg2,
			int arg3) {
		Log.e("DatabaseHelper", "onUpgrade1");
		switch (arg2){
		case 1: // 4.0.2
			try {
				TableUtils.createTable(connectionSource, AdvertisementDao.class);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		case 2: // 4.0.5
			//channe 表中加入企业id字段
			try {
				db.execSQL("ALTER TABLE channe ADD " + ChanneDao.CHANNEL_ENTERPRISE + " TEXT");
				db.execSQL("UPDATE channe SET " + ChanneDao.CHANNEL_ENTERPRISE + "=0");
				db.execSQL("ALTER TABLE channe ADD " + ChanneDao.ENTERPRISE_CHANNEL_TYPE + " INTEGER");
			} catch (Exception e) {
				e.printStackTrace();
			}
        case 3: // 内测版本 新增通讯录
            try {
                TableUtils.createTableIfNotExists(connectionSource, ContactsDao.class);
            
                TableUtils.createTableIfNotExists(connectionSource, SMessageDao.class);
                TableUtils.createTableIfNotExists(connectionSource, SMessageChatDao.class);
            
                TableUtils.createTableIfNotExists(connectionSource, NewFriendsDao.class);
                TableUtils.createTableIfNotExists(connectionSource, FriendsMessageDao.class);
                TableUtils.createTableIfNotExists(connectionSource, CompanyMessageDao.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            
        case 4:
            try {
                db.execSQL(" ALTER TABLE " + SMessageDao.TABLE_NAME + " ADD " + SMessageDao.NICK_NAME + " TEXT ");
                db.execSQL(" ALTER TABLE " + SMessageDao.TABLE_NAME + " ADD " + SMessageDao.COMMENT + " TEXT ");
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        case 5: //4.1.0 channel表新增channelLanguage字段，标识频道语言类型
        	try {
        		db.execSQL("ALTER TABLE channe ADD " + ChanneDao.CHANNEL_LANGUAGE + " TEXT");
        		db.execSQL("ALTER TABLE channe ADD " + ChanneDao.CHANNEL_TYPE + " INTEGER");
				db.execSQL("UPDATE channe SET " + ChanneDao.CHANNEL_LANGUAGE + "=" + LanguageUtil.LANGUAGE_CH);
				db.execSQL("UPDATE channe SET " + ChanneDao.CHANNEL_TYPE + "=" + ChanneDao.CHANNEL_TYPE_RECOMMENDED);
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
		Log.e("DatabaseHelper", "onUpgrade2");
    }
	
	@Override
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.e("DatabaseHelper", "onDowngrade1");
		dropAllTables();
		initTables();
		Log.e("DatabaseHelper", "onDowngrade1");
		super.onDowngrade(db, oldVersion, newVersion);
	}
	
	private void dropAllTables() {
		Log.e("DatabaseHelper", "dropAllTables1");
		try {
			TableUtils.dropTable(connectionSource, ChanneDao.class, true);
			TableUtils.dropTable(connectionSource, ChanneDao.class, true);
			TableUtils.dropTable(connectionSource, CollectDao.class, true);
	        TableUtils.dropTable(connectionSource, ReadNewsDao.class, true);
	        TableUtils.dropTable(connectionSource, SearchDao.class, true);
	        TableUtils.dropTable(connectionSource, DownloadDao.class, true);
	        TableUtils.dropTable(connectionSource, NewsCashDao.class, true);
	        TableUtils.dropTable(connectionSource, NewsDetailCashDao.class, true);
	        TableUtils.dropTable(connectionSource, AdvertisementDao.class, true);
	        TableUtils.dropTable(connectionSource, ContactsDao.class, true);
	        TableUtils.dropTable(connectionSource, SMessageDao.class, true);
	        TableUtils.dropTable(connectionSource, SMessageChatDao.class, true);
	        TableUtils.dropTable(connectionSource, NewFriendsDao.class, true);
	        TableUtils.dropTable(connectionSource, FriendsMessageDao.class, true);
	        TableUtils.dropTable(connectionSource, CompanyMessageDao.class, true);
	        TableUtils.dropTable(connectionSource, GameCashDao.class, true);
	        TableUtils.dropTable(connectionSource, GameDownInfoDao.class, true);
		} catch (SQLException e) {
			Log.e("DatabaseHelper", "dropAllTables2");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    /**
     * 栏目
     */
	public Dao<ChanneDao, Integer> getChanneDao() throws SQLException {
		if (channeDao == null) {
            channeDao = getDao(ChanneDao.class);
		}
		return channeDao;
	}

    /**
     * 收藏
     */
	public Dao<CollectDao, String> getCollectDao() throws SQLException {
		if (imagesDao == null) {
			imagesDao = getDao(CollectDao.class);
		}
		return imagesDao;
	}

    /**
     * 已读新闻
     */
    public Dao<ReadNewsDao, String> getReadNewsDao() throws SQLException {
        if (readNewsDao == null) {
            readNewsDao = getDao(ReadNewsDao.class);
        }
        return readNewsDao;
    }
    /**
     * 已读游戏
     */
    public Dao<ReadGameDao, String> getReadGameDao() throws SQLException {
        if (readGameDao == null) {
        	try{
        		readGameDao = getDao(ReadGameDao.class);
        	}catch(Exception e){
        		readGameDao=null;
        	}
        	
        }
        return readGameDao;
    }
    /**
     * 搜索历史
     */
    public Dao<SearchDao, String> getSearchDao() throws SQLException {
        if (searchDao == null) {
            searchDao = getDao(SearchDao.class);
        }
        return searchDao;
    }

    /**
     * 下载
     */
    public Dao<DownloadDao, Long> getDownloadDao() throws SQLException {
        if (downloadDao == null) {
            downloadDao = getDao(DownloadDao.class);
        }
        return downloadDao;
    }

    /**
     * 栏目缓存
     */
    public Dao<NewsCashDao, String> getNewsCashDao() throws SQLException {
        if (newsCashDao == null) {
            newsCashDao = getDao(NewsCashDao.class);
        }
        return newsCashDao;
    }
    /**
     * 
     * @Name getGameCashDao 
     * @Description TODO 下载游戏缓存
     * @return
     * @throws SQLException 
     * @return Dao<GameCashDao,String>
     * @Author zxe
     * @Date 2015年6月29日 下午2:10:14
    *
     */
    public Dao<GameCashDao, String> getGameCashDao() throws SQLException {
        if (gameCashDao == null) {
        	gameCashDao = getDao(GameCashDao.class);
        }
        return gameCashDao;
    }
    public Dao<GameDownInfoDao, String> getGameDownInfoDao() throws SQLException {
        if (gameDownInfoDao == null) {
        	gameDownInfoDao = getDao(GameDownInfoDao.class);
        }
        return gameDownInfoDao;
    }
    /**
     * 获得栏目缓存
     */
    public Dao<NewsDetailCashDao, String> getNewsDetailCashDao() throws SQLException {
        if (newsDetailCashDaos == null) {
            newsDetailCashDaos = getDao(NewsDetailCashDao.class);
        }
        return newsDetailCashDaos;
    }
    
    public Dao<AdvertisementDao, String> getAdvertisementDao() throws SQLException {
    	if (advertisementDao == null) {
    		advertisementDao = getDao(AdvertisementDao.class);
    	}
    	return advertisementDao;
    }

    /**
     * 初始化当前栏目
     */
    private void initChanne() {
    	Log.e("DatabaseHelper", "initChanne");
        try {
            InputStream is = mContext.getAssets().open("channel_list.xml");
            List<ChanneDao> list = ChanneXmlResolve.doParse(is);
            for(ChanneDao item : list) {
            	item.setChannelLanguage(LanguageUtil.LANGUAGE_CH);
                getChanneDao().create(item);
            }
        } catch (IOException e) {
            LogUtil.e(TAG, e);
            Log.e("DatabaseHelper", "initChanne1");
            e.printStackTrace();
        } catch (SQLException e) {
        	Log.e("DatabaseHelper", "initChanne2");
            e.printStackTrace();
            
        }
    }
}
