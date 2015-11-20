// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class RecommendImages$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.base.view.RecommendImages target, Object source) {
    View view;
    view = finder.findById(source, 2131428055);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428055' for method 'first' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.first();
        }
      });
    view = finder.findById(source, 2131428056);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428056' for method 'second' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.second();
        }
      });
    view = finder.findById(source, 2131428057);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428057' for method 'third' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.third();
        }
      });
    view = finder.findById(source, 2131428058);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428058' for method 'forth' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.forth();
        }
      });
  }

  public static void reset(com.cplatform.xhxw.ui.ui.base.view.RecommendImages target) {
  }
}
