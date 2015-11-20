// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class NewsFunctionRecommend$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.base.view.NewsFunctionRecommend target, Object source) {
    View view;
    view = finder.findById(source, 2131428332);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428332' for method 'img1OnClickAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.img1OnClickAction(p0);
        }
      });
    view = finder.findById(source, 2131428333);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428333' for method 'img2OnClickAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.img2OnClickAction(p0);
        }
      });
    view = finder.findById(source, 2131428334);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428334' for method 'img3OnClickAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.img3OnClickAction(p0);
        }
      });
    view = finder.findById(source, 2131428335);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428335' for method 'img4OnClickAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.img4OnClickAction(p0);
        }
      });
  }

  public static void reset(com.cplatform.xhxw.ui.ui.base.view.NewsFunctionRecommend target) {
  }
}
