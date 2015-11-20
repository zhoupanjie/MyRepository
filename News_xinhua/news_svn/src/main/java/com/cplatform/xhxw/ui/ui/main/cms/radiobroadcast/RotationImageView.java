package com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import com.cplatform.xhxw.ui.R;

/**
 * 
 * @ClassName RotationImageView
 * @Description TODO 留声机旋转唱盘
 * @Version 1.0
 * @Author zxe
 * @Creation 2015年9月23日 上午10:31:27
 * @Mender zxe
 * @Modification 2015年9月23日 上午10:31:27
 * @Copyright Copyright © 2013 - 2015 Channelsoft (Beijing) Technology Co.,
 *            Ltd.All Rights Reserved.
 * 
 */
public class RotationImageView extends ImageView {
	private String description;// 当前播放广播描述，相当于广播标题
	private String audioid;
	private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;// /按比例扩大/缩小图片的size居中显示，使得图片的高等于View的高，使得图片宽等于或大于View的宽

	private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
	private static final int COLORDRAWABLE_DIMENSION = 2;

	private static final int DEFAULT_BORDER_WIDTH = 0;
	private static final int DEFAULT_BORDER_COLOR = Color.BLACK;

	private final RectF mDrawableRect = new RectF();
	private final RectF mBorderRect = new RectF();

	private final Matrix mShaderMatrix = new Matrix();
	private final Paint mBitmapPaint = new Paint();
	private final Paint mBorderPaint = new Paint();

	private int mBorderColor = DEFAULT_BORDER_COLOR;
	private int mBorderWidth = DEFAULT_BORDER_WIDTH;

	private Bitmap mBitmap;
	private BitmapShader mBitmapShader;
	private int mBitmapWidth;
	private int mBitmapHeight;

	private float mDrawableRadius;
	private float mBorderRadius;
	private ColorFilter mColorFilter;
	private boolean mReady;
	private boolean mSetupPending;
	private boolean isReversal = false;// 是否倒转，false--顺时针，true--逆时针
	private int stateRotation = TAG_STATE_DEFAULT;// 选择状态
	public static final int TAG_STATE_DEFAULT = 0x00;// 未开始
	public static final int TAG_STATE_START = 0x01;// 旋转
	public static final int TAG_STATE_PAUSE = 0x02;// 暂停
	ThreadRotation tr = null;// 旋转线程

	public RotationImageView(Context context) {
		super(context);
		init();
	}

