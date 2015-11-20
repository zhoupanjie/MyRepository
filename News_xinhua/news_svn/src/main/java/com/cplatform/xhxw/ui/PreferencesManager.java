package com.cplatform.xhxw.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.cplatform.xhxw.ui.http.responseType.NewsDetTextSize;
import com.cplatform.xhxw.ui.model.LocationPoint;
import com.cplatform.xhxw.ui.ui.main.forelanguage.utils.LanguageUtil;
import com.cplatform.xhxw.ui.util.Actions;
import com.cplatform.xhxw.ui.util.BasePerferencesManager;

public class PreferencesManager extends BasePerferencesManager {

    private static PreferencesManager instance;

    private static final String GUIDE_VERSION = "guide_version"; // 当前引导版本号
    private static final String DISPLAY_MODEL = "displayModel"; // 显示模式 日间/夜间
    private static final String IS_PUSH_BIND = "isPushBind"; // 推送是否已绑定到服务器
    private static final String IS_OPEN_PUSH = "isPush"; // 是否打开推动
    private static final String DEV_ID = "devId"; // 当前设备id
    private static final String NEWS_DET_TEXT_SIZE = "newsDetTextSize"; // 新闻详情字体大小设置
    private static final String MESSAGE_NEW_COUNT = "messageNewContent"; // 新消息的数量

    private static final String PUSH_DEVICE_TOKEN = "pushDeviceToken"; // push deviceToken

    private static final String VERSION_CODE = "versionCode"; // 项目的版本号
    private static final String OLD_VSERSION_CODE = "oldVsersionCode"; // 上个版本号
    private static final String VERSION_UPDATE_TYPE = "updateType"; // 是否是强制更新类型是1，默认是0
    private static final String UPDATE_TIME = "updateTime"; // 更新时间（即访问接口成功后，保存的时间）
    
    private static final String LAST_POSITION_TIME = "last_position_time";//上一次成功获取位置信息的时间
    private static final String POSITION_LONGITUDE = "position_longitude";//定位经度
    private static final String POSITION_LATITUDE = "position_latitude";//定位纬度
    
    private static final String IS_TOP_ADVER_CACHE_DONE = "is_top_adver_cache_done";//loading页上半部分广告图片是否缓存完成
    private static final String IS_BOTTOM_ADVER_CACHE_DONE = "is_bottom_adver_cache_done";//loading页下半部分广告图片是否缓存完成
    
    private static final String KEYBOARD_HEIGHT = "soft_keyboard_height";//软键盘高度
    
    private static final String CAREER_LIST = "career_list"; //职业选项列表
    private static final String BLOOD_LIST = "blood_list"; //血型选型列表

    private static final String LAST_UPLOAD_CONTACTS_TIME = "last_upload_contacts_time"; //最后一次同步联系人时间

    private static final String NEW_FRIEND_NEW_COUNT ="new_friend_new_count"; // 新的好友数量

    private static final String MSG_NEW_COMPANY_CIRCLE_COUTN = "msg_new_company_circle_count"; // 公司圈新消息数量
    private static final String MSG_NEW_FRIENDS_CIRCLE_COUTN = "msg_new_friends_circle_count"; // 朋友圈新消息数量
    private static final String MSG_NEW_S_MESSAGE = "msg_new_s_message"; // S信数量
    private static final String IS_AGREE_READ_CONTACTS = "is_agree_read_contacts"; // 是否允许同步通讯录
    private static final String NEW_FRIENDS_LIMIT = "new_friends_limit"; // 好友添加未读
    
    private static final String ACTIVITY_DISPLAY_DATE = "activity_display_date";
    
    private static final String LANGUAGE_PREFERENC = "xw_language_preference";
    
    //程序启动时是否显示抽奖提醒
    private static final String IS_TO_SHOW_DRAW_PRIZE = "is_to_show_draw_prize_alert";
    private static final String DRAW_PRIZE_URL = "draw_prize_url";

    private int guideVersion;
    private int displayModel;
    private boolean isPushBind;
    private boolean isOpenPush;
    private String devId;
    private int newsDetTextSize;
    private int messageNewCount;

