package com.cplatform.xhxw.ui.ui.main.cms.game;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class GameUtil {
	public static final boolean ISOPEN_GAMECHANNEL = true;
	// public static boolean ISHAVEGAMECHANNEL=false;
	public static final String CHANNELID_GAME = "100";
	public static final String istest = "";// 是否测试
	// 下载状态：正常，暂停，下载中，已下载，排队中
	public static final int GAME_DOWN_UN = 0x00;// 未下载 显示:下载 game_down
	public static final int GAME_DOWN_WAIT = 0x01;
	public static final int GAME_DOWN_ING = 0x02;// 下载中 暂停 game_down_pause
	public static final int GAME_DOWN_PAUSE = 0x03;// 暂停 继续 game_down_continue
	public static final int GAME_DOWN_CONTINUE = 0x04;// 继续 暂停
	public static final int GAME_DOWN_COMPLETE = 0x05;// 下载完成 安装
	public static final int GAME_DOWN_FAILURE = 0x06;// 下载失败 下载
	public static final int GAME_INSTALL_ING = 0x07;// 安装中 安装中
	public static final int GAME_INSTALL_COMPLETE = 0x08;// 安装成功 打开
	public static final int GAME_INSTALL_FAILURE = 0x09;// 安装失败 下载
	
	public static final int GAME_TAG_DOWN_SUCCESS=1;//成功
	public static final int GAME_TAG_DOWN_FAILURE=2;//失败
	public static final int GAME_TAG_DOWN_ING=3;//下载中
	public static final int GAME_TAG_DOWN_PAUSE=4;// 暂停
	public static final int GAME_TAG_INSTALL_SUCCESS=5;//成功
	public static final int GAME_TAG_INSTALL_FAILURE=6;//失败
	
	public static boolean ISFINSHING = false;
	public static final int THREADCOUNT = 1;// 线程数量
	public static final int BUFFER_SIZE = 1024 * 1024;
	public static boolean ISFIRSTDOWN = true;
	public static int MAXDOWNAMOUNT = 3;
	public static String ACTION_GAME="com.shanshan.GameDownService";
	
	public static final DisplayImageOptions dio = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.def_img_16_9)
			.showImageForEmptyUri(R.drawable.def_img_16_9)
			.showImageOnFail(R.drawable.def_img_16_9).cacheInMemory()
			.cacheOnDisc().build();
	public static final int REQUEST_CODE_INSTALL = 10001;
	private static List<APKInstallData> laid = new ArrayList<APKInstallData>();

	/**
	 * 
	 * @Name isGameChannel
	 * @Description TODO
	 * @param channelID
	 * @return
	 * @return boolean
	 * @Author zxe
	 * @Date 2015年6月29日 下午4:49:09
	 * 
	 */
	public static boolean isGameChannel(String channelID) {
		return ISOPEN_GAMECHANNEL && channelID.equals(CHANNELID_GAME);
	}

	/**
	 * 
	 * @Name installAPK
	 * @Description TODO 安装apk
	 * @param con
	 * @param urlDown
	 *            下载地址
	 * @return void
	 * @Author zxe
	 * @Date 2015年6月28日 下午4:56:42
	 * 
	 */
	public static void installAPK(Activity a, String gameId, String urlDown,
			String packageName, int launchUI) {
		String apkPath = Constants.Directorys.DOWNLOAD
				+ getFileNameByUrl(urlDown);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(apkPath)),
				"application/vnd.android.package-archive");
		APKInstallData aid = new APKInstallData();
		aid.setGameId(gameId);
		aid.setFilePath(apkPath);
		aid.setPackageName(packageName);
		aid.setLaunchUI(launchUI);
		laid.add(aid);
		a.startActivityForResult(intent, REQUEST_CODE_INSTALL);
	}

	public static boolean isNullInstallList() {
		return laid == null || laid.isEmpty();
	}

	public static List<APKInstallData> getInstallListByUI(int launchUI) {
		List<APKInstallData> list = new ArrayList<APKInstallData>();
		for (APKInstallData aid : laid) {
			if (aid.getLaunchUI() == launchUI) {
				list.add(aid);
			}
		}
		return list;
	}

	public static boolean removeApkByGameId(String gameId) {
		int pos = -1;
		List<APKInstallData> list = new ArrayList<APKInstallData>();
		for (APKInstallData aid : laid) {
			if (aid.getGameId() == gameId) {
				pos = laid.indexOf(aid);
				break;
			}
		}
		if (pos == -1) {
			return false;
		} else {
			laid.remove(pos);
			return true;
		}
	}

	/**
	 * 
	 * @Name openAppByByPackageName
	 * @Description TODO 根据包名启动应用
	 * @param con
	 * @param packageName
	 * @return void
	 * @Author zxe
	 * @Date 2015年7月9日 下午12:05:13
	 * 
	 */
	public static void openAppByByPackageName(Context con, String packageName) {
		Intent intent = con.getPackageManager().getLaunchIntentForPackage(
				packageName);
		con.startActivity(intent);
	}

	/**
	 * 
	 * @Name getFileNameByUrl
	 * @Description TODO 根据url获取文件名
	 * @param url
	 * @return
	 * @return String
	 * @Author zxe
	 * @Date 2015年7月8日 上午10:32:43
	 * 
	 */
	public static String getFileNameByUrl(String url) {
		return url.substring(url.lastIndexOf("/") + 1);
	}

	/**
	 * 
	 * @Name isInstallByPackageName
	 * @Description TODO 检测是否安装了指定的apk
	 * @param con
	 * @param packageName
	 * @return
	 * @return boolean
	 * @Author zxe
	 * @Date 2015年7月8日 下午3:12:11
	 * 
	 */
	public static boolean isInstallByPackageName(Context con, String packageName) {
		final PackageManager packageManager = con.getPackageManager();
		// 获取所有已安装程序的包信息
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
		for (int i = 0; i < pinfo.size(); i++) {
			if (pinfo.get(i).packageName.equalsIgnoreCase(packageName)) {
				return true;
			}
		}
		return false;
	}

	public static PackageInfo getPackageInfoByPackageName(Context con,
			String packageName) {
		final PackageManager packageManager = con.getPackageManager();
		// 获取所有已安装程序的包信息
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
		for (int i = 0; i < pinfo.size(); i++) {
			if (pinfo.get(i).packageName.equalsIgnoreCase(packageName))
				return pinfo.get(i);
		}
		return null;
	}

	/**
	 * 
	 * @Name getPackageNameByApk
	 * @Description TODO 根据apk文件获取包名
	 * @param con
	 * @param apkName
	 * @return
	 * @return String
	 * @Author zxe
	 * @Date 2015年7月8日 下午3:10:18
	 * 
	 */
	public String getPackageNameByApk(Context con, String apkName) {
		String apckageName = null;
		PackageManager pm = con.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(
				Constants.Directorys.DOWNLOAD + apkName,
				PackageManager.GET_ACTIVITIES);
		if (info != null) {
			ApplicationInfo appInfo = info.applicationInfo;
			apckageName = appInfo.packageName;
		}
		return apckageName;
	}

	/**
	 * 
	 * @Name getVersionCodeByApk
	 * @Description TODO 获取apkVersionCode
	 * @param con
	 * @param apkName
	 * @return
	 * @return int
	 * @Author zxe
	 * @Date 2015年7月8日 下午3:22:04
	 * 
	 */
	public int getVersionCodeByApk(Context con, String apkName) {
		int versionCode = 0;
		PackageManager pm = con.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(
				Constants.Directorys.DOWNLOAD + apkName,
				PackageManager.GET_ACTIVITIES);
		if (info != null) {
			versionCode = info.versionCode;
		}
		return versionCode;
	}

	/**
	 * 
	 * @Name getVersionNameByApk
	 * @Description TODO 获取应用版本名称
	 * @param con
	 * @param apkName
	 * @return
	 * @return String
	 * @Author zxe
	 * @Date 2015年7月8日 下午3:25:39
	 * 
	 */
	public String getVersionNameByApk(Context con, String apkName) {
		String versionName = null;
		PackageManager pm = con.getPackageManager();
		PackageInfo info = pm.getPackageArchiveInfo(
				Constants.Directorys.DOWNLOAD + apkName,
				PackageManager.GET_ACTIVITIES);
		if (info != null) {
			versionName = info.versionName;
		}
		return versionName;
	}

	/**
	 * 
	 * @Name runAppByPackageName
	 * @Description TODO 根据包名启动应用
	 * @param con
	 * @param packageName
	 * @return void
	 * @Author zxe
	 * @Date 2015年7月8日 下午3:39:22
	 * 
	 */
	public void runAppByPackageName(Context con, String packageName) {
		PackageInfo pi;
		try {
			pi = con.getPackageManager().getPackageInfo(packageName, 0);
			Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
			resolveIntent.setPackage(pi.packageName);
			PackageManager pManager = con.getPackageManager();
			List apps = pManager.queryIntentActivities(resolveIntent, 0);

			ResolveInfo ri = (ResolveInfo) apps.iterator().next();
			if (ri != null) {
				packageName = ri.activityInfo.packageName;
				String className = ri.activityInfo.name;
				Intent intent = new Intent(Intent.ACTION_MAIN);
				ComponentName cn = new ComponentName(packageName, className);
				intent.setComponent(cn);
				con.startActivity(intent);
			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @Name toMByB 
	 * @Description TODO k转M
	 * @param k
	 * @return 
	 * @return String
	 * @Author zxe
	 * @Date 2015年7月24日 上午9:17:30
	*
	 */
	public static String toMByB(int k) { // 该参数表示kb的值
		if (k > (1024)) {
			return ((double) ((long) Math
					.round(((double) k / 1024.0 / 1024.0) * 100)) / 100.0)
					+ " M";
		} else {
			return k + " k";
		}
	}

	/**
	 * 
	 * @Name getGameDownSpeed
	 * @Description TODO 获取下载速度
	 * @param k
	 * @return
	 * @return String
	 * @Author zxe
	 * @Date 2015年7月23日 下午6:29:19
	 * 
	 */
	public static String getGameDownSpeed(int k) {
		if (k > 1000) {
			return "   "
					+ ((double) ((long) Math.round(((double) k / 1024.0) * 100)) / 100.0)
					+ " M/s";
		} else {
			return "   " + (k) + " k/s";
		}
	}
	/**
	 * 
	 * @Name timestampsToDate 
	 * @Description TODO 时间戳转日期
	 * @param time
	 * @return 
	 * @return String
	 * @Author zxe
	 * @Date 2015年7月24日 上午9:17:00
	*
	 */
	public static String timestampsToDate(String time) {
		String re_StrTime = null;
		SimpleDateFormat sdf = null;
		if (time.equals("")) {
			return "";
		}
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		long loc_time = Long.valueOf(time);
		re_StrTime = sdf.format(new Date(loc_time * 1000L));
		return re_StrTime;
	}

	public static String getStringDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	public static String getPackageNameByAPK(Context con, String filePath) {
		PackageManager packageManager = con.getPackageManager();
		PackageInfo packageInfo = packageManager.getPackageArchiveInfo(
				filePath, PackageManager.GET_ACTIVITIES);
		return packageInfo.packageName;
		// Log.d("uid", packageInfo.sharedUserId);
		// Log.d("vname", packageInfo.versionName);
		// Log.d("code", packageInfo.versionCode+"");
	}

	/**
	 * 
	 * @Name isWifi
	 * @Description TODO 判断是否为wifi模式
	 * @param mContext
	 * @return
	 * @return boolean
	 * @Author zxe
	 * @Date 2015年7月21日 上午10:46:03
	 * 
	 */
	public static boolean isWifi(Context mContext) {
		ConnectivityManager connectivityManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null
				&& activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @Name isConnect
	 * @Description TODO 判断网络是否连接
	 * @param context
	 * @return
	 * @return boolean
	 * @Author zxe
	 * @Date 2015年7月21日 上午10:46:49
	 * 
	 */
	public static boolean isConnect(Context context) {
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity != null) {
				// 获取网络连接管理的对象
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info != null && info.isConnected()) {
					// 判断当前网络是否已经连接
					if (info.getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			Log.v("error", e.toString());
		}
		return false;
	}
	/**
	 * 
	 * @Name getJsonTextByGame
	 * @Description TODO 根据游戏实体获得jason字符串
	 * @param g
	 * @return
	 * @return String
	 * @Author zxe
	 * @Date 2015年7月22日 下午2:06:01
	 * 
	 */
	public static String getJsonTextByObject(Object src) {
		if (src != null) {
			return new Gson().toJson(src);
		} else {
			return "";
		}

	}
}
