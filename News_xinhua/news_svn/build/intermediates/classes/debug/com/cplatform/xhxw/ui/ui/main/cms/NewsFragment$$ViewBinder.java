// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.cms;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class NewsFragment$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.main.cms.NewsFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427675, "field 'mListView'");
    target.mListView = finder.castView(view, 2131427675, "field 'mListView'");
    view = finder.findRequiredView(source, 2131427570, "field 'mDefView'");
    target.mDefView = finder.castView(view, 2131427570, "field 'mDefView'");
    view = finder.findRequiredView(source, 2131427765, "field 'rootView'");
    target.rootView = view;
  }

  @Override public void unbind(T target) {
    target.mListView = null;
    target.mDefView = null;
    target.rootView = null;
  }
}
