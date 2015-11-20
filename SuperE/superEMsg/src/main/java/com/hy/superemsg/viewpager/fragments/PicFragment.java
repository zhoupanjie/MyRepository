
package com.hy.superemsg.viewpager.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.baseproject.image.ImageCallback;
import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.rsp.Image;
import com.hy.superemsg.utils.ImageUtils;
import com.hy.superemsg.viewpager.AbsFragment;

public class PicFragment extends AbsFragment {
    private ImageView image;
    private Image img;
    private ProgressBar loading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        img = getArguments().getParcelable(SuperEMsgApplication.EXTRA_PIC_DETAIL);
        return inflater.inflate(R.layout.fragment_pic, null);
    }

    @Override
    protected void initUI() {
        image = (ImageView) this.getView().findViewById(R.id.item_image);
        loading = (ProgressBar) getView().findViewById(R.id.item_loading);
        loading.setVisibility(View.VISIBLE);
    }

    @Override
    protected void excuteTask() {
        if (img != null) {
            ImageUtils.Image.loadImage(img.src, image, new ImageCallback() {

                @Override
                public void imageLoaded(Bitmap bitmap, String imageUrl) {
                    loading.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    protected void resetUI() {
        // TODO Auto-generated method stub

    }

    public Bitmap getBitmap() {
        if (img != null) {
            this.image.buildDrawingCache();
            return image.getDrawingCache();
        }
        return null;
    }

}
