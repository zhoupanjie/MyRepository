package com.cplatform.xhxw.ui.ui.main.cms.video;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Handler;
import android.os.Message;
import android.view.Surface;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast.RadioBroadcastFragment;

public class Player implements SurfaceTextureListener,
		OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener,
		OnVideoSizeChangedListener {

	private int videoWidth;
	private int videoHeight;
	private static MediaPlayer mMediaPlayer;

	private static TextureView textureViewCorner;
	private static SeekBar seekBar;
	private Timer mTimer = new Timer();
	private static BaseAdapter adapter = null;

	public static int currentIndex = -1;
	public static int playPosition = -1;
	public boolean isPaused = false;
	public boolean isPlaying = true;
	public boolean isCorner = false;
	public boolean isEnd = false;
	private static boolean isDestroy = false;

	public static String path = "";
	private Surface s;
	private static ProgressBar loadingBar;
	private static RelativeLayout textureview_layout;
	private static TextView video_current_time;
	private static TextView video_duration_time;
	private ImageView video_pause;
	private static ProgressBar video_corner_progressbar;

	public static final int START_PLAY = 0;
	public static final int STOP_PLAY = 1;
	public static final int INIT_PLAY = 2;

	public static String zero = "00:00";

	public Player() {
		// initLayout(textureview_layout,adapter);
		initPlayer();
		mTimer.schedule(mTimerTask, 0, 500);
	}

	public void initLayout(RelativeLayout textureview_layout,
			BaseAdapter adapter) {
		this.adapter = adapter;
		this.textureview_layout = textureview_layout;
		textureViewCorner = (TextureView) textureview_layout
				.findViewById(R.id.textureview);
		textureViewCorner.setSurfaceTextureListener(this);
		seekBar = (SeekBar) textureview_layout.findViewById(R.id.video_seekbar);
		seekBar.setOnSeekBarChangeListener(seekBarListener);
		video_corner_progressbar = (ProgressBar) textureview_layout
				.findViewById(R.id.video_corner_progressbar);
		loadingBar = (ProgressBar) textureview_layout
				.findViewById(R.id.video_loading_progressbar);
		video_current_time = (TextView) textureview_layout
				.findViewById(R.id.video_current_time);
		video_duration_time = (TextView) textureview_layout
				.findViewById(R.id.video_duration_time);
		video_pause = (ImageView) textureview_layout
				.findViewById(R.id.video_pause);
		video_pause.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mMediaPlayer != null) {
					if (mMediaPlayer.isPlaying()) {
						mMediaPlayer.pause();
						video_pause
								.setImageResource(R.drawable.video_continue_play_bg);
					} else {
						mMediaPlayer.start();
						video_pause.setImageResource(R.drawable.video_pause_bg);
					}
				}
			}
		});
	}

	public void initPlayer() {
		if (mMediaPlayer == null) {
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mMediaPlayer.setOnErrorListener(mErrorListener);
			mMediaPlayer.setOnBufferingUpdateListener(this);
			mMediaPlayer.setOnCompletionListener(this);
			mMediaPlayer.setOnPreparedListener(this);
			mMediaPlayer.setOnVideoSizeChangedListener(this);
			isDestroy = false;
			if (s != null) {
				mMediaPlayer.setSurface(s);
			}
		}
	}

	/*******************************************************
	 * 通过定时器和Handler来更新进度条
	 ******************************************************/
	TimerTask mTimerTask = new TimerTask() {

		@Override
		public void run() {
			if (isDestroy == true || mMediaPlayer == null || seekBar == null) {
				return;
			}
			if (mMediaPlayer.isPlaying() && seekBar.isPressed() == false) {
				handleProgress.sendEmptyMessage(START_PLAY);
				return;
			}
		}
	};

	static Handler handleProgress = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case START_PLAY:
				if (mMediaPlayer != null && seekBar != null
						&& textureview_layout != null
						&& textureViewCorner != null
						&& video_current_time != null
						&& video_duration_time != null && loadingBar != null
						&& video_corner_progressbar != null) {

					int position = mMediaPlayer.getCurrentPosition();
					int duration = mMediaPlayer.getDuration();
					playPosition = position;
					if (duration > 0) {
						if (position > 0) {
							if (textureview_layout.getVisibility() == View.INVISIBLE) {
								textureview_layout.setVisibility(View.VISIBLE);
							}
							if (textureViewCorner.getVisibility() == View.INVISIBLE) {
								textureViewCorner.setVisibility(View.VISIBLE);
							}
						}
						String currentTime = getTime(position, duration);
						video_current_time.setText(currentTime);
						String durationTime = getTime(duration, duration);
						video_duration_time.setText(durationTime);
						long pos = seekBar.getMax() * position / duration;
						seekBar.setProgress((int) pos);
						video_corner_progressbar.setProgress((int) pos);
						if (loadingBar.getVisibility() == View.VISIBLE) {
							loadingBar.setVisibility(View.GONE);
						}
						if (duration - position < 1000) {
							Message msg2 = new Message();
							msg2.what = STOP_PLAY;
							msg2.obj = durationTime;
							handleProgress.sendMessageDelayed(msg2, 1000);
						}
					}
				}
				break;
			case STOP_PLAY:
				String duration = (String) msg.obj;
				if (mMediaPlayer != null) {
					mMediaPlayer.stop();
					mMediaPlayer.reset();
				}
				if (textureview_layout != null && video_current_time != null) {
					textureview_layout.setVisibility(View.INVISIBLE);
					video_current_time.setText(duration);
				}
				currentIndex = -1;
				playPosition = -1;
				path = "";
				if (adapter != null) {
					adapter.notifyDataSetChanged();
				}
				handleProgress.sendEmptyMessageDelayed(INIT_PLAY, 300);
				break;
			case INIT_PLAY:
				if (video_current_time != null && video_duration_time != null
						&& seekBar != null && video_corner_progressbar != null) {
					video_current_time.setText(zero);
					video_duration_time.setText(zero);
					seekBar.setSecondaryProgress(0);
					seekBar.setProgress(0);
					video_corner_progressbar.setSecondaryProgress(0);
					video_corner_progressbar.setProgress(0);
				}
				break;
			default:
				break;
			}

		};
	};

	OnSeekBarChangeListener seekBarListener = new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			if (mMediaPlayer != null) {
				int progress = seekBar.getProgress();
				int position = progress * mMediaPlayer.getDuration() / 100;
				mMediaPlayer.seekTo(position);
			}
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			if (fromUser) {
				if (mMediaPlayer != null
						&& !video_duration_time.getText().equals(zero)) {
					int duration = mMediaPlayer.getDuration();
					int position = progress * duration / 100;
					String currentTime = getTime(position, duration);
					video_current_time.setText(currentTime);
				}
			}
		}
	};

	// *****************************************************

	public void play(String videoUrl) {
		// if(playPosition>0)
		// {
		// mMediaPlayer.seekTo(playPosition);
		// mMediaPlayer.start();
		// return;
		// }
		// videoUrl =
		// "http://static.xw.feedss.com/uploadfile/videos/20150806/2554341be40646910911552ea494edab.mp4";
		RadioBroadcastFragment.OnPausePlay();
		if (path.equals(videoUrl)) {
			return;
		}
		handleProgress.sendEmptyMessage(INIT_PLAY);
		loadingBar.setVisibility(View.VISIBLE);
		textureViewCorner.setVisibility(View.INVISIBLE);
		path = videoUrl;
		// Surface s = new Surface(textureView.getSurfaceTexture());

		// textureViewCorner.onDetachedFromWindow();
		// SurfaceTexture s = textureViewCorner.getSurfaceTexture();
		// if (s != null) {
		// s.
		// s.updateTexImage();
		// }
		// mMediaPlayer.setSurface(s);

		try {
			initPlayer();
			if (mMediaPlayer.isPlaying()) {
				mMediaPlayer.stop();
			}
			mMediaPlayer.reset();
			mMediaPlayer.setDataSource(path);
			mMediaPlayer.prepareAsync();// prepare之后自动播放
			// mMediaPlayer.start();
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
	}

	public void pause() {
		if (mMediaPlayer != null) {
			mMediaPlayer.pause();
			playPosition = mMediaPlayer.getCurrentPosition();
		}
	}

	public void stop() {
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			playPosition = 0;
			handleProgress.sendEmptyMessage(STOP_PLAY);
		}
	}

	public void destroy() {
		mTimer.cancel();
		release();
		adapter = null;
	}

	public static void release() {
		isDestroy = true;
		if (mMediaPlayer != null) {
			mMediaPlayer.stop();
			mMediaPlayer.release();
			mMediaPlayer = null;
			playPosition = 0;
			handleProgress.sendEmptyMessage(STOP_PLAY);
		}
	}

	public int getCurrentPosition() {
		return playPosition;
	}

	@Override
	/**  
	 * 通过onPrepared播放  
	 */
	public void onPrepared(MediaPlayer arg0) {
		if (mMediaPlayer != null) {
			videoWidth = mMediaPlayer.getVideoWidth();
			videoHeight = mMediaPlayer.getVideoHeight();
		}
		if (videoHeight != 0 && videoWidth != 0) {
			arg0.start();
		}

	}

	@Override
	public void onCompletion(MediaPlayer arg0) {
		// loadingBar.setVisibility(View.GONE);
		// if (mMediaPlayer != null) {
		// mMediaPlayer.seekTo(0);
		// mMediaPlayer.stop();
		// currentIndex = -1;
		// isPaused = false;
		// isPlaying = false;
		// holder.mProgressBar.setVisibility(View.GONE);
		// adapter.notifyDataSetChanged();
		// playPosition = 0;
		// }
		// position = 0;
	}

	@Override
	public void onBufferingUpdate(MediaPlayer arg0, int bufferingProgress) {
		// seekBar.setSecondaryProgress(bufferingProgress);
		// video_corner_progressbar.setSecondaryProgress(bufferingProgress);
		// int currentProgress = seekBar.getMax()
		// * mMediaPlayer.getCurrentPosition()
		// / mMediaPlayer.getDuration();
		// Log.e(currentProgress + "% play", bufferingProgress + "% buffer");

	}

	@Override
	public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSurfaceTextureAvailable(SurfaceTexture surface, int width,
			int height) {
		s = new Surface(surface);
		if (mMediaPlayer != null) {
			mMediaPlayer.setSurface(s);
		}
	}

	@Override
	public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width,
			int height) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
		return false;
	}

	@Override
	public void onSurfaceTextureUpdated(SurfaceTexture surface) {
		// textureViewCorner.setBackground(null);
		// textureViewCorner.draw(null);
		// textureViewCorner.setDrawingCacheEnabled(true);
		// textureViewCorner.destroyDrawingCache();
		// textureViewCorner.refreshDrawableState();
		// textureViewCorner.invalidate();
		// surface.release();
		// s = new Surface(surface);
		// mMediaPlayer.setSurface(s);
	}

	public static String getTime(long milliseconds, long duration) {
		if (milliseconds > duration || milliseconds < 0) {
			return zero;
		}
		int time = (int) milliseconds / 1000;
		int hour = 0;
		int min = time / 60;
		int second = time % 60;
		String hourStr = "";
		String minStr = "";
		String secondStr = "";
		if (min >= 60) {
			hour = min / 60;
			min = min % 60;
		}

		if (min < 10) {
			minStr = "0" + min + ":";
		} else {
			minStr = min + ":";
		}
		if (second < 10) {
			secondStr = "0" + second;
		} else {
			secondStr = second + "";
		}
		if (hour > 0) {
			if (hour < 10) {
				hourStr = "0" + hour + ":";
			} else {
				hourStr = hour + ":";
			}
		}
		return hourStr + minStr + secondStr;

	}

	private MediaPlayer.OnErrorListener mErrorListener = new MediaPlayer.OnErrorListener() {
		public boolean onError(MediaPlayer mp, int framework_err, int impl_err) {
			if (framework_err == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
				if (mMediaPlayer != null) {
					mMediaPlayer.reset();// 可调用此方法重置
				}
			} else if (framework_err == MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK) {
			} else if (framework_err == MediaPlayer.MEDIA_ERROR_UNKNOWN) {
				// mMediaPlayer.reset();
			}
			return true;
		}
	};

}