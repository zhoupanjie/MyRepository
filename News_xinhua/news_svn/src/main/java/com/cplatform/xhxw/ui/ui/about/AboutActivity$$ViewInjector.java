// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.about;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class AboutActivity$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.about.AboutActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131492896);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131492896' for field 'nowVersion1' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.nowVersion1 = (android.widget.TextView) view;
    view = finder.findById(source, 2131492898);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131492898' for field 'nowVersion2' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.nowVersion2 = (android.widget.TextView) view;
    view = finder.findById(source, 2131492895);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131492895' for field 'nowLayout' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.nowLayout = (android.widget.LinearLayout) view;
    view = finder.findById(source, 2131492900);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131492900' for field 'updateBtn' and method 'update' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.updateBtn = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.update();
        }
      });
    view = finder.findById(source, 2131492899);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131492899' for field 'newVersion' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.newVersion = (android.widget.TextView) view;
    view = finder.findById(source, 2131492897);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131492897' for field 'newLayout' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.newLayout = (android.widget.LinearLayout) view;
    view = finder.findById(source, 2131492892);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131492892' for method 'goBack' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.goBack();
        }
      });
  }

  public static void reset(com.cplatform.xhxw.ui.ui.about.AboutActivity target) {
    target.nowVersion1 = null;
    target.nowVersion2 = null;
    target.nowLayout = null;
    target.updateBtn = null;
    target.newVersion = null;
    target.newLayout = null;
  }
}
