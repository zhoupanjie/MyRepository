// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.search;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SearchActivity$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.search.SearchActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131427615);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427615' for method 'clear' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.clear();
        }
      });
    view = finder.findById(source, 2131427616);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427616' for method 'search' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.search();
        }
      });
  }

  public static void reset(com.cplatform.xhxw.ui.ui.search.SearchActivity target) {
  }
}
