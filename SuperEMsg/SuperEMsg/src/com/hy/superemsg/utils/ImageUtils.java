
package com.hy.superemsg.utils;

import android.support.v4.app.FragmentActivity;

import com.baseproject.image.ImageCache;
import com.baseproject.image.ImageFetcher;
import com.baseproject.image.Utils;

public class ImageUtils {
    public static ImageFetcher Image;

    public static void initImage(FragmentActivity activity) {
        final int width = activity.getResources().getDisplayMetrics().widthPixels;
        final int height = activity.getResources().getDisplayMetrics().heightPixels;
        final int shortest = height > width ? width : height;
        Image = new ImageFetcher(activity, shortest);
        Image.setImageCache(ImageCache.findOrCreateCache(activity,
                Utils.IMAGE_CACHE_DIR));
        Image.setImageFadeIn(true);
    }
}
