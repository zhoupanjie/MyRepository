package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.cplatform.xhxw.ui.R;

public class FriendsMessageFooter extends LinearLayout {

	private View view;
	
    public FriendsMessageFooter(Context context) {
        super(context);
        init();
    }

    public FriendsMessageFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public FriendsMessageFooter(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.activity_friends_message_bottom_layout, this);
    }

    /**
     * 设置footer显示
     */
    public void setFooterVisibility(int visibility) {
    	view.findViewById(R.id.friends_message_more_layout).setVisibility(visibility);
    }

    /**
     * 设置footer点击监听
     */
    public void setFooterOnClickListener(OnClickListener listener) {
    	view.findViewById(R.id.friends_message_more_layout).setOnClickListener(listener);
    }

    /**
     * 设置footer隐藏
     */
    public void setFooterGone(int visibility) {
    	view.findViewById(R.id.friends_message_more_layout).setVisibility(visibility);
    }

}
