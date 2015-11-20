package com.cplatform.xhxw.ui.ui.main.cms.game;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.cplatform.xhxw.ui.Constants;

public class Downloader {
	private String localfile;// 保存路径
	private int threadcount;// 线程数
	private Handler mHandler;// 消息处理器
	private int fileSize;// 所要下载的文件的大小
	private Context context;
	private List<DownloadInfo> infos;// 存放下载信息类的集合
	private static final int INIT = 1;// 定义三种下载的状态：初始化状态，正在下载状态，暂停状态
	private static final int DOWNLOADING = 2;
	private static final int PAUSE = 3;
	private int state = INIT;
	String GameId;
	String DownUrl;
	String IconUrl;
	String AppName;
	String TypeName;
	String TypeId;

	public Downloader(int threadcount, Context context, Handler mHandler,
			String gameId, String downUrl, String iconUrl, String appName,
			String typeName, String typeId) {
		Log.d("Downloader", "Downloader");
		GameId = gameId;
		DownUrl = downUrl;
		IconUrl = iconUrl;
		AppName = appName;
		TypeName = typeName;
		TypeId = typeId;
		this.localfile = Constants.Directorys.DOWNLOAD
				+ GameUtil.getFileNameByUrl(DownUrl);
		this.threadcount = threadcount;

		this.mHandler = mHandler;
		this.context = context;
	}

	/**
	 * 判断是否正在下载
	 */
	public boolean isdownloading() {
		return state == DOWNLOADING;
	}

	/**
	 * 得到downloader里的信息 首先进行判断是否是第一次下载，如果是第一次就要进行初始化，并将下载器的信息保存到数据库中
	 * 如果不是第一次下载，那就要从数据库中读出之前下载的信息（起始位置，结束为止，文件大小等），并将下载信息返回给下载器
	 */
	public LoadInfo getDownloaderInfors() {
		Log.d("Downloader", "getDownloaderInfors0");
		if (isFirst(context, DownUrl)) {
			Log.v("TAG", "isFirst");
			init();
			int range = fileSize / threadcount;
			infos = new ArrayList<DownloadInfo>();
			for (int i = 0; i < threadcount - 1; i++) {
				DownloadInfo info = new DownloadInfo(i, i * range, (i + 1)
						* range - 1, 0, DownUrl);
				infos.add(info);
			}
			DownloadInfo info = new DownloadInfo(threadcount - 1,
					(threadcount - 1) * range, fileSize - 1, 0, DownUrl);
			infos.add(info);
			// 保存infos中的数据到数据库
			GameDownInfoDB.saveData(context, infos);
			// 创建一个LoadInfo对象记载下载器的具体信息
			LoadInfo loadInfo = new LoadInfo(fileSize, 0, DownUrl);
			Log.d("Downloader", "getDownloaderInfors1");
			return loadInfo;
		} else {
			// 得到数据库中已有的urlstr的下载器的具体信息
			infos = GameManager.getAllGameDownInfoList(context, DownUrl);
			// Log.v("TAG", "not isFirst size=" + infos.size());
			int size = 0;
			int compeleteSize = 0;
			if (infos != null && (false == infos.isEmpty())) {
				for (DownloadInfo info : infos) {
					compeleteSize += info.getCompeleteSize();
					size += info.getEndPos() - info.getStartPos() + 1;
				}
			}
			Log.d("Downloader", "getDownloaderInfors2");
			return new LoadInfo(size, compeleteSize, DownUrl);
		}
	}

	/**
	 * 初始化
	 */
	private void init() {
		Log.d("Downloader", "init");
		try {
			URL url = new URL(DownUrl);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setConnectTimeout(5000);
			connection.setRequestMethod("GET");
			fileSize = connection.getContentLength();

			File file = new File(localfile);
			if (!file.exists()) {
				file.createNewFile();
			}
			// 本地访问文件
			RandomAccessFile accessFile = new RandomAccessFile(file, "rwd");
			accessFile.setLength(fileSize);
			accessFile.close();
			connection.disconnect();
		} catch (Exception e) {
			Log.e("DownLoader-->init()", e.getMessage());
		}
	}

	/**
	 * 判断是否是第一次 下载
	 */
	private boolean isFirst(Context con, String downUrl) {
		return (false == GameManager.isHaveDownInfoById(con, downUrl));
	}

