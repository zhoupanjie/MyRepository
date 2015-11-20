// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.login;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class ToFindPassWordTelephonActivity$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.login.ToFindPassWordTelephonActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427684, "field 'mTelephone'");
    target.mTelephone = finder.castView(view, 2131427684, "field 'mTelephone'");
    view = finder.findRequiredView(source, 2131427687, "field 'mCode'");
    target.mCode = finder.castView(view, 2131427687, "field 'mCode'");
    view = finder.findRequiredView(source, 2131427688, "field 'mPassWord'");
    target.mPassWord = finder.castView(view, 2131427688, "field 'mPassWord'");
    view = finder.findRequiredView(source, 2131427686, "field 'codeBtn' and method 'getCode'");
    target.codeBtn = finder.castView(view, 2131427686, "field 'codeBtn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.getCode();
        }
      });
    view = finder.findRequiredView(source, 2131427683, "field 'toFindOk' and method 'register'");
    target.toFindOk = finder.castView(view, 2131427683, "field 'toFindOk'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.register();
        }
      });
    view = finder.findRequiredView(source, 2131427685, "field 'timeText'");
    target.timeText = finder.castView(view, 2131427685, "field 'timeText'");
    view = finder.findRequiredView(source, 2131427610, "field 'passSwitch' and method 'passSwitch'");
    target.passSwitch = finder.castView(view, 2131427610, "field 'passSwitch'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.passSwitch();
        }
      });
    view = finder.findRequiredView(source, 2131427356, "method 'back'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.back();
        }
      });
  }

  @Override public void unbind(T target) {
    target.mTelephone = null;
    target.mCode = null;
    target.mPassWord = null;
    target.codeBtn = null;
    target.toFindOk = null;
    target.timeText = null;
    target.passSwitch = null;
  }
}
