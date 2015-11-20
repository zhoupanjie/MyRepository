// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AdapterBroadcast$ViewHolderRadio$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast.AdapterBroadcast.ViewHolderRadio> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427973, "field 'tvName'");
    target.tvName = finder.castView(view, 2131427973, "field 'tvName'");
    view = finder.findRequiredView(source, 2131427974, "field 'tvTime'");
    target.tvTime = finder.castView(view, 2131427974, "field 'tvTime'");
    view = finder.findRequiredView(source, 2131427972, "field 'riv'");
    target.riv = finder.castView(view, 2131427972, "field 'riv'");
  }

  @Override public void unbind(T target) {
    target.tvName = null;
    target.tvTime = null;
    target.riv = null;
  }
}
