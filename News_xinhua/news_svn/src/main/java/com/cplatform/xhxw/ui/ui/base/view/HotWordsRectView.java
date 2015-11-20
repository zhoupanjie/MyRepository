package com.cplatform.xhxw.ui.ui.base.view;

import java.util.ArrayList;
import java.util.List;

import com.cplatform.xhxw.ui.model.HotSearchWord;
import com.cplatform.xhxw.ui.util.LogUtil;

import android.R.integer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class HotWordsRectView extends View implements OnTouchListener {
	private static final int MIN_MOVE_DIS = 20;
	private static final float GAP_HEIGHT_OF_COLUMN = 55f;
	private Context mCon;
	private float mNormalTextSize;
	private int mGapHeightBLines1;
	private int mGapHeightBLines2;
	private Paint mPaint;
	private Rect mMaxRect;
	private Rect mCenterRect;
	private Rect mNormalRect;
	private int mAreaL;
	private int mAreaT;
	private int mAreaR;
	private int mAreaB;
	private int mCenterX;
	private int mCenterY;

	private int sWidth;
	private int sHeight;

	private boolean isDragMove = false;
	private float mDownX;
	private float mDownY;
	private float mLastDownX;
	private float mLastDownY;

	private List<HotSearchWord> mWords;
	private List<WordPointer> mPointers;
	private onHotWordClickListener mOnhotWordClickListener;

	public HotWordsRectView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mCon = context;
		init();
	}

	public HotWordsRectView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public HotWordsRectView(Context context) {
		this(context, null);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		sWidth = MeasureSpec.getSize(widthMeasureSpec);
		sHeight = MeasureSpec.getSize(heightMeasureSpec);
		mCenterX = sWidth / 2;
		mCenterY = sHeight / 2;
		initRect(sWidth, sHeight);
		initPointers();
		setMeasuredDimension(sWidth, sHeight);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		for (WordPointer wp : mPointers) {
			mPaint.setTextSize(wp.textSize);
			mPaint.setColor(getColor(wp.scale));
			canvas.drawText(wp.text, wp.X, wp.Y, mPaint);
		}
	}

	private void init() {
		mPaint = new Paint();
		mPaint.setTypeface(Typeface.DEFAULT_BOLD);
		mNormalTextSize = (int) (16 * mCon.getResources().getDisplayMetrics().density);
		mGapHeightBLines1 = (int) (mNormalTextSize * 1.5);
		mGapHeightBLines2 = (int) (mNormalTextSize * 1.5 + 20);
		setOnTouchListener(this);
	}

	private void initRect(int width, int height) {
		int centerX = width / 2;
		int centerY = height / 2;
		int r = Math.min(width, height) / 2;
		final int R = 2 * r;
		mAreaL = centerX - r;
		mAreaT = centerY - r;
		mAreaR = centerX + r;
		mAreaB = centerY + r;
		mNormalRect = new Rect(mAreaL, mAreaT, mAreaR, mAreaB);
		mCenterRect = new Rect(mAreaL + r / 4, mAreaT + r / 4, mAreaR - r / 4,
				mAreaB - r / 4);
		mMaxRect = new Rect(centerX - r / 4, centerY - r / 4, centerX + r / 4,
				centerY + r / 4);
	}

	private void initPointers() {
		if (mPointers == null) {
			mPointers = new ArrayList<HotWordsRectView.WordPointer>();
		} else {
			mPointers.clear();
		}
		if (mWords == null) {
			return;
		}
		int wordCount = mWords.size();
		float area1X = mCenterX + 5;
		float area1Y = mCenterY - 5;
		int area1LineNum = 1;
		int area1wordCountInLine = 0;
		int area1Height = (int) area1Y;
		float area2X = mAreaL + 5;
		float area2Y = mCenterY - 65;
		int area2LineNum = 1;
		int area2wordCountInLine = 0;
		int area2Height = (int) area2Y;
		float area3X = mAreaL + 5;
		float area3Y = mCenterY + 100;
		int area3LineNum = 1;
		int area3wordCountInLine = 0;
		int area3Height = (int) area3Y;
		float area4X = mCenterX + 5;
		float area4Y = mCenterY + 165;
		int area4LineNum = 1;
		int area4wordCountInLine = 0;
		int area4Height = (int) area4Y;

		for (int i = 0; i < wordCount; i++) {
			WordPointer wp = new WordPointer();
			wp.text = mWords.get(wordCount - 1 - i).getTag();
			boolean isScaleBig = true;
			switch (i % 4) {
			case 0:
				if (area1X > (mAreaR - 30)) {
					area1X = mCenterX + 5;
					area1Y = area1Height - mGapHeightBLines1;
					area1LineNum += 1;
					area1wordCountInLine = 0;
				}
				if (area1Y > mAreaT + 10) {
					if (area1LineNum % 2 == 1) {
						if (area1wordCountInLine == 0) {
							area1X += 150;
						}
						if (area1wordCountInLine % 2 == 1) {
							isScaleBig = false;
						}
					} else {
						if (area1wordCountInLine % 2 == 0) {
							isScaleBig = false;
						}
					}
					float area1Scale = getScale((int) area1X, (int) area1Y,
							isScaleBig);
					float tSize1 = mNormalTextSize * area1Scale;
					mPaint.setTextSize(tSize1);
					float tWid1 = mPaint.measureText(wp.text);
					area1Height = (int) ((area1Height < area1Y - tSize1) ? area1Height
							: (area1Y - tSize1));
					wp.X = (int) area1X;
					wp.Y = (int) area1Y;
					wp.textSize = tSize1;
					wp.textWidth = tWid1;
					wp.scale = area1Scale;
					wp.textArea = new Rect(wp.X, wp.Y - (int) tSize1, wp.X
							+ (int) tWid1, wp.Y);
					area1X = area1X + GAP_HEIGHT_OF_COLUMN + tWid1;
					area1wordCountInLine += 1;
					break;
				}

			case 1:
				if (area2X > (mCenterX - 150)) {
					area2X = mAreaL + 5;
					area2Y = area2Height - mGapHeightBLines2;
					area2LineNum += 1;
					area2wordCountInLine = 0;
				}
				if (area2Y > mAreaT + 10) {
					if (area2LineNum % 2 == 0) {
						if (area2wordCountInLine == 0) {
							area2X += 150;
						}
						if (area2wordCountInLine % 2 == 1) {
							isScaleBig = false;
						}
					} else {
						if (area2wordCountInLine % 2 == 0) {
							isScaleBig = false;
						}
					}
					float area2Scale = getScale((int) area2X, (int) area2Y,
							isScaleBig);
					float tSize2 = mNormalTextSize * area2Scale;
					mPaint.setTextSize(tSize2);
					float tWid2 = mPaint.measureText(wp.text);
					area2Height = (int) ((area2Height < area2Y - tSize2) ? area2Height
							: (area2Y - tSize2));
					wp.X = (int) area2X;
					wp.Y = (int) area2Y;
					wp.textSize = tSize2;
					wp.textWidth = tWid2;
					wp.scale = area2Scale;
					wp.textArea = new Rect(wp.X, wp.Y - (int) tSize2, wp.X
							+ (int) tWid2, wp.Y);
					area2X = area2X + GAP_HEIGHT_OF_COLUMN + tWid2;
					area2wordCountInLine += 1;
					break;
				}
			case 2:
				if (area3X > (mCenterX - 150)) {
					area3X = mAreaL + 5;
					area3Y = area3Height + mGapHeightBLines1;
					area3LineNum += 1;
					area3wordCountInLine = 0;
				}
				if (area3Y < mAreaB) {
					if (area3LineNum % 2 == 1) {
						if (area3wordCountInLine == 0) {
							area3X += 150;
						}
						if (area3wordCountInLine % 2 == 1) {
							isScaleBig = false;
						}
					} else {
						if (area3wordCountInLine % 2 == 0) {
							isScaleBig = false;
						}
					}
					float area3Scale = getScale((int) area3X, (int) area3Y,
							isScaleBig);
					float tSize3 = mNormalTextSize * area3Scale;
					mPaint.setTextSize(tSize3);
					float tWid3 = mPaint.measureText(wp.text);
					area3Height = (int) ((area3Height >= area3Y + tSize3) ? area3Height
							: (area3Y + tSize3));
					wp.X = (int) area3X;
					wp.Y = (int) area3Y;
					wp.textSize = tSize3;
					wp.textWidth = tWid3;
					wp.scale = area3Scale;
					wp.textArea = new Rect(wp.X, wp.Y - (int) tSize3, wp.X
							+ (int) tWid3, wp.Y);
					area3X = area3X + GAP_HEIGHT_OF_COLUMN + tWid3;
					area3wordCountInLine += 1;
					break;
				}
			case 3:
				if (area4X > (mAreaR - 30)) {
					area4X = mCenterX + 5;
					area4Y = area4Height + mGapHeightBLines2;
					area4LineNum += 1;
					area4wordCountInLine = 0;
				}
				if (area4Y < mAreaB) {
					if (area4LineNum % 2 == 0) {
						if (area4wordCountInLine == 0) {
							area4X += 150;
						}
						if (area4wordCountInLine % 2 == 1) {
							isScaleBig = false;
						}
					} else {
						if (area4wordCountInLine % 2 == 0) {
							isScaleBig = false;
						}
					}
					float area4Scale = getScale((int) area4X, (int) area4Y,
							isScaleBig);
					float tSize4 = mNormalTextSize * area4Scale;
					mPaint.setTextSize(tSize4);
					float tWid4 = mPaint.measureText(wp.text);
					area4Height = (int) ((area4Height >= area4Y + tSize4) ? area4Height
							: (area4Y + tSize4));
					wp.X = (int) area4X;
					wp.Y = (int) area4Y;
					wp.textSize = tSize4;
					wp.textWidth = tWid4;
					wp.scale = area4Scale;
					wp.textArea = new Rect(wp.X, wp.Y - (int) tSize4, wp.X
							+ (int) tWid4, wp.Y);
					area4X = area4X + GAP_HEIGHT_OF_COLUMN + tWid4;
					area4wordCountInLine += 1;
					break;
				}
			default:
				break;
			}
			// LogUtil.e("----", "wp is null?>>>>>>>>>>>>>" + (wp == null) +
			// " rect is null?>>>>>>" + (wp.textArea == null) +
			// " index>>>>>>>>>>>>>>" + i % 4 + " mcX>>" + mCenterX +
			// " mcY>>>" + mCenterY);
			if (wp.textArea != null) {
				mPointers.add(wp);
			}
		}
	}

	private int getColor(float scale) {
		int color = 0;
		if (scale < 0.8) {
			color = Color.rgb(255, 206, 176);
		} else if (scale < 0.9) {
			color = Color.rgb(250, 206, 135);
		} else if (scale < 1) {
			color = Color.rgb(235, 150, 50);
		} else if (scale < 1.2) {
			color = Color.rgb(255, 91, 0);
		} else if (scale < 1.4) {
			color = Color.rgb(255, 0, 0);
		} else {
			color = Color.rgb(255, 0, 0);
		}
		return color;
	}

	private float getScale(int x, int y, boolean isToScaleBig) {
//		float scale = 0.7f;
//		if (mMaxRect.contains(x, y)) {
//			if (isToScaleBig) {
//				scale = 1.4f;
//			} else {
//				scale = 0.7f;
//			}
//		} else if (mCenterRect.contains(x, y)) {
//			if (isToScaleBig) {
//				scale = 1.2f;
//			} else {
//				scale = 0.8f;
//			}
//		} else if (mNormalRect.contains(x, y)) {
//			if (isToScaleBig) {
//				scale = 1.0f;
//			} else {
//				scale = 0.9f;
//			}
//		}
//		return scale;
		float scale = 0.7f;
		float distanx = (x - mCenterX) * (x - mCenterX);
		float distany = (y - mCenterY) * (y - mCenterY);
		double distance = Math.sqrt(distanx + distany);
		int width = Math.min(sHeight, sWidth);
		if (distance > width) {
			scale = 1.0f;
		} else {
			float temp = (float) (1.2f * distance / width);
			if (isToScaleBig) {
				scale = 1.6f - temp;
			} else {
				scale = 0.4f + temp;
			}
		}
		return scale;
	}

	public class WordPointer {
		private int X;
		private int Y;
		private String text;
		private float scale;
		private Rect textArea;
		private float textWidth;
		private float textSize;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDownX = event.getX();
			mDownY = event.getY();
			mLastDownX = mDownX;
			mLastDownY = mDownY;
			break;
		case MotionEvent.ACTION_MOVE:
			float moveX = event.getX();
			float moveY = event.getY();
			float moveXDis = Math.abs(moveX - mLastDownX);
			float moveYDis = Math.abs(moveY - mLastDownY);
			if (!isDragMove) {
				if (moveXDis > MIN_MOVE_DIS || moveYDis > MIN_MOVE_DIS) {
					isDragMove = true;
				}
			}
			if (isDragMove) {
				if (moveXDis >= moveYDis) {
					moveXDirection(moveX, moveY);
				} else {
//					moveYDirection(moveX, moveY);
				}
				invalidate();
			}
			break;
		case MotionEvent.ACTION_UP:
			float upX = event.getX();
			float upY = event.getY();
			if (!isDragMove) {
				onClickUp(upX, upY);
			}
			isDragMove = false;
			break;
		default:
			break;
		}
		return true;
	}

	/**
	 * X轴方向拖动
	 * 
	 * @param x
	 * @param y
	 */
	private void moveXDirection(float x, float y) {
		float movedDis = x - mLastDownX;
		mLastDownX = x;
		mLastDownY = y;
		for (WordPointer wp : mPointers) {
			boolean isToScaleBig = true;
			int newX;
			if (wp.scale >= 1.0f) {
				isToScaleBig = true;
				newX = Math.round(wp.X + movedDis*2);
				if (newX < mAreaL) {
					newX = mAreaL + (mAreaL - newX);
					isToScaleBig = false;
				} else if (newX > mAreaR) {
					newX = mAreaR - (newX - mAreaR);
					isToScaleBig = false;
				}
			} else {
				isToScaleBig = false;
				newX = Math.round(wp.X - movedDis*2);
				if (newX < mAreaL) {
					newX = mAreaL + (mAreaL - newX);
					isToScaleBig = true;
				} else if (newX > mAreaR) {
					newX = mAreaR - (newX - mAreaR);
					isToScaleBig = true;
				}
			}
			wp.X = newX;
			wp.scale = getScale(newX, wp.Y, isToScaleBig);
			float tSize = wp.scale * mNormalTextSize;
			mPaint.setTextSize(tSize);
			float wid = mPaint.measureText(wp.text);
			wp.textSize = tSize;
			wp.textWidth = wid;
			wp.textArea = new Rect(wp.X, (int) (wp.Y - tSize),
					(int) (wp.X + wid), wp.Y);
		}
	}

	/**
	 * Y轴方向拖动
	 * 
	 * @param x
	 * @param y
//	 */
//	private void moveYDirection(float x, float y) {
//		float movedDis = y - mLastDownY;
//		mLastDownX = x;
//		mLastDownY = y;
//		for (WordPointer wp : mPointers) {
//			boolean isToScaleBig = true;
//			int newY;
//			if (wp.scale >= 1.0f) {
//				isToScaleBig = true;
//				newY = Math.round(wp.Y + movedDis);
//				if (newY < mAreaT) {
//					newY = mAreaT + (mAreaT - newY);
//					isToScaleBig = false;
//				} else if (newY > mAreaB) {
//					newY = mAreaB - (newY - mAreaB);
//					isToScaleBig = false;
//				}
//			} else {
//				isToScaleBig = false;
//				newY = Math.round(wp.Y - movedDis);
//				if (newY < mAreaT) {
//					newY = mAreaT + (mAreaT - newY);
//					isToScaleBig = true;
//				} else if (newY > mAreaB) {
//					newY = mAreaB - (newY - mAreaB);
//					isToScaleBig = true;
//				}
//			}
//
//			wp.Y = newY;
//			wp.scale = getScale(wp.X, newY, isToScaleBig);
//			float tSize = wp.scale * mNormalTextSize;
//			mPaint.setTextSize(tSize);
//			float wid = mPaint.measureText(wp.text);
//			wp.textSize = tSize;
//			wp.textWidth = wid;
//			wp.textArea = new Rect(wp.X, (int) (wp.Y - tSize),
//					(int) (wp.X + wid), wp.Y);
//		}
//	}

	private void onClickUp(float x, float y) {
		for (WordPointer wp : mPointers) {
			if (wp.textArea.contains((int) x, (int) y)) {
				LogUtil.e("000", "on word click-------->>>>" + wp.text);
				if (mOnhotWordClickListener != null) {
					mOnhotWordClickListener.onHotWordClick(wp.text);
				}
			}
		}
	}

	public List<HotSearchWord> getmWords() {
		return mWords;
	}

	public void setmWords(List<HotSearchWord> mWords) {
		this.mWords = mWords;
		initPointers();
		invalidate();
	}

	public onHotWordClickListener getmOnhotWordClickListener() {
		return mOnhotWordClickListener;
	}

	public void setmOnhotWordClickListener(
			onHotWordClickListener mOnhotWordClickListener) {
		this.mOnhotWordClickListener = mOnhotWordClickListener;
	}

	public interface onHotWordClickListener {
		void onHotWordClick(String word);
	}
}
