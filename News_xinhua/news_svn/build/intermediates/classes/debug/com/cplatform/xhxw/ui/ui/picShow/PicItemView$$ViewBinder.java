// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.picShow;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class PicItemView$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.picShow.PicItemView> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131428003, "field 'mImg'");
    target.mImg = finder.castView(view, 2131428003, "field 'mImg'");
    view = finder.findRequiredView(source, 2131427570, "field 'mDefView'");
    target.mDefView = finder.castView(view, 2131427570, "field 'mDefView'");
  }

  @Override public void unbind(T target) {
    target.mImg = null;
    target.mDefView = null;
  }
}
