package com.cplatform.xhxw.ui.ui.detailpage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.PreferencesManager;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.ui.apptips.TipsActivity;
import com.cplatform.xhxw.ui.ui.apptips.TipsUtil;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.main.forelanguage.utils.LanguageUtil;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;

/**
 * 新闻详情 Created by cy-love on 13-12-29.
 */
public class NewsPageActivity extends BaseActivity {
	// private static final int MSG_SHOW_VIDEO = 1;
	// private static final int MSG_HIDE_VIDEO = 2;
	//
	// private TextureView mTextureView;
	// private Button mRotateBtn;
	// private Surface mSurface;
	// private boolean isPlayVideo = false;
	// private boolean isVideoFullScreen = false;
	private NewsPageFragment mContent;

	// private String mVideoUrl = null;
	//
	// private MediaPlayer mMediaPlayer;
	//
	// private Handler mHandler = new Handler() {
	//
	// @Override
	// public void handleMessage(Message msg) {
	// if (msg.what == MSG_SHOW_VIDEO) {
	// mVideoUrl = msg.getData().getString("url");
	// addVideoPlayer(msg.getData().getString("url"));
	// } else if (msg.what == MSG_HIDE_VIDEO) {
	// removeVideoPlayer();
	// }
	// }
	// };
	public static Intent getIntent(Context context, String newsid,
			boolean isEnterprise) {
		Intent intent = new Intent(context, NewsPageActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("newsid", newsid);
		bundle.putBoolean("isEnterprise", isEnterprise);
		intent.putExtras(bundle);
		return intent;
	}

	@Override
	protected String getScreenName() {
		return "NewsPageActivity";
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_page);
		boolean isPush = getIntent().getBooleanExtra("isPush", false);
		if (isPush) {
			App.getPreferenceManager().messageNewCountMinus();
		}
		// 添加fragment
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		Bundle bundle = getIntent().getExtras();
		if (bundle == null) {
			this.finish();
			return;
		}

		mContent = new NewsPageFragment();
		mContent.setArguments(bundle);
		t.replace(R.id.fg_content, mContent);
		t.commit();
	}

	@Override
	protected void onResume() {
		// initViews();
		super.onResume();

		if (!TipsUtil.isTipShown(TipsUtil.TIP_DETAIL_MORE)
				&& PreferencesManager.getLanguageInfo(this).equals(
						LanguageUtil.LANGUAGE_CH)) {
			startActivity(TipsActivity.getTipIntent(this,
					TipsUtil.TIP_DETAIL_MORE));
		}
	}

