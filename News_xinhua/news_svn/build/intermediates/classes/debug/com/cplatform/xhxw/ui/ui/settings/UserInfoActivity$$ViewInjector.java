// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.settings;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class UserInfoActivity$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.settings.UserInfoActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131427669);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427669' for method 'onAvatarAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onAvatarAction();
        }
      });
    view = finder.findById(source, 2131427713);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427713' for method 'onSaveAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onSaveAction();
        }
      });
  }

  public static void reset(com.cplatform.xhxw.ui.ui.settings.UserInfoActivity target) {
  }
}
