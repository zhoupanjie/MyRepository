// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.login;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class LoginActivity$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.login.LoginActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427505, "field 'account'");
    target.account = finder.castView(view, 2131427505, "field 'account'");
    view = finder.findRequiredView(source, 2131427506, "field 'password'");
    target.password = finder.castView(view, 2131427506, "field 'password'");
    view = finder.findRequiredView(source, 2131427509, "method 'register'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.register();
        }
      });
    view = finder.findRequiredView(source, 2131427507, "method 'login_ok'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.login_ok();
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
    view = finder.findRequiredView(source, 2131427508, "method 'onfogotPasswordAction'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onfogotPasswordAction();
        }
      });
    view = finder.findRequiredView(source, 2131427512, "method 'onSinaweiboOauthVerifyAction'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onSinaweiboOauthVerifyAction();
        }
      });
    view = finder.findRequiredView(source, 2131427513, "method 'onQqOauthVerifyAction'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onQqOauthVerifyAction();
        }
      });
    view = finder.findRequiredView(source, 2131427511, "method 'onWeixinOauthVerifyAction'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onWeixinOauthVerifyAction();
        }
      });
    view = finder.findRequiredView(source, 2131427514, "method 'onTencentweiboOauthVerifyAction'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onTencentweiboOauthVerifyAction();
        }
      });
  }

  @Override public void unbind(T target) {
    target.account = null;
    target.password = null;
  }
}
