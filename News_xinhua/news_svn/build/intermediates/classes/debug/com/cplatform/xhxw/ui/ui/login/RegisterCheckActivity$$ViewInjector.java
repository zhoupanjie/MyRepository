// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.login;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class RegisterCheckActivity$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.login.RegisterCheckActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131427599);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427599' for method 'goTelephon' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.goTelephon();
        }
      });
    view = finder.findById(source, 2131427600);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427600' for method 'goEmail' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.goEmail();
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

  public static void reset(com.cplatform.xhxw.ui.ui.login.RegisterCheckActivity target) {
  }
}
