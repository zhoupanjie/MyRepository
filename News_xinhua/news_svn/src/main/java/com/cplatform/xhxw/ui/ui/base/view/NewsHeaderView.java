package com.cplatform.xhxw.ui.ui.base.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.cplatform.xhxw.ui.model.Focus;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;

import java.util.List;

/**
 * 新闻列表headerview
 * Created by cy-love on 14-2-18.
 */
public class NewsHeaderView extends RelativeLayout {

    private View view;

    public NewsHeaderView(Context context) {
        super(context);
    }

    public NewsHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewsHeaderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 设置推荐图
     */
    public void setSlider(List<Focus> focus, OnSliderImgOnClickListener listener) {
        SliderView sliderView;
        if (view != null && !(view instanceof SliderView)) {
            removeAllViews();
        }
        if (view == null ) {
            sliderView = new SliderView(getContext(), listener);
            view = sliderView;
            addView(view);
        } else {
            sliderView = (SliderView) view;
        }
        sliderView.setDate(focus);
    }

    /**
     * 设置频道列表
     */
    public void setNewsThumbnail(String url) {
        NewsThumbnailView thumbnail;
        if (view != null && !(view instanceof NewsThumbnailView)) {
            removeAllViews();
        }
        if (view == null ) {
            thumbnail = new NewsThumbnailView(getContext());
            view = thumbnail;
            addView(view);
        } else {
            thumbnail = (NewsThumbnailView) view;
        }
        ImageLoader.getInstance().displayImage(url, thumbnail,
                DisplayImageOptionsUtil.newsSliderImgOptions);
    }

    public void removeView() {
        removeAllViews();
        view = null;
    }
}
