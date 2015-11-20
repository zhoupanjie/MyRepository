package com.cplatform.xhxw.ui.ui.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import com.cplatform.xhxw.ui.Constants;

/**
 * 新闻推荐图
 * Created by cy-love on 14-1-13.
 */
public class NewsThumbnailView extends ImageView {

    public NewsThumbnailView(Context context) {
        super(context);
        init();
    }

    public NewsThumbnailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NewsThumbnailView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(Constants.screenWidth, getSliderHeight());
        setLayoutParams(lp);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private int getSliderHeight() {
        double height = Constants.screenWidth * Constants.HomeThumbnailSize.height / Constants.HomeThumbnailSize.width;
        return (int) Math.ceil(height) + 1;
    }
}
