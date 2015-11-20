package com.cplatform.xhxw.ui.ui.base.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class MyLetterListView extends View {
	
	OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	String[] b = {"#","A","B","C","D","E","F","G","H","I","J","K","L"
			,"M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	int choose = -1;
	Paint paint = new Paint();
	boolean showBkg = false; 

	public MyLetterListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyLetterListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyLetterListView(Context context) {
		super(context);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(showBkg){
//			canvas.drawColor(Color.parseColor("#40000000"));
			
//			paint.setAntiAlias(true);                       //设置画笔为无锯齿  
//		    paint.setColor(Color.BLACK);  
//		    canvas.drawColor(Color.parseColor("#40000000"));  //背景  
//		    paint.setStrokeWidth((float) 1.0);              //线宽  
//		    paint.setStyle(Style.STROKE);       
//			RectF r2=new RectF();                           //RectF对象  
//		    r2.left=1;                                 //左边  
//		    r2.top=1;                                 //上边  
//		    r2.right=1;                                   //右边  
//		    r2.bottom=1;
//		    canvas.drawRoundRect(r2, 10, 10, paint);
		}else {
			
		}
		
	    int height = getHeight();
	    int width = getWidth();
	    int singleHeight = height / b.length;
	    for(int i=0;i<b.length;i++){
	    	paint.setTextSize(sp2px(12));
	       paint.setColor(Color.parseColor("#000000"));
	       paint.setTypeface(Typeface.DEFAULT);
	       paint.setAntiAlias(true);
	       if(i == choose){
	    	   paint.setColor(Color.parseColor("#3399ff"));
	    	   paint.setFakeBoldText(true);
	       }
	       float xPos = (width  - paint.measureText(b[i]))/2;
	       float yPos = singleHeight * i + singleHeight;
	       canvas.drawText(b[i], xPos, yPos, paint);
	       paint.reset();
	    }
	   
	}
	
	/**
	  * 将sp值转换为px值，保证文字大小不变
	  */
	public int sp2px(float spValue) { 
        final float fontScale = getResources().getDisplayMetrics().scaledDensity; 
        return (int) (spValue * fontScale + 0.5f); 
    } 
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
	    final float y = event.getY();
	    final int oldChoose = choose;
	    final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
	    final int c = (int) (y/getHeight()*b.length);
	    
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				showBkg = true;
				if(oldChoose != c && listener != null){
					if(c > 0 && c< b.length){
						listener.onTouchingLetterChanged(b[c]);
						choose = c;
						invalidate();
					}
				}
				
				break;
			case MotionEvent.ACTION_MOVE:
				if(oldChoose != c && listener != null){
					if(c > 0 && c< b.length){
						listener.onTouchingLetterChanged(b[c]);
						choose = c;
						invalidate();
					}
				}
				break;
			case MotionEvent.ACTION_UP:
				showBkg = false;
				choose = -1;
				invalidate();
				break;
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return super.onTouchEvent(event);
	}

	public void setOnTouchingLetterChangedListener(
			OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	public interface OnTouchingLetterChangedListener{
		public void onTouchingLetterChanged(String s);
	}
	
}
