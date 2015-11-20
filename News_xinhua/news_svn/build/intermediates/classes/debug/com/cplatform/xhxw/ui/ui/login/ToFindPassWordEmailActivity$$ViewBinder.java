// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.login;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ToFindPassWordEmailActivity$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.login.ToFindPassWordEmailActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427682, "field 'mEmail'");
    target.mEmail = finder.castView(view, 2131427682, "field 'mEmail'");
    view = finder.findRequiredView(source, 2131427683, "method 'register'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.register();
        }
      });
    view = finder.findRequiredView(source, 2131427356, "method 'back'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.back();
        }
      });
  }

  @Override public void unbind(T target) {
    target.mEmail = null;
  }
}
