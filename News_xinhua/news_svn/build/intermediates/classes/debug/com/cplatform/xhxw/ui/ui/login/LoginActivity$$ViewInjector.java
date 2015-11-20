// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.login;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class LoginActivity$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.login.LoginActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131427505);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427505' for field 'account' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.account = (android.widget.EditText) view;
    view = finder.findById(source, 2131427506);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427506' for field 'password' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.password = (android.widget.EditText) view;
    view = finder.findById(source, 2131427509);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427509' for method 'register' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.register();
        }
      });
    view = finder.findById(source, 2131427507);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427507' for method 'login_ok' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.login_ok();
        }
      });
    view = finder.findById(source, 2131427356);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427356' for method 'back' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.back();
        }
      });
    view = finder.findById(source, 2131427508);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427508' for method 'onfogotPasswordAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onfogotPasswordAction();
        }
      });
    view = finder.findById(source, 2131427512);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427512' for method 'onSinaweiboOauthVerifyAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onSinaweiboOauthVerifyAction();
        }
      });
    view = finder.findById(source, 2131427513);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427513' for method 'onQqOauthVerifyAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onQqOauthVerifyAction();
        }
      });
    view = finder.findById(source, 2131427511);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427511' for method 'onWeixinOauthVerifyAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onWeixinOauthVerifyAction();
        }
      });
    view = finder.findById(source, 2131427514);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427514' for method 'onTencentweiboOauthVerifyAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onTencentweiboOauthVerifyAction();
        }
      });
  }

  public static void reset(com.cplatform.xhxw.ui.ui.login.LoginActivity target) {
    target.account = null;
    target.password = null;
  }
}
