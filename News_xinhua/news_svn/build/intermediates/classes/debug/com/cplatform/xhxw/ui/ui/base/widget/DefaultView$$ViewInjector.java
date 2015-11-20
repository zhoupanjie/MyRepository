// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.widget;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class DefaultView$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.base.widget.DefaultView target, Object source) {
    View view;
    view = finder.findById(source, 2131427413);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427413' for method 'onTapAction' was not found. If this view is optional add '@Optional' annotation.");
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
  }
}
