package com.cplatform.xhxw.ui.ui.guide;

import com.cplatform.xhxw.ui.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class CloudyView extends View implements Runnable {

	private Bitmap bitmap;

	private int left;
	private int top;

	private int screenWidth;
	private int screenHeight;

	private int dx = 1;

	private int sleepTime;

	public static boolean IsRunning = true;

	private Handler handler;

	public CloudyView(Context context) {
		super(context);
	}

	public CloudyView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
	}

	public void init(int left, int top, int width, int resource, int sleepTime) {
		this.setLayoutParams(new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT));
		bitmap = BitmapFactory.decodeResource(getResources(), resource);
		this.screenWidth = width;
		this.left = left;
		this.top = top;
		this.sleepTime = sleepTime;

		handler = new Handler() {
			public void handleMessage(android.os.Message msg) {
				CloudyView.this.invalidate();
			};
		};

	}

	// public void initAirPlane(int width, int height) {
	//
	// screenWidth = width;
	// screenHeight = height;
	// airWidth = width - 200;
	// airHeight = (int) (airWidth * 0.34);
	// this.setLayoutParams(new FrameLayout.LayoutParams(airWidth, airHeight));
	// // LayoutParams lp = (FrameLayout.LayoutParams)getLayoutParams();
	// // lp.height = airHeight;
	// // lp.width = airWidth;
	// // this.setLayoutParams(lp);
	// bitmap = BitmapFactory.decodeResource(getResources(),
	// R.drawable.airplane);
	// this.screenWidth = width;
	// this.left = -airWidth;
	// left = 100;
	// this.top = screenHeight - airHeight;
	// this.sleepTime = 10;
	//
	// handler = new Handler() {
	// public void handleMessage(android.os.Message msg) {
	// CloudyView.this.invalidate();
	// };
	// };
	//
	// }

	public void move() {
		new Thread(this).start();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.drawBitmap(bitmap, left, top, null);
	}

	@Override
	public void run() {

		while (CloudyView.IsRunning) {
			if ((bitmap != null) && (left > (screenWidth))) {
				left = -bitmap.getWidth();
			}
			left = left + dx;
			if (handler != null) {
				handler.sendMessage(handler.obtainMessage());
			}
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void release() {
		if (bitmap != null) {
			bitmap.recycle();
		}
		handler = null;
	}
}