	public RotationImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RotationImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.RotationImageView, defStyle, 0);

		mBorderWidth = a.getDimensionPixelSize(
				R.styleable.RotationImageView_border_width1,
				DEFAULT_BORDER_WIDTH);
		mBorderColor = a.getColor(R.styleable.RotationImageView_border_color1,
				DEFAULT_BORDER_COLOR);

		a.recycle();

		init();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	private void init() {
		super.setScaleType(SCALE_TYPE);
		mReady = true;

		if (mSetupPending) {
			setup();
			mSetupPending = false;
		}
	}

	@Override
	public ScaleType getScaleType() {
		return SCALE_TYPE;
	}

	@Override
	public void setScaleType(ScaleType scaleType) {
		if (scaleType != SCALE_TYPE) {
			throw new IllegalArgumentException(String.format(
					"ScaleType %s not supported.", scaleType));
		}
	}

	@Override
	public void setAdjustViewBounds(boolean adjustViewBounds) {
		if (adjustViewBounds) {
			throw new IllegalArgumentException(
					"adjustViewBounds not supported.");
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (getDrawable() == null) {
			return;
		}
		canvas.drawCircle(getWidth() / 2, getHeight() / 2, mDrawableRadius,
				mBitmapPaint);
		if (mBorderWidth != 0) {
			canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBorderRadius,
					mBorderPaint);
		}
		// Paint paint = new Paint();
		// paint.setTextSize(50);
		// paint.setAntiAlias(true);
		// paint.setColor(Color.WHITE);
		// Path path = new Path();
		// float x=this.getX();
		// float y=this.getY();
		// path.addCircle(getWidth() / 2, getHeight() / 2, mDrawableRadius,
		// Direction.CW);
		// canvas.drawTextOnPath(text, path, 0, 0, paint);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setup();
	}

	public int getBorderColor() {
		return mBorderColor;
	}

	public void setBorderColor(int borderColor) {
		if (borderColor == mBorderColor) {
			return;
		}

		mBorderColor = borderColor;
		mBorderPaint.setColor(mBorderColor);
		invalidate();
	}

	public int getBorderWidth() {
		return mBorderWidth;
	}

	public void setBorderWidth(int borderWidth) {
		if (borderWidth == mBorderWidth) {
			return;
		}

		mBorderWidth = borderWidth;
		setup();
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		// Utils.d("", "setBitMap");
		mBitmap = bm;
		setup();
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		// Utils.d("", "setDrawable");
		mBitmap = getBitmapFromDrawable(drawable);
		setup();
	}

	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
		// Utils.d("", "setImageResource");
		mBitmap = getBitmapFromDrawable(getDrawable());
		setup();
	}

	@Override
	public void setImageURI(Uri uri) {
		super.setImageURI(uri);
		// Utils.d("", "setImageurl");
		mBitmap = getBitmapFromDrawable(getDrawable());
		setup();
	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		if (cf == mColorFilter) {
			return;
		}

		mColorFilter = cf;
		mBitmapPaint.setColorFilter(mColorFilter);
		invalidate();
	}

	private Bitmap getBitmapFromDrawable(Drawable drawable) {
		if (drawable == null) {
			return null;
		}

		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}

		try {
			Bitmap bitmap;

			if (drawable instanceof ColorDrawable) {
				bitmap = Bitmap.createBitmap(COLORDRAWABLE_DIMENSION,
						COLORDRAWABLE_DIMENSION, BITMAP_CONFIG);
			} else {
				bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(), BITMAP_CONFIG);
			}

			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
			drawable.draw(canvas);
			return bitmap;
		} catch (OutOfMemoryError e) {
			return null;
		}
	}

	private void setup() {
		if (!mReady) {
			mSetupPending = true;
			return;
		}

		if (mBitmap == null) {
			return;
		}

		mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP,
				Shader.TileMode.CLAMP);

		mBitmapPaint.setAntiAlias(true);
		mBitmapPaint.setShader(mBitmapShader);

		mBorderPaint.setStyle(Paint.Style.STROKE);
		mBorderPaint.setAntiAlias(true);
		mBorderPaint.setColor(mBorderColor);
		mBorderPaint.setStrokeWidth(mBorderWidth);

		mBitmapHeight = mBitmap.getHeight();
		mBitmapWidth = mBitmap.getWidth();

		mBorderRect.set(0, 0, getWidth(), getHeight());
		mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2,
				(mBorderRect.width() - mBorderWidth) / 2);

		mDrawableRect.set(mBorderWidth, mBorderWidth, mBorderRect.width()
				- mBorderWidth, mBorderRect.height() - mBorderWidth);
		mDrawableRadius = Math.min(mDrawableRect.height() / 2,
				mDrawableRect.width() / 2);

		updateShaderMatrix();
		invalidate();
	}

	private void updateShaderMatrix() {
		float scale;
		float dx = 0;
		float dy = 0;

		mShaderMatrix.set(null);

		if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width()
				* mBitmapHeight) {
			scale = mDrawableRect.height() / (float) mBitmapHeight;
			dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
		} else {
			scale = mDrawableRect.width() / (float) mBitmapWidth;
			dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
		}

		mShaderMatrix.setScale(scale, scale);
		mShaderMatrix.postTranslate((int) (dx + 0.5f) + mBorderWidth,
				(int) (dy + 0.5f) + mBorderWidth);
		mShaderMatrix.preRotate(currentRoatate, mBitmapWidth / 2,// 瀹炵幇鍥剧墖鐨勬棆杞�
				mBitmapHeight / 2);
		mBitmapShader.setLocalMatrix(mShaderMatrix);
	}

	private int currentRoatate = 0;

	public void setRoatate(int roateate) {
		this.currentRoatate = roateate;
		setup();
	}

	public int getRoatate() {
		return currentRoatate;
	}

	public int getStateRotation() {
		return stateRotation;
	}

	public void setStateRotation(int stateRotation) {
		this.stateRotation = stateRotation;
	}

	/**
	 * 
	 * @Name isRotation
	 * @Description TODO 是否旋转
	 * @return
	 * @return boolean
	 * @Author zxe
	 * @Date 2015年8月28日 下午3:19:59
	 * 
	 */
	public boolean isRotation() {
		return getStateRotation() == TAG_STATE_START;
	}

	/**
	 * 
	 * @Name onRotation
	 * @Description TODO 设置旋转
	 * @return void
	 * @Author zxe
	 * @Date 2015年8月27日 上午11:27:25
	 * 
	 */
	public void startRotation() {
		if (tr != null) {
			tr = new ThreadRotation();
			setStateRotation(TAG_STATE_START);
			tr.start();
			// }
		} else {
			tr = new ThreadRotation();
			setStateRotation(TAG_STATE_START);
			tr.start();
		}
	}

	/**
	 * 
	 * @Name stopRotation
	 * @Description TODO 停止旋转
	 * @return void
	 * @Author zxe
	 * @Date 2015年8月27日 上午11:28:12
	 * 
	 */
	public void stopRotation() {
		onStop();
//		onReset();
	}

	/**
	 * 
	 * @Name onReset
	 * @Description TODO 复位
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月23日 下午4:09:49
	 * 
	 */
	public void onReset() {
		currentRoatate = 0;
		setup();
	}

	/**
	 * 
	 * @Name onStop
	 * @Description TODO 停止
	 * @return void
	 * @Author zxe
	 * @Date 2015年9月23日 下午4:10:01
	 * 
	 */
	public void onStop() {
		if (tr != null) {
			setStateRotation(TAG_STATE_DEFAULT);
			tr.interrupt();
			mHandler.removeCallbacks(tr);
			tr = null;
		}
	}

	//
	//
	// /**
	// * 旋转开始
	// */
	// public void onStart() {
	// onStop();
	// tr = new ThreadRotation();
	// setStateRotation(TAG_STATE_START);
	// tr.start();
	// }

	// public void onResume() {
	// if (tr != null) {
	// setStateRotation(TAG_STATE_START);
	// tr.notify();
	// }
	// }
	//
	public boolean isReversal() {
		return isReversal;
	}

	public void setReversal(boolean isReversal) {
		this.isReversal = isReversal;
	}

	public class ThreadRotation extends Thread {
		public ThreadRotation() {
		}

		@Override
		public void run() {
			int countError = 0;// 最大次数，如果当前控件id不等于广播播放器id,此id每次加一，如果超过5次，取消该线程
			while (getStateRotation() == TAG_STATE_START) {
				if ((false == TextUtils.isEmpty(getAudioid()))
						&& (false == TextUtils
								.isEmpty(RadioBroadcastFragment.player
										.getAudioid()))
						&& getAudioid().equals(
								RadioBroadcastFragment.player.getAudioid())) {
					if (isReversal) {
						if (currentRoatate == 0) {
							currentRoatate = 359;
						}
						currentRoatate--;
					} else {
						if (currentRoatate == 359) {
							currentRoatate = 0;
						}
						currentRoatate++;
					}
					Message message = Message.obtain();
					message.what = currentRoatate;
					mHandler.sendMessage(message);
					// publishProgress(currentRoatate);
					try {
						sleep(50);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						// e.printStackTrace();
					}
				} else {
					if (countError < 3) {
						countError++;
						try {
							sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							// e.printStackTrace();
						}
						Log.d("旋转按钮id不等于播放器id", countError + "");
					} else {
						stopRotation();
					}
				}
			}
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			int s = msg.what;
			setRoatate(s);
			super.handleMessage(msg);
		}
	};

	public String getAudioid() {
		return audioid;
	}

	public void setAudioid(String audioid) {
		this.audioid = audioid;
	}
}