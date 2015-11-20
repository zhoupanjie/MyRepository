// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.picShow;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class PicShowActivity$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.picShow.PicShowActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131427577);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427577' for method 'onDownAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onDownAction(p0);
        }
      });
    view = finder.findById(source, 2131427578);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427578' for method 'onShareAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onShareAction(p0);
        }
      });
    view = finder.findById(source, 2131427579);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427579' for method 'onFavoriteAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onFavoriteAction(p0);
        }
      });
    view = finder.findById(source, 2131427571);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427571' for method 'goImageComment' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.goImageComment(p0);
        }
      });
  }

  public static void reset(com.cplatform.xhxw.ui.ui.picShow.PicShowActivity target) {
  }
}
