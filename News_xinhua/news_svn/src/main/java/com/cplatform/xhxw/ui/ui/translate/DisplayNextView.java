package com.cplatform.xhxw.ui.ui.translate;  
  
import com.cplatform.xhxw.ui.ui.main.HomeActivity;
import com.cplatform.xhxw.ui.ui.main.saas.EnterpriseMainFragment;

import android.app.Activity;  
import android.view.animation.Animation;  
  
public class DisplayNextView implements Animation.AnimationListener {  
  
    Object obj;  
  
    // 动画监听器的构造函数  
    static Activity ac;  
    static int order;  
  
    public DisplayNextView(Activity ac, int order) {  
        this.ac = ac;  
        this.order = order;  
    }  
  
    public void onAnimationStart(Animation animation) {  
    }  
  
    public void onAnimationEnd(Animation animation) {  
        doSomethingOnEnd(order);  
    }  
  
    public void onAnimationRepeat(Animation animation) {  
    }  
  
    private static final class SwapViews implements Runnable {  
        public void run() {  
            switch (order) {  
            case TranslateConstants.KEY_FIRST_INVERSE:  
//                ((HomeActivity) ac).jumpToSecond();  
                break;  
            case TranslateConstants.KEY_SECOND_CLOCKWISE:  
                ((EnterpriseMainFragment) ac).jumpToFirst();  
                break;  
            }  
        }  
    }  
  
    public void doSomethingOnEnd(int _order) {  
        switch (_order) {  
        case TranslateConstants.KEY_FIRST_INVERSE:  
//            ((HomeActivity) ac).jumpToSecond();  
            break;  
  
        case TranslateConstants.KEY_SECOND_CLOCKWISE:  
            ((EnterpriseMainFragment) ac).jumpToFirst();  
            break;  
        }  
        ac = null;
    }  
}  