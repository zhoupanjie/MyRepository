package com.cplatform.xhxw.ui;

import java.io.File;

import android.content.Context;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.cplatform.xhxw.ui.http.HttpClientConfig;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.model.PushInfoTmp;
import com.cplatform.xhxw.ui.model.UserInfo;
import com.cplatform.xhxw.ui.ui.main.forelanguage.utils.LanguageUtil;
import com.cplatform.xhxw.ui.util.AppInfoUtil;
import com.cplatform.xhxw.ui.util.DensityUtil;
import com.cplatform.xhxw.ui.util.FileUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.google.gson.Gson;
public class Constants {

	private static final String TAG = Constants.class.getSimpleName();

	private static long sOldNewsLodTime; // 上次数据加载时间
	public static PushInfoTmp sPushInfoTmp; // push平台临时信息
	public static int DOWNINGCOUNT=0;
	/**
	 * manifest channel key
	 */
	public static final String APP_CHANNEL_KEY = "UMENG_CHANNEL";

	/**
	 * 推荐栏目id
	 */
	public static final String RECOM_COLUMU = "111";
	/**
	 * 数据库中默认用户的id
	 */
	public static final String DB_DEFAULT_USER_ID = "-1";

	public static final int GUIDE_VERSION = 1;

	public static final int BASE_SCREEN_WIDTH = 640;
	public static final int BASE_SCREEN_HEIGHT = 960;

	/**
	 * 新闻推送相关常量
	 */
	// public static final String PUSH_CONFIGINFO_URL =
	// "http://apitest.xw.feedss.com/push/platformlocate";// 获取push host和端口信息
	public static final String PUSH_CONFIGINFO_URL = HttpClientConfig.BASE_URL
			+ "/push/platformlocate";
	// public static final String MI_CONFIGINFO_URL =
	// "http://apitest.xw.feedss.com/push/xiaomiapp";// 获取小米相关信息
	public static final String MI_CONFIGINFO_URL = HttpClientConfig.BASE_URL
			+ "/push/xiaomiapp";// 获取小米相关信息

	/**
	 * 普通用户类型
	 */
	public static final int USER_TYPE_NORMAL = 0;
	/**
	 * 企业用户类型
	 */
	public static final int USER_TYPE_ENTERPRISE = 1;

	/**
	 * 显示模式(白天)
	 */
	public static final int DISPLAY_MODEL_DAY = 0;
	/**
	 * 显示模式(夜晚)
	 */
	public static final int DISPLAY_MODEL_NIGHT = 1;
	/**
	 * 评论图片显示数字的下边界值
	 */
	public static final int COMMENT_SHOW_NUMBER_LOWER_BOUND = 20;
	/**
	 * 评论图片显示数字和加号的下边界值
	 */
	public static final int COMMENT_SHOW_NUMBER_PLUS_LOWER_BOUND = 100;

	/**
	 * 新闻列表显示评论数的下边界值
	 */
	public static final int NEWS_LIST_SHOW_COMMENT_NUMBER_LOWER_BOUND = 20;

	/**
	 * 列表每次加载数量
	 */
	public static final int LIST_LOAD_COUNT = 20;

	public static int screenWidth, screenHeight;
	public static float screenDensity;

	/**
	 * 判断是否显示引导
	 * 
	 * @return true为显示，否则为隐藏
	 */
	public static boolean isShowGuide() {
		return App.getPreferenceManager().getGuideVersion() != GUIDE_VERSION;
	}

	public static void updateGuideVersion(String version_name) {
		App.getPreferenceManager().setGuideVersion(GUIDE_VERSION, version_name);
	}

	/**
	 * 设备id
	 */
	public static String getDevId() {
		return App.getPreferenceManager().getDevId();
	}

	// 用户信息
	public static UserInfo userInfo;

	/**
	 * http header
	 * 
	 * @return
	 */
	public static String getUid() {
		if (hasLogin()) {
			return userInfo.getUserId();
		}
		return getDevId();
	}

	public static String getDbUserId() {
		if (hasLogin()) {
			return userInfo.getUserId();
		}
		return DB_DEFAULT_USER_ID;
	}

	public static String getBindmobile() {
		if (hasLogin()) {
			return userInfo.getBindmobile();
		}
		return "";
	}

	public static String getEnterpriseId() {
		if (hasLogin()) {
			if (userInfo.getEnterprise() != null) {
				return userInfo.getEnterprise().getId();
			}
		}
		return null;
	}

