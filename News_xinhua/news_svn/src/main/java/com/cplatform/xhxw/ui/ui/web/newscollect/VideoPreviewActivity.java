package com.cplatform.xhxw.ui.ui.web.newscollect;

import java.io.File;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.skd.androidrecording.video.AdaptiveSurfaceView;
import com.skd.androidrecording.video.PlaybackHandler;
import com.skd.androidrecording.video.VideoPlaybackManager;

public class VideoPreviewActivity extends BaseActivity {

	@Override
	protected String getScreenName() {
		// TODO Auto-generated method stub
		return null;
	}

	public static String FileNameArg = "arg_filename";
	
	private static String mFileName = null;
	
	private AdaptiveSurfaceView mVideoView;
	
	private VideoPlaybackManager mPlaybackManager;
	
	private PlaybackHandler mPlaybackHandler = new PlaybackHandler() {
		@Override
		public void onPreparePlayback() {
			runOnUiThread (new Runnable() {
		    	public void run() {
		    		mPlaybackManager.showMediaController();
		    	}
		    });
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_preview);
		initActionBar();
		Intent i = getIntent();
		if ((i != null) && (i.getExtras() != null)) {
			mFileName = i.getExtras().getString(FileNameArg);
		}
		Log.e("", "size--------------------------------" + new File(mFileName).length());
		mVideoView = (AdaptiveSurfaceView) findViewById(R.id.video_preview_view);
		
		mPlaybackManager = new VideoPlaybackManager(this, mVideoView, mPlaybackHandler);
		mPlaybackManager.setupPlayback(mFileName);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		mPlaybackManager.showMediaController();
	    return false;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		mPlaybackManager.pause();
		mPlaybackManager.hideMediaController();
	}
	
	@Override
	protected void onDestroy() {
		mPlaybackManager.dispose();
		mPlaybackHandler = null;
		
		super.onDestroy();
	}
}
