package com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
/**
 * 
 * @ClassName MTextView 
 * @Description TODO 播放标题滚动文本
 * @Version 1.0
 * @Author zxe
 * @Creation 2015年9月23日 上午10:13:37 
 * @Mender zxe
 * @Modification 2015年9月23日 上午10:13:37 
 * @Copyright Copyright © 2013 - 2015 Channelsoft (Beijing) Technology Co., Ltd.All Rights Reserved.
*
 */
public class MTextView  extends TextView{
	
    public MTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
    
    public MTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

	@Override
    public boolean isFocused() {
        // TODO Auto-generated method stub
        return true;
    }

}
