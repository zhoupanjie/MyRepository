// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.login;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class RegisterTelephonActivity$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.login.RegisterTelephonActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131427607);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427607' for field 'mTelephone' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mTelephone = (android.widget.EditText) view;
    view = finder.findById(source, 2131427608);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427608' for field 'mCode' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mCode = (android.widget.EditText) view;
    view = finder.findById(source, 2131427609);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427609' for field 'mPassWord' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mPassWord = (android.widget.EditText) view;
    view = finder.findById(source, 2131427389);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427389' for field 'mSecurityCode' and method 'getCode' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mSecurityCode = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.getCode();
        }
      });
    view = finder.findById(source, 2131427509);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427509' for field 'mRegister' and method 'register' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mRegister = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.register();
        }
      });
    view = finder.findById(source, 2131427604);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427604' for field 'mCheckBox' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mCheckBox = (android.widget.CheckBox) view;
    view = finder.findById(source, 2131427388);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427388' for field 'mTimeText' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mTimeText = (android.widget.TextView) view;
    view = finder.findById(source, 2131427605);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427605' for field 'mAgreement' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mAgreement = (android.widget.TextView) view;
    view = finder.findById(source, 2131427610);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427610' for field 'passSwitch' and method 'passSwitch' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.passSwitch = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.passSwitch();
        }
      });
    view = finder.findById(source, 2131427356);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427356' for method 'back' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.back();
        }
      });
  }

  public static void reset(com.cplatform.xhxw.ui.ui.login.RegisterTelephonActivity target) {
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
