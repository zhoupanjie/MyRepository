// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class FlagAdView$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.base.view.FlagAdView target, Object source) {
    View view;
    view = finder.findById(source, 2131428291);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428291' for field 'mTitle' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mTitle = (android.widget.TextView) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.base.view.FlagAdView target) {
    target.mTitle = null;
  }
}
