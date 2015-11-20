// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SliderView$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.base.view.SliderView target, Object source) {
    View view;
    view = finder.findById(source, 2131427569);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427569' for field 'mVp' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mVp = (android.support.v4.view.ViewPager) view;
    view = finder.findById(source, 2131428359);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428359' for field 'mCpi' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mCpi = (com.viewpagerindicator.CirclePageIndicator) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.base.view.SliderView target) {
    target.mVp = null;
    target.mCpi = null;
  }
}
