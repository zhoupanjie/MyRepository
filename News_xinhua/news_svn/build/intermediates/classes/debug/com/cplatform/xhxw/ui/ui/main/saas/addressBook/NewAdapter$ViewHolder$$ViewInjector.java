// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class NewAdapter$ViewHolder$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.main.saas.addressBook.NewAdapter.ViewHolder target, Object source) {
    View view;
    view = finder.findById(source, 2131427537);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427537' for field 'userImage' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.userImage = (android.widget.ImageView) view;
    view = finder.findById(source, 2131427538);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427538' for field 'userName' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.userName = (android.widget.TextView) view;
    view = finder.findById(source, 2131427539);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427539' for field 'comment' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.comment = (android.widget.TextView) view;
    view = finder.findById(source, 2131427541);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427541' for field 'addFriends' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.addFriends = (android.widget.Button) view;
    view = finder.findById(source, 2131427540);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427540' for field 'addedFriends' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.addedFriends = (android.widget.TextView) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.main.saas.addressBook.NewAdapter.ViewHolder target) {
    target.userImage = null;
    target.userName = null;
    target.comment = null;
    target.addFriends = null;
    target.addedFriends = null;
  }
}
