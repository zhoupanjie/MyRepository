// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.saas.picShow;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SaasPicShowActivity$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.main.saas.picShow.SaasPicShowActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427569, "field 'mVp'");
    target.mVp = finder.castView(view, 2131427569, "field 'mVp'");
    view = finder.findRequiredView(source, 2131427570, "field 'mDefView'");
    target.mDefView = finder.castView(view, 2131427570, "field 'mDefView'");
  }

  @Override public void unbind(T target) {
    target.mVp = null;
    target.mDefView = null;
  }
}
