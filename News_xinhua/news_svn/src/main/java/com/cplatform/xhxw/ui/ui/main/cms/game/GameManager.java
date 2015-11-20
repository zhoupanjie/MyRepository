package com.cplatform.xhxw.ui.ui.main.cms.game;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.db.NewsCashDB;
import com.cplatform.xhxw.ui.db.dao.NewsCashDao;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.BaseResponse;
import com.cplatform.xhxw.ui.util.DateUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class GameManager {
	// 下载断点详情
	private static Map<String, Downloader> downloaders = new HashMap<String, Downloader>();
	// 游戏列表当前列表数据
	private static List<Game> curListGame = new ArrayList<Game>();

	/**
	 * 
	 * @Name addTask
	 * @Description TODO 添加任务
	 * @param con
	 * @param gameid
	 * @param downloader
	 * @return void
	 * @Author zxe
	 * @Date 2015年7月22日 上午10:59:18
	 * 
	 */
	public static void addTask(Context con, String gameid, Downloader downloader) {
		downloaders.put(gameid, downloader);
		// saveGameData(con, gameid);
	}

	/**
	 * 
	 * @Name saveGameData
	 * @Description TODO 保存下载的游戏任务
	 * @param con
	 * @param gameId
	 * @return void
	 * @Author zxe
	 * @Date 2015年7月20日 上午10:10:55
	 * 
	 */
	private static void saveGameData(Context con, String gameId) {
		GameCashDao gcdGame = new GameCashDao();
		gcdGame.setAppId(gameId);
		if (curListGame == null || curListGame.isEmpty()) {
			curListGame = getGameCatchList(con);
		}
		gcdGame.setJson(GameUtil.getJsonTextByObject(getGameById(curListGame, gameId)));
		gcdGame.setSaveTime(DateUtil.getTime());
		GameCashDB.saveData(con, gcdGame);
	}

	public static void saveGameDataByGame(Context con, Game item, int stateDown) {
		GameCashDao gcdGame = new GameCashDao();
		gcdGame.setAppId(item.getId());
		item.setStateDown(stateDown);
		gcdGame.setJson(GameUtil.getJsonTextByObject(item));
		gcdGame.setSaveTime(DateUtil.getTime());
		GameCashDB.saveData(con, gcdGame);
	}

	/**
	 * 
	 * @Name isHaveDownInfoById
	 * @Description TODO 是否有下载项
	 * @param con
	 * @param DownUrl
	 * @return
	 * @return boolean
	 * @Author zxe
	 * @Date 2015年7月22日 下午12:09:04
	 * 
	 */
	public static boolean isHaveDownInfoById(Context con, String DownUrl) {
		return (null != GameDownInfoDB.getGameCacheByAppId(con, DownUrl));
	}

	/**
	 * 
	 * @Name updateDownInfo
	 * @Description TODO 更新下载详情
	 * @param con
	 * @param threadId
	 * @param compeleteSize
	 * @param urlstr
	 * @return void
	 * @Author zxe
	 * @Date 2015年7月22日 下午12:09:26
	 * 
	 */
	public static void updateDownInfo(Context con, int threadId,
			int compeleteSize, String urlstr) {
		DownloadInfo di = getGameDownInfoByUrlAndThreadId(con, urlstr, threadId);
		if (di != null) {
			GameDownInfoDao adid = new GameDownInfoDao();
			di.setCompeleteSize(compeleteSize);
			adid.setAppId(urlstr);
			adid.setJson(GameUtil.getJsonTextByObject(di));
			adid.setSaveTime(DateUtil.getTime());
			GameDownInfoDB.updateGameCache(con, adid);
		}
	}

	/**
	 * 
	 * @Name onPauseById
	 * @Description TODO 暂停对应id的下载项
	 * @param con
	 * @param gameid
	 * @return void
	 * @Author zxe
	 * @Date 2015年7月22日 下午12:09:47
	 * 
	 */
	public static void onPauseById(Context con, String gameid) {
		if (false == downloaders.isEmpty() && downloaders.get(gameid) != null) {
			downloaders.get(gameid).pause();
		}
	}
	public static Game getGameById(Context con, String gameid) {
		Game g = null;
		if (curListGame != null || (false == curListGame.isEmpty())) {
			g = getGameById(curListGame, gameid);
		}
		// 从数据库中去历史任务列表
		if (g == null) {
			GameCashDao gcd = GameCashDB.getGameCacheByAppId(con, gameid);
			if (gcd != null && (false == TextUtils.isEmpty(gcd.getJson()))) {
				g = new Gson().fromJson(gcd.getJson(), Game.class);
			}
		}
		if (g == null) {
			curListGame = getGameCatchList(con);
			g = getGameById(curListGame, gameid);
		}
		return g;
	}

	public static void startNextDown(Context con) {
		List<Game> lg = getAllGameDownCacheList(con);
		Game g = null;
		if (lg != null && (false == lg.isEmpty())) {
			for (Game gCur : lg) {
				if (gCur.getStateDown() == GameUtil.GAME_DOWN_WAIT) {
					g = gCur;
					break;
				}
			}
		}
		if (g != null) {
			saveGameDataByGame(con, g, GameUtil.GAME_DOWN_ING);
			startDown(con, g.getId(), g.getDownloadurl(), g.getIcon(),
					g.getName(), g.getCatname(), g.getCatid());
		}
	}

	/**
	 * 
	 * @Name updateGameCacheByDown
	 * @Description TODO 发送广播后更新下载游戏的状态
	 * @param con
	 * @param gameid
	 * @param RunState
	 * @param FileSize
	 * @param Complete
	 * @return void
	 * @Author zxe
	 * @Date 2015年7月19日 下午5:48:40
	 * 
	 */
	public static void updateGameCacheByDown(Context con, String gameid,
			int RunState, int FileSize, int Complete) {

		switch (RunState) {
		case GameUtil.GAME_TAG_DOWN_SUCCESS: {// 下载成功
			if (FileSize == Complete) {
				GameCashDao gcdGame = new GameCashDao();
				gcdGame.setAppId(gameid);
				Game g = getGameById(con, gameid);
				gcdGame.setSaveTime(DateUtil.getTime());
				if (g != null) {
					Log.d("updateGameCacheByDown", "下载成功");
					g.setStateDown(GameUtil.GAME_DOWN_COMPLETE);
					g.setFileSize(FileSize);
					g.setComplete(Complete);
					g.setDownTime(GameUtil.getStringDate());
					GameManager.removeDownTask(con, gameid);
					loadStatData(gameid);
//					 Intent intent1 = ActivityUtil.getImageFileIntent("");
//					 PendingIntent pendingIntent =
//					 PendingIntent.getActivity(con, 0, intent1, 0);
//					 NotificationUtil.notifyStatus(con,
//					 NotificationUtil.DOWNLOAD, "下载完成", "点击查看", pendingIntent,
//					 R.drawable.ic_notification_icon);
					Toast.makeText(con, "【" + g.getName() + "】下载完成！", 1000)
							.show();
					gcdGame.setJson(GameUtil.getJsonTextByObject(g));
					GameCashDB.updateGameCache(con, gcdGame);
					startNextDown(con);
				}
			} else {
				Log.d("保存下载进度", gameid+"下载中");
				
//				GameCashDao gcdGame = new GameCashDao();
//				gcdGame.setAppId(gameid);
//				Game g = getGameById(con, gameid);
//				gcdGame.setSaveTime(DateUtil.getTime());
//				g.setStateDown(GameUtil.GAME_DOWN_ING);
//				g.setFileSize(FileSize);
//				g.setComplete(Complete);
//				g.setDownTime(GameUtil.getStringDate());
//				gcdGame.setJson(getJsonTextByObject(g));
//				GameCashDB.updateGameCache(con, gcdGame);
//				Log.d("updateGameCacheByDown", "下载中");
			}
		}
			break;
		case GameUtil.GAME_TAG_DOWN_FAILURE: {// 下载失败
			GameCashDao gcdGame = new GameCashDao();
			gcdGame.setAppId(gameid);
			Game g = getGameById(con, gameid);
			gcdGame.setSaveTime(DateUtil.getTime());
			Log.d("updateGameCacheByDown", "下载失败");
			if (g != null) {
				g.setStateDown(GameUtil.GAME_DOWN_FAILURE);
				g.setFileSize(FileSize);
				g.setComplete(Complete);
				GameManager.removeDownTask(con, gameid);
				Toast.makeText(con, "【" + g.getName() + "】下载失败！", 1000).show();
				gcdGame.setJson(GameUtil.getJsonTextByObject(g));
				GameCashDB.updateGameCache(con, gcdGame);
				startNextDown(con);
			}

		}
			break;
		case GameUtil.GAME_TAG_DOWN_ING: {// 下载中
			Log.d("updateGameCacheByDown", "下载中2");
			// g.setStateDown(GameUtil.GAME_DOWN_ING);
			// g.setFileSize(FileSize);
			// g.setComplete(Complete);
		}
			break;
		case GameUtil.GAME_TAG_DOWN_PAUSE: {// 暂停
			GameCashDao gcdGame = new GameCashDao();
			gcdGame.setAppId(gameid);
			Game g = getGameById(con, gameid);
			gcdGame.setSaveTime(DateUtil.getTime());
			if (g != null) {
				Log.d("updateGameCacheByDown", "下载暂停");
				g.setStateDown(GameUtil.GAME_DOWN_PAUSE);
				gcdGame.setJson(GameUtil.getJsonTextByObject(g));
				GameCashDB.updateGameCache(con, gcdGame);
				startNextDown(con);
			}
		}
			break;
		case GameUtil.GAME_TAG_INSTALL_SUCCESS: {// 安装成功
			GameCashDao gcdGame = new GameCashDao();
			gcdGame.setAppId(gameid);
			Game g = getGameById(con, gameid);
			gcdGame.setSaveTime(DateUtil.getTime());
			if (g != null) {
				Log.d("updateGameCacheByDown", "安装成功");
				g.setStateDown(GameUtil.GAME_INSTALL_COMPLETE);
				g.setInstallTime(GameUtil.getStringDate());
				gcdGame.setJson(GameUtil.getJsonTextByObject(g));
				GameCashDB.updateGameCache(con, gcdGame);
			}
		}
			break;
		case GameUtil.GAME_TAG_INSTALL_FAILURE: {// 安装失败
			GameCashDao gcdGame = new GameCashDao();
			gcdGame.setAppId(gameid);
			Game g = getGameById(con, gameid);
			gcdGame.setSaveTime(DateUtil.getTime());
			if (g != null) {
				Log.d("updateGameCacheByDown", "安装失败");
				g.setStateDown(GameUtil.GAME_INSTALL_FAILURE);
				g.setInstallTime(GameUtil.getStringDate());
				gcdGame.setJson(GameUtil.getJsonTextByObject(g));
				GameCashDB.updateGameCache(con, gcdGame);
			}
		}
			break;
		}
	}

	/**
	 * 
	 * @Name loadStatData
	 * @Description TODO 下载统计接口
	 * @param id
	 * @return void
	 * @Author zxe
	 * @Date 2015年7月17日 下午3:11:04
	 * 
	 */
	public static void loadStatData(String id) {
		LogUtil.d("游戏下载", "游戏下载统计请求参数：id=" + id);
		GameDetailRequest ndr = new GameDetailRequest(id);
		ndr.setIstest(GameUtil.istest);
		APIClient.gameStat(ndr, ahrh);
	}

	/**
	 * 下载统计结果处理
	 */
	static AsyncHttpResponseHandler ahrh = new AsyncHttpResponseHandler() {
		@Override
		public void onFinish() {

		}

		@Override
		protected void onPreExecute() {

		}

		@Override
		public void onSuccess(String content) {
			Log.e("游戏下载统计结果", content);
		}

		@Override
		public void onFailure(Throwable error, String content) {
			// mDefView.setStatus(DefaultView.Status.error);
			Log.e("游戏下载统计失败", "onFailure");
		}
	};

	public static void setStartDown(final Context con, final TextView ivDown,
			final Game item, final int strId, final int stateDown) {
		if (GameUtil.isConnect(con)) {
			if ((false == GameUtil.isWifi(con)) && GameUtil.ISFIRSTDOWN) {
				new AlertDialog.Builder(con)
						.setMessage("非wifi环境下是否继续下载？")
						.setTitle("下载提示")
						.setPositiveButton(R.string.confirm,
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										updateDownUI(con, ivDown, item, strId,
												stateDown, item.getId(),
												item.getDownloadurl(),
												item.getIcon(), item.getName(),
												item.getCatname(),
												item.getCatid());
									}
								}).setNegativeButton(R.string.cancel, null)
						.show();
				GameUtil.ISFIRSTDOWN = false;
			} else {
				updateDownUI(con, ivDown, item, strId, stateDown, item.getId(),
						item.getDownloadurl(), item.getIcon(), item.getName(),
						item.getCatname(), item.getCatid());
			}
		} else {
			GameManager.showNoNetworkDialog(con);
		}
	}

	public static void showNoNetworkDialog(Context con) {
		new AlertDialog.Builder(con)
				.setMessage("无网络连接，请稍候再试!")
				.setTitle("下载提示")
				.setPositiveButton(R.string.confirm,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).setNegativeButton(R.string.cancel, null).show();
	}

	public static void updateDownUI(Context con, TextView ivDown, Game item,
			int strId, int stateDown, String GameId, String DownUrl,
			String IconUrl, String AppName, String TypeName, String TypeId) {
		List<GameCashDao> lgcd = GameCashDB.getGameCacheList(con);
		List<Game> glist = new ArrayList<Game>();
		for (GameCashDao gcd : lgcd) {
			Game g = getGameItemByGameCashDao(gcd);
			if (g.getStateDown() == GameUtil.GAME_DOWN_ING
					|| g.getStateDown() == GameUtil.GAME_DOWN_CONTINUE) {
				glist.add(g);
			}
		}
		if ((glist.size() < GameUtil.MAXDOWNAMOUNT)) {
			ivDown.setText(strId);
			item.setStateDown(stateDown);
			// saveGameData(con, gameId);
			saveGameDataByGame(con, item, GameUtil.GAME_DOWN_ING);
			startDown(con, GameId, DownUrl, IconUrl, AppName, TypeName, TypeId);
		} else {
			ivDown.setText(R.string.game_down_wait);
			item.setStateDown(GameUtil.GAME_DOWN_WAIT);
			setWaitDownState(con, GameId);
		}
	}

	/**
	 * 
	 * @Name startDown
	 * @Description TODO 开始下载
	 * @param con
	 * @param GameId
	 * @param DownUrl
	 * @param IconUrl
	 * @param AppName
	 * @param TypeName
	 * @param TypeId
	 * @return void
	 * @Author zxe
	 * @Date 2015年7月22日 上午11:54:30
	 * 
	 */
	public static void startDown(Context con, String GameId, String DownUrl,
			String IconUrl, String AppName, String TypeName, String TypeId) {
		String testUrl="http://shouji.360tpcdn.com/150716/d475f2d388d36dce761e36c99360b19b/sh.lilith.dgame.s360_50422.apk";
		Intent intent2 = new Intent(con, GameDownService.class);
		intent2.putExtra("GameId", GameId);
//		intent2.putExtra("DownUrl", testUrl);
		intent2.putExtra("DownUrl", DownUrl);
		intent2.putExtra("IconUrl", IconUrl);
		intent2.putExtra("AppName", AppName);
		intent2.putExtra("TypeName", TypeName);
		intent2.putExtra("TypeId", TypeId);
		con.startService(intent2);
		// saveGameData(con, GameId);
	}

	/**
	 * 
	 * @Name getGameItemByGameCashDao
	 * @Description TODO 根据GameCashDao获得Game项
	 * @param gcd
	 * @return
	 * @return Game
	 * @Author zxe
	 * @Date 2015年7月22日 下午1:58:09
	 * 
	 */
	public static Game getGameItemByGameCashDao(GameCashDao gcd) {
		if (gcd != null && (false == TextUtils.isEmpty(gcd.getJson()))) {
			return new Gson().fromJson(gcd.getJson(), Game.class);
		} else {
			return null;
		}
	}

	

	/**
	 * 
	 * @Name setWaitDownState
	 * @Description TODO 设置等待状态
	 * @param con
	 * @param GameId
	 * @return
	 * @return Game
	 * @Author zxe
	 * @Date 2015年7月22日 下午2:05:43
	 * 
	 */
	public static Game setWaitDownState(Context con, String GameId) {
		GameCashDao gcd = new GameCashDao();
		Game g = getGameById(con, GameId);
		g.setStateDown(GameUtil.GAME_DOWN_WAIT);
		gcd.setAppId(GameId);
		gcd.setJson(GameUtil.getJsonTextByObject(g));
		GameCashDB.saveData(con, gcd);
		return g;
	}

	/**
	 * 
	 * @Name removeDownTask
	 * @Description TODO 移除当卡游戏id断点下载任务
	 * @param con
	 * @param gameid
	 * @return void
	 * @Author zxe
	 * @Date 2015年7月22日 上午11:49:17
	 * 
	 */
	public static void removeDownTask(Context con, String gameid) {
		if ((false == downloaders.isEmpty()) && downloaders.get(gameid) != null) {
			GameDownInfoDB.delGameCashByAppId(con, downloaders.get(gameid)
					.getDownUrl());
			downloaders.get(gameid).delete(gameid);
			downloaders.get(gameid).reset();
			downloaders.remove(gameid);
		}
	}

	/**
	 * 
	 * @Name getAllGameDownCaseList
	 * @Description TODO 获得所有游戏下载记录
	 * @param con
	 * @return
	 * @return List<Game>
	 * @Author zxe
	 * @Date 2015年7月15日 下午2:58:55
	 * 
	 */
	public static List<Game> getAllGameDownCacheList(Context con) {
		List<GameCashDao> lgcd = GameCashDB.getGameCacheList(con);
		List<Game> lgDown = new ArrayList<Game>();
		for (GameCashDao gcd : lgcd) {
			Game g = getGameItemByGameCashDao(gcd);
			if (g != null) {
				lgDown.add(g);
			}
		}
		return lgDown;
	}

	/**
	 * 
	 * @Name getAllGameDownInfoList
	 * @Description TODO 获得该url的所有下载详情
	 * @param con
	 * @param DownUrl
	 * @return
	 * @return List<DownloadInfo>
	 * @Author zxe
	 * @Date 2015年7月22日 下午1:30:32
	 * 
	 */
	public static List<DownloadInfo> getAllGameDownInfoList(Context con,
			String DownUrl) {
		List<GameDownInfoDao> lgdid = GameDownInfoDB.getGameCacheList(con);
		List<DownloadInfo> ldi = new ArrayList<DownloadInfo>();
		for (GameDownInfoDao gdid : lgdid) {
			if (gdid.getAppId().equals(DownUrl)) {
				DownloadInfo di = new Gson().fromJson(gdid.getJson(),
						DownloadInfo.class);
				if (di != null) {
					ldi.add(di);
				}
			}
		}
		return ldi;
	}

	/**
	 * 
	 * @Name delGameCashByAppId
	 * @Description TODO 删除游戏id对应的下载任务
	 * @param con
	 * @param gameid
	 * @param url
	 * @return void
	 * @Author zxe
	 * @Date 2015年7月22日 上午11:49:09
	 * 
	 */
	public static void delGameCashByAppId(Context con, String gameid, String url) {
		GameCashDB.delGameCashByAppId(con, gameid);
		GameDownInfoDB.delGameCashByAppId(con, url);
	}

	public static DownloadInfo getGameDownInfoByUrlAndThreadId(Context con,
			String DownUrl, int threadId) {
		List<GameDownInfoDao> lgdid = GameDownInfoDB.getGameCacheList(con);
		for (GameDownInfoDao gdid : lgdid) {
			if (gdid.getAppId().equals(DownUrl)) {
				DownloadInfo di = new Gson().fromJson(gdid.getJson(),
						DownloadInfo.class);
				if (di.getThreadId() == threadId) {
					return di;
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @Name getGameById
	 * @Description TODO 获得对应id的游戏
	 * @param listGame
	 * @param gameId
	 * @return
	 * @return Game
	 * @Author zxe
	 * @Date 2015年7月22日 下午1:32:28
	 * 
	 */
	private static Game getGameById(List<Game> listGame, String gameId) {
		Game g = null;
		if (TextUtils.isEmpty(gameId) == false && (listGame.isEmpty() == false)) {
			for (Game item : listGame) {
				if (item.getId().equals(gameId)) {
					g = item;
					break;
				}
			}
		}
		return g;
	}

	/**
	 * 
	 * @Name getGameDownCache
	 * @Description TODO 根据游戏id获取指定游戏下载记录
	 * @param con
	 * @param gameId
	 * @return
	 * @return Game
	 * @Author zxe
	 * @Date 2015年7月15日 下午3:05:02
	 * 
	 */
	public static Game getGameDownCache(Context con, String gameId) {
		GameCashDao gcd = GameCashDB.getGameCacheByAppId(con, gameId);
		if (gcd != null) {
			return new Gson().fromJson(gcd.getJson(), Game.class);
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @Name getGameCatchList
	 * @Description TODO 获得游戏频道中的列表
	 * @param con
	 * @return
	 * @return List<Game>
	 * @Author zxe
	 * @Date 2015年7月22日 上午11:35:52
	 * 
	 */
	public static List<Game> getGameCatchList(Context con) {
		List<Game> lg = new ArrayList<Game>();
		NewsCashDao dao_ = NewsCashDB.getNewsCashByColumnId(con,
				GameUtil.CHANNELID_GAME);
		BaseResponse response_ = getResponse(dao_);
		if (response_ != null && response_ instanceof GameListResponse) {
			GameListResponse res = (GameListResponse) response_;
			if (res.getData() != null) {
				GameListResponse.Conetnt conetnt = res.getData();
				lg = conetnt.getList();
			}
		}
		return lg;
	}

	/**
	 * 
	 * @Name getResponse
	 * @Description TODO 获得响应
	 * @param dao
	 * @return
	 * @return BaseResponse
	 * @Author zxe
	 * @Date 2015年7月22日 上午11:47:04
	 * 
	 */
	private static BaseResponse getResponse(NewsCashDao dao) {
		if (dao == null) {
			// Log.d("aaa",
			// "doin__cacheChannelId="+mChannelid+"  "+"dao == null");
			return null;
		}
		try {
			if (TextUtils.isEmpty(dao.getJson())) {
				// Log.d("aaa",
				// "doin__cacheChannelId="+mChannelid+"  "+"新闻列表内容为空");
				throw new NullPointerException("新闻id::"
						+ GameUtil.CHANNELID_GAME + "  新闻列表内容为空！");
			}
			ResponseUtil.checkResponse(dao.getJson());
			// Log.d("aaa",
			// "doin__cacheChannelId="+mChannelid+"  "+"return new Gson()");
			return new Gson().fromJson(dao.getJson(), GameListResponse.class);
		} catch (Exception e) {
			// Log.d("aaa",
			// "doin__cacheChannelId="+mChannelid+"  "+"getResponse__Exception e");
			LogUtil.w("", e);
		}
		return null;
	}

	/**
	 * 
	 * @Name getItem
	 * @Description TODO 获得当前游戏id的下载信息
	 * @param gameid
	 * @return
	 * @return Downloader
	 * @Author zxe
	 * @Date 2015年7月22日 上午11:47:41
	 * 
	 */
	public static Downloader getItem(String gameid) {
		if (false == downloaders.isEmpty() && downloaders.get(gameid) != null) {
			return downloaders.get(gameid);
		} else {
			return null;
		}
	}

	public static Map<String, Downloader> getDownloaders() {
		return downloaders;
	}

	public static void setDownloaders(Map<String, Downloader> downloaders) {
		GameManager.downloaders = downloaders;
	}

	public static List<Game> getListGame() {
		return curListGame;
	}

	public static void setListGame(List<Game> listGame) {
		curListGame = listGame;
	}

	/**
	 * 
	 * @Name setGameByDownHistory
	 * @Description TODO 根据下载历史设置游戏项
	 * @param item
	 * @return
	 * @return Game
	 * @Author zxe
	 * @Date 2015年7月22日 下午3:56:34
	 * 
	 */
	public static Game setGameByDownHistory(Context con, Game item) {
		Log.d("GameAdapter下载更新", "开始");
		// tv.setText(R.string.game_down);
		Game cGame = GameManager.getGameDownCache(con, item.getId());
		if (cGame == null) {// 无下载记录
			// 判断包名是否为空
			if (TextUtils.isEmpty(item.getPackageName())) {
				// 检查sd卡上是否有安装包
				if (GameUtil.isInstallByPackageName(con, item.getPackageName())) {// 已存在
					// tv.setText(R.string.game_install_open);
					item.setStateDown(GameUtil.GAME_DOWN_COMPLETE);
				} else {
					// tv.setText(R.string.game_down);
					item.setStateDown(GameUtil.GAME_DOWN_UN);
				}
			} else {
				PackageInfo pi = GameUtil.getPackageInfoByPackageName(con,
						item.getPackageName());
				if (pi == null) {// 未安装
					// tv.setText(R.string.game_down);
					item.setStateDown(GameUtil.GAME_DOWN_UN);
				} else {// 已安装
					// 判断当前版本号是否大于已安装应用版本号
					if ((false == TextUtils.isEmpty(item.getVersionCode()))
							&& Integer.parseInt(item.getVersionCode()) > pi.versionCode) {// 更新
						// tv.setText(R.string.game_down);
						item.setStateDown(GameUtil.GAME_DOWN_UN);
					} else {// 已安装
						// tv.setText(R.string.game_install_open);
						item.setStateDown(GameUtil.GAME_INSTALL_COMPLETE);
					}
				}
			}
		} else {

			boolean isEmptyCur = TextUtils.isEmpty(item.getVersionCode());
			boolean isEmptyHis = TextUtils.isEmpty(cGame.getVersionCode());

			if (isEmptyCur) {// 当前版本为空
				// item.setStateDown(cGame.getStateDown());
				item.setFileSize(cGame.getFileSize());
				item.setComplete(cGame.getComplete());
				item.setDownTime(cGame.getDownTime());
				// item.setStateDown(GameUtil.GAME_DOWN_PAUSE);
			} else if (isEmptyHis) {// 历史记录版本为空
				// item.setStateDown(GameUtil.GAME_DOWN_UN);
			} else {
				// 若当前版本大于历史下载记录版本
				if (Integer.parseInt(item.getVersionCode()) > Integer
						.parseInt(cGame.getVersionCode())) {
					// 重新下载
					// tv.setText(R.string.game_down);
					item.setStateDown(GameUtil.GAME_DOWN_UN);
				} else if (Integer.parseInt(item.getVersionCode()) == Integer
						.parseInt(cGame.getVersionCode())) {
					// 如果文件存在
//					if (new File(Constants.Directorys.DOWNLOAD
//							+ GameUtil.getFileNameByUrl(item.getDownloadurl()))
//							.exists()) {

						if ((cGame.getStateDown() == GameUtil.GAME_DOWN_CONTINUE)) {// 下载中，恢复为暂停
							item.setStateDown(GameUtil.GAME_DOWN_PAUSE);
						} else if ((cGame.getStateDown() == GameUtil.GAME_INSTALL_ING)) {// 安装中恢复为下载完成状态
							item.setStateDown(GameUtil.GAME_DOWN_COMPLETE);
						} else if((cGame.getStateDown()==GameUtil.GAME_DOWN_ING)){
								item.setStateDown(GameUtil.GAME_DOWN_PAUSE);
						}else{
							item.setStateDown(cGame.getStateDown());
						}
						item.setFileSize(cGame.getFileSize());
						item.setComplete(cGame.getComplete());
//					} else {
//						GameManager.delGameCashByAppId(con, cGame.getId(),
//								cGame.getDownloadurl());
//						GameManager.removeDownTask(con, cGame.getId());
//						// tv.setText(R.string.game_down);
//						item.setStateDown(GameUtil.GAME_DOWN_UN);
//					}
				} else {
					GameManager.delGameCashByAppId(con, cGame.getId(),
							cGame.getDownloadurl());
					GameManager.removeDownTask(con, cGame.getId());
					// tv.setText(R.string.game_down);
					item.setStateDown(GameUtil.GAME_DOWN_UN);
				}
			}
			item.setDownTime(cGame.getDownTime());
			item.setInstallTime(cGame.getInstallTime());
		}
		if ((item.getStateDown() == GameUtil.GAME_INSTALL_COMPLETE)
				&& (false == GameUtil.isInstallByPackageName(con,
						item.getPackageName()))) {// 安装中恢复为下载完成状态
			item.setStateDown(GameUtil.GAME_DOWN_COMPLETE);
		}
		return item;
	}
}
