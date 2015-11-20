// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SpecialTopicSliderView$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.base.view.SpecialTopicSliderView> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427569, "field 'mVp'");
    target.mVp = finder.castView(view, 2131427569, "field 'mVp'");
    view = finder.findRequiredView(source, 2131428359, "field 'mCpi'");
    target.mCpi = finder.castView(view, 2131428359, "field 'mCpi'");
  }

  @Override public void unbind(T target) {
    target.mVp = null;
    target.mCpi = null;
  }
}
