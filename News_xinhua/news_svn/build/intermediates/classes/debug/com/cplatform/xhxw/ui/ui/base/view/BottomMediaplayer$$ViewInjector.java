// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class BottomMediaplayer$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.base.view.BottomMediaplayer target, Object source) {
    View view;
    view = finder.findById(source, 2131427727);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427727' for method 'imageMediaStart' was not found. If this view is optional add '@Optional' annotation.");
    }
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
      throw new IllegalStateException("Required view with id '2131427729' for method 'imageMediaPause' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.imageMediaPause();
        }
      });
  }

  public static void reset(com.cplatform.xhxw.ui.ui.base.view.BottomMediaplayer target) {
  }
}
