// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.login;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class RegisterEmailActivity$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.login.RegisterEmailActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427601, "field 'account'");
    target.account = finder.castView(view, 2131427601, "field 'account'");
    view = finder.findRequiredView(source, 2131427602, "field 'passWord'");
    target.passWord = finder.castView(view, 2131427602, "field 'passWord'");
    view = finder.findRequiredView(source, 2131427603, "field 'register' and method 'register'");
    target.register = finder.castView(view, 2131427603, "field 'register'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.register();
        }
      });
    view = finder.findRequiredView(source, 2131427604, "field 'checkBox'");
    target.checkBox = finder.castView(view, 2131427604, "field 'checkBox'");
    view = finder.findRequiredView(source, 2131427605, "field 'agreement'");
    target.agreement = finder.castView(view, 2131427605, "field 'agreement'");
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
    target.account = null;
    target.passWord = null;
    target.register = null;
    target.checkBox = null;
    target.agreement = null;
  }
}
