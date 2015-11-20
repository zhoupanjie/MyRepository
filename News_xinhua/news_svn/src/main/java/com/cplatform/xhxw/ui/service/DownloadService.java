package com.cplatform.xhxw.ui.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.db.DownloadDB;
import com.cplatform.xhxw.ui.db.dao.DownloadDao;
import com.cplatform.xhxw.ui.receiver.StartServiceReceiver;
import com.cplatform.xhxw.ui.util.ActivityUtil;
import com.cplatform.xhxw.ui.util.CommonUtils;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.cplatform.xhxw.ui.util.NotificationUtil;

import java.net.URL;

import java.io.*;
import java.net.HttpURLConnection;

/**
 * 图片下载任务
 * Created by cy-love on 14-1-29.
 */
public class DownloadService extends IntentService {

    private static final String TAG = DownloadService.class.getSimpleName();
    private LocalBroadcastManager mBroadcastManager;

    //图片保存的路径
    private String dirPath;
    
    //图片名称
    private String fileName;
    /**
     * 暂停下载
     */
    public static final String ACTION_DOWNLOAD_JOB_PAUSE = "com.xuanwen.mobile.news.ACTION_DOWNLOAD_JOB_PAUSE";

    private Receiver mReceiver;
    private boolean isStop;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            DownloadDao downloadDao;
            while (!isStop &&
                    ((downloadDao = getNextJob()) != null)) {
                startJob(downloadDao);
            }
        } finally {
            StartServiceReceiver.completeWakefulIntent(intent);
        }
    }

    /**
     * 获得下一个下载任务
     *
     * @return
     */
    private DownloadDao getNextJob() {
        return DownloadDB.getNextDownload(getApplicationContext());
    }

    /**
     * 开始下载任务
     */
    private void startJob(DownloadDao dao) {

        try {
        	if (!isSDcark()) {
				return;
			}
        	
        	/*String SDCARD = Environment
					.getExternalStorageDirectory().toString()
					+ "/DCIM/Camera/"
					+ String.valueOf(System.currentTimeMillis())
					+ ".jpg";*/
        	
//        	String dirPath = Constants.Directorys.DOWNLOAD; // 获得下载目录
            //文件目录
//        	dirPath="/storage/sdcard1/DCIM/Camera";
        	dirPath = getSDPath();
        	
        	//文件名
            fileName = CommonUtils.getFileName(dao.getUrl());//   /storage/sdcard0/yskj_news/download/1fd68696c24369af0b58010f28027a51.jpg
            File file = getFileFromServer(getApplicationContext(), dao, dirPath, fileName);
            if (isStop) {
                return; // 停止下载
            }
            // 移除已完成的任务
            if (file != null && file.exists()) {
                DownloadDB.removeDownload(getApplicationContext(), dao.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
            DownloadDB.removeDownload(getApplicationContext(), dao.getId());
            LogUtil.e(TAG, "downloadFile exception:" + e.toString());
        }
    }

    /**
     * 联网下载文件
     *
     * @param context  上下文
     * @param dao      任务
     * @param downDir  下载目录
     * @param fileName 保存文件名称
     * @return 文件
     * @throws Exception
     */
    private File getFileFromServer(Context context, DownloadDao dao, String downDir, String fileName) throws Exception {
        if (!isSDCardExist()) {
            Toast.makeText(context, "请插入SD卡", Toast.LENGTH_SHORT).show();
            throw new Exception();
        }
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        HttpURLConnection conn = (HttpURLConnection) new URL(dao.getUrl()).openConnection();
        conn.setConnectTimeout(5000);
        //获取到文件的大小
        int max = conn.getContentLength();
        InputStream is = conn.getInputStream();
        LogUtil.i(TAG, Environment.getExternalStorageDirectory().getAbsolutePath());
        File dir = new File(downDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(downDir, fileName);
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        BufferedInputStream bis = new BufferedInputStream(is);
        byte[] buffer = new byte[1024];
        int len;
        int total = 0;
        while (!isStop && (len = bis.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
            total += len;
            NotificationUtil.notifyProgressStatus(this, NotificationUtil.DOWNLOADING, getString(R.string.downloading), "", total * 100 / max,
                    null, R.drawable.ic_notification_icon);
        }
        NotificationUtil.notifyCancel(this, NotificationUtil.DOWNLOADING);

        if (total == max) {
            Intent intent1 = ActivityUtil.getImageFileIntent(file.getAbsolutePath());
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent1, 0);
            NotificationUtil.notifyStatus(this, NotificationUtil.DOWNLOAD, getString(R.string.download_done), getString(R.string.click_to_view), pendingIntent,
                    R.drawable.ic_notification_icon);
            showToastByRunnable(this, getString(R.string.download_done), Toast.LENGTH_SHORT);
        }
        if (isStop) {
            return null;
        }
        fos.close();
        bis.close();
        is.close();
        return file;
    }

    /**
     * 获得SD卡目录
     *
     * @return null表示无SD卡
     */
    private static boolean isSDCardExist() {
        return Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
    }

    private static void showToastByRunnable(final IntentService context, final CharSequence text, final int duration)     {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, text, duration).show();
            }
        });
    }

    @Override
    public void onCreate() {
        mBroadcastManager = LocalBroadcastManager.getInstance(this);
        mReceiver = new Receiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_DOWNLOAD_JOB_PAUSE);
        mBroadcastManager.registerReceiver(mReceiver, filter);
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        mBroadcastManager.unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_DOWNLOAD_JOB_PAUSE.equals(intent.getAction())) {
                isStop = true;
            }
        }
    }

    /** 判断sd卡是否可用 */
	private boolean isSDcark() {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			Toast.makeText(getApplicationContext(), getApplicationContext().getResources()
					.getString(R.string.rank_activity_sdcard), Toast.LENGTH_LONG).show();
			return false;
		}
	}
	
	/** 获得SD卡的路径 */
	private String getSDPath() {
		String path = "";
		File file = new File("/storage/sdcard1/DCIM/Camera");
		if (file.exists()) {
			path = "/storage/sdcard1/DCIM/Camera";
		}else {
			path = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera";
		}
		return path;
	}
}
