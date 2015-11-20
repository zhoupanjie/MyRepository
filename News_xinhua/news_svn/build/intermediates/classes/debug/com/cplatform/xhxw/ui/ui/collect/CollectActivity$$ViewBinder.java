// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.collect;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class CollectActivity$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.collect.CollectActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427401, "field 'mListView'");
    target.mListView = finder.castView(view, 2131427401, "field 'mListView'");
  }

  @Override public void unbind(T target) {
    target.mListView = null;
  }
}
