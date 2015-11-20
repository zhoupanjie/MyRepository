// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class CompanyMessageNativeAdapter$ViewHolder$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.main.saas.addressBook.CompanyMessageNativeAdapter.ViewHolder target, Object source) {
    View view;
    view = finder.findById(source, 2131493061);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493061' for field 'time' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.time = (android.widget.TextView) view;
    view = finder.findById(source, 2131493059);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493059' for field 'userImage' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.userImage = (android.widget.ImageView) view;
    view = finder.findById(source, 2131493060);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493060' for field 'userName' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.userName = (android.widget.TextView) view;
    view = finder.findById(source, 2131493062);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493062' for field 'reply' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.reply = (android.widget.TextView) view;
    view = finder.findById(source, 2131493063);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493063' for field 'content' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.content = (android.widget.TextView) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.main.saas.addressBook.CompanyMessageNativeAdapter.ViewHolder target) {
    target.time = null;
    target.userImage = null;
    target.userName = null;
    target.reply = null;
    target.content = null;
  }
}