    private String pushDeviceToken;

    private int versionCode;
    private int updateType;
    private long updateTime;
    private long oldVersionCode;

    private long lastUploadContactsTime;
    private int newFriendNewCount;
    private int msgNewCompanyCircleCount;
    private int msgNewFriendsCircleCount;
    private int msgNewSMessage;
    private int newFriendsLimit;

    private boolean isAgreeReadContacts;

    public static PreferencesManager getInstance(Context context) {
        if (null == instance) instance = new PreferencesManager(context.getApplicationContext());
        return instance;
    }

    private PreferencesManager(Context context) {
        super(context);
        loadData();
    }

    private void loadData() {
        try {
            displayModel = getInt(DISPLAY_MODEL, Constants.DISPLAY_MODEL_DAY);
            isOpenPush = getBoolean(IS_OPEN_PUSH, true);
            isPushBind = getBoolean(IS_PUSH_BIND, false);
            devId = getString(DEV_ID, "");
            newsDetTextSize = getInt(NEWS_DET_TEXT_SIZE, NewsDetTextSize.MIDDLE);

            versionCode = getInt(VERSION_CODE, 0);
            updateType = getInt(VERSION_UPDATE_TYPE, 0);
            updateTime = getLong(UPDATE_TIME, 0);
            guideVersion = getInt(GUIDE_VERSION, 0);
            oldVersionCode = getLong(OLD_VSERSION_CODE, 0);
            messageNewCount = getInt(MESSAGE_NEW_COUNT, 0);

            pushDeviceToken = getString(PUSH_DEVICE_TOKEN, null);
            lastUploadContactsTime = getLong(LAST_UPLOAD_CONTACTS_TIME, 0);
            newFriendNewCount = getInt(NEW_FRIEND_NEW_COUNT, 0);
            msgNewCompanyCircleCount = getInt(MSG_NEW_COMPANY_CIRCLE_COUTN, 0);
            msgNewFriendsCircleCount = getInt(MSG_NEW_FRIENDS_CIRCLE_COUTN, 0);
            msgNewSMessage = getInt(MSG_NEW_S_MESSAGE, 0);
            isAgreeReadContacts = getBoolean(IS_AGREE_READ_CONTACTS, false);

            newFriendsLimit = getInt(NEW_FRIENDS_LIMIT, 0);
        } catch (Exception e){}
    }

    /** 判断是否是夜间模式 */
    public int getDispalyModel() {
        return displayModel;
    }

    /**
     * 设置显示模式
     * @param model Constants.DISPLAY_MODEL_DAY | Constants.DISPLAY_MODEL_NIGHT
     */
    public void setDisplayModel(int model) {
        this.displayModel = model;
        saveInt(DISPLAY_MODEL, model);
    }

//    /**
//     * 判断是否绑定psuh
//     * @return true绑定 | false未绑定
//     */
//    public boolean isPushBind() {
//        return isPushBind;
//    }

//    /**
//     * 设置push是否绑定
//     * @param isPushBind
//     * @return
//     */
//    public void setPushBind(boolean isPushBind) {
//        this.isPushBind = isPushBind;
//        saveBoolean(IS_PUSH_BIND, isPushBind);
//    }

    /**
     * 判断是否开启psuh
     * @return true开启 | false未开启
     */
    public boolean isOpenPush() {
        return isOpenPush;
    }

    /**
     * 设置是否开启push
     * @param isPush
     * @return
     */
    public void setOpenPush(boolean isPush) {
        this.isOpenPush = isPush;
        saveBoolean(IS_OPEN_PUSH, isPush);
    }

    /**
     * 设置设备id
     * @param devId
     */
    public synchronized void setDevId(String devId) {
        if (!TextUtils.isEmpty(this.devId)) {
            return;
        }
        this.devId = devId;
        saveString(DEV_ID, devId);
    }

    /**
     * 获得设备id
     * @return
     */
    public String getDevId() {
        return this.devId;
    }

