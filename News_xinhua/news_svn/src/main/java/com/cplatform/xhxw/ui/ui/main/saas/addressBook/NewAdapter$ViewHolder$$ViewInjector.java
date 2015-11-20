// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class NewAdapter$ViewHolder$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.main.saas.addressBook.NewAdapter.ViewHolder target, Object source) {
    View view;
    view = finder.findById(source, 2131493073);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493073' for field 'userImage' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.userImage = (android.widget.ImageView) view;
    view = finder.findById(source, 2131493074);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493074' for field 'userName' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.userName = (android.widget.TextView) view;
    view = finder.findById(source, 2131493076);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493076' for field 'addedFriends' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.addedFriends = (android.widget.TextView) view;
    view = finder.findById(source, 2131493075);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493075' for field 'comment' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.comment = (android.widget.TextView) view;
    view = finder.findById(source, 2131493077);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493077' for field 'addFriends' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.addFriends = (android.widget.Button) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.main.saas.addressBook.NewAdapter.ViewHolder target) {
    target.userImage = null;
    target.userName = null;
    target.addedFriends = null;
    target.comment = null;
    target.addFriends = null;
  }
}
