// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.settings;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class CropAvatarActivity$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.settings.CropAvatarActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131492967);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131492967' for field 'mImageView' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mImageView = (com.cylib.imageCrop.CropImageView) view;
    view = finder.findById(source, 2131492970);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131492970' for method 'onSaveAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onSaveAction();
        }
      });
    view = finder.findById(source, 2131492969);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131492969' for method 'onCancelAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onCancelAction();
        }
      });
    view = finder.findById(source, 2131492971);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131492971' for method 'onRotateRightAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onRotateRightAction();
        }
      });
  }

  public static void reset(com.cplatform.xhxw.ui.ui.settings.CropAvatarActivity target) {
    target.mImageView = null;
  }
}
