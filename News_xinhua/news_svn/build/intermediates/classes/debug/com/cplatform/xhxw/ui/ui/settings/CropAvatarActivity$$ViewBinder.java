// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.settings;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class CropAvatarActivity$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.settings.CropAvatarActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427431, "field 'mImageView'");
    target.mImageView = finder.castView(view, 2131427431, "field 'mImageView'");
    view = finder.findRequiredView(source, 2131427433, "method 'onCancelAction'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onCancelAction();
        }
      });
    view = finder.findRequiredView(source, 2131427434, "method 'onSaveAction'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onSaveAction();
        }
      });
    view = finder.findRequiredView(source, 2131427435, "method 'onRotateRightAction'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onRotateRightAction();
        }
      });
  }

  @Override public void unbind(T target) {
    target.mImageView = null;
  }
}
