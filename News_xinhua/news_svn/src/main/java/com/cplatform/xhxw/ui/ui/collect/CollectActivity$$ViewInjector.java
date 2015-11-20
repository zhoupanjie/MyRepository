// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.collect;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class CollectActivity$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.collect.CollectActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131492937);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131492937' for field 'mListView' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mListView = (android.widget.ListView) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.collect.CollectActivity target) {
    target.mListView = null;
  }
}
