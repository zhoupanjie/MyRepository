// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.detailpage;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class NewsPageFragment$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.detailpage.NewsPageFragment target, Object source) {
    View view;
    view = finder.findById(source, 2131427578);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427578' for method 'onShareAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onShareAction();
        }
      });
  }

  public static void reset(com.cplatform.xhxw.ui.ui.detailpage.NewsPageFragment target) {
  }
}
