package com.cplatform.xhxw.ui.ui.base.view;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.util.MediaPlayerManager;
import com.cplatform.xhxw.ui.util.MediaPlayerManager.OnMediaPlayerManagerListener;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

/**
 * 底部-播放器-布局
 */
public class BottomMediaplayer extends RelativeLayout implements OnMediaPlayerManagerListener{

	private Context context;
	@Bind(R.id.bottom_media_start) ImageView imageStart;
	@Bind(R.id.bottom_media_pause) ImageView imagePause;
	@Bind(R.id.bottom_media_progressBar) ProgressBar progressBar;
	@Bind(R.id.bottom_media_linear) LinearLayout linearLayout;
    
    private MediaPlayerManager mediaPlayerManager;
    
    private String url = "";
    
    /**
     * 判断是否正在播放歌曲(包括准备阶段和播放阶段
     * 
     * 注意：在准备阶段，调用mediaPlayer.isPlaying()返回false
     * */
    private boolean isPlaying = false;
	
	public BottomMediaplayer(Context context) {
		super(context);
		init(context);
	}
    
    public BottomMediaplayer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public BottomMediaplayer(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.bottommediaplayer, this);
        ButterKnife.bind(this);
        
        imageStart = (ImageView) findViewById(R.id.bottom_media_start);
        imagePause = (ImageView) findViewById(R.id.bottom_media_pause);
        progressBar = (ProgressBar) findViewById(R.id.bottom_media_progressBar);
        linearLayout = (LinearLayout) findViewById(R.id.bottom_media_linear);
        
	}

	@OnClick(R.id.bottom_media_start)
    public void imageMediaStart() {
//		context.startActivity(new Intent(context, RecommendImagesActivity.class));
		start(url);
    }
	
	@OnClick(R.id.bottom_media_pause)
	public void imageMediaPause(){
		pauseMedia();
	}
	
	/**向播放器传递url(网上获取语音)*/
	public void setMediaUrl(String url) {
		this.url = url;
	}
	
	/**开始语音播放*/
	private void start(String url) {
		if (mediaPlayerManager == null) {
			mediaPlayerManager = new MediaPlayerManager(context);
		    mediaPlayerManager.setOnMediaPlayerManagerListener(this);
		}
		imageStart.setVisibility(View.GONE);
		linearLayout.setVisibility(View.VISIBLE);
		imagePause.setBackgroundResource(R.drawable.ic_pauseaudio);
		linearLayout.setBackgroundResource(R.drawable.media_style_visity);
		isPlaying = true;
		progressBar.setProgress(0);
		mediaPlayerManager.myStart(url);
	}
	
	/**暂停或者继续语音播放*/
	public void pauseMedia() {
        if (mediaPlayerManager == null) {
            return;
        }
		if (isPlaying) {
			linearLayout.setBackgroundResource(R.drawable.media_style_gone);
			progressBar.setVisibility(View.GONE);
			imagePause.setBackgroundResource(R.drawable.ic_playaudio);
			handler.removeMessages(MSG_TIMER_REFRESH);
			mediaPlayerManager.myPause();
			isPlaying = false;
		}else {
			linearLayout.setBackgroundResource(R.drawable.media_style_visity);
			progressBar.setVisibility(View.VISIBLE);
			imagePause.setBackgroundResource(R.drawable.ic_pauseaudio);
			handler.sendEmptyMessage(MSG_TIMER_REFRESH);
			mediaPlayerManager.myResume();
			isPlaying = true;
		}
	}

	/**关闭界面时，释放资源，销毁播放器*/
	public void destroy() {
		if (mediaPlayerManager != null) {
			mediaPlayerManager.myStop();
			mediaPlayerManager = null;
		}
	}
	
	private final int MSG_TIMER_REFRESH = 0;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_TIMER_REFRESH: {
				if (mediaPlayerManager != null) {
					try {
						if (mediaPlayerManager.getMediaPlayer().isPlaying()) {
							int current = mediaPlayerManager.getMediaPlayer().getCurrentPosition();
							int total = mediaPlayerManager.getMediaPlayer().getDuration();
							// 设置进度条总值 和 显示的值
							progressBar.setMax(total);
							progressBar.setProgress(current);
						}
					} catch (Exception e) {
						
					}
					sendEmptyMessageDelayed(MSG_TIMER_REFRESH, 100);
				}
				break;
			}
			default: {
				super.handleMessage(msg);
				break;
			}
			}
		}

	};

	/**资源准备好以后，回调此方法*/
	@Override
	public void onMediaPlayerPrepared() {
		handler.sendEmptyMessage(MSG_TIMER_REFRESH);
	}
	
	/**播放成功后，回调此方法*/
	@Override
	public void onMediaPlayerManager(boolean isSuccess) {
		imageStart.setVisibility(View.VISIBLE);
		linearLayout.setVisibility(View.GONE);
		isPlaying = false;
		destroy();
	}
}

