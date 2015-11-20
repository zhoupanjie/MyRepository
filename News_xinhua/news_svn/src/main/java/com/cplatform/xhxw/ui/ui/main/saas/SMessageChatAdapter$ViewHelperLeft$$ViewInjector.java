// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.saas;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SMessageChatAdapter$ViewHelperLeft$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.main.saas.SMessageChatAdapter.ViewHelperLeft target, Object source) {
    View view;
    view = finder.findById(source, 2131493887);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493887' for field 'body' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.body = view;
    view = finder.findById(source, 2131493885);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493885' for field 'date' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.date = (android.widget.TextView) view;
    view = finder.findById(source, 2131493889);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493889' for field 'pic' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.pic = (android.widget.ImageView) view;
    view = finder.findById(source, 2131493886);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493886' for field 'avatar' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.avatar = (android.widget.ImageView) view;
    view = finder.findById(source, 2131493888);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493888' for field 'msg' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.msg = (android.widget.TextView) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.main.saas.SMessageChatAdapter.ViewHelperLeft target) {
    target.body = null;
    target.date = null;
    target.pic = null;
    target.avatar = null;
    target.msg = null;
  }
}
