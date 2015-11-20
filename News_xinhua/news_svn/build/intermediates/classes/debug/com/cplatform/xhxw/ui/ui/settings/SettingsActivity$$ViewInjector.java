// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.settings;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SettingsActivity$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.settings.SettingsActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131427667);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427667' for method 'onLogOutAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onLogOutAction();
        }
      });
    view = finder.findById(source, 2131427646);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427646' for method 'onChangeNewsTextSizeAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onChangeNewsTextSizeAction();
        }
      });
    view = finder.findById(source, 2131427659);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427659' for method 'setAbout' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.setAbout();
        }
      });
    view = finder.findById(source, 2131427650);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427650' for method 'setClear' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.setClear();
        }
      });
  }

  public static void reset(com.cplatform.xhxw.ui.ui.settings.SettingsActivity target) {
  }
}
