// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.login;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class ToFindPassWordEmailActivity$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.login.ToFindPassWordEmailActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131493218);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493218' for field 'mEmail' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mEmail = (android.widget.EditText) view;
    view = finder.findById(source, 2131493219);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493219' for method 'register' was not found. If this view is optional add '@Optional' annotation.");
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

  public static void reset(com.cplatform.xhxw.ui.ui.login.ToFindPassWordEmailActivity target) {
    target.mEmail = null;
  }
}