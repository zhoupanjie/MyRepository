// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.login;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class ToFindPassWordActivity$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.login.ToFindPassWordActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131427437);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427437' for method 'telephon' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.telephon();
        }
      });
    view = finder.findById(source, 2131427439);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427439' for method 'email' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.email();
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
  }

  public static void reset(com.cplatform.xhxw.ui.ui.login.ToFindPassWordActivity target) {
  }
}
