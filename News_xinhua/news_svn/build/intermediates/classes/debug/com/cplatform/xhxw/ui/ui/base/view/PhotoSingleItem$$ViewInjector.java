// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class PhotoSingleItem$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.base.view.PhotoSingleItem target, Object source) {
    View view;
    view = finder.findById(source, 2131428346);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428346' for field 'mImg' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mImg = (android.widget.ImageView) view;
    view = finder.findById(source, 2131428010);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428010' for field 'mTitle' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mTitle = (android.widget.TextView) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.base.view.PhotoSingleItem target) {
    target.mImg = null;
    target.mTitle = null;
  }
}
