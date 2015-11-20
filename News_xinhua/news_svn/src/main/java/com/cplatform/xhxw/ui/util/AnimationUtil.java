package com.cplatform.xhxw.ui.util;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import com.cplatform.xhxw.ui.R;

/**
 * 动画工具类
 * Created by cy-love on 14-1-26.
 */
public class AnimationUtil {

    /**
     * 由上向下显示
     * @param context
     * @param view 执行动画并显示的view
     */
    public static void startViewTopInAndVisible(Context context, View view) {
        startViewTopInAndVisible(context, view, view);
    }

    /**
     * 由上向下显示
     * @param context
     * @param view 执行动画的view
     * @param visibleView 执行动画后需要显示的view
     */
    public static void startViewTopInAndVisible(Context context, View view, View visibleView) {
        visibleView.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.translate_top_in);
        view.startAnimation(anim);
    }

    /**
     * 由下向上显示
     * @param context
     * @param view 执行动画并显示的view
     */
    public static void startViewBottomInAndVisible(Context context, View view) {
        startViewBottomInAndVisible(context, view, view);
    }

    /**
     * 由下向上显示
     * @param context
     * @param view 执行动画的view
     * @param visibleView 执行动画后需要显示的view
     */
    public static void startViewBottomInAndVisible(Context context, View view, View visibleView) {
        visibleView.setVisibility(View.VISIBLE);
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.translate_bottom_in);
        view.startAnimation(anim);
    }

    /**
     * 由下向上隐藏
     * @param context
     * @param view 执行动画并隐藏的view
     */
    public static void startViewTopOutAndGone(Context context, View view) {
        startViewTopOutAndGone(context, view, view);
    }

    /**
     * 由下向上隐藏
     * @param context
     * @param view
     * @param hideView 动画结束后需要隐藏的view
     */
    public static void startViewTopOutAndGone(Context context, View view, final View hideView) {
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.translate_top_out);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (hideView != null) hideView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(anim);
    }

    /**
     * 由上向下隐藏
     * @param context
     * @param view 执行动画并隐藏的view
     */
    public static void startViewBottomOutAndGone(Context context, View view) {
        startViewBottomOutAndGone(context, view, view);
    }

    /**
     * 由上向下隐藏
     * @param context
     * @param view
     * @param hideView 动画结束后需要隐藏的view
     */
    public static void startViewBottomOutAndGone(Context context, View view, final View hideView) {
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.translate_bottom_out);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (hideView != null) hideView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(anim);
    }

}
