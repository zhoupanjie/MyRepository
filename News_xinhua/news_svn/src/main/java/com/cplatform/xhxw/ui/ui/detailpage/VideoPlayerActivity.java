package com.cplatform.xhxw.ui.ui.detailpage;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.MediaController;
import android.widget.VideoView;
import butterknife.ButterKnife;
import butterknife.Bind;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.db.ReadNewsDB;
import com.cplatform.xhxw.ui.db.dao.ReadNewsDao;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.NewsDetailRequest;
import com.cplatform.xhxw.ui.http.net.NewsDetailResponse;
import com.cplatform.xhxw.ui.model.NewsDetail;
import com.cplatform.xhxw.ui.model.Videos;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.widget.DefaultView;
import com.cplatform.xhxw.ui.util.DateUtil;
import com.cplatform.xhxw.ui.util.ListUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 视频播放 Created by cy-love on 14-1-1.
 */
public class VideoPlayerActivity extends BaseActivity implements
		OnCompletionListener, OnErrorListener, OnPreparedListener,
		DefaultView.OnTapListener {

	private static final String TAG = VideoPlayerActivity.class.getSimpleName();
	@Bind(R.id.def_view)
	DefaultView mDefView;
	@Bind(R.id.video_view)
	VideoView mVideoView;
	private String mVideoUrl;
	private String mNewsId;
	private AsyncHttpResponseHandler mHttpHandler;

	public static Intent getIntent(Context context, String newsId) {
		return getIntent(context, newsId, null);
	}

	public static Intent getIntent(Context context, String newsId, String url) {
		Intent intent = new Intent(context, VideoPlayerActivity.class);
		intent.putExtra("url", url);
		intent.putExtra("newsId", newsId);
		return intent;
	}

	@Override
	protected String getScreenName() {
		return "VideoPlayerActivity";
	}

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_video_player);
		ButterKnife.bind(this);
		mVideoView.setOnCompletionListener(this);
		mVideoView.setOnErrorListener(this);
		mVideoView.setOnPreparedListener(this);
		// 然后指定需要播放文件的路径，初始化MediaPlayer
		mVideoUrl = getIntent().getStringExtra("url");
		mNewsId = getIntent().getStringExtra("newsId");
		boolean isPush = getIntent().getBooleanExtra("isPush", false);
		if (isPush) {
			App.getPreferenceManager().messageNewCountMinus();
		}
		mDefView.setListener(this);
		if (TextUtils.isEmpty(mVideoUrl)) {
			loadData(mNewsId);
		} else {
			startPlay();
		}
	}

	private void loadData(final String newsId) {
		APIClient.newsDetail(new NewsDetailRequest(newsId),
				new AsyncHttpResponseHandler() {
					@Override
					protected void onPreExecute() {
						if (mHttpHandler != null) {
							mHttpHandler.cancle();
						}
						mHttpHandler = this;
						mDefView.setStatus(DefaultView.Status.loading);
					}

					@Override
					public void onFinish() {
						mHttpHandler = null;
					}

					@Override
					public void onSuccess(String content) {
						NewsDetailResponse response;
						try {
							ResponseUtil.checkResponse(content);
							response = new Gson().fromJson(content,
									NewsDetailResponse.class);
						} catch (Exception e) {
							showToast(R.string.data_format_error);
							LogUtil.e(TAG, e);
							return;
						}
						if (response.isSuccess()) {
							NewsDetail det = response.getData();
							List<Videos> videos = det.getVideos();
							if (!ListUtil.isEmpty(videos)) {
								mVideoUrl = videos.get(0).getUrl();
								startPlay();
							} else {
								showToast("无视频数据");
								mDefView.setStatus(DefaultView.Status.error);
							}
							// 显示数据
						} else {
							showToast(response.getMsg());
							mDefView.setStatus(DefaultView.Status.error);
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						showToast(R.string.load_server_failure);
						mDefView.setStatus(DefaultView.Status.error);
					}
				});
	}

	private void startPlay() {
		if (mDefView.getStatus() != DefaultView.Status.loading) {
			mDefView.setStatus(DefaultView.Status.loading);
		}
		mVideoView.setMediaController(new MediaController(this));
		mVideoView.setVideoURI(Uri.parse(mVideoUrl));
		mVideoView.start();
		mVideoView.requestFocus();
		ReadNewsDB.saveNews(this, new ReadNewsDao(mNewsId, DateUtil.getTime()));
	}

	@Override
	public void onPrepared(MediaPlayer player) {
		mDefView.setStatus(DefaultView.Status.showData);
	}

	@Override
	public boolean onError(MediaPlayer player, int whatError, int extra) {
		LogUtil.v(TAG, "Play Error:::onError called");
		switch (whatError) {
		case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
			LogUtil.v(TAG, "Play Error:::MEDIA_ERROR_SERVER_DIED");
			break;
		case MediaPlayer.MEDIA_ERROR_UNKNOWN:
			LogUtil.v(TAG, "Play Error:::MEDIA_ERROR_UNKNOWN");
			break;
		default:
			break;
		}
		// new AlertDialog.Builder(this)
		// .setTitle("提示")
		// .setMessage("视频播放错误")
		// .setPositiveButton("确定", null)
		// .setOnDismissListener(new DialogInterface.OnDismissListener() {
		// @Override
		// public void onDismiss(DialogInterface dialog) {
		// finish();
		// }
		// })
		// .create().show();
		return false;
	}

	@Override
	public void onCompletion(MediaPlayer player) {
		// 当MediaPlayer播放完成后触发
		LogUtil.v(TAG, "Play Over:::onComletion called");
		this.finish();
	}

	@Override
	protected void onStop() {
		mVideoView.pause();
		super.onStop();
	}

	protected void onPause() {
		has_channel_id = true;
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		if (mHttpHandler != null) {
			mHttpHandler.cancle();
			mHttpHandler = null;
		}
		mVideoView.stopPlayback();
		super.onDestroy();
	}

	@Override
	public void onTapAction() {
		if (!TextUtils.isEmpty(mVideoUrl)) {
			startPlay();
		} else {
			loadData(mNewsId);
		}
	}
}