// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.settings;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SettingsActivity$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.settings.SettingsActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427667, "field 'mLogout' and method 'onLogOutAction'");
    target.mLogout = finder.castView(view, 2131427667, "field 'mLogout'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onLogOutAction();
        }
      });
    view = finder.findRequiredView(source, 2131427642, "field 'mPushSetting'");
    target.mPushSetting = finder.castView(view, 2131427642, "field 'mPushSetting'");
    view = finder.findRequiredView(source, 2131427649, "field 'mTextSize'");
    target.mTextSize = finder.castView(view, 2131427649, "field 'mTextSize'");
    view = finder.findRequiredView(source, 2131427659, "field 'aboutLayout' and method 'setAbout'");
    target.aboutLayout = finder.castView(view, 2131427659, "field 'aboutLayout'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setAbout();
        }
      });
    view = finder.findRequiredView(source, 2131427650, "field 'clearLayout' and method 'setClear'");
    target.clearLayout = finder.castView(view, 2131427650, "field 'clearLayout'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setClear();
        }
      });
    view = finder.findRequiredView(source, 2131427653, "field 'clearProgress'");
    target.clearProgress = finder.castView(view, 2131427653, "field 'clearProgress'");
    view = finder.findRequiredView(source, 2131427654, "field 'clearText'");
    target.clearText = finder.castView(view, 2131427654, "field 'clearText'");
    view = finder.findRequiredView(source, 2131427646, "method 'onChangeNewsTextSizeAction'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onChangeNewsTextSizeAction();
        }
      });
  }

  @Override public void unbind(T target) {
    target.mLogout = null;
    target.mPushSetting = null;
    target.mTextSize = null;
    target.aboutLayout = null;
    target.clearLayout = null;
    target.clearProgress = null;
    target.clearText = null;
  }
}