    /**
     * 设置新闻详情页内容展示字体大小
     * @param textSize {@link com.cplatform.xhxw.ui.http.responseType.NewsDetTextSize} 常量
     */
    public void setNewsDetTextSize(int textSize) {
        this.newsDetTextSize = textSize;
        saveInt(NEWS_DET_TEXT_SIZE, textSize);
        LocalBroadcastManager.getInstance(App.CONTEXT).sendBroadcast(new Intent(Actions.ACTION_NEWSLIST_TEXTSIZE_CHANGE));
    }

    /**
     * 新闻详情页面内容字体大小
     * @return @param textSize {@link com.cplatform.xhxw.ui.http.responseType.NewsDetTextSize} 常量
     */
    public int getNewsDetTextSize() {
        return this.newsDetTextSize;
    }

    /*******获得版本号*****/
	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	/*******获得更新类型*****/
	public int getUpdateType() {
		return updateType;
	}

	public void setUpdateType(int updateType) {
		this.updateType = updateType;
	}

	/*******获得更新时间*****/
	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

    /**
     * 获得当前版本号
     * @return
     */
    public int getGuideVersion() {
        return getInt(GUIDE_VERSION+Constants.AppInfo.getVersionName(), 0);
    }

    /**
     * 设置当前版本号码
     */
    public void setGuideVersion(int version,String version_name) {
        this.guideVersion = version;
        saveInt(GUIDE_VERSION+version_name, version);
    }

    /**
     * 获得上次启动的版本号
     * @return
     */
    public long getOldVersionCode() {
        return oldVersionCode;
    }

    /**
     * 设置上次启动的版本号码
     * @param oldVersionCode
     */
    public void setOldVsersionCode(long oldVersionCode) {
        this.oldVersionCode = oldVersionCode;
        saveLong(OLD_VSERSION_CODE, oldVersionCode);
    }

    /**
     * 设置新消息数量
     */
    public void setMessageNewCount(int count) {
        this.messageNewCount = count;
        saveInt(MESSAGE_NEW_COUNT, count);
    }

    /**
     * 返回新消息的数量
     */
    public int getMessageNewCount() {
        return messageNewCount;
    }

    public synchronized void messageNewCountAdd() {
        this.messageNewCount++;
        saveInt(MESSAGE_NEW_COUNT, messageNewCount);
    }

    /**
     * 减少新消息数量
     */
    public synchronized void messageNewCountMinus() {
        if (messageNewCount <= 0) {
            return;
        }
        this.messageNewCount--;
        saveInt(MESSAGE_NEW_COUNT, messageNewCount);
    }
    
    /**
     * 存储成功获取定位信息的时刻
     * @param con
     * @param time
     */
    public static void savePositionTime(Context con, long time) {
    	getInstance(con).saveLong(LAST_POSITION_TIME, time);
    }
    
    /**
     * 获取上次成功获取定位信息的时刻
     * @param con
     * @return
     */
    public static long getLastPositionTime(Context con) {
    	return getInstance(con).getLong(LAST_POSITION_TIME, -1);
    }
    
    /**
     * 存储测定的软键盘高度
     * @param con
     * @param height
     */
    public static void saveSoftKeyboardHeight(Context con, int height) {
    	getInstance(con).saveInt(KEYBOARD_HEIGHT, height);
    }
    
    /**
     * 获取存储的软键盘高度
     * @param con
     * @return
     */
    public static int getSoftKeyboardHeight(Context con) {
    	return getInstance(con).getInt(KEYBOARD_HEIGHT, 0);
    }
    
    /**
     * 存储定位信息
     * @param con
     * @param point
     */
    public static void savePositionInfo(Context con, LocationPoint point) {
    	getInstance(con).saveFloat(POSITION_LONGITUDE, (float) point.getLongitude());
    	getInstance(con).saveFloat(POSITION_LATITUDE, (float) point.getLatitude());
    }
    
    /**
     * 获取上次成功定位的位置信息
     * @param con
     * @return
     */
    public static LocationPoint getPositionInfo(Context con) {
    	LocationPoint point = new LocationPoint();
    	point.setLongitude(getInstance(con).getFloat(POSITION_LONGITUDE, -1));
    	point.setLatitude(getInstance(con).getFloat(POSITION_LATITUDE, -1));
    	return point;
    }
    
