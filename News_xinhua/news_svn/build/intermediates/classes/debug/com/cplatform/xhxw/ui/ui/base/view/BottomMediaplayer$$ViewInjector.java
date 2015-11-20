// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class BottomMediaplayer$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.base.view.BottomMediaplayer target, Object source) {
    View view;
    view = finder.findById(source, 2131427727);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427727' for field 'imageStart' and method 'imageMediaStart' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.imageStart = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.imageMediaStart();
        }
      });
    view = finder.findById(source, 2131427729);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427729' for field 'imagePause' and method 'imageMediaPause' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.imagePause = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.imageMediaPause();
        }
      });
    view = finder.findById(source, 2131427730);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427730' for field 'progressBar' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.progressBar = (android.widget.ProgressBar) view;
    view = finder.findById(source, 2131427728);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427728' for field 'linearLayout' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.linearLayout = (android.widget.LinearLayout) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.base.view.BottomMediaplayer target) {
    target.imageStart = null;
    target.imagePause = null;
    target.progressBar = null;
    target.linearLayout = null;
  }
}
