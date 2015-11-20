// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.search;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SearchActivity$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.search.SearchActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131493153);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493153' for field 'mHotWordsView' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mHotWordsView = (com.cplatform.xhxw.ui.ui.base.view.HotWordsRectView) view;
    view = finder.findById(source, 2131492937);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131492937' for field 'listView' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.listView = (com.cplatform.xhxw.ui.ui.base.widget.PullRefreshListView) view;
    view = finder.findById(source, 2131493106);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493106' for field 'mDefView' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mDefView = (com.cplatform.xhxw.ui.ui.base.widget.DefaultView) view;
    view = finder.findById(source, 2131493151);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493151' for field 'clear' and method 'clear' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.clear = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.clear();
        }
      });
    view = finder.findById(source, 2131493149);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493149' for field 'mRootContainer' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mRootContainer = (android.widget.LinearLayout) view;
    view = finder.findById(source, 2131493150);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493150' for field 'editText' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.editText = (android.widget.EditText) view;
    view = finder.findById(source, 2131493152);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493152' for method 'search' was not found. If this view is optional add '@Optional' annotation.");
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
    target.mHotWordsView = null;
    target.listView = null;
    target.mDefView = null;
    target.clear = null;
    target.mRootContainer = null;
    target.editText = null;
  }
}
