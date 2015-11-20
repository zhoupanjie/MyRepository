package com.cplatform.xhxw.ui.ui.web.newscollect;

import java.io.File;
import java.io.IOException;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TextView;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.util.FileSizeUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.skd.androidrecording.video.AdaptiveSurfaceView;
import com.skd.androidrecording.video.CameraHelper;
import com.skd.androidrecording.video.VideoRecordingHandler;
import com.skd.androidrecording.video.VideoRecordingManager;

public class VideoRecordActivity extends BaseActivity {

	@Override
	protected String getScreenName() {
		return null;
	}
	private static final int MSG_UPGRADE_TIME = 1;
	private static final int MAX_FILE_SIZE_TO_RECORD = 15 * 1025 * 1025 - 900 * 1024;
	
	private static String mFileName = null;
	private String mVideoName = null;

	private Button mRecordBtn;
	private Button mCancelBtn;
	private Button mPreviewBtn;
	private Spinner mVideoSizeSpinner;
	private TextView mVideoDurationtv;

	private Size mVideoSize = null;
	private VideoRecordingManager mRecordManager;
	
	private int mHour = 0;
	private int mMinutes = 0;
	private int mSecond = 0;
	private boolean isRecording = false;

	private VideoRecordingHandler mRecordHandler = new VideoRecordingHandler() {
		@Override
		public boolean onPrepareRecording() {
			if (mVideoSizeSpinner == null) {
				initVideoSizeSpinner();
				return true;
			}
			return false;
		}

		@Override
		public Size getVideoSize() {
			return mVideoSize;
		}

		@Override
		public int getDisplayRotation() {
			return VideoRecordActivity.this.getWindowManager()
					.getDefaultDisplay().getRotation();
		}
	};
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if(msg.what == MSG_UPGRADE_TIME) {
				mVideoDurationtv.setText(updateTime());
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_record);
		
		mVideoName = "myVideo_" + System.currentTimeMillis() + ".mp4";
		mFileName = Constants.Directorys.VIDEO + mVideoName ;

		AdaptiveSurfaceView videoView = (AdaptiveSurfaceView) findViewById(R.id.videoView);
		mRecordManager = new VideoRecordingManager(videoView, mRecordHandler);

		mVideoDurationtv = (TextView) findViewById(R.id.video_record_time_tv);
		mRecordBtn = (Button) findViewById(R.id.video_recordbtn);
		mCancelBtn = (Button) findViewById(R.id.video_cancelbtn);
		mPreviewBtn = (Button) findViewById(R.id.video_record_previewbtn);
		mPreviewBtn.setEnabled(false);
		mRecordBtn.setOnClickListener(mOnclick);
		mCancelBtn.setOnClickListener(mOnclick);
		mPreviewBtn.setOnClickListener(mOnclick);
	}
	
	OnClickListener mOnclick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.video_recordbtn:
				record(true);
				break;
			case R.id.video_record_previewbtn:
				play();
				break;
			case R.id.video_cancelbtn:
				setResult(RESULT_CANCELED);
				finish();
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onDestroy() {
		mRecordManager.dispose();
		mRecordHandler = null;

		super.onDestroy();
	}

	@SuppressLint("NewApi")
	private void initVideoSizeSpinner() {
		mVideoSizeSpinner = (Spinner) findViewById(R.id.video_record_spinner);
		if (Build.VERSION.SDK_INT >= 11) {
			List<Size> sizes = CameraHelper
					.getCameraSupportedVideoSizes(mRecordManager
							.getCameraManager().getCamera());
			mVideoSizeSpinner.setAdapter(new VideoSizeAdapter(sizes));
			mVideoSizeSpinner
					.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							mVideoSize = (Size) arg0.getItemAtPosition(arg2);
							mRecordManager.setPreviewSize(mVideoSize);
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
						}
					});
			mVideoSize = (Size) mVideoSizeSpinner.getItemAtPosition(0);
		} else {
			mVideoSizeSpinner.setVisibility(View.GONE);
		}
	}

	@SuppressLint("NewApi")
	private void updateVideoSizes() {
		if (Build.VERSION.SDK_INT >= 11) {
			((VideoSizeAdapter) mVideoSizeSpinner.getAdapter()).set(CameraHelper
					.getCameraSupportedVideoSizes(mRecordManager
							.getCameraManager().getCamera()));
			mVideoSizeSpinner.setSelection(0);
			mVideoSize = (Size) mVideoSizeSpinner.getItemAtPosition(0);
			mRecordManager.setPreviewSize(mVideoSize);
		}
	}

	private void switchCamera() {
		mRecordManager.getCameraManager().switchCamera();
		updateVideoSizes();
	}

	private void record(boolean isClick) {
		if (mRecordManager.stopRecording()) {
			isRecording = false;
			mRecordBtn.setText(R.string.confirm);
			mPreviewBtn.setEnabled(true);
			mVideoSizeSpinner.setEnabled(true);
		} else {
			if(mRecordBtn.getText().toString().trim().equals(getString(R.string.confirm)) && isClick) {
				Intent it = new Intent();
				it.putExtra("file_path", mFileName);
				it.putExtra("file_name", mVideoName);
				it.putExtra("time", mHour * 3600 + mMinutes * 60 + mSecond);
				setResult(RESULT_OK, it);
				finish();
			} else if(mRecordBtn.getText().toString().trim().equals(getString(R.string.video_record_start))) {
				startRecording();
			}
		}
	}

	private void startRecording() {
		File mFile = new File(mFileName);
		if(!mFile.exists()) {
			try {
				mFile.createNewFile();
				LogUtil.e("file-----------", ">>>>>>>>>>>>>>>>>" + mFile.exists());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (mRecordManager.startRecording(mFileName, mVideoSize)) {
			isRecording = true;
			mRecordBtn.setText(R.string.video_record_stop);
			mPreviewBtn.setEnabled(false);
			mVideoSizeSpinner.setEnabled(false);
			new Thread() {
				@Override
				public void run() {
					while(isRecording) {
						try {
							Thread.sleep(1000);
							mHandler.sendEmptyMessage(MSG_UPGRADE_TIME);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
			return;
		}
	}
	
	private String updateTime() {
		if(mSecond + 1 == 60) {
			mSecond =0;
			mMinutes += 1;
			if(mMinutes == 60) {
				mMinutes = 0;
				mHour += 1;
			}
		} else {
			mSecond += 1;
		}
		long fileSize = new File(mFileName).length();
		if(fileSize > MAX_FILE_SIZE_TO_RECORD) {
			record(false);
		}
		return "" + FileSizeUtil.formetFileSize(fileSize) + "/15M      " + mHour + ":" + mMinutes + ":" + mSecond;
	}

	private void play() {
		Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(mFileName), "video/mp4");
        startActivity(intent);
	}

}
