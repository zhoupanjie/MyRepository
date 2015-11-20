package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.cplatform.xhxw.ui.R;

/**
 * 联系人头部布局
 * Created by cy-love on 14-8-4.
 */
public class AddressBookHeader extends LinearLayout {

    public AddressBookHeader(Context context) {
        super(context);
        init();
    }

    public AddressBookHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public AddressBookHeader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_address_book_header, this);
    }

    /**
     * 设置搜索是否可见
     */
    public void setSearchVisibility(int visibility) {
        findViewById(R.id.ly_search).setVisibility(visibility);
    }

    /**
     * 设置搜索点击监听
     */
    public void setSearchOnClickListener(OnClickListener listener) {
        findViewById(R.id.ly_search).setOnClickListener(listener);
    }

    /**
     * 设置新的朋友布局是否可见
     */
    public void setNewFriendVisibility(int visibility) {
        findViewById(R.id.ly_new_friend).setVisibility(visibility);
    }

    /**
     * 设置新的朋友布局是否可见
     */
    public void setNewFriendNum(int num) {
        TextView view = (TextView)findViewById(R.id.tv_num);
        if (num == 0) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
            view.setText(String.valueOf(num));
        }
    }

    public int getNewFriendNum() {
        TextView view = (TextView)findViewById(R.id.tv_num);
        if (view.getVisibility() == View.GONE) {
            return 0;
        }
        String count = view.getText().toString();
        try {
            return Integer.valueOf(count);
        } catch (Exception e) {
            return 0;
        }
    }
}
