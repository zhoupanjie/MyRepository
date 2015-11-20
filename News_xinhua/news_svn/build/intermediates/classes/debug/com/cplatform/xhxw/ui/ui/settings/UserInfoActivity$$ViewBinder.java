// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.settings;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class UserInfoActivity$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.settings.UserInfoActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427669, "field 'mAvatar' and method 'onAvatarAction'");
    target.mAvatar = finder.castView(view, 2131427669, "field 'mAvatar'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onAvatarAction();
        }
      });
    view = finder.findRequiredView(source, 2131427713, "method 'onSaveAction'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onSaveAction();
        }
      });
  }

  @Override public void unbind(T target) {
    target.mAvatar = null;
  }
}
