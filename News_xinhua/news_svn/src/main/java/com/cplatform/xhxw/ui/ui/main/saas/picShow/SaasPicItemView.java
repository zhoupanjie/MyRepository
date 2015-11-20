package com.cplatform.xhxw.ui.ui.main.saas.picShow;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import butterknife.ButterKnife;
import butterknife.Bind;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.ui.base.widget.DefaultView;
import com.cplatform.xhxw.ui.ui.picShow.PhotoView;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

/**
 * Created by cy-love on 13-12-31.
 */
public class SaasPicItemView extends RelativeLayout {

    private static final String TAG = SaasPicItemView.class.getSimpleName();
    @Bind(R.id.iv_img)
    PhotoView mImg;
    @Bind(R.id.def_view)
    DefaultView mDefView;

    private String mUrl;

    public SaasPicItemView(Context context, String imgUrl) {
        super(context);
        mUrl = imgUrl;
        this.init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_saas_pic_item, this);
        ButterKnife.bind(this);
        mDefView.setHidenOtherView(mImg);
        mDefView.setListener(new DefaultView.OnTapListener() {
            @Override
            public void onTapAction() {
                displayImg();
            }
        });
        displayImg();
    }

    private void displayImg() {
        ImageLoader.getInstance().loadImage(mUrl, new ImageLoadingListener() {
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

    public interface OnViewTapListener {
        public void onViewTap();
    }

}
