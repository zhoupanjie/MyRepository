// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class VideoSingleItem$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.base.view.VideoSingleItem target, Object source) {
    View view;
    view = finder.findById(source, 2131493546);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493546' for field 'mTitle' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mTitle = (android.widget.TextView) view;
    view = finder.findById(source, 2131493110);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493110' for field 'mDesc' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mDesc = (android.widget.TextView) view;
    view = finder.findById(source, 2131493882);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493882' for field 'mImg' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mImg = (android.widget.ImageView) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.base.view.VideoSingleItem target) {
    target.mTitle = null;
    target.mDesc = null;
    target.mImg = null;
  }
}
