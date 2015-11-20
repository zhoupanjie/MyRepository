// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ChannelManageView$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.base.view.ChannelManageView> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131428278, "field 'mShow'");
    target.mShow = finder.castView(view, 2131428278, "field 'mShow'");
    view = finder.findRequiredView(source, 2131428280, "field 'mHide'");
    target.mHide = finder.castView(view, 2131428280, "field 'mHide'");
    view = finder.findRequiredView(source, 2131428276, "field 'mContent'");
    target.mContent = view;
    view = finder.findRequiredView(source, 2131428272, "field 'view_channel_manage_layout'");
    target.view_channel_manage_layout = finder.castView(view, 2131428272, "field 'view_channel_manage_layout'");
    view = finder.findRequiredView(source, 2131428275, "method 'closeAction'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.closeAction();
        }
      });
  }

  @Override public void unbind(T target) {
    target.mShow = null;
    target.mHide = null;
    target.mContent = null;
    target.view_channel_manage_layout = null;
  }
}
