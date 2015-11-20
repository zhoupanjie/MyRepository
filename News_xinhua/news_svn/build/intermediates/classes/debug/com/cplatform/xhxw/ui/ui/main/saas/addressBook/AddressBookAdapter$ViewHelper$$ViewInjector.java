// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class AddressBookAdapter$ViewHelper$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.main.saas.addressBook.AddressBookAdapter.ViewHelper target, Object source) {
    View view;
    view = finder.findById(source, 2131428270);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428270' for field 'photo' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.photo = (android.widget.ImageView) view;
    view = finder.findById(source, 2131427973);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427973' for field 'name' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.name = (android.widget.TextView) view;
    view = finder.findById(source, 2131428271);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428271' for field 'index' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.index = (android.widget.TextView) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.main.saas.addressBook.AddressBookAdapter.ViewHelper target) {
    target.photo = null;
    target.name = null;
    target.index = null;
  }
}