    /**
     * 存储loading页广告图片缓存状态
     * @param con
     * @param isDone
     */
    public static void setAdverCacheStatus(Context con, boolean isDone, boolean isTopAdver) {
    	if(isTopAdver) {
    		getInstance(con).saveBoolean(IS_TOP_ADVER_CACHE_DONE, isDone);
    	} else {
    		getInstance(con).saveBoolean(IS_BOTTOM_ADVER_CACHE_DONE, isDone);
    	}
    }
    
    /**
     * 获取loading页广告图片缓存状态
     * @param con
     * @return
     */
    public static boolean isAdverrCacheDone(Context con, boolean isTopAdver) {
    	if(isTopAdver) {
    		return getInstance(con).getBoolean(IS_TOP_ADVER_CACHE_DONE, false);
    	} else {
    		return getInstance(con).getBoolean(IS_BOTTOM_ADVER_CACHE_DONE, false);
    	}
    }
    
    /**
     * 存储职业选项列表
     * @param con
     * @param list
     */
    public static void saveCareerList(Context con, String list) {
    	getInstance(con).saveString(CAREER_LIST, list);
    }
    
    /**
     * 获取职业选项列表
     * @param con
     * @return
     */
    public static String getCareerList(Context con) {
    	return getInstance(con).getString(CAREER_LIST, null);
    }
    
    /**
     * 存储血型选项列表
     * @param con
     * @param list
     */
    public static void saveBloodList(Context con, String list) {
    	getInstance(con).saveString(BLOOD_LIST, list);
    }
    
    /**
     * 获取血型选项列表
     * @param con
     * @return
     */
    public static String getBloodList(Context con) {
    	return getInstance(con).getString(BLOOD_LIST, null);
    }
    
    /**
     * 存储页面语言类型
     * @param con
     * @param list
     */
    public static void saveLanguageInfo(Context con, String list) {
    	getInstance(con).saveString(LANGUAGE_PREFERENC, list);
    }
    
    /**
     * 获取页面语言类型
     * @param con
     * @return
     */
    public static String getLanguageInfo(Context con) {
    	return getInstance(con).getString(LANGUAGE_PREFERENC, LanguageUtil.LANGUAGE_CH);
    }
    
    /**
     * 存储本次活动页面显示时间
     * @param con
     * @param date
     */
    public static void saveActivityDisplayDate(Context con, String date) {
    	getInstance(con).saveString(ACTIVITY_DISPLAY_DATE, date);
    }
    
    /**
     * 获取上次活动页面显示时间
     * @param con
     * @return
     */
    public static String getActivityDisplayDate(Context con) {
    	return getInstance(con).getString(ACTIVITY_DISPLAY_DATE, null);
    }
    
    /**
     * 保存下次应用前台显示时是否提示抽奖
     * @param isToAlert
     */
    public static void setDrawPrizeAlert(boolean isToAlert) {
    	getInstance(App.CONTEXT).saveBoolean(IS_TO_SHOW_DRAW_PRIZE, isToAlert);
    }
    
    public static boolean isToShowDrawPrizeAlert() {
    	return getInstance(App.CONTEXT).getBoolean(IS_TO_SHOW_DRAW_PRIZE, false);
    }
    
    /**
     * 存储抽奖页面地址
     * @param con
     * @param url
     */
    public static void saveDrawPrizeUrl(Context con, String url) {
    	getInstance(con).saveString(DRAW_PRIZE_URL, url);
    }
    
    /**
     * 获取抽奖页面url
     * @param con
     * @return
     */
    public static String getDrawPrizeUrl(Context con) {
    	return getInstance(con).getString(DRAW_PRIZE_URL, null);
    }

    /**
     * 获得push deviceToken
     * 注：众品SDK集成后添加,此字段可能为null
     *    添加时间: 2014.4.9
     *    versionCode : 24
     *    versionName : 4.0.5
     */
    public String getPushDeviceToken() {
        return pushDeviceToken;
    }

