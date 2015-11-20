package com.cplatform.xhxw.ui.ui.main.cms.game;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class GameDownService extends Service {
	// 要下载的文件链接
	private int totalSize;// 文件总长度
	private int downloadedSize;// 已下载的文件长度

	private int ncount = 0;
	private int count = 0;

	private Intent intent;

	String GameId;
	String DownUrl;
	String IconUrl;
	String AppName;
	String TypeName;
	String TypeId;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		this.intent = new Intent(GameUtil.ACTION_GAME);
		Log.d("GameDownService", "onStart1");
		if (intent != null) {
			Log.d("GameDownService", "intent!=null");
			GameId = intent.getStringExtra("GameId");
			DownUrl = intent.getStringExtra("DownUrl");
			IconUrl = intent.getStringExtra("IconUrl");
			AppName = intent.getStringExtra("AppName");
			TypeName = intent.getStringExtra("TypeName");
			TypeId = intent.getStringExtra("TypeId");
			DownloadTask downloadTask = new DownloadTask(
					this.getApplicationContext(), GameId, DownUrl, IconUrl,
					AppName, TypeName, TypeId);
			downloadTask.execute(DownUrl);
		}
		Log.d("GameDownService", "onStart2");
	}

	class DownloadTask extends AsyncTask<String, Integer, LoadInfo> {
		Downloader downloader = null;
		Context con;
		String urlstr = null;
		String GameId;
		String DownUrl;
		String IconUrl;
		String AppName;
		String TypeName;
		String TypeId;

		public DownloadTask(Context context, String gameId, String downUrl,
				String iconUrl, String appName, String typeName, String typeId) {
			con = context;
			GameId = gameId;
			DownUrl = downUrl;
			IconUrl = iconUrl;
			AppName = appName;
			TypeName = typeName;
			TypeId = typeId;
		}

		@Override
		protected void onPreExecute() {
			Log.d("GameDownService->DownloadTask", "onPreExecute");
		}

		@Override
		protected LoadInfo doInBackground(String... params) {
			Log.d("GameDownService->DownloadTask", "doInBackground");
			// int threadcount = Integer.parseInt(params[2]);
			// 初始化一个downloader下载器
			downloader = GameManager.getItem(GameId);
			if (downloader == null) {
				downloader = new Downloader(GameUtil.THREADCOUNT, con,
						mHandler, GameId, DownUrl, IconUrl, AppName, TypeName,
						TypeId);
				GameManager.addTask(con, GameId, downloader);
			}
			if (downloader.isdownloading())
				return null;
			// 得到下载信息类的个数组成集合
			return downloader.getDownloaderInfors();
		}

		@Override
		protected void onPostExecute(LoadInfo loadInfo) {
			Log.d("GameDownService->DownloadTask", "onPostExecute");
			if (loadInfo != null) {
				// 显示进度条

				// showProgress(loadInfo, item);
				intent.putExtra("RunState", GameUtil.GAME_TAG_DOWN_ING);
				intent.putExtra("GameId", GameId);
				intent.putExtra("FileSize", loadInfo.getFileSize());
				intent.putExtra("Complete", loadInfo.getComplete());
				sendBroadcast(intent);
				// 调用方法开始下载
				downloader.download();
			}
		}
	}

	public void setBroadcastData(Message msg) {
		Log.d("GameDownService", "setBroadcastData");
		String content = (String) msg.obj;
		String[] strArray = content.split("&&");
		String gameId = strArray[0];
		int fileSize = Integer.parseInt(strArray[1]);
		int complete = Integer.parseInt(strArray[2]);
		int length = msg.arg1;
		intent.putExtra("RunState", msg.what);
		intent.putExtra("GameId", gameId);
		intent.putExtra("totalSize", length);
		intent.putExtra("FileSize", fileSize);
		intent.putExtra("Complete", complete);
		sendBroadcast(intent);
		GameManager.updateGameCacheByDown(this, gameId, msg.what, fileSize,
				complete);
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
		Log.e("游戏下载服务", "onLowMemory");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e("游戏下载服务", "GameDownService");
	}

	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case 1:// 成功
				setBroadcastData(msg);
				break;
			case 2:// 失败
				setBroadcastData(msg);
				break;
			case 4:// 暂停
				break;
			}
			setBroadcastData(msg);
			super.handleMessage(msg);
		}
	};
}
