package com.cplatform.xhxw.ui.util;

import java.io.FileDescriptor;
import com.cplatform.xhxw.ui.R;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.text.TextUtils;
import android.widget.Toast;

public class MediaPlayerManager implements OnPreparedListener,
		OnCompletionListener, OnSeekCompleteListener, OnErrorListener {

	private Context context;
	private MediaPlayer mediaPlayer;
	private boolean isPrepared = false;
	private boolean isPauseed = false;

	private OnMediaPlayerManagerListener onMediaPlayerManagerListener;

	public MediaPlayerManager(Context context) {
		this.context = context;
		mediaPlayer = new MediaPlayer();

		mediaPlayer.setOnPreparedListener(this);
		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.setOnSeekCompleteListener(this);
		mediaPlayer.setOnErrorListener(this);
	}

	/******************************************************************
	 * 线程执行结束回调
	 * 
	 * @param listener
	 */
	public void setOnMediaPlayerManagerListener(
			OnMediaPlayerManagerListener listener) {
		onMediaPlayerManagerListener = listener;
	}

	/** 获得MediaPlayer对象 */
	public MediaPlayer getMediaPlayer() {
		if (mediaPlayer != null) {
			return mediaPlayer;
		} else {
			return new MediaPlayer();
		}
	}

	/** 联网加载数据 */
	public void myStart(String url) {

		if (TextUtils.isEmpty(url)) {
			return;
		}

		if (mediaPlayer != null) {
			try {
				MediaPlayerUtil.reset(mediaPlayer);
			} catch (Exception e) {
			}
		}
		MediaPlayerUtil.setDataSourceAndPrepare(mediaPlayer, url,
				new Runnable() {

					@Override
					public void run() {
						playFailed(mediaPlayer);
					}
				});
	}

	/**加载本地文件*/
	public void setDateFile(FileDescriptor fd) {
		MediaPlayerUtil.setDateFile(mediaPlayer, fd);
	}
	
	/** 暂停 */
	public void myPause() {
		if (mediaPlayer != null) {
			int a = mediaPlayer.getCurrentPosition();
			/**
			 * 如果isPrepared = true，说明准备好了（可能刚准备好或者正在播放）， 点击“暂停播放”，就正常暂停
			 * 
			 * 如果isPrepared = false，说明正在准备中。。。 点击“暂停按钮”，当准备好了，就不播放歌曲
			 */
			if (isPrepared) {
				mediaPlayer.pause();
			} else {
				isPauseed = true;
			}
		}
	}

	/** 重新开始播放 */
	public void myResume() {
		if (mediaPlayer != null) {
			isPauseed = false;
			if (isPrepared) {
				MediaPlayerUtil.start(mediaPlayer);
			}
		}
	}

	/** 停止播放 */
	public void myStop() {
		if (mediaPlayer != null) {
			MediaPlayerUtil.release(mediaPlayer);
			mediaPlayer = null;
		}
	}

	private void playFailed(MediaPlayer mp) {
		Toast.makeText(context, context.getString(R.string.play_error),
				Toast.LENGTH_LONG).show();
		MediaPlayerUtil.reset(mp);
		/*
		 * if (onMediaPlayerManagerListener != null) {
		 * onMediaPlayerManagerListener.onMediaPlayerManager(false); }
		 */
	}

	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		playFailed(mp);
		return true;
	}

	@Override
	public void onSeekComplete(MediaPlayer arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		MediaPlayerUtil.reset(mp);
		if (onMediaPlayerManagerListener != null) {
			onMediaPlayerManagerListener.onMediaPlayerManager(true);
		}
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		// TODO Auto-generated method stub
		try {
			/**
			 * 如果准备好了 isPrepared = true;
			 */
			isPrepared = true;
			int a = mediaPlayer.getDuration();
			if (isPauseed) {

			} else {
				MediaPlayerUtil.start(mediaPlayer);
				if (onMediaPlayerManagerListener != null) {
					onMediaPlayerManagerListener.onMediaPlayerPrepared();
				}
			}
		} catch (Exception e) {
			Toast.makeText(context, context.getString(R.string.play_error),
					Toast.LENGTH_LONG).show();
		}
	}

	/**开始播放本地数据*/
	public void startFile() {
		MediaPlayerUtil.start(mediaPlayer);
	}
	
	public interface OnMediaPlayerManagerListener {
		/**
		 * MediaPlayerManagerTask执行结束回调
		 * 
		 * @param isSuccess
		 *            true表示成功,否则否则表示失败.
		 */
		public void onMediaPlayerManager(boolean isSuccess);

		public void onMediaPlayerPrepared();
	}
}
