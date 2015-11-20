// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.widget;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class DefaultView$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.base.widget.DefaultView target, Object source) {
    View view;
    view = finder.findById(source, 2131493826);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493826' for field 'mBg' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mBg = (android.widget.ImageView) view;
    view = finder.findById(source, 2131493825);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493825' for field 'mLoading' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mLoading = (android.widget.ProgressBar) view;
    view = finder.findById(source, 2131492949);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131492949' for method 'onTapAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onTapAction(p0);
        }
      });
  }

  public static void reset(com.cplatform.xhxw.ui.ui.base.widget.DefaultView target) {
    target.mBg = null;
    target.mLoading = null;
  }
}
