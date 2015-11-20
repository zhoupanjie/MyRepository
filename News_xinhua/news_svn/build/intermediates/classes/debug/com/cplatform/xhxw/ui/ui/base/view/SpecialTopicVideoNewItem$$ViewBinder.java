// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SpecialTopicVideoNewItem$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.base.view.SpecialTopicVideoNewItem> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131428363, "field 'mHeader'");
    target.mHeader = finder.castView(view, 2131428363, "field 'mHeader'");
    view = finder.findRequiredView(source, 2131428276, "field 'mContent'");
    target.mContent = finder.castView(view, 2131428276, "field 'mContent'");
  }

  @Override public void unbind(T target) {
    target.mHeader = null;
    target.mContent = null;
  }
}
