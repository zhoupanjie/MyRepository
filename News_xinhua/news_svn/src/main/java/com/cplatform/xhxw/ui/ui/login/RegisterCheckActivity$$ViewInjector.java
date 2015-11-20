// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.login;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class RegisterCheckActivity$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.login.RegisterCheckActivity target, Object source) {
    View view;
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
    view = finder.findById(source, 2131493136);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493136' for method 'goEmail' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.goEmail();
        }
      });
    view = finder.findById(source, 2131493135);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493135' for method 'goTelephon' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.goTelephon();
        }
      });
  }

  public static void reset(com.cplatform.xhxw.ui.ui.login.RegisterCheckActivity target) {
  }
}
