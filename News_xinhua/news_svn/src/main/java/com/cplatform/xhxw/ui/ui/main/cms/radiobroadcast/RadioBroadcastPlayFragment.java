package com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast;

import java.util.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.ui.base.BaseFragment;
import com.cplatform.xhxw.ui.ui.main.HomeActivity;
import com.cplatform.xhxw.ui.ui.main.cms.game.GameUtil;

/**
 * 
 * @ClassName PlayFragment
 * @Description TODO 广播播放
 * @Version 1.0
 * @Author zxe
 * @Creation 2015年8月18日 下午8:39:07
 * @Mender zxe
 * @Modification 2015年8月18日 下午8:39:07
 * @Copyright Copyright © 2013 - 2015 Channelsoft (Beijing) Technology Co.,
 *            Ltd.All Rights Reserved.
 * 
 */
public class RadioBroadcastPlayFragment extends BaseFragment implements
		OnClickListener {
	private static Context con;
	RotationImageView civMid;
	RelativeLayout flPlay;
	RelativeLayout rlStylusNeedle;
	ImageView ivStylusNeedle;
	public static ImageView ivLast;
	public static ImageView ivPlay;
	public static ImageView ivNext;
	LinearLayout llSpectrum;// 频谱
	ImageView ivSpectrum;// 频谱
	ImageView ivSpectrumDefault;
	SeekBar seekBarPlay;
	private TextView tvTimeSum;
	private TextView tvTimePlay;
	AnimationDrawable adSpectrum;
	

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_radiobroadcast_play,
				container, false);
		con = this.getActivity();
		civMid = (RotationImageView) rootView.findViewById(R.id.civ_mid);
		rlStylusNeedle = (RelativeLayout) rootView.findViewById(R.id.rl_stylus);
		ivStylusNeedle = (ImageView) rootView
				.findViewById(R.id.iv_stylus_head_needle);
		ivLast = (ImageView) rootView.findViewById(R.id.iv_last);
		ivPlay = (ImageView) rootView.findViewById(R.id.iv_play);
		ivNext = (ImageView) rootView.findViewById(R.id.iv_next);
		flPlay = (RelativeLayout) rootView.findViewById(R.id.rl_play);
		ivSpectrum = (ImageView) rootView.findViewById(R.id.iv_spectrum);
		ivSpectrumDefault = (ImageView) rootView
				.findViewById(R.id.iv_spectrum_default);
		seekBarPlay = (SeekBar) rootView.findViewById(R.id.seekbar_play);
		seekBarPlay.setEnabled(false);
		tvTimeSum = (TextView) rootView.findViewById(R.id.tv_time_sum);
		tvTimePlay = (TextView) rootView.findViewById(R.id.tv_time_play);
		llSpectrum = (LinearLayout) rootView.findViewById(R.id.ll_spectrum);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		ivLast.setOnClickListener(this);
		ivPlay.setOnClickListener(this);
		ivNext.setOnClickListener(this);
		flPlay.setOnClickListener(this);
		ivPlay.setImageResource(R.drawable.radiobroadcast_play_bg);
		seekBarPlay.setOnSeekBarChangeListener(new SeekBarChangeEvent());
		RadioBroadcastFragment.player.setHandlerPlay(mHandlerPlay);
	}

	@Override
	public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
		// TODO Auto-generated method stub
		return super.onCreateAnimation(transit, enter, nextAnim);
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d("广播播放界面", "onPause");
	}

	@Override
	public void onResume() {
		// initChannels();
		Log.d("广播播放界面", "onResume");
		super.onResume();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.iv_last: {// 上一首
			RadioBroadcastFragment.getIrpc().setLastPlay();
		}
			break;
		case R.id.iv_play: {// 播放
			setPlayButton();
		}
			break;
		case R.id.iv_next: {// 下一首
			RadioBroadcastFragment.getIrpc().setNextPlay();
		}
			break;
		}
	}

	/**
	 * 
	 * @Name setPlayButton
	 * @Description TODO 播放
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月22日 下午5:28:23
	 * 
	 */
	void setPlayButton() {
		if (GameUtil.isConnect(con)) {
			if (HomeActivity.isPlayRadio) {// 判断是否为推荐广播播放
				RadioBroadcastFragment.setAutoPlay();
				RadioBroadcastFragment.irpc.firstLoadData();
			} else {
				setEnabled(false);
				if (RadioBroadcastFragment.player.isPlaying()) {// 停止播放
					ivPlay.setImageResource(R.drawable.radiobroadcast_play_bg);
					RadioBroadcastFragment.player.pause();
				} else {// 播放
					ivPlay.setImageResource(R.drawable.radiobroadcast_pause_bg);
					RadioBroadcastFragment.player.play();
				}
			}
		} else {
			RadioBroadcastUtil.showTipInformationDialog(con,
					R.string.text_play_tips, R.string.text_no_network);
		}
	}

	public void setPauseUI() {
		if (civMid != null && civMid.isRotation()) {
			// Toast.makeText(con, "停止播放", Toast.LENGTH_SHORT).show();
			civMid.stopRotation();
			if (adSpectrum.isRunning()) {
				adSpectrum.stop();
				ivSpectrum.setVisibility(View.GONE);
				ivSpectrumDefault.setVisibility(View.VISIBLE);
			}
			removeStylus();
			ivPlay.setImageResource(R.drawable.radiobroadcast_play_bg);
		}
	}

	/**
	 * 
	 * @Name setSpectrumAnimationDrawable
	 * @Description TODO 设置频谱动画背景
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月23日 上午10:21:37
	 * 
	 */
	public void setSpectrumAnimationDrawable() {
		adSpectrum = new AnimationDrawable();
		Log.d("频谱帧数", RadioBroadcastUtil.NUM_FRAME + "");
		for (int i = 0; i < RadioBroadcastUtil.NUM_FRAME; i++) {
			while (true) {
				int number = new Random()
						.nextInt(RadioBroadcastUtil.ARRAY_SPECTRUM.length);
				int d = RadioBroadcastUtil.ARRAY_SPECTRUM[number];
				// Log.e("第" + number + "个", d + "");
				Drawable drawable = getResources().getDrawable(d);
				adSpectrum.addFrame(drawable, RadioBroadcastUtil.DURATION_SPECTRUM);
				break;
			}
		}
		adSpectrum.setOneShot(false);
	}

	@SuppressLint("NewApi")
	public void playSpectrum() {
		ivSpectrum.setBackground(adSpectrum);
		ivSpectrumDefault.setVisibility(View.GONE);
		ivSpectrum.setVisibility(View.VISIBLE);
		adSpectrum.start();
	}

	public void setStopUI() {
		if (civMid != null && civMid.isRotation()) {
			civMid.stopRotation();
			if (adSpectrum.isRunning()) {
				adSpectrum.stop();
				adSpectrum = null;
				ivSpectrum.setVisibility(View.GONE);
				ivSpectrumDefault.setVisibility(View.VISIBLE);
			}
			shakeStylus();
			ivPlay.setImageResource(R.drawable.radiobroadcast_play_bg);
		}
	}

	public static void setEnabled(boolean isEnable) {
		if (ivPlay != null) {
			ivPlay.setEnabled(isEnable);
		}
		if (ivLast != null) {
			ivLast.setEnabled(isEnable);
		}
		if (ivNext != null) {
			ivNext.setEnabled(isEnable);
		}
	}

	public void setPlayUI(String audioid, String name) {
		ivPlay.setImageResource(R.drawable.radiobroadcast_pause_bg);
		moveStylus();
		civMid.setAudioid(audioid);
		civMid.setDescription(name);
		civMid.startRotation();
		playSpectrum();
	}

	public void removeStylus() {
		long begin = 20;
		int end = 0;
		// 这个是设置需要旋转的角度（也是初始化），我设置的是当前需要旋转的角度
		RotateAnimation rotateAnimation = new RotateAnimation(begin, end,
				Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f);
		// 这个是设置动画时间的
		rotateAnimation.setDuration(500);
		// 动画执行完毕后是否停在结束时的角度上
		rotateAnimation.setFillAfter(true);
		// 启动动画
		ivStylusNeedle.startAnimation(rotateAnimation);
	}

	public void shakeStylus() {
		Animation shake = AnimationUtils.loadAnimation(con,
				R.anim.anim_stylus_shake);
		shake.reset();
		shake.setFillAfter(true);
		ivStylusNeedle.startAnimation(shake);
	}

	public void moveStylus() {

		long begin = 0;
		int end = 20;
		// 这个是设置需要旋转的角度（也是初始化），我设置的是当前需要旋转的角度
		RotateAnimation rotateAnimation = new RotateAnimation(begin, end,
				Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f);
		// 这个是设置动画时间的
		rotateAnimation.setDuration(500);
		// 动画执行完毕后是否停在结束时的角度上
		rotateAnimation.setFillAfter(true);
		// 启动动画
		ivStylusNeedle.startAnimation(rotateAnimation);
	}

	class SeekBarChangeEvent implements OnSeekBarChangeListener {
		int progress;

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {

		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// 通知用户已经开始一个触摸拖动手势。客户端可能需要使用这个来禁用seekbar的滑动功能。
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// seekTo()的参数是相对与影片时间的数字，而不是与seekBar.getMax()相对的数字
			// 通知用户触摸手势已经结束。户端可能需要使用这个来启用seekbar的滑动功能。
			progress = seekBar.getProgress()
					* RadioBroadcastFragment.player.mediaPlayer.getDuration()
					/ seekBar.getMax();
			RadioBroadcastFragment.player.mediaPlayer.seekTo(progress);
		}

	}

	@SuppressLint("HandlerLeak")
	private Handler mHandlerPlay = new Handler() {
		public void handleMessage(Message msg) {
			final DataRadioBroadcast drb = RadioBroadcastFragment.player
					.getCurDataRadioBroadcast();
			setEnabled(true);
			if (drb != null) {
				switch (msg.what) {
				case RadioBroadcastUtil.STATE_RADIO_PLAY:
					civMid.stopRotation();
					RadioBroadcastFragment.tvTitle.setText(drb.getName());
					seekBarPlay.setEnabled(true);
					RadioBroadcastFragment.player.setSeekBar(seekBarPlay);
					RadioBroadcastFragment.player.setTvPlayTime(tvTimePlay);
					String timeSum = RadioBroadcastFragment.player
							.getTimeDuration();
					if (RadioBroadcastFragment.player.mediaPlayer != null) {
						// int d = RadioBroadcastFragment.player.mediaPlayer
						// .getDuration();
						// numFrame = d / durationSpectrum;
						Log.d("总时长", timeSum);
						tvTimeSum.setText(timeSum);
						setSpectrumAnimationDrawable();
					} else {
						Log.e("RadioBroadcastPlayFragment-->BroadcastReceiver",
								"MediaPlayer为空");
					}
					setPlayUI(drb.getAudioid(), drb.getName());
					civMid.startRotation();
					break;
				case RadioBroadcastUtil.STATE_RADIO_PAUSE:
					setPauseUI();
					break;
				case RadioBroadcastUtil.STATE_RADIO_STOP:
					setStopUI();
					break;
				case RadioBroadcastUtil.STATE_RADIO_PLAY_AUTO:
					HomeActivity.isPlayRadio = false;
					RadioBroadcastPlayFragment.setEnabled(false);
					new Thread(new Runnable() {

						@Override
						public void run() {
							RadioBroadcastFragment.player
									.setCurDataRadioBroadcast(drb);
							RadioBroadcastFragment.player.setPlayData(drb
									.getUrl());
							RadioBroadcastFragment.player.onResumePlay();
						}
					}).start();
					break;
				case RadioBroadcastUtil.STATE_RADIO_PLAY_NONETWORK:
					if (RadioBroadcastFragment.player.isPlaying()) {// 停止播放
						RadioBroadcastFragment.player.pause();
					}
					RadioBroadcastUtil.showNoNetworkDialog(con);
					break;
				}
			}
			// setBroadcastData(msg);
			super.handleMessage(msg);
		}
	};
}