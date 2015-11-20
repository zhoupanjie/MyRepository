// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.settings;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class BindPhoneActivity$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.settings.BindPhoneActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131427386);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427386' for field 'mTelephone' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mTelephone = (android.widget.EditText) view;
    view = finder.findById(source, 2131427387);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427387' for field 'mCode' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mCode = (android.widget.EditText) view;
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
    view = finder.findById(source, 2131427390);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427390' for field 'bind_mobile' and method 'bindMobile' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.bind_mobile = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.bindMobile();
        }
      });
    view = finder.findById(source, 2131427388);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427388' for field 'mTimeText' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mTimeText = (android.widget.TextView) view;
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

  public static void reset(com.cplatform.xhxw.ui.ui.settings.BindPhoneActivity target) {
    target.mTelephone = null;
    target.mCode = null;
    target.mSecurityCode = null;
    target.bind_mobile = null;
    target.mTimeText = null;
  }
}
