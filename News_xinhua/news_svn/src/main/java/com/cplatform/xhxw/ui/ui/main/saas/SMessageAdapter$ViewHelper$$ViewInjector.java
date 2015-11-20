// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.saas;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SMessageAdapter$ViewHelper$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.main.saas.SMessageAdapter.ViewHelper target, Object source) {
    View view;
    view = finder.findById(source, 2131493112);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493112' for field 'num' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.num = (android.widget.TextView) view;
    view = finder.findById(source, 2131493349);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493349' for field 'photo' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.photo = (android.widget.ImageView) view;
    view = finder.findById(source, 2131493891);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493891' for field 'lastMsg' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.lastMsg = (android.widget.TextView) view;
    view = finder.findById(source, 2131493509);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493509' for field 'name' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.name = (android.widget.TextView) view;
    view = finder.findById(source, 2131493510);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493510' for field 'time' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.time = (android.widget.TextView) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.main.saas.SMessageAdapter.ViewHelper target) {
    target.num = null;
    target.photo = null;
    target.lastMsg = null;
    target.name = null;
    target.time = null;
  }
}
