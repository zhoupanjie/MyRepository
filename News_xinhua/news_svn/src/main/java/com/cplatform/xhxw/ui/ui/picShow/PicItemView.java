package com.cplatform.xhxw.ui.ui.picShow;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import butterknife.Bind;
import com.cplatform.xhxw.ui.ui.base.widget.DefaultView;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.cplatform.xhxw.ui.R;

/**
 * Created by cy-love on 13-12-31.
 */
public class PicItemView extends RelativeLayout {

    private static final String TAG = PicItemView.class.getSimpleName();
    @Bind(R.id.iv_img)
    PhotoView mImg;
    @Bind(R.id.def_view)
    DefaultView mDefView;

    private String mUrl;

    public PicItemView(Context context, String imgUrl, OnViewTapListener listener) {
        super(context);
        mUrl = imgUrl;
        this.init(listener);
    }

    private void init(final OnViewTapListener listener) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_pic_item, this);
        ButterKnife.bind(this);
        mDefView.setHidenOtherView(mImg);
        mDefView.setListener(new DefaultView.OnTapListener() {
            @Override
            public void onTapAction() {
                displayImg();
            }
        });
        displayImg();
        mImg.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                if (listener != null) {
                    listener.onViewTap();
                }
            }
        });
    }

    private void displayImg() {
    	LogUtil.e(TAG, "-----murl--------->>>" + mUrl);
    	DisplayImageOptions opts = new DisplayImageOptions.Builder()
    					.bitmapConfig(Bitmap.Config.RGB_565)
    					.imageScaleType(ImageScaleType.EXACTLY).build();
        ImageLoader.getInstance().loadImage(mUrl, opts, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                mDefView.setStatus(DefaultView.Status.loading);
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                mDefView.setStatus(DefaultView.Status.error);
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                mDefView.setStatus(DefaultView.Status.showData);
                try {
                    mImg.setImageBitmap(bitmap);
                } catch (OutOfMemoryError e) {
                    LogUtil.w(TAG, e);
                } catch (Exception e) {
                    LogUtil.w(TAG, e);
                }
            }

            @Override
            public void onLoadingCancelled(String s, View view) {}
        });
    }

    public String getUrl() {
        return mUrl;
    }

    public interface OnViewTapListener {
        public void onViewTap();
    }

}
