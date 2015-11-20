// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.login;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class ToFindPassWordTelephonActivity$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.login.ToFindPassWordTelephonActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131427684);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427684' for field 'mTelephone' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mTelephone = (android.widget.EditText) view;
    view = finder.findById(source, 2131427687);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427687' for field 'mCode' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mCode = (android.widget.EditText) view;
    view = finder.findById(source, 2131427688);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427688' for field 'mPassWord' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mPassWord = (android.widget.EditText) view;
    view = finder.findById(source, 2131427686);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427686' for field 'codeBtn' and method 'getCode' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.codeBtn = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.getCode();
        }
      });
    view = finder.findById(source, 2131427683);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427683' for field 'toFindOk' and method 'register' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.toFindOk = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.register();
        }
      });
    view = finder.findById(source, 2131427685);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427685' for field 'timeText' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.timeText = (android.widget.TextView) view;
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

  public static void reset(com.cplatform.xhxw.ui.ui.login.ToFindPassWordTelephonActivity target) {
    target.mTelephone = null;
    target.mCode = null;
    target.mPassWord = null;
    target.codeBtn = null;
    target.toFindOk = null;
    target.timeText = null;
    target.passSwitch = null;
  }
}
