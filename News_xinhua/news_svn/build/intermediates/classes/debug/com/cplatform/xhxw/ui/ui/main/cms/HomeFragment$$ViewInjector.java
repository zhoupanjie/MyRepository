// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.cms;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class HomeFragment$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.main.cms.HomeFragment target, Object source) {
    View view;
    view = finder.findById(source, 2131427848);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427848' for field 'mTabbar' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mTabbar = (com.cplatform.xhxw.ui.ui.base.view.PagerSlidingTabStrip) view;
    view = finder.findById(source, 2131427847);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427847' for field 'mTabbarLayout' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mTabbarLayout = (android.widget.LinearLayout) view;
    view = finder.findById(source, 2131427850);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427850' for field 'mViewPage' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mViewPage = (android.support.v4.view.ViewPager) view;
    view = finder.findById(source, 2131427851);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427851' for field 'mManageView' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mManageView = (com.cplatform.xhxw.ui.ui.base.view.ChannelManageView) view;
    view = finder.findById(source, 2131427849);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427849' for method 'addChannelAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.addChannelAction();
        }
      });
  }

  public static void reset(com.cplatform.xhxw.ui.ui.main.cms.HomeFragment target) {
    target.mTabbar = null;
    target.mTabbarLayout = null;
    target.mViewPage = null;
    target.mManageView = null;
  }
}
