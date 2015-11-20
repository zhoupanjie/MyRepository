// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.login;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class RegisterEmailActivity$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.login.RegisterEmailActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131427601);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427601' for field 'account' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.account = (android.widget.EditText) view;
    view = finder.findById(source, 2131427602);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427602' for field 'passWord' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.passWord = (android.widget.EditText) view;
    view = finder.findById(source, 2131427603);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427603' for field 'register' and method 'register' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.register = (android.widget.Button) view;
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
      throw new IllegalStateException("Required view with id '2131427604' for field 'checkBox' and field 'terms_service_text' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.checkBox = (android.widget.CheckBox) view;
    target.terms_service_text = (android.widget.CheckBox) view;
    view = finder.findById(source, 2131427605);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427605' for field 'agreement' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.agreement = (android.widget.TextView) view;
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

  public static void reset(com.cplatform.xhxw.ui.ui.login.RegisterEmailActivity target) {
    target.account = null;
    target.passWord = null;
    target.register = null;
    target.checkBox = null;
    target.terms_service_text = null;
    target.agreement = null;
  }
}
