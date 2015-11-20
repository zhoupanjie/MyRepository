// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SpecialTopicVideoNewItem$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.base.view.SpecialTopicVideoNewItem target, Object source) {
    View view;
    view = finder.findById(source, 2131493812);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493812' for field 'mContent' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mContent = (android.support.v7.widget.GridLayout) view;
    view = finder.findById(source, 2131493899);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493899' for field 'mHeader' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mHeader = (android.widget.TextView) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.base.view.SpecialTopicVideoNewItem target) {
    target.mContent = null;
    target.mHeader = null;
  }
}
