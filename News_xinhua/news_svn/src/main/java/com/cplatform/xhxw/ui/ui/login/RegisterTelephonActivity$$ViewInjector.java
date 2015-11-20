// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.login;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class RegisterTelephonActivity$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.login.RegisterTelephonActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131493045);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493045' for field 'mRegister' and method 'register' was not found. If this view is optional add '@Optional' annotation.");
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
    view = finder.findById(source, 2131493145);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493145' for field 'mPassWord' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mPassWord = (android.widget.EditText) view;
    view = finder.findById(source, 2131493141);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493141' for field 'mAgreement' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mAgreement = (android.widget.TextView) view;
    view = finder.findById(source, 2131493143);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493143' for field 'mTelephone' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mTelephone = (android.widget.EditText) view;
    view = finder.findById(source, 2131493146);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493146' for field 'passSwitch' and method 'passSwitch' was not found. If this view is optional add '@Optional' annotation.");
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
    view = finder.findById(source, 2131492925);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131492925' for field 'mSecurityCode' and method 'getCode' was not found. If this view is optional add '@Optional' annotation.");
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
    view = finder.findById(source, 2131492924);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131492924' for field 'mTimeText' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mTimeText = (android.widget.TextView) view;
    view = finder.findById(source, 2131493144);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493144' for field 'mCode' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mCode = (android.widget.EditText) view;
    view = finder.findById(source, 2131493140);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493140' for field 'mCheckBox' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mCheckBox = (android.widget.CheckBox) view;
    view = finder.findById(source, 2131492892);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131492892' for method 'back' was not found. If this view is optional add '@Optional' annotation.");
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
    target.mRegister = null;
    target.mPassWord = null;
    target.mAgreement = null;
    target.mTelephone = null;
    target.passSwitch = null;
    target.mSecurityCode = null;
    target.mTimeText = null;
    target.mCode = null;
    target.mCheckBox = null;
  }
}
