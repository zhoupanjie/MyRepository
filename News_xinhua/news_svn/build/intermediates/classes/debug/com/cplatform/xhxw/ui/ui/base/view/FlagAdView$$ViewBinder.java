// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FlagAdView$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.base.view.FlagAdView> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131428291, "field 'mTitle'");
    target.mTitle = finder.castView(view, 2131428291, "field 'mTitle'");
  }

  @Override public void unbind(T target) {
    target.mTitle = null;
  }
}
