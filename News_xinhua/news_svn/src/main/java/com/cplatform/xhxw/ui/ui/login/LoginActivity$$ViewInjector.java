// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.login;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class LoginActivity$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.login.LoginActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131493042);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493042' for field 'password' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.password = (android.widget.EditText) view;
    view = finder.findById(source, 2131493041);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493041' for field 'account' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.account = (android.widget.EditText) view;
    view = finder.findById(source, 2131493049);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493049' for method 'onQqOauthVerifyAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onQqOauthVerifyAction();
        }
      });
    view = finder.findById(source, 2131493044);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493044' for method 'onfogotPasswordAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onfogotPasswordAction();
        }
      });
    view = finder.findById(source, 2131493047);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493047' for method 'onWeixinOauthVerifyAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onWeixinOauthVerifyAction();
        }
      });
    view = finder.findById(source, 2131493048);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493048' for method 'onSinaweiboOauthVerifyAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onSinaweiboOauthVerifyAction();
        }
      });
    view = finder.findById(source, 2131493043);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493043' for method 'login_ok' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.login_ok();
        }
      });
    view = finder.findById(source, 2131493050);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493050' for method 'onTencentweiboOauthVerifyAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onTencentweiboOauthVerifyAction();
        }
      });
    view = finder.findById(source, 2131493045);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493045' for method 'register' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.register();
        }
      });
    view = finder.findById(source, 2131492892);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131492892' for method 'back' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.back();
        }
      });
  }

  public static void reset(com.cplatform.xhxw.ui.ui.login.LoginActivity target) {
    target.password = null;
    target.account = null;
  }
}
