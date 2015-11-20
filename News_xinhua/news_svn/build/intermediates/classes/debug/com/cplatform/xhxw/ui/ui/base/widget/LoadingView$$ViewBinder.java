// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.widget;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class LoadingView$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.base.widget.LoadingView> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427947, "field 'mMsg'");
    target.mMsg = finder.castView(view, 2131427947, "field 'mMsg'");
  }

  @Override public void unbind(T target) {
    target.mMsg = null;
  }
}
