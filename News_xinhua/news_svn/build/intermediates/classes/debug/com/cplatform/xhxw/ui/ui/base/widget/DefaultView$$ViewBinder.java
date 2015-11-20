// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.widget;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class DefaultView$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.base.widget.DefaultView> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131428289, "field 'mLoading'");
    target.mLoading = finder.castView(view, 2131428289, "field 'mLoading'");
    view = finder.findRequiredView(source, 2131428290, "field 'mBg'");
    target.mBg = finder.castView(view, 2131428290, "field 'mBg'");
    view = finder.findRequiredView(source, 2131427413, "method 'onTapAction'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onTapAction(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.mLoading = null;
    target.mBg = null;
  }
}
