// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.about;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class AboutActivity$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.about.AboutActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131427359);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427359' for field 'nowLayout' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.nowLayout = (android.widget.LinearLayout) view;
    view = finder.findById(source, 2131427360);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427360' for field 'nowVersion1' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.nowVersion1 = (android.widget.TextView) view;
    view = finder.findById(source, 2131427361);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427361' for field 'newLayout' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.newLayout = (android.widget.LinearLayout) view;
    view = finder.findById(source, 2131427362);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427362' for field 'nowVersion2' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.nowVersion2 = (android.widget.TextView) view;
    view = finder.findById(source, 2131427363);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427363' for field 'newVersion' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.newVersion = (android.widget.TextView) view;
    view = finder.findById(source, 2131427364);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427364' for field 'updateBtn' and method 'update' was not found. If this view is optional add '@Optional' annotation.");
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
    view = finder.findById(source, 2131427356);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427356' for method 'goBack' was not found. If this view is optional add '@Optional' annotation.");
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
    target.nowLayout = null;
    target.nowVersion1 = null;
    target.newLayout = null;
    target.nowVersion2 = null;
    target.newVersion = null;
    target.updateBtn = null;
  }
}
