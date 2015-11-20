package com.cplatform.xhxw.ui.ui.base.view;

import com.cplatform.xhxw.ui.ui.main.saas.addressBook.OnclikViewRight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SeekBar_Right extends View {

	private String[] text = new String[26];

	private Paint paint_1;

	private int TextRange;

	private OnclikViewRight onclikViewRight;

	public SeekBar_Right(Context context, AttributeSet attrs) {
		super(context, attrs);
		Init();
	}

	public void Init() {

		for (int i = 0; i < text.length; i++) {
			text[i] = ((char) ((int) 'A' + i)) + "";
		}

		paint_1 = new Paint();
		paint_1.setColor(new Color().WHITE);
		paint_1.setTextSize(15);
		paint_1.setAntiAlias(true);
		
		
		setBackgroundColor(0x50000000);
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);

		int screen_w = getWidth();
		int screen_h = getHeight();
		TextRange = screen_h / 26;

		for (int i = 0; i < text.length; i++) {
			int X = screen_w - getTextWidth(text[i]) >> 1;
			canvas.drawText(text[i], X, (i + 1) * TextRange, paint_1);
		}
	}

	/**
	 * ��������
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {

		case MotionEvent.ACTION_UP:
//			setBackgroundColor(0xffFFFFFF);
			setBackgroundColor(0x50000000);
			//android:background="#ffFFFFFF"

			if (onclikViewRight != null) {
				onclikViewRight.seteventUP();
			}
			break;

		case MotionEvent.ACTION_DOWN:
			setBackgroundColor(0xa0000000);
			break;
		case MotionEvent.ACTION_MOVE:

//			setBackgroundColor(0xffECEFEF);
			setBackgroundColor(0xa0000000);
			float Y = event.getY(); // �õ���ǰ����
			int YY = (int) Y / TextRange; // ���㵱ǰλ�õ���ĸ

			if (YY >= text.length) {
				YY = text.length - 1;
			}
			//TODO
			if(YY<0){
				YY=0;
			}
			String str = text[YY];

			if (onclikViewRight != null) {
				onclikViewRight.seteventDownAndMove(str);
			}

			break;
		}

		return true;
	}

	/**
	 * ����������
	 * 
	 * @param str
	 * @return
	 */
	public int getTextWidth(String str) {

		Rect rect = new Rect();
		paint_1.getTextBounds(str, 0, str.length(), rect);
		return rect.width();

	}

	/** ���ü��� */
	public void setOnclikViewRight(OnclikViewRight onclikViewRight) {
		this.onclikViewRight = onclikViewRight;
	}

}
