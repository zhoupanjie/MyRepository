// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.settings;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SettingsActivity$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.settings.SettingsActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131493190);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493190' for field 'clearText' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.clearText = (android.widget.TextView) view;
    view = finder.findById(source, 2131493185);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493185' for field 'mTextSize' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mTextSize = (android.widget.TextView) view;
    view = finder.findById(source, 2131493186);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493186' for field 'clearLayout' and method 'setClear' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.clearLayout = (android.widget.RelativeLayout) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.setClear();
        }
      });
    view = finder.findById(source, 2131493189);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493189' for field 'clearProgress' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.clearProgress = (android.widget.ProgressBar) view;
    view = finder.findById(source, 2131493195);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493195' for field 'aboutLayout' and method 'setAbout' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.aboutLayout = (android.widget.RelativeLayout) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.setAbout();
        }
      });
    view = finder.findById(source, 2131493178);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493178' for field 'mPushSetting' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mPushSetting = (com.cplatform.xhxw.ui.ui.base.view.SwitchButton) view;
    view = finder.findById(source, 2131493203);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493203' for field 'mLogout' and method 'onLogOutAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mLogout = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onLogOutAction();
        }
      });
    view = finder.findById(source, 2131493182);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493182' for method 'onChangeNewsTextSizeAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onChangeNewsTextSizeAction();
        }
      });
  }

  public static void reset(com.cplatform.xhxw.ui.ui.settings.SettingsActivity target) {
    target.clearText = null;
    target.mTextSize = null;
    target.clearLayout = null;
    target.clearProgress = null;
    target.aboutLayout = null;
    target.mPushSetting = null;
    target.mLogout = null;
  }
}
