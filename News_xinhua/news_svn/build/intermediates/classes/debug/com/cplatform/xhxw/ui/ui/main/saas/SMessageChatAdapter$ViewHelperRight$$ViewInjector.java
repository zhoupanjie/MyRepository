// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.saas;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SMessageChatAdapter$ViewHelperRight$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.main.saas.SMessageChatAdapter.ViewHelperRight target, Object source) {
    View view;
    view = finder.findById(source, 2131428349);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428349' for field 'time' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.time = (android.widget.TextView) view;
    view = finder.findById(source, 2131428350);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428350' for field 'avatar' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.avatar = (android.widget.ImageView) view;
    view = finder.findById(source, 2131428352);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428352' for field 'msg' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.msg = (android.widget.TextView) view;
    view = finder.findById(source, 2131428353);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428353' for field 'pic' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.pic = (android.widget.ImageView) view;
    view = finder.findById(source, 2131428354);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428354' for field 'error' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.error = (android.widget.ImageView) view;
    view = finder.findById(source, 2131428040);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428040' for field 'progressBar' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.progressBar = view;
    view = finder.findById(source, 2131428351);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428351' for field 'body' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.body = view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.main.saas.SMessageChatAdapter.ViewHelperRight target) {
    target.time = null;
    target.avatar = null;
    target.msg = null;
    target.pic = null;
    target.error = null;
    target.progressBar = null;
    target.body = null;
  }
}