	/**
	 * 判断是否登录
	 * 
	 * @return true登录 | false未登录
	 */
	public static boolean hasLogin() {
		if (userInfo == null) {
			initUserInfo();
		}
		return (null != userInfo && userInfo.isToken());
	}

	public static boolean hasEnterpriseAccountLoggedIn() {
		return (hasLogin() && userInfo.getType() != null && userInfo.getType()
				.equals("1"));
	}

	/**
	 * 推荐新闻
	 */
	public static class HomeSliderSize {
		public static int width = 16;
		public static int height = 9;
		public static int title_height = 0;
	}

	/**
	 * 缩略图
	 */
	public static class HomeThumbnailSize {
		public static int width = 2;
		public static int height = 1;
	}

	/**
	 * 专题四图比例
	 */
	public static class SpecialTopicPicItemSize {
		public static final int width = 4;
		public static final int height = 3;
	}

	/**
	 * 专题四视频比例
	 */
	public static class SpecialTopicVideoItemSize {
		public static final int width = 4;
		public static final int height = 3;
	}

	public static class RecommendSmallScale {
		public static float scale = 0.75f;
	}

	public static class RecommendBigScale {
		public static float scale = 0.5f;
	}

	/**
	 * 初始化数据
	 * 
	 * @param context
	 */
	public static void initValue(Context context) {
		initScreenSize(context);
		initUserInfo();
		Constants.AppInfo.init(context);
	}

	/**
	 * 退出登录
	 */
	public static void logout() {
		FileUtil.writeFile(App.getCurrentApp(), FileName.USER_INFO, "");
		userInfo = null;
		AppPushManager.stopWork(App.CONTEXT);
		AppPushManager.startWork(App.CONTEXT);
		initUserInfo();
		PreferencesManager mg = App.getPreferenceManager();
		mg.logout();
	}

	/**
	 * 保存用户数据
	 */
	public static void saveUserInfo() {
		try {
			FileUtil.writeFile(App.getCurrentApp(),
					Constants.FileName.USER_INFO, new Gson().toJson(userInfo));
		} catch (Exception e) {
		}
		Constants.initUserInfo();
	}

	/**
	 * 新闻详情页面内容字体大小
	 * 
	 * @return @param textSize
	 *         {@link com.cplatform.xhxw.ui.http.responseType.NewsDetTextSize}
	 *         常量
	 */
	public static void setNewsDetTextSize(int textSize) {
		App.getPreferenceManager().setNewsDetTextSize(textSize);
	}

	/**
	 * 新闻详情页面内容字体大小
	 * 
	 * @return @param textSize
	 *         {@link com.cplatform.xhxw.ui.http.responseType.NewsDetTextSize}
	 *         常量
	 */
	public static int getNewsDetTextSize() {
		return App.getPreferenceManager().getNewsDetTextSize();
	}

	public static class AppInfo {
		// 应用信息详情，在app中初始化

		private static String _versionName; // 当前版本名
		private static long _vsersionCode; // 当前版本号
		private static String _platform; // 发布平台

		/**
		 * 获得版本名
		 */
		public static String getVersionName() {
			return _versionName;
		}

		/**
		 * 获得版本号
		 */
		public static long getVsersionCode() {
			return _vsersionCode;
		}

		/**
		 * 获得发布平台
		 */
		public static String getPlatform() {
			return _platform;
		}

		private static void init(Context context) {
			_versionName = AppInfoUtil.getVsersionName(context);
			_vsersionCode = AppInfoUtil.getVersionCode(context);
			try {
				_platform = AppInfoUtil.getMetaDate(context, APP_CHANNEL_KEY);
				LogUtil.d(TAG, "app 渠道:" + _platform);
			} catch (Exception e) {
				LogUtil.e(TAG, "Manifeast文件获得应用渠道失败！！！ error:" + e.toString());
				e.printStackTrace();
			}
		}
	}

	private static void initUserInfo() {
		String userInfoText = FileUtil.readFile(App.getCurrentApp(),
				FileName.USER_INFO);
		if (!TextUtils.isEmpty(userInfoText)) {
			try {
				ResponseUtil.checkResponse(userInfoText);
				userInfo = new Gson().fromJson(userInfoText, UserInfo.class);
			} catch (Exception e) {
				userInfo = null;
			}
		} else {
			userInfo = null;
		}
	}

