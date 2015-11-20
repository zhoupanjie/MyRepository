// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.settings;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class BindPhoneActivity$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.settings.BindPhoneActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427386, "field 'mTelephone'");
    target.mTelephone = finder.castView(view, 2131427386, "field 'mTelephone'");
    view = finder.findRequiredView(source, 2131427387, "field 'mCode'");
    target.mCode = finder.castView(view, 2131427387, "field 'mCode'");
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
    view = finder.findRequiredView(source, 2131427390, "field 'bind_mobile' and method 'bindMobile'");
    target.bind_mobile = finder.castView(view, 2131427390, "field 'bind_mobile'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.bindMobile();
        }
      });
    view = finder.findRequiredView(source, 2131427388, "field 'mTimeText'");
    target.mTimeText = finder.castView(view, 2131427388, "field 'mTimeText'");
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
    target.mSecurityCode = null;
    target.bind_mobile = null;
    target.mTimeText = null;
  }
}
