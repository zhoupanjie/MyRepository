// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SpecialTopicSliderView$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.base.view.SpecialTopicSliderView target, Object source) {
    View view;
    view = finder.findById(source, 2131493895);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493895' for field 'mCpi' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mCpi = (com.viewpagerindicator.CirclePageIndicator) view;
    view = finder.findById(source, 2131493105);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493105' for field 'mVp' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mVp = (android.support.v4.view.ViewPager) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.base.view.SpecialTopicSliderView target) {
    target.mCpi = null;
    target.mVp = null;
  }
}