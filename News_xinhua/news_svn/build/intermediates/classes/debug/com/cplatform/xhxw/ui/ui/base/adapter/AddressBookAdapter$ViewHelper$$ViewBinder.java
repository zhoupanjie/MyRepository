// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.adapter;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AddressBookAdapter$ViewHelper$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.base.adapter.AddressBookAdapter.ViewHelper> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131428270, "field 'photo'");
    target.photo = finder.castView(view, 2131428270, "field 'photo'");
    view = finder.findRequiredView(source, 2131427973, "field 'name'");
    target.name = finder.castView(view, 2131427973, "field 'name'");
    view = finder.findRequiredView(source, 2131428271, "field 'index'");
    target.index = finder.castView(view, 2131428271, "field 'index'");
  }

  @Override public void unbind(T target) {
    target.photo = null;
    target.name = null;
    target.index = null;
  }
}
