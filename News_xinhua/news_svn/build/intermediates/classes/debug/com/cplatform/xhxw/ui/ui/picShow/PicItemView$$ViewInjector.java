// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.picShow;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class PicItemView$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.picShow.PicItemView target, Object source) {
    View view;
    view = finder.findById(source, 2131428003);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428003' for field 'mImg' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mImg = (com.cplatform.xhxw.ui.ui.picShow.PhotoView) view;
    view = finder.findById(source, 2131427570);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427570' for field 'mDefView' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mDefView = (com.cplatform.xhxw.ui.ui.base.widget.DefaultView) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.picShow.PicItemView target) {
    target.mImg = null;
    target.mDefView = null;
  }
}
