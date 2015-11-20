// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.saas.picShow;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SaasPicShowActivity$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.main.saas.picShow.SaasPicShowActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131493106);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493106' for field 'mDefView' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mDefView = (com.cplatform.xhxw.ui.ui.base.widget.DefaultView) view;
    view = finder.findById(source, 2131493105);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493105' for field 'mVp' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mVp = (android.support.v4.view.ViewPager) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.main.saas.picShow.SaasPicShowActivity target) {
    target.mDefView = null;
    target.mVp = null;
  }
}
