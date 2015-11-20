// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SpecialTopicPicNewItem$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.base.view.SpecialTopicPicNewItem target, Object source) {
    View view;
    view = finder.findById(source, 2131428363);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428363' for field 'mHeader' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mHeader = (android.widget.TextView) view;
    view = finder.findById(source, 2131428276);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428276' for field 'mContent' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mContent = (android.support.v7.widget.GridLayout) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.base.view.SpecialTopicPicNewItem target) {
    target.mHeader = null;
    target.mContent = null;
  }
}
