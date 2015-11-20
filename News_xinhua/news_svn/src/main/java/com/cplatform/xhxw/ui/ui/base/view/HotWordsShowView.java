package com.cplatform.xhxw.ui.ui.base.view;

import java.util.ArrayList;
import java.util.List;

import com.cplatform.xhxw.ui.model.HotSearchWord;
import com.cplatform.xhxw.ui.util.LogUtil;

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

public class HotWordsShowView extends View implements OnTouchListener {
	private static final int MIN_PADDING = 20;
	private static final int MIN_MOVE_DISTANCE = 20;
	
	private int mNormalTextSize;
	private List<HotSearchWord> mWords = new ArrayList<HotSearchWord>();
	private int mRadius;
	private int mCenterX;
	private int mCenterY;
	private Context mCon;
	private List<TextPointer> mPointers = new ArrayList<TextPointer>();
	private Paint mPaint;
	private double mRadian = Math.PI / 180;
	
	private float mDownX;
	private float mDownY;
	private float mLastDownX;
	private float mLastDownY;
	private boolean mIsMove = false;
	
	private onHotWordClickListener mOnhotWordClickListener;
	
	public HotWordsShowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mCon = context;
		init();
	}

	public HotWordsShowView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public HotWordsShowView(Context context) {
		this(context, null);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		LogUtil.e("--", "width--------" + MeasureSpec.getSize(widthMeasureSpec) + " hei--------"
				+ MeasureSpec.getSize(heightMeasureSpec));
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		mCenterX = width / 2;
		mCenterY = height / 2;
		mRadius = (Math.min(width, height) - MIN_PADDING * 2) / 2;
		initPointers();
		setMeasuredDimension(width, height);
	}

	private void init() {
		LogUtil.e("", "width::" + getWidth() + " messuredWidth::" + getMeasuredWidth()
				+ " height::" + getHeight() + " measuredHeight::" + getMeasuredHeight());
		mPaint = new Paint();
		mPaint.setTypeface(Typeface.DEFAULT_BOLD);
		mNormalTextSize = (int) (24 * mCon.getResources().getDisplayMetrics().density);
		setOnTouchListener(this);
	}
	
	private void initPointers() {
		int wordsCount = (mWords == null) ? 0 : mWords.size();
		if(wordsCount < 1) return;
		mPointers.clear();
		int aveAng = (wordsCount == 1) ? 0 : 360 / (wordsCount - 1);
		int aveVerticalAng = (wordsCount == 1) ? 0 : 150 / (wordsCount - 2);
		
		Rect rect = new Rect();
		float tSize = (float) (mNormalTextSize * 1.5);
		mPaint.setTextSize(tSize);
		float twid = mPaint.measureText(mWords.get(0).getTag());
		TextPointer tp = new TextPointer();
		tp.textSize = tSize;
		tp.textWidth = twid;
		tp.pointX = (int) (mCenterX - twid / 2);
		tp.pointY = (int) (mCenterY + tSize / 2);
		rect.left = tp.pointX;
		rect.top = (int) (tp.pointY - tSize);
		rect.right = (int) (tp.pointX + twid);
		rect.bottom = tp.pointY;
		tp.text = mWords.get(0).getTag();
		tp.textArea = rect;
		tp.textScale = 1.5;
		mPointers.add(tp);
		
		for(int i = 1; i < wordsCount; i++) {
			int ev = 0;
			if(i % 2 == 0) {
				ev = 90 - aveVerticalAng * i - 10;
			} else {
				ev = ((-aveVerticalAng * i - 25) < -90) ? (-aveVerticalAng * i - 25 + 180)
						: (-aveVerticalAng * i - 25);
				ev = (ev > 70) ? 60 : ev;
			}
			
			double scale = ((1.0 + Math.sin(ev * mRadian) / 2) < 0.4) ? 0.4 : (1.0 + Math.sin(ev * mRadian) / 2);
			scale = (scale > 1.5) ? 1.5 : scale;
			float textSize = (float) (mNormalTextSize * scale);
			mPaint.setTextSize(textSize);
			Rect rt = new Rect();
			float textWidth = mPaint.measureText(mWords.get(i).getTag());
			
			int X = (mCenterX + (int) (mRadius * Math.cos(ev * mRadian) * Math.cos(aveAng * i * mRadian))) - (int)textWidth / 2;
			int Y = (mCenterY - (int) (mRadius * Math.cos(ev * mRadian) * Math.sin(aveAng * i * mRadian))) + (int)textSize / 2;
			LogUtil.e("init", "init rect width:::" + textWidth + " height::" +
					textSize + " textSize:::::" + mNormalTextSize * scale + " \nAng::" + ev
					+ " aveAng------>>>>>" + aveAng * i + " centerX::" + X + 
					" centerY::::" + Y + " radius>>>>>>" + mRadius + " text::" + mWords.get(i).getTag()
					+ "\n centX::" + mCenterX + " centY::" + mCenterY + " orSize::" + mNormalTextSize);
			TextPointer tPointer = new TextPointer();
			tPointer.pointY = Y;
			tPointer.pointX = X;
			tPointer.textSize = textSize;
			tPointer.textWidth = textWidth;
			rt.left = tPointer.pointX;
			rt.top = (int) (tPointer.pointY - textSize);
			rt.right = (int) (tPointer.pointX + textWidth);
			rt.bottom = tPointer.pointY;
			tPointer.text = mWords.get(i).getTag();
			tPointer.textArea = rt;
			tPointer.textScale = scale;
			mPointers.add(tPointer);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		LogUtil.e("draw", "onDraw-------->>>>>>" + mPointers.size());
		for(int i = 0; i < mPointers.size(); i++) {
			TextPointer tp = mPointers.get(i);
			setPaint(tp);
			LogUtil.e("draw", "draw text:" + tp.text + " coorX:" + tp.pointX + 
					" coorY:" + tp.pointY);
			canvas.drawText(tp.text, tp.pointX, tp.pointY, mPaint);
		}
	}
	
	private void setPaint(TextPointer tp) {
		if(tp.textScale <= 0.6) {
			mPaint.setColor(Color.rgb(135, 206, 250));
		} else if(tp.textScale < 0.8) {
			mPaint.setColor(Color.rgb(135, 206, 235));
		} else if(tp.textScale < 1) {
			mPaint.setColor(Color.rgb(0, 191, 255));
		} else if(tp.textScale <= 1.3) {
			mPaint.setColor(Color.rgb(30, 144, 255));
		} else if(tp.textScale <= 1.5) {
			mPaint.setColor(Color.rgb(0, 0, 255));
		}
		mPaint.setTextSize((float) (mNormalTextSize * tp.textScale));
	}

	public List<HotSearchWord> getmWords() {
		return mWords;
	}

	public void setmWords(List<HotSearchWord> mWords) {
		this.mWords = mWords;
		initPointers();
		invalidate();
	}
	
	public class TextPointer {
		private int pointX;
		private int pointY;
		private String text;
		private double textScale;
		private Rect textArea;
		private float textWidth;
		private float textSize;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			LogUtil.e("touch", "-----------------down");
			mDownX = event.getX();
			mDownY = event.getY();
			mLastDownX = mDownX;
			mLastDownY = mDownY;
			break;
		case MotionEvent.ACTION_MOVE:
			float moveX = event.getX();
			float moveY = event.getY();
			int moveDistance = (int) Math.sqrt(Math.pow(moveX - mDownX, 2) + Math.pow(moveY - mDownY, 2));
			if(moveDistance > MIN_MOVE_DISTANCE) {
				mIsMove = true;
			}
			if(mIsMove) {
				moveItems(moveX, moveY);
				mLastDownX = moveX;
				mLastDownY = moveY;
			}
			break;
		case MotionEvent.ACTION_UP:
			float upX = event.getX();
			float upY = event.getY();
			
			if(!mIsMove) {
				if(mOnhotWordClickListener != null) {
					TextPointer tp = getClickedWord(upX, upY);
					if(tp != null) {
						mOnhotWordClickListener.onHotWordClick(tp.text);
						LogUtil.e("click", "-----------clicked hot word=========" + tp.text);
					}
				}
			}
			
			mIsMove = false;
			break;

		default:
			break;
		}
		return true;
	}
	
	private void moveItems(float targetX, float targetY) {
		boolean isMoveXLine = Math.abs(targetX - mLastDownX) >= Math.abs(targetY - mLastDownY);
		double moveDis = isMoveXLine ? Math.abs(targetX - mLastDownX) : Math.abs(targetY - mLastDownY);
		double movedRadians = moveDis / mRadius;
		for(TextPointer tp : mPointers) {
			if(isMoveXLine) {
				if(targetX < mLastDownX) {
					movedRadians = 0 - movedRadians;
				}
				int centerX = tp.pointX + (int) (tp.textWidth / 2);
				int centerY = tp.pointY - (int) (tp.textSize / 2);
				double r = Math.sqrt(mRadius * mRadius - centerY * centerY);
				double originalRad = Math.asin(tp.pointX / r);
				int circleCenterX = centerX - mCenterX;
				int circleCenterY = centerY - mCenterY;
				if(tp.textScale < 1) {
					if(circleCenterX > 0) {
						originalRad = Math.PI - originalRad;
					} else {
						originalRad = Math.PI + originalRad;
					}
				} else {
					if(circleCenterX < 0) {
						originalRad = Math.PI * 2 - originalRad;
					}
				}
				tp.pointX = (int) (mCenterX + r * Math.sin(originalRad + movedRadians));
				tp.textScale = 1 + (r * Math.cos(originalRad + movedRadians)) / (2 * mRadius);
				float tSize = (float) (mNormalTextSize * tp.textScale);
				float wid = mPaint.measureText(tp.text);
				tp.textArea.left = tp.pointX;
			}
		}
	}
	
	private TextPointer getClickedWord(float coorX, float coorY) {
		for(TextPointer tp : mPointers) {
//			LogUtil.e("---", "X:::" + coorX + " Y::" + coorY + " rectL::" + tp.textArea.left +
//					" t::" + tp.textArea.top + " r::" + tp.textArea.right + " b::" + tp.textArea.bottom +
//					" text::" + tp.text);
			if(tp.textArea.contains((int)coorX, (int)coorY)) {
				return tp;
			}
		}
		return null;
	}
	
	public onHotWordClickListener getmOnhotWordClickListener() {
		return mOnhotWordClickListener;
	}

	public void setmOnhotWordClickListener(onHotWordClickListener mOnhotWordClickListener) {
		this.mOnhotWordClickListener = mOnhotWordClickListener;
	}

	public interface onHotWordClickListener {
		void onHotWordClick(String word);
	}
}
