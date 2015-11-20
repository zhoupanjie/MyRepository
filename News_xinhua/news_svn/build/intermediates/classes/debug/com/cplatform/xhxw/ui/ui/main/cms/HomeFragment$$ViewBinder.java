// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.cms;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class HomeFragment$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.main.cms.HomeFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427848, "field 'mTabbar'");
    target.mTabbar = finder.castView(view, 2131427848, "field 'mTabbar'");
    view = finder.findRequiredView(source, 2131427847, "field 'mTabbarLayout'");
    target.mTabbarLayout = finder.castView(view, 2131427847, "field 'mTabbarLayout'");
    view = finder.findRequiredView(source, 2131427850, "field 'mViewPage'");
    target.mViewPage = finder.castView(view, 2131427850, "field 'mViewPage'");
    view = finder.findRequiredView(source, 2131427851, "field 'mManageView'");
    target.mManageView = finder.castView(view, 2131427851, "field 'mManageView'");
    view = finder.findRequiredView(source, 2131427849, "method 'addChannelAction'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.addChannelAction();
        }
      });
  }

  @Override public void unbind(T target) {
    target.mTabbar = null;
    target.mTabbarLayout = null;
    target.mViewPage = null;
    target.mManageView = null;
  }
}
