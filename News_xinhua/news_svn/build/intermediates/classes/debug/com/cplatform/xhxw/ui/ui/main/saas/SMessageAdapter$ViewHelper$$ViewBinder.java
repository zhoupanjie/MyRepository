// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.saas;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SMessageAdapter$ViewHelper$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.main.saas.SMessageAdapter.ViewHelper> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427813, "field 'photo'");
    target.photo = finder.castView(view, 2131427813, "field 'photo'");
    view = finder.findRequiredView(source, 2131427973, "field 'name'");
    target.name = finder.castView(view, 2131427973, "field 'name'");
    view = finder.findRequiredView(source, 2131427974, "field 'time'");
    target.time = finder.castView(view, 2131427974, "field 'time'");
    view = finder.findRequiredView(source, 2131428355, "field 'lastMsg'");
    target.lastMsg = finder.castView(view, 2131428355, "field 'lastMsg'");
    view = finder.findRequiredView(source, 2131427576, "field 'num'");
    target.num = finder.castView(view, 2131427576, "field 'num'");
  }

  @Override public void unbind(T target) {
    target.photo = null;
    target.name = null;
    target.time = null;
    target.lastMsg = null;
    target.num = null;
  }
}
