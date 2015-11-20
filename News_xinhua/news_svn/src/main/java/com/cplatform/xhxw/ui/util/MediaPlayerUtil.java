package com.cplatform.xhxw.ui.util;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;

public class MediaPlayerUtil {

	private static ExecutorService executorService = Executors
			.newCachedThreadPool();

	public static void setDataSourceAndPrepare(final MediaPlayer mp,
			final String url, final Runnable failureRunable) {
		final Handler handler = Looper.myLooper() != null ? new Handler()
				: null;
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				try {
					long startTime = System.currentTimeMillis();
					mp.setDataSource(url);
					/*LogUtil.d("MediaPlayer.setDataSource spend time "
							+ (System.currentTimeMillis() - startTime) + "ms");*/

					startTime = System.currentTimeMillis();
					Thread.sleep(1000);
					mp.prepare();
					/*LogUtil.d("MediaPlayer.prepare spend time "
							+ (System.currentTimeMillis() - startTime) + "ms");*/

				} catch (IllegalStateException e) {
					/*LogUtil.d("MediaPlayer状态不对，可能已经被release了，直接过去，不报错");*/
				} catch (Exception e) {
					LogUtil.w(e);
					if (handler != null) {
						handler.post(failureRunable);
					} else {
						failureRunable.run();
					}
				}
			}
		});
	}

	/**加载本地文件*/
	public static void setDateFile(final MediaPlayer mp, FileDescriptor fd) {
		if (mp == null || fd == null) {
			return;
		}
		try {
			mp.setDataSource(fd);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void start(final MediaPlayer mp) {
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				try {
					long startTime = System.currentTimeMillis();
					mp.start();
					/*LogUtil.d("MediaPlayer.start spend time "
							+ (System.currentTimeMillis() - startTime) + "ms");*/
				} catch (IllegalStateException e) {
					LogUtil.w(e);
				}
			}
		});
	}

	/**
	 * 异步释放mediaplayer
	 * 
	 * @param mp
	 */
	public static void release(final MediaPlayer mp) {
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				long startTime = System.currentTimeMillis();
				mp.release();
				long endTime = System.currentTimeMillis();
				/*LogUtil.d("MediaPlayer.relase spend time "
						+ (endTime - startTime) + "ms");*/
			}
		});
	}

	/**
	 * 异步重置mediaplayer
	 * 
	 * @param mp
	 */
	public static void reset(final MediaPlayer mp) {
		executorService.execute(new Runnable() {

			@Override
			public void run() {
				long startTime = System.currentTimeMillis();
				mp.reset();
				long endTime = System.currentTimeMillis();
				/*LogUtil.d("MediaPlayer.reset spend time "
						+ (endTime - startTime) + "ms");*/
			}
		});
	}
}