    /**
     * 设置push deviceToken
     * 注：众品SDK集成后添加,此字段可能为null
     *    添加时间: 2014.4.9
     *    versionCode : 24
     *    versionName : 4.0.5
     */
    public void setPushDeviceToken(String pushDeviceToken) {
        this.pushDeviceToken = pushDeviceToken;
        saveString(PUSH_DEVICE_TOKEN, pushDeviceToken);
    }

    /**
     * 最后一次更新时间
     * @return 时间戳（单位秒）
     */
    public long getLastUploadContactsTime() {
        return lastUploadContactsTime;
    }

    /**
     * 设置最后一次更新时间
     * @param lastUploadContactsTime  时间戳（单位秒）
     */
    public void setLastUploadContactsTime(long lastUploadContactsTime) {
        this.lastUploadContactsTime = lastUploadContactsTime;
        saveLong(LAST_UPLOAD_CONTACTS_TIME, lastUploadContactsTime);
    }

    /** 获得新的好友提示数量 */
    public int getNewFriendNewCount() {
        return newFriendNewCount;
    }

    /** 设置新的好友提示数量 */
    public void setNewFriendNewCount(int newFriendNewCount) {
        if (newFriendNewCount < 0) {
            newFriendNewCount = 0;
        }
        this.newFriendNewCount = newFriendNewCount;
        saveInt(NEW_FRIEND_NEW_COUNT, newFriendNewCount);
    }

    /**
     * 公司圈
     */
    public int getMsgNewCompanyCircleCount() {
        return msgNewCompanyCircleCount;
    }

    /**
     * 公司圈
     */
    public void setMsgNewCompanyCircleCount(int msgNewCompanyCircleCount) {
        if (msgNewCompanyCircleCount < 0) {
            msgNewCompanyCircleCount = 0;
        }
        this.msgNewCompanyCircleCount = msgNewCompanyCircleCount;
        saveInt(MSG_NEW_COMPANY_CIRCLE_COUTN, msgNewCompanyCircleCount);
    }

    /**
     * 朋友圈
     */
    public int getMsgNewFriendsCircleCount() {
        return msgNewFriendsCircleCount;
    }

    /**
     * 朋友圈
     */
    public void setMsgNewFriendsCircleCount(int msgNewFriendsCircleCount) {
        if (msgNewFriendsCircleCount < 0) {
            msgNewFriendsCircleCount = 0;
        }
        this.msgNewFriendsCircleCount = msgNewFriendsCircleCount;
        saveInt(MSG_NEW_FRIENDS_CIRCLE_COUTN, msgNewFriendsCircleCount);
    }

    /**
     * S信
     * @return
     */
    public int getMsgNewSMessage() {
        return msgNewSMessage;
    }

    /**
     * S信
     * @param msgNewSMessage
     */
    public void setMsgNewSMessage(int msgNewSMessage) {
        if (msgNewSMessage < 0) {
            msgNewSMessage = 0;
        }
        this.msgNewSMessage = msgNewSMessage;
        saveInt(MSG_NEW_S_MESSAGE, msgNewSMessage);
    }

    public int getNewFriendsLimit() {
        return newFriendsLimit;
    }

    public void setNewFriendsLimit(int newFriendsLimit) {
        this.newFriendsLimit = newFriendsLimit;
        saveInt(NEW_FRIENDS_LIMIT, newFriendsLimit);
    }

    /**
     * 判断是否允许读取通讯录
     * @return true允许，false不允许
     */
    public boolean isAgreeReadContacts() {
        return isAgreeReadContacts;
    }

    /**
     * 设置是否允许读取通讯录
     * @param isAgreeReadContacts true允许，false不允许
     */
    public void setAgreeReadContacts(boolean isAgreeReadContacts) {
        this.isAgreeReadContacts = isAgreeReadContacts;
        saveBoolean(IS_AGREE_READ_CONTACTS, isAgreeReadContacts);
    }

    @Override
    public void logout() {
        setNewFriendNewCount(0);
        setMsgNewCompanyCircleCount(0);
        setMsgNewFriendsCircleCount(0);
        setMsgNewSMessage(0);
    }
}