	/**
	 * 利用线程开始下载数据
	 */
	public void download() {
		Log.d("Downloader", "download");
		if (infos != null) {
			if (state == DOWNLOADING)
				return;
			state = DOWNLOADING;
			for (DownloadInfo info : infos) {
				new MyThread(info.getThreadId(), info.getStartPos(),
						info.getEndPos(), info.getCompeleteSize(),
						info.getUrl()).start();
			}
		}
	}

	public class MyThread extends Thread {
		private int threadId;
		private int startPos;
		private int endPos;
		private int compeleteSize;
		private String urlstr;
		private int oldcompeleteSize;

		public MyThread(int threadId, int startPos, int endPos,
				int compeleteSize, String urlstr) {
			this.threadId = threadId;
			this.startPos = startPos;
			this.endPos = endPos;
			this.compeleteSize = compeleteSize;
			oldcompeleteSize = compeleteSize;
			this.urlstr = urlstr;
		}

		@Override
		public void run() {
			Log.d("Downloader-->MyThread", "run");
			HttpURLConnection connection = null;
			RandomAccessFile randomAccessFile = null;
			InputStream is = null;
			try {
				URL url = new URL(urlstr);
				connection = (HttpURLConnection) url.openConnection();
				connection.setConnectTimeout(5000);
				connection.setRequestMethod("GET");
				// 设置范围，格式为Range：bytes x-y;
				connection.setRequestProperty("Range", "bytes="
						+ (startPos + compeleteSize) + "-" + endPos);

				randomAccessFile = new RandomAccessFile(localfile, "rwd");
				randomAccessFile.seek(startPos + compeleteSize);
				// 将要下载的文件写到保存在保存路径下的文件中
				is = connection.getInputStream();
				byte[] buffer = new byte[GameUtil.BUFFER_SIZE];
				int length = -1;
				long startTime = System.currentTimeMillis();
				while ((length = is.read(buffer)) != -1) {
					randomAccessFile.write(buffer, 0, length);
					compeleteSize += length;
					// 更新数据库中的下载信息
					GameManager.updateDownInfo(context, threadId,
							compeleteSize, urlstr);
					long curTime = System.currentTimeMillis();
					int usedTime = (int) (curTime - startTime);//相差多少毫秒
					int speed=(((compeleteSize-oldcompeleteSize)/usedTime)*1000)/1024;
					Log.v("下载速度", speed+"k/s");
					// 用消息将下载信息传给进度条，对进度条进行更新
					if (state == PAUSE) {
						Message message = Message.obtain();
						message.what = GameUtil.GAME_TAG_DOWN_PAUSE;
						message.obj = GameId + "&&"
								+ getDownloaderInfors().getFileSize() + "&&"
								+ getDownloaderInfors().getComplete();
						message.arg1 = speed;
						mHandler.sendMessage(message);
						return;
					} else {
						Message message = Message.obtain();
						message.what = GameUtil.GAME_TAG_DOWN_SUCCESS;
						message.obj = GameId + "&&"
								+ getDownloaderInfors().getFileSize() + "&&"
								+ getDownloaderInfors().getComplete();
						message.arg1 = speed;
						mHandler.sendMessage(message);
					}
				}
			} catch (Exception e) {
				// e.printStackTrace();
				Message message = Message.obtain();
				message.what = GameUtil.GAME_TAG_DOWN_FAILURE;
				message.obj = GameId + "&&"
						+ getDownloaderInfors().getFileSize() + "&&"
						+ getDownloaderInfors().getComplete();
				mHandler.sendMessage(message);
			}
		}
	}

	// 删除数据库中urlstr对应的下载器信息
	public void delete(String urlstr) {
		Log.d("Downloader", "delete");
		GameDownInfoDB.delGameCashByAppId(context, urlstr);
	}

	// 设置暂停
	public void pause() {
		state = PAUSE;
	}

	// 重置下载状态
	public void reset() {
		state = INIT;
	}

	public String getGameId() {
		return GameId;
	}

	public void setGameId(String gameId) {
		GameId = gameId;
	}

	public String getDownUrl() {
		return DownUrl;
	}

	public void setDownUrl(String downUrl) {
		DownUrl = downUrl;
	}

	public String getIconUrl() {
		return IconUrl;
	}

	public void setIconUrl(String iconUrl) {
		IconUrl = iconUrl;
	}

	public String getAppName() {
		return AppName;
	}

	public void setAppName(String appName) {
		AppName = appName;
	}

	public String getTypeName() {
		return TypeName;
	}

	public void setTypeName(String typeName) {
		TypeName = typeName;
	}

	public String getTypeId() {
		return TypeId;
	}

	public void setTypeId(String typeId) {
		TypeId = typeId;
	}

}