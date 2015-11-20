// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.saas;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SMessageAdapter$ViewHelper$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.main.saas.SMessageAdapter.ViewHelper target, Object source) {
    View view;
    view = finder.findById(source, 2131427813);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427813' for field 'photo' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.photo = (android.widget.ImageView) view;
    view = finder.findById(source, 2131427973);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427973' for field 'name' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.name = (android.widget.TextView) view;
    view = finder.findById(source, 2131427974);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427974' for field 'time' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.time = (android.widget.TextView) view;
    view = finder.findById(source, 2131428355);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428355' for field 'lastMsg' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.lastMsg = (android.widget.TextView) view;
    view = finder.findById(source, 2131427576);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427576' for field 'num' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.num = (android.widget.TextView) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.main.saas.SMessageAdapter.ViewHelper target) {
    target.photo = null;
    target.name = null;
    target.time = null;
    target.lastMsg = null;
    target.num = null;
  }
}
