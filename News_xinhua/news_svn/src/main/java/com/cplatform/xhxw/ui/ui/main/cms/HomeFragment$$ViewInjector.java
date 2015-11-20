// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.cms;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class HomeFragment$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.main.cms.HomeFragment target, Object source) {
    View view;
    view = finder.findById(source, 2131493383);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493383' for field 'mTabbarLayout' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mTabbarLayout = (android.widget.LinearLayout) view;
    view = finder.findById(source, 2131493384);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493384' for field 'mTabbar' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mTabbar = (com.cplatform.xhxw.ui.ui.base.view.PagerSlidingTabStrip) view;
    view = finder.findById(source, 2131493387);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493387' for field 'mManageView' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mManageView = (com.cplatform.xhxw.ui.ui.base.view.ChannelManageView) view;
    view = finder.findById(source, 2131493386);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493386' for field 'mViewPage' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mViewPage = (android.support.v4.view.ViewPager) view;
    view = finder.findById(source, 2131493385);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493385' for method 'addChannelAction' was not found. If this view is optional add '@Optional' annotation.");
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
    target.mTabbarLayout = null;
    target.mTabbar = null;
    target.mManageView = null;
    target.mViewPage = null;
  }
}
