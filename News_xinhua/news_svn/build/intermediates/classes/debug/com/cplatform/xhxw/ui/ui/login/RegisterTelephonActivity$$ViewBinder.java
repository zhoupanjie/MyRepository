// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.login;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class RegisterTelephonActivity$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.login.RegisterTelephonActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427607, "field 'mTelephone'");
    target.mTelephone = finder.castView(view, 2131427607, "field 'mTelephone'");
    view = finder.findRequiredView(source, 2131427608, "field 'mCode'");
    target.mCode = finder.castView(view, 2131427608, "field 'mCode'");
    view = finder.findRequiredView(source, 2131427609, "field 'mPassWord'");
    target.mPassWord = finder.castView(view, 2131427609, "field 'mPassWord'");
    view = finder.findRequiredView(source, 2131427389, "field 'mSecurityCode' and method 'getCode'");
    target.mSecurityCode = finder.castView(view, 2131427389, "field 'mSecurityCode'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.getCode();
        }
      });
    view = finder.findRequiredView(source, 2131427509, "field 'mRegister' and method 'register'");
    target.mRegister = finder.castView(view, 2131427509, "field 'mRegister'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.register();
        }
      });
    view = finder.findRequiredView(source, 2131427604, "field 'mCheckBox'");
    target.mCheckBox = finder.castView(view, 2131427604, "field 'mCheckBox'");
    view = finder.findRequiredView(source, 2131427388, "field 'mTimeText'");
    target.mTimeText = finder.castView(view, 2131427388, "field 'mTimeText'");
    view = finder.findRequiredView(source, 2131427605, "field 'mAgreement'");
    target.mAgreement = finder.castView(view, 2131427605, "field 'mAgreement'");
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
    target.mSecurityCode = null;
    target.mRegister = null;
    target.mCheckBox = null;
    target.mTimeText = null;
    target.mAgreement = null;
    target.passSwitch = null;
  }
}