	/**
	 * 初始化屏幕大小
	 */
	private static void initScreenSize(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		screenDensity = dm.density;
		screenWidth = dm.widthPixels <= dm.heightPixels ? dm.widthPixels
				: dm.heightPixels;
		screenHeight = dm.heightPixels >= dm.widthPixels ? dm.heightPixels
				: dm.widthPixels;

		if (screenWidth == 0) {
			screenWidth = BASE_SCREEN_WIDTH;
		}
		if (screenHeight == 0) {
			screenHeight = BASE_SCREEN_HEIGHT;
		}

		LogUtil.d(TAG, "screenWidth = " + screenWidth);
		LogUtil.d(TAG, "screenHeight = " + screenHeight);
	}

	/**
	 * 程序目录定义
	 */
	public static class Directorys {
		/**
		 * SD卡根目录
		 */
		public static String SDCARD = Environment.getExternalStorageDirectory()
				.toString();
		/**
		 * 程序根目录
		 */
		public static final String ROOT = SDCARD
				+ File.separator
				+ App.getCurrentApp().getString(
						R.string.app_name_directory_name) + File.separator;
		/**
		 * 临时位置
		 */
		public static final String TEMP = ROOT + "temp" + File.separator;
		/**
		 * 缓存位置
		 */
		public static final String CACHE = ROOT + "cache" + File.separator;

		/**
		 * 下载位置
		 */
		public static final String DOWNLOAD = ROOT + "download"
				+ File.separator;
		/**
		 * 拍摄存储
		 */
		public static final String TAKE_PHOTO = ROOT + "takephoto"
				+ File.separator;

		/**
		 * 视频存储
		 */
		public static final String VIDEO = ROOT + "videos" + File.separator;
	}

	static {
		makeDirectoriesIngoreMedia();
	}

	/**
	 * 创建SD卡目录
	 */
	public static void makeDirectoriesIngoreMedia() {
		makeDirectoryIngoreMedia(Directorys.TEMP);
		makeDirectoryIngoreMedia(Directorys.TAKE_PHOTO);
		makeDirectoryIngoreMedia(Directorys.CACHE);
		makeDirectoryIngoreMedia(Directorys.VIDEO);
		makeDirectoryIngoreMedia(Directorys.DOWNLOAD);
	}

	/**
	 * 创建文件夹
	 */
	public static void makeDirectoryIngoreMedia(String dirName) {
		try {
			// new File(dirName + File.separator + ".nomedia").createNewFile();
			new File(dirName).mkdirs();
		} catch (Exception e) {
			LogUtil.w(e);
		}
	}

	/**
	 * 程序文件名
	 */
	public static class FileName {
		public static final String USER_INFO = "user_info";
	}

	/**
	 * 返回上次新闻数据加载时间
	 */
	public static long getsOldNewsLoadTime() {
		return sOldNewsLodTime;
	}

	/**
	 * 设置新闻数据加载时间
	 */
	public static void setsOldNewsLoadTime(long sOldNewsLodTime) {
		Constants.sOldNewsLodTime = sOldNewsLodTime;
	}

	public static int titleViewWidth = DensityUtil.getNewsTitleWid(App.CONTEXT);

	/**
	 * 新闻列表摘要字号
	 */
	public static final float LIST_SUMMARY_TEXT_SIZE = 12.0f;

	public static TelephonyManager phoneStatus = (TelephonyManager) App.CONTEXT
			.getSystemService(Context.TELEPHONY_SERVICE);

	private static final String HTML_CH = "file:///android_asset/index.html";

	private static final String HTML_EN = "file:///android_asset/index.html";

	/**
	 * 根据中英文版,选择网页模板
	 */
	public static String getHtmlPath() {
		if (PreferencesManager.getLanguageInfo(App.CONTEXT).equals(
				LanguageUtil.LANGUAGE_EN)) {
			return HTML_EN;
		} else {
			return HTML_CH;
		}
	}

	/**
	 * 汽车频道推荐功能
	 */
	public static String[] aTitle = { "报价大全", "新车图库", "智能选车", "热车推荐" };
	public static int[] aImg = { R.drawable.ic_baojiadaquan,
			R.drawable.ic_xinchetuku, R.drawable.ic_zhinengxuanche,
			R.drawable.ic_rechetuijian };
	public static String[] aUrl = {
			"http://auto.news18a.com/m/price/index/20140827/index.php?from=xinhuawang&platform=app",
			"http://auto.news18a.com/m/photo/?from=xinhuawang&platform=app",
			"http://auto.news18a.com/m/price/product/20140827/index.php?from=xinhuawang&platform=app",
			"http://auto.news18a.com/m/price/product/20140827/index.php?price=0&level=8&series=0&from=xinhuawang&platform=app" };
}
