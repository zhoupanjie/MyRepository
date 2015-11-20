// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.test;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class Text$$ViewBinder<T extends com.cplatform.xhxw.ui.test.Text> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131428124, "field 'button' and method 'button1'");
    target.button = finder.castView(view, 2131428124, "field 'button'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.button1();
        }
      });
    view = finder.findRequiredView(source, 2131428125, "method 'getUserInfo'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.getUserInfo();
        }
      });
    view = finder.findRequiredView(source, 2131428126, "method 'setUserInfo'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.setUserInfo();
        }
      });
    view = finder.findRequiredView(source, 2131428127, "method 'feedBack'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.feedBack();
        }
      });
  }

  @Override public void unbind(T target) {
    target.button = null;
  }
}
