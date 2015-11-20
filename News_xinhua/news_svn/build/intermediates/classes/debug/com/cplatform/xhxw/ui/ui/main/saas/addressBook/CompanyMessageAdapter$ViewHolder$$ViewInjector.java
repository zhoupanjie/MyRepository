// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class CompanyMessageAdapter$ViewHolder$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.main.saas.addressBook.CompanyMessageAdapter.ViewHolder target, Object source) {
    View view;
    view = finder.findById(source, 2131427523);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427523' for field 'userImage' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.userImage = (android.widget.ImageView) view;
    view = finder.findById(source, 2131427524);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427524' for field 'userName' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.userName = (android.widget.TextView) view;
    view = finder.findById(source, 2131427525);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427525' for field 'time' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.time = (android.widget.TextView) view;
    view = finder.findById(source, 2131427526);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427526' for field 'reply' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.reply = (android.widget.TextView) view;
    view = finder.findById(source, 2131427527);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427527' for field 'content' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.content = (android.widget.TextView) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.main.saas.addressBook.CompanyMessageAdapter.ViewHolder target) {
    target.userImage = null;
    target.userName = null;
    target.time = null;
    target.reply = null;
    target.content = null;
  }
}
