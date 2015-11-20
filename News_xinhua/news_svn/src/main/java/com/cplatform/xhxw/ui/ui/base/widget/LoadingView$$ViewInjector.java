// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.widget;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class LoadingView$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.base.widget.LoadingView target, Object source) {
    View view;
    view = finder.findById(source, 2131493483);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493483' for field 'mMsg' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mMsg = (android.widget.TextView) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.base.widget.LoadingView target) {
    target.mMsg = null;
  }
}
