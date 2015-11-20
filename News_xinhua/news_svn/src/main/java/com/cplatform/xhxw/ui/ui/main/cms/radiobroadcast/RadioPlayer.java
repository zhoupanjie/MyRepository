package com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cplatform.xhxw.ui.ui.main.cms.game.GameUtil;
import com.cplatform.xhxw.ui.ui.main.cms.video.Player;
import com.cplatform.xhxw.ui.util.ListUtil;

/**
 * 
 * @ClassName RadioPlayer
 * @Description TODO 广播播放器
 * @Version 1.0
 * @Author zxe
 * @Creation 2015年9月23日 上午10:26:19
 * @Mender zxe
 * @Modification 2015年9月23日 上午10:26:19
 * @Copyright Copyright © 2013 - 2015 Channelsoft (Beijing) Technology Co.,
 *            Ltd.All Rights Reserved.
 * 
 */
public class RadioPlayer implements OnBufferingUpdateListener,
		OnCompletionListener, OnPreparedListener, OnErrorListener {
	public MediaPlayer mediaPlayer; // 媒体播放器
	private SeekBar seekBar; // 拖动条
	private Timer mTimer = new Timer(); // 计时器
	private DataRadioBroadcast curDataRadioBroadcast;
	private List<DataRadioBroadcast> listData;
	private int curIndex;
	private Context con;
	private TextView tvPlayTime;
	private String timeDuration;
	private String audioid;
	private boolean isPlay = false;
	private Handler handlerPlay;
	private Handler handlerList;

	/**
	 * 
	 * @Name Player
	 * @Description TODO 初始化播放器
	 * @param context
	 * @Date 2015年9月6日 下午5:05:19
	 * 
	 */
	public RadioPlayer(Context context) {
		super();
		con = context;
		initMediaPlay();
		mTimer.schedule(timerTask, 0, 1000);
		// 每一秒触发一次
	}

	public void initMediaPlay() {
		try {
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);// 设置媒体流类型
			mediaPlayer.setOnBufferingUpdateListener(this);
			mediaPlayer.setOnPreparedListener(this);
			mediaPlayer.setOnCompletionListener(this);
			mediaPlayer.setOnErrorListener(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Name setPlayError
	 * @Description TODO 播放错误设置
	 * @param error
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月18日 上午11:17:09
	 * 
	 */
	void setPlayError(int error) {
		switch (error) {
		case MediaPlayer.MEDIA_ERROR_UNKNOWN: {
			// 播放错误，未知错误。 常量值：0
			Log.e("播放错误", "播放错误，未知错误");
		}
			break;
		case MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK: {
			// 播放错误（一般视频播放比较慢或视频本身有问题会引发）。常量值：200
			Log.e("播放错误", "播放错误（一般视频播放比较慢或视频本身有问题会引发）");
		}
			break;
		case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING: {
			// 视频过于复杂，无法解码：不能快速解码帧。此时可能只能正常播放音频。参见MediaPlayer.OnInfoListener。
			// 常量值：700
			Log.e("播放错误", "视频过于复杂，无法解码：不能快速解码帧");
		}
			break;
		case MediaPlayer.MEDIA_INFO_BUFFERING_START: {
			// MediaPlayer暂停播放等待缓冲更多数据。 常量值：701
			Log.e("播放错误", "MediaPlayer暂停播放等待缓冲更多数据");
		}
			break;
		case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE: {
			// 媒体不支持Seek，例如直播流。常量值：801
			Log.e("播放错误", "媒体不支持Seek，例如直播流");
		}
			break;
		}
	}

	/**
	 * 
	 * @Name updatePlay
	 * @Description TODO 更新ui
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月19日 下午5:56:07
	 */
	public void updatePlay() {
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
			if (GameUtil.isConnect(con)) {
				Log.e("网络状态", "已连接");
				if (seekBar != null && seekBar.isPressed() == false) {
					if (RadioBroadcastUtil.isBeyondPlayTime()) {
						pause();
						RadioBroadcastUtil.SETTING_RADIO_TIMER_END = RadioBroadcastUtil.SETTING_RADIO_TIME_NOLIMIT;
					} else {
						handler.sendEmptyMessage(0); // 发送消息
					}
				}
			} else {
				Log.e("网络状态", "已断开");
				if (!RadioBroadcastUtil.isShowNoNetwork) {
					updateUI(RadioBroadcastUtil.STATE_RADIO_PLAY_NONETWORK);
				}

			}
		}
	}

	/**
	 * 计时器
	 */
	TimerTask timerTask = new TimerTask() {

		@Override
		public void run() {
			updatePlay();
		}
	};

	/**
	 * 更新进度
	 */
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (mediaPlayer != null && seekBar != null) {
				int position = mediaPlayer.getCurrentPosition();
				int duration = mediaPlayer.getDuration();
				if ((duration > 0) && seekBar != null) {
					Log.d("播放id" + curDataRadioBroadcast.getName(), "position"
							+ position + "&&duration" + duration);
					if (position != duration) {
						// 计算进度（获取进度条最大刻度*当前音乐播放位置 / 当前音乐时长）
						long pos = seekBar.getMax() * position / duration;
						Log.d("更新进度条进度为" + curDataRadioBroadcast.getName(), pos
								+ "");
						seekBar.setProgress((int) pos);
						if (null != tvPlayTime) {
							String playTime = RadioBroadcastUtil
									.getFormatPlayTime(position);
							Log.d("播放id" + curDataRadioBroadcast.getName(),
									playTime);
							tvPlayTime.setText(playTime);
						}
					}

				}
			}
		};
	};

	/**
	 * 
	 * @Name play
	 * @Description TODO 播放
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月6日 下午5:04:53
	 * 
	 */
	public void play() {
		Log.e("播放状态", "播放");
		if ((getCurDataRadioBroadcast() == null)
				&& (!ListUtil.isEmpty(listData))) {
			curIndex = 0;
			if (ListUtil.isEmpty(listData)) {//若列表为空，播放下一条
				RadioBroadcastFragment.irpc.setNextPlay();
			} else {
				setCurDataRadioBroadcast(listData.get(curIndex));
				playUrl(curDataRadioBroadcast.getUrl());
			}
		} else {
			onResumePlay();
		}
	}

	/**
	 * 设置自动播放
	 * 
	 * @Name setAutoPlay
	 * @Description TODO
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月9日 下午5:38:13
	 * 
	 */
	void onResumePlay() {
		RadioBroadcastPlayFragment.setEnabled(false);
		Player.release();
		if (RadioBroadcastUtil.isShowNoNetwork) {
			RadioBroadcastUtil.isShowNoNetwork = false;
		}
		if (mediaPlayer != null) {
			updateUI(RadioBroadcastUtil.STATE_RADIO_PLAY);
			mediaPlayer.start();
		}
	}

	/**
	 * 
	 * @Name playByIndex
	 * @Description TODO 播放指定位置
	 * @param index
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月6日 下午5:12:50
	 * 
	 */
	public void playByIndex(int index) {
		curIndex = index;
		setCurDataRadioBroadcast(listData.get(curIndex));
		playUrl(curDataRadioBroadcast.getUrl());
	}

	/**
	 * 
	 * @Name playUrl
	 * @Description TODO 根据url播放
	 * @param url
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月6日 下午5:07:02
	 * 
	 */
	public void playUrl(final String url) {
		RadioBroadcastPlayFragment.setEnabled(false);
		Player.release();
		if (RadioBroadcastUtil.isShowNoNetwork) {
			RadioBroadcastUtil.isShowNoNetwork = false;
		}
		// if (mediaPlayer == null) {
		// initMediaPlay();
		// }
		new Thread(new Runnable() {

			@Override
			public void run() {
				setPlayData(url);
				updateUI(RadioBroadcastUtil.STATE_RADIO_PLAY);
			}
		}).start();

	}

	/**
	 * 
	 * @Name nextPlay
	 * @Description TODO 下一条
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月6日 下午5:07:21
	 * 
	 */
	public void nextPlay() {
		if (curIndex < (listData.size() - 1)) {
			curIndex++;
			setCurDataRadioBroadcast(listData.get(curIndex));
			playUrl(curDataRadioBroadcast.getUrl());
		} else {
			pause();
		}
	}

	/**
	 * 
	 * @Name lastPlay
	 * @Description TODO 上一条
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月6日 下午5:07:31
	 * 
	 */
	public void lastPlay() {
		if (curIndex > 0) {
			curIndex--;
			setCurDataRadioBroadcast(listData.get(curIndex));
			playUrl(curDataRadioBroadcast.getUrl());
		} else {
			pause();
		}
	}

	/**
	 * 是否播放
	 * 
	 * @Name isPlaying
	 * @Description TODO
	 * @return
	 * @return boolean
	 * @Author zxe
	 * @Date 2015年9月9日 下午5:37:57
	 * 
	 */
	boolean isPlaying() {
		return mediaPlayer != null && mediaPlayer.isPlaying() ? true : false;
	}

	/**
	 * 
	 * @Name setPlayData
	 * @Description TODO 设置播放器
	 * @param url
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月6日 下午5:06:14
	 * 
	 */
	public void setPlayData(String url) {
		if (mediaPlayer != null) {

			mediaPlayer.reset();
			try {
				mediaPlayer.setDataSource(url);// 设置数据源
				mediaPlayer.prepare(); // prepare自动播放
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			int d = mediaPlayer.getDuration();
			setTimeDuration(RadioBroadcastUtil.getFormatPlayTime(d));
		}
	}

	/**
	 * 
	 * @Name pause
	 * @Description TODO 暂停
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月6日 下午5:07:48
	 * 
	 */
	public void pause() {
		Log.e("播放状态", "暂停");
		mediaPlayer.pause();
		updateUI(RadioBroadcastUtil.STATE_RADIO_PAUSE);
	}

	/**
	 * 
	 * @Name stop
	 * @Description TODO 停止
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月6日 下午5:08:00
	 * 
	 */
	public void stop() {
		close();
		updateUI(RadioBroadcastUtil.STATE_RADIO_STOP);
	}

	public void close() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mTimer.cancel();
			mediaPlayer.release();
			mediaPlayer = null;
			mTimer.cancel();
			// mTimer=null;
		}
	}

	/**
	 * 
	 * @Name OnRelease
	 * @Description TODO 可以释放播放器占用的资源，一旦确定不再使用播放器时应当尽早调用它释放资源。
	 * @return void
	 * @Author zxe
	 * @Date 2015年8月24日 下午5:23:51
	 * 
	 */
	public void OnRelease() {
		if (mediaPlayer != null) {
			mediaPlayer.release();
		}

	}

	/**
	 * 
	 * @Name OnReset
	 * @Description TODO 可以使播放器从Error状态中恢复过来，重新会到Idle状态。
	 * @return void
	 * @Author zxe
	 * @Date 2015年8月24日 下午5:24:28
	 * 
	 */
	public void OnReset() {
		if (mediaPlayer != null) {
			mediaPlayer.reset();
		}
	}

	/**
	 * 
	 * @Name isEndList
	 * @Description TODO 当前播放是否为最后一条
	 * @return
	 * @return boolean
	 * @Author zxe
	 * @Date 2015年9月6日 下午5:09:17
	 * 
	 */
	public boolean isEndList() {
		if (ListUtil.isEmpty(listData)) {
			return true;
		} else {
			return curIndex == (listData.size() - 1);
		}
	}

	public SeekBar getSeekBar() {
		return seekBar;
	}

	public void setSeekBar(SeekBar seekBar) {
		this.seekBar = seekBar;
	}

	public TextView getTvPlayTime() {
		return tvPlayTime;
	}

	public void setTvPlayTime(TextView tvPlayTime) {
		this.tvPlayTime = tvPlayTime;
	}

	public MediaPlayer getMediaPlayer() {
		return mediaPlayer;
	}

	public void setMediaPlayer(MediaPlayer mediaPlayer) {
		this.mediaPlayer = mediaPlayer;
	}

	public DataRadioBroadcast getCurDataRadioBroadcast() {
		return curDataRadioBroadcast;
	}

	public void setCurDataRadioBroadcast(
			DataRadioBroadcast curDataRadioBroadcast) {
		this.curDataRadioBroadcast = curDataRadioBroadcast;
		setAudioid(curDataRadioBroadcast.getAudioid());
	}

	public List<DataRadioBroadcast> getListData() {
		return listData;
	}

	public void setListData(List<DataRadioBroadcast> listData) {
		this.listData = listData;
	}

	public int getCurIndex() {
		return curIndex;
	}

	public void setCurIndex(int curIndex) {
		this.curIndex = curIndex;
	}

	public String getAudioid() {
		return audioid;
	}

	public void setAudioid(String audioid) {
		this.audioid = audioid;
	}

	public String getTimeDuration() {
		return timeDuration;
	}

	public void setTimeDuration(String timeDuration) {
		this.timeDuration = timeDuration;
	}

	public boolean isPlay() {
		return isPlay;
	}

	public void setPlay(boolean isPlay) {
		this.isPlay = isPlay;
	}

	public void updateUI(final int state) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message messagePlay = Message.obtain();
				messagePlay.what = state;
				Message messageList = Message.obtain();
				messageList.what = state;
				if (getHandlerPlay() != null) {
					getHandlerPlay().sendMessage(messagePlay);
				}
				if (getHandlerList() != null) {
					getHandlerList().sendMessage(messageList);
				}
			}
		}).run();

	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		mp.start();
		Log.e("mediaPlayer", "onPrepared");
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		Log.e("mediaPlayer", "onCompletion");
		updateUI(RadioBroadcastUtil.STATE_RADIO_COMPLETION);
	}

	/**
	 * 
	 * @Name onBufferingUpdate
	 * @Description TODO 缓冲更新
	 * @param mp
	 * @param percent
	 * @see android.media.MediaPlayer.OnBufferingUpdateListener#onBufferingUpdate(android.media.MediaPlayer, int)
	 * @Date 2015年9月23日 下午2:15:45
	*
	 */
	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		if (seekBar != null) {
			// seekBar.setSecondaryProgress(percent);
			// int currentProgress = seekBar.getMax()
			// * mediaPlayer.getCurrentPosition()
			// / mediaPlayer.getDuration();
			// Log.d(currentProgress + "% play", percent + " buffer");
		}
	}

	@Override
	public boolean onError(MediaPlayer arg0, int arg1, int arg2) {

		// TODO Auto-generated method stub
		Log.e("广播播放器", "错误监听" + arg1);
		setPlayError(arg1);
		pause();
		return false;
	}

	public Handler getHandlerPlay() {
		return handlerPlay;
	}

	public void setHandlerPlay(Handler handlerPlay) {
		this.handlerPlay = handlerPlay;
	}

	public Handler getHandlerList() {
		return handlerList;
	}

	public void setHandlerList(Handler handlerList) {
		this.handlerList = handlerList;
	}

}
