// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.test;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class Text$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.test.Text target, Object source) {
    View view;
    view = finder.findById(source, 2131493660);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493660' for field 'button' and method 'button1' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.button = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.button1();
        }
      });
    view = finder.findById(source, 2131493663);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493663' for method 'feedBack' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.feedBack();
        }
      });
    view = finder.findById(source, 2131493662);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493662' for method 'setUserInfo' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.setUserInfo();
        }
      });
    view = finder.findById(source, 2131493661);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493661' for method 'getUserInfo' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.getUserInfo();
        }
      });
  }

  public static void reset(com.cplatform.xhxw.ui.test.Text target) {
    target.button = null;
  }
}
