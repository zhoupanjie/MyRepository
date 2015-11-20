package com.cplatform.xhxw.ui;

import java.util.ArrayList;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import com.baidu.frontia.FrontiaApplication;
import com.cplatform.xhxw.ui.util.NotificationUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class App extends FrontiaApplication {

	public enum RunScreen {
		NONE, // 其他
		SMESSAGE, // S信主页面
		SCHAT, // S信聊天
		NEW_FRIENDS, // 新的好友
		ADDRESS_BOOK, // 通讯录
		// COMPANY_FRIENDS, // 朋友圈
		// COMPANY_CIRCLE // 公司圈

	}

	private static App app;
	private static PreferencesManager preferenceManager;

	private static Context instance;

	public static Context CONTEXT;

	private String sChatRoomId;
	private RunScreen screen = RunScreen.NONE;

	public static String channel_id = "";
	public static String news_id = "";

	public static App getCurrentApp() {
		return app;
	}

	public static PreferencesManager getPreferenceManager() {
		return preferenceManager;
	}

	public static int getDispalyModel() {
		return preferenceManager.getDispalyModel();
	}

	private ArrayList<Integer> notificationIds;

	@Override
	public void onCreate() {
		super.onCreate();
		notificationIds = new ArrayList<Integer>();
		app = this;
		setContext(this);
		preferenceManager = PreferencesManager.getInstance(this);

		initImageLoader(this);
	}

	private static void setContext(Context mContext) {
		instance = mContext;
		CONTEXT = mContext;
	}

	public static Context getContext() {
		return instance;
	}

	private void initImageLoader(Context context) {

		int memoryCacheSize;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
			int memClass = ((ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE))
					.getMemoryClass();
			memoryCacheSize = (memClass / 10) * 1024 * 1024; // 1/8 of app
																// memory
			// limit
		} else {
			memoryCacheSize = 2 * 1024 * 1024;
		}

		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.memoryCacheSize(memoryCacheSize)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.discCacheFileCount(100)
				.threadPoolSize(5)
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.defaultDisplayImageOptions(
						new DisplayImageOptions.Builder()
								.showStubImage(R.drawable.transparent)
								.showImageForEmptyUri(R.drawable.transparent)
								.showImageOnFail(R.drawable.transparent)
								.cacheInMemory().cacheOnDisc().build()).build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	public RunScreen getScreen() {
		return screen;
	}

	public void setScreen(RunScreen screen) {
		this.screen = screen;
	}

	public String getsChatRoomId() {
		return sChatRoomId;
	}

	public void setsChatRoomId(String sChatRoomId) {
		this.sChatRoomId = sChatRoomId;
	}

	/**
	 * 添加通知id，用户app启动后清除通知栏内容
	 * 
	 * @param id
	 */
	public void addNotificationIds(int id) {
		notificationIds.add(id);
	}

	/**
	 * 清除需要清理的通知
	 */
	public void cleanNotification() {
		for (int id : notificationIds) {
			NotificationUtil.notifyCancel(getApplicationContext(), id);
		}
	}

}
