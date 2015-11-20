// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.test;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class Text$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.test.Text target, Object source) {
    View view;
    view = finder.findById(source, 2131428124);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428124' for method 'button1' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.button1();
        }
      });
    view = finder.findById(source, 2131428125);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428125' for method 'getUserInfo' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.getUserInfo();
        }
      });
    view = finder.findById(source, 2131428126);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428126' for method 'setUserInfo' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.setUserInfo();
        }
      });
    view = finder.findById(source, 2131428127);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428127' for method 'feedBack' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.feedBack();
        }
      });
  }

  public static void reset(com.cplatform.xhxw.ui.test.Text target) {
  }
}