	protected void onPause() {
		has_channel_id = true;
		super.onPause();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		/** 分享 */
		UMSocialService mController = UMServiceFactory.getUMSocialService(
				"com.umeng.share");
		/** 使用SSO授权必须添加如下代码 */
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	// private void initViews() {
	// mTextureView = (TextureView)
	// findViewById(R.id.detail_video_player_texture);
	// mTextureView.setSurfaceTextureListener(this);
	// mRotateBtn = (Button) findViewById(R.id.detail_video_rotate_btn);
	// mRotateBtn.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// scaleVideoFullScreen();
	// }
	// });
	// mRotateBtn.setVisibility(View.GONE);
	// mContent.setVideoPlayListener(mVideoPlayL);
	// }
	//
	// private void addVideoPlayer(String videoUrl) {
	// DisplayMetrics dm = getResources().getDisplayMetrics();
	// RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(
	// Constants.screenWidth, Constants.screenWidth * 9 / 16);
	// rllp.addRule(RelativeLayout.ALIGN_TOP, R.id.fg_content);
	// rllp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	// rllp.topMargin = (int) (dm.density * 48);
	// mTextureView.setLayoutParams(rllp);
	// mTextureView.setVisibility(View.VISIBLE);
	// isPlayVideo = true;
	// }
	//
	// private void scaleVideoFullScreen() {
	// isVideoFullScreen = true;
	// RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(
	// Constants.screenHeight, Constants.screenWidth);
	// rllp.addRule(RelativeLayout.CENTER_IN_PARENT, -1);
	// float scale = (Constants.screenHeight * 1.0f) / (Constants.screenWidth *
	// 1.0f);
	// mTextureView.setRotation(90);
	// mTextureView.setBackgroundColor(Color.BLACK);
	// mTextureView.setScaleX(scale);
	// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
	// WindowManager.LayoutParams.FLAG_FULLSCREEN);
	// mTextureView.setLayoutParams(rllp);
	// }
	//
	// private void shrinkVideoToNormal() {
	// isVideoFullScreen = false;
	// DisplayMetrics dm = getResources().getDisplayMetrics();
	// RelativeLayout.LayoutParams rllp = new RelativeLayout.LayoutParams(
	// Constants.screenWidth, Constants.screenWidth * 9 / 16);
	// rllp.addRule(RelativeLayout.ALIGN_TOP, R.id.fg_content);
	// rllp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	// rllp.topMargin = (int) (dm.density * 48);
	// float scale = (Constants.screenWidth * 1.0f) / (Constants.screenWidth * 9
	// / (16 * 1.0f));
	// mTextureView.setRotation(0);
	// mTextureView.setBackgroundColor(Color.BLACK);
	// mTextureView.setScaleX(scale);
	// final WindowManager.LayoutParams attrs = getWindow().getAttributes();
	// attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
	// getWindow().setAttributes(attrs);
	// getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
	// mTextureView.setLayoutParams(rllp);
	// }

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			if (mContent.onTouchDiapatch(ev.getX(), ev.getY())) {
				return true;
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mContent.onBackKeyUp()) {
				return true;
			}
			// if (isPlayVideo) {
			// if(isVideoFullScreen) {
			// shrinkVideoToNormal();
			// } else {
			// removeVideoPlayer();
			// }
			// return true;
			// }
		}
		return super.onKeyUp(keyCode, event);
	}

	// private void removeVideoPlayer() {
	// isPlayVideo = false;
	// mMediaPlayer.stop();
	// mTextureView.setVisibility(View.GONE);
	// }
	//
	// onVideoPlayListener mVideoPlayL = new onVideoPlayListener() {
	//
	// @Override
	// public void onVideoPlay(String url) {
	// Message msg = new Message();
	// msg.what = MSG_SHOW_VIDEO;
	// Bundle data = new Bundle();
	// data.putString("url", url);
	// msg.setData(data);
	// mHandler.sendMessage(msg);
	// }
	// };
	//
	// @Override
	// public void onSurfaceTextureAvailable(SurfaceTexture surface, int width,
	// int height) {
	// this.mSurface = new Surface(surface);
	// mMediaPlayer = new MediaPlayer();
	// new Thread() {
	//
	// @Override
	// public void run() {
	// try {
	// mMediaPlayer.setDataSource(mVideoUrl);
	// mMediaPlayer.setSurface(mSurface);
	// mMediaPlayer.prepare();
	// mMediaPlayer.setOnBufferingUpdateListener(NewsPageActivity.this);
	// mMediaPlayer.setOnCompletionListener(NewsPageActivity.this);
	// mMediaPlayer.setOnPreparedListener(NewsPageActivity.this);
	// mMediaPlayer.setOnVideoSizeChangedListener(NewsPageActivity.this);
	// mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
	// mMediaPlayer.start();
	// } catch (IllegalArgumentException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (SecurityException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IllegalStateException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	// }.start();
	// }
	//
	// @Override
	// public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
	// if (this.mSurface != null) {
	// this.mSurface.release();
	// this.mSurface = null;
	// }
	// return true;
	// }
	//
	// @Override
	// public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int
	// width,
	// int height) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onSurfaceTextureUpdated(SurfaceTexture surface) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// private void loadVideo(String url) {
	// if (mSurface == null)
	// return;
	//
	// /* IMPORTANT PLACE */
	// RelativeLayout.LayoutParams l;
	// DisplayMetrics metrics = new DisplayMetrics();
	// getWindowManager().getDefaultDisplay().getMetrics(metrics);
	// l = new RelativeLayout.LayoutParams(metrics.heightPixels,
	// metrics.widthPixels);
	// l.addRule(RelativeLayout.CENTER_IN_PARENT, -1);
	// float scale = (metrics.heightPixels * 1.0f)
	// / (metrics.widthPixels * 1.0f);
	// mTextureView.setScaleX(scale);
	// mTextureView.setLayoutParams(l);
	// /* END OF IMPORTANT PLACE */
	//
	// try {
	// // mMdeiaPlayer.reset();
	// // mMdeiaPlayer.setSurface(mSurface);
	// // mMdeiaPlayer.setDataSource(url);
	// // mMdeiaPlayer.setOnPreparedListener(this);
	// // mMdeiaPlayer.setOnCompletionListener(this);
	// // mMdeiaPlayer.setOnErrorListener(this);
	// // mMdeiaPlayer.setOnVideoSizeChangedListener(this);
	// // mMdeiaPlayer.setScreenOnWhilePlaying(true);
	// // mMdeiaPlayer.setOnBufferingUpdateListener(this);
	// // mMdeiaPlayer.setOnInfoListener(this);
	// // mMdeiaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
	// // mMdeiaPlayer.prepareAsync();
	// } catch (Exception e) {
	// // Utils.hideHud();
	// // showStatus("Media load failed");
	// // Utils.alert(this, "Playback Error", e.getMessage(),
	// // finishHandler);
	// }
	// }
	//
	// @Override
	// public void onBufferingUpdate(MediaPlayer mp, int percent) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onCompletion(MediaPlayer mp) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onPrepared(MediaPlayer mp) {
	// // TODO Auto-generated method stub
	//
	// }
}
