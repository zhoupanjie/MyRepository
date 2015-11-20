// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.saas;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SMessageChatAdapter$ViewHelperRight$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.main.saas.SMessageChatAdapter.ViewHelperRight target, Object source) {
    View view;
    view = finder.findById(source, 2131493886);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493886' for field 'avatar' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.avatar = (android.widget.ImageView) view;
    view = finder.findById(source, 2131493885);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493885' for field 'time' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.time = (android.widget.TextView) view;
    view = finder.findById(source, 2131493888);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493888' for field 'msg' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.msg = (android.widget.TextView) view;
    view = finder.findById(source, 2131493576);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493576' for field 'progressBar' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.progressBar = view;
    view = finder.findById(source, 2131493889);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493889' for field 'pic' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.pic = (android.widget.ImageView) view;
    view = finder.findById(source, 2131493887);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493887' for field 'body' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.body = view;
    view = finder.findById(source, 2131493890);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493890' for field 'error' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.error = (android.widget.ImageView) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.main.saas.SMessageChatAdapter.ViewHelperRight target) {
    target.avatar = null;
    target.time = null;
    target.msg = null;
    target.progressBar = null;
    target.pic = null;
    target.body = null;
    target.error = null;
  }
}
