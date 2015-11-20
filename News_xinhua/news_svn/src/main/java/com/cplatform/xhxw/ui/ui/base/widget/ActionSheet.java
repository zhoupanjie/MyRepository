package com.cplatform.xhxw.ui.ui.base.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.*;
import android.widget.Button;
import android.widget.LinearLayout;
import com.cplatform.xhxw.ui.R;

import java.util.ArrayList;

public class ActionSheet extends Dialog {

    private static final String TAG = ActionSheet.class.getSimpleName();

    public interface GPopupMenuListener {
        public void onMenuSelected(MenuItem item);
    }

    /**
     * 每个菜单项封装
     */
    public static class MenuItem {
        //背景颜色类型
        public static final int BACKGROUND_BLACK = 1;
        public static final int BACKGROUND_GRAY = 2;
        public static final int BACKGROUND_RED = 3;

        private Object tag;//存储需要回传的数据
        private CharSequence action;
        private GPopupMenuListener listener;
        private boolean enable = true;
    
        private int backgroundType = BACKGROUND_GRAY;//背景类型

        public MenuItem(CharSequence action, Object tag, GPopupMenuListener listener) {
            this.action = action;
            this.listener = listener;
            this.tag = tag;
        }

        public MenuItem(CharSequence action, int backgroundType, Object tag, GPopupMenuListener listener) {
            this(action, tag, listener);
            this.backgroundType = backgroundType;
        }

        public MenuItem(CharSequence action, Object tag, boolean enable, GPopupMenuListener listener) {
            this(action, tag, listener);
            this.enable = enable;
        }

        public Object getTag() {
            return this.tag;
        }

        public CharSequence getAction() {
            return this.action;
        }
    }

    private Context context;
    private ArrayList<MenuItem> actionList;

    public ActionSheet(Context context, ArrayList<MenuItem> actionList) {
        super(context);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCanceledOnTouchOutside(true);
        this.context = context;
        this.actionList = actionList;
        init();
    }

    public ActionSheet(Context context, int theme, ArrayList<MenuItem> actionList) {
        this(context, actionList);
    }

    private LinearLayout menuContainer;

    /**
     * 构造菜单列表
     */
    private void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.widget_action_sheet, null);
        menuContainer = (LinearLayout) view.findViewById(R.id.menu_container);

        LinearLayout menuButtonLy;
        Resources resources = context.getResources();
        for (int i = 0, n = actionList.size(); i < n; i++) {
            final MenuItem menuItem = actionList.get(i);
            menuButtonLy = (LinearLayout) inflater.inflate(R.layout.widget_popup_menu_item, null);
            Button btn = (Button) menuButtonLy.findViewById(R.id.textView);
//			//设置菜单项间间隙
//			if (i > 0) {
//				LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
//				params.topMargin = (int) resources.getDimension(R.dimen.popup_menu_item_divider);
//				menuButtonLy.setLayoutParams(params);
//			}
            //设置文字
            btn.setText(menuItem.action);
            btn.setEnabled(menuItem.enable);

            //设置背景
//			if (menuItem.backgroundType == MenuItem.BACKGROUND_BLACK) {//黑背景
//				menuButton.setBackgroundResource(R.drawable.bg_menu_item_black_selector);
//				menuButton.setTextColor(resources.getColor(R.color.white));
//			}
//			else {
//				menuButton.setTextColor(resources.getColor(R.color.black));
//				if (menuItem.backgroundType == MenuItem.BACKGROUND_RED) {//红背景（因需求，暂时不使用红色，全部改为灰色 2013年3月7号）
//					menuButton.setBackgroundResource(R.drawable.bg_menu_item_gray_selector);
//				}
//				else {//灰背景
//					menuButton.setBackgroundResource(R.drawable.bg_menu_item_gray_selector);
//				}
//			}
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (menuItem.listener != null) {
                        menuItem.listener.onMenuSelected(menuItem);
                    }
                    dismiss();
                }
            });
            menuContainer.addView(menuButtonLy);
        }

        view.findViewById(R.id.popup_menu_btn_cancel).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        setContentView(view);
    }

    public void dismiss() {
        actionList.clear();
        super.dismiss();
    }

    public void show() {
        Window window = getWindow();
        window.setWindowAnimations(R.style.bottomWindowAnim); //设置窗口弹出动画
        window.setBackgroundDrawableResource(R.color.transparent); //设置对话框背景为透明
        WindowManager.LayoutParams wl = window.getAttributes();
        //窗口需要显示的位置
        wl.gravity = Gravity.BOTTOM;
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wl);
        super.show();
    }

    /**
     * 菜单构造器
     */
    public static class Builder {
        private Context context;
        private ArrayList<MenuItem> actionList;

        public Builder(Context context) {
            this.context = context;
            this.actionList = new ArrayList<ActionSheet.MenuItem>();
        }

        public Builder appendMenuItem(CharSequence action, Object tag, GPopupMenuListener listener) {
            actionList.add(new MenuItem(action, tag, listener));
            return this;
        }

        public Builder appendMenuItem(CharSequence action, int backgroundType, Object tag, GPopupMenuListener listener) {
            actionList.add(new MenuItem(action, backgroundType, tag, listener));
            return this;
        }

        public Builder appendMenuItem(CharSequence action, Object tag, boolean enable, GPopupMenuListener listener) {
            actionList.add(new MenuItem(action, tag, enable, listener));
            return this;
        }

        /**
         * Create the custom dialog
         */
        private ActionSheet create() {
            ActionSheet dialog = new ActionSheet(context, actionList);
            return dialog;
        }

        public ActionSheet show() {
            ActionSheet dialog = create();
            dialog.show();
            return dialog;
        }
    }
}
