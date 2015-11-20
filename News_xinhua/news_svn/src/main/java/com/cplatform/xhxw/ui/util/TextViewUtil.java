package com.cplatform.xhxw.ui.util;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.TextView;
import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;

/**
 * Created by cy-love on 14-1-23.
 */
public class TextViewUtil {

    /**
     * 设置（日间/夜间）显示模式
     * @param context 上下文
     * @param header 头布局
     * @param title 标题
     * @param desc 简介
     * @param isRead 是否已读
     */
    public static void setDisplayModel(Context context,TextView header, TextView title, TextView desc, boolean isRead) {
        int model = App.getPreferenceManager().getDispalyModel();
        Resources res = context.getResources();
        switch (model) {
            case Constants.DISPLAY_MODEL_DAY:
            {
                int color = isRead ? R.color.base_list_desc_color : R.color.base_list_title_color;
                title.setTextColor(res.getColor(color));
                header.setTextColor(res.getColor(R.color.base_list_title_color));
                desc.setTextColor(res.getColor(color));
            }
            break;
            case Constants.DISPLAY_MODEL_NIGHT:
            {
                int color = isRead ? R.color.night_base_list_desc_color : R.color.night_base_list_title_color;
                title.setTextColor(res.getColor(color));
                header.setTextColor(res.getColor(R.color.base_list_title_color));
                desc.setTextColor(res.getColor(color));
            }
            break;
        }
    }

    /**
     * 设置（日间/夜间）显示模式
     * @param context 上下文
     * @param title 标题
     * @param desc 简介
     * @param isRead 是否已读
     */
    public static void setDisplayModel(Context context, TextView title, TextView desc, boolean isRead) {
        int model = App.getPreferenceManager().getDispalyModel();
        Resources res = context.getResources();
        switch (model) {
            case Constants.DISPLAY_MODEL_DAY:
            {
                int color = isRead ? R.color.base_list_desc_color : R.color.base_list_title_color;
                title.setTextColor(res.getColor(color));
                desc.setTextColor(res.getColor(color));
            }
            break;
            case Constants.DISPLAY_MODEL_NIGHT:
            {
                int color = isRead ? R.color.night_base_list_desc_color : R.color.night_base_list_title_color;
                title.setTextColor(res.getColor(color));
                desc.setTextColor(res.getColor(color));
            }
            break;
        }
    }
    
    /**
     * 
     * @Name setGameDisplayModel 
     * @Description TODO 设置游戏（日间/夜间）显示模式
     * @param context
     * @param title
     * @param desc
     * @param isRead 
     * @return void
     * @Author zxe
     * @Date 2015年7月7日 上午11:03:07
    *
     */
    public static void setGameDisplayModel(Context context, TextView title, TextView desc, boolean isRead) {
        int model = App.getPreferenceManager().getDispalyModel();
        Resources res = context.getResources();
        switch (model) {
            case Constants.DISPLAY_MODEL_DAY:
            {
                int color = isRead ? R.color.base_list_desc_color : R.color.base_list_title_color;
                title.setTextColor(res.getColor(color));
                desc.setTextColor(res.getColor(color));
            }
            break;
            case Constants.DISPLAY_MODEL_NIGHT:
            {
                int color = isRead ? R.color.night_base_list_desc_color : R.color.night_base_list_title_color;
                title.setTextColor(res.getColor(color));
                desc.setTextColor(res.getColor(color));
            }
            break;
        }
    }
    /**
     * 设置（日间/夜间）显示模式
     * @param context 上下文
     * @param title 标题
     */
    public static void setDisplayModel(Context context, TextView title) {
        int model = App.getPreferenceManager().getDispalyModel();
        Resources res = context.getResources();
        switch (model) {
            case Constants.DISPLAY_MODEL_DAY:
            {
                title.setTextColor(res.getColor(R.color.base_list_title_color));
            }
            break;
            case Constants.DISPLAY_MODEL_NIGHT:
            {
                title.setTextColor(res.getColor(R.color.night_base_list_title_color));
            }
            break;
        }
    }
    
    /**
     * 设置（日间/夜间）显示模式
     * @param context 上下文
     * @param title 标题
     * @param isRead 是否已读
     */
    public static void setDisplayModel(Context context, TextView title, boolean isRead) {
        int model = App.getPreferenceManager().getDispalyModel();
        Resources res = context.getResources();
        switch (model) {
            case Constants.DISPLAY_MODEL_DAY:
            {
                int color = isRead ? R.color.base_list_desc_color : R.color.base_list_title_color;
                title.setTextColor(res.getColor(color));
            }
            break;
            case Constants.DISPLAY_MODEL_NIGHT:
            {
                int color = isRead ? R.color.night_base_list_desc_color : R.color.night_base_list_title_color;
                title.setTextColor(res.getColor(color));
            }
                break;
        }
    }

    /**
     * 设置灰色半透明背景（日间/夜间）显示模式
     * @param context 上下文
     * @param title 标题
     * @param isRead 是否已读
     */
    public static void setTranslucentBgDisplayModel(Context context, TextView title, boolean isRead) {
        int model = App.getPreferenceManager().getDispalyModel();
        Resources res = context.getResources();
        switch (model) {
            case Constants.DISPLAY_MODEL_DAY:
            {
                int color = isRead ? R.color.base_list_desc_color : R.color.white;
                title.setTextColor(res.getColor(color));
            }
            break;
            case Constants.DISPLAY_MODEL_NIGHT:
            {
                int color = isRead ? R.color.night_base_list_desc_color : R.color.white;
                title.setTextColor(res.getColor(color));
            }
            break;
        }
    }

    /**
     * 设置背景（日间/夜间）显示模式
     */
    public static void setDisplayBgModel(Context context, View view) {
        int model = App.getPreferenceManager().getDispalyModel();
        switch (model) {
            case Constants.DISPLAY_MODEL_DAY:
            {
                view.setBackgroundColor(context.getResources().getColor(R.color.base_main_bg_color));
            }
            break;
            case Constants.DISPLAY_MODEL_NIGHT:
            {
                view.setBackgroundColor(context.getResources().getColor(R.color.night_base_main_bg_color));
            }
            break;
        }
    }

}
