// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.search;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SearchActivity$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.search.SearchActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427614, "field 'editText'");
    target.editText = finder.castView(view, 2131427614, "field 'editText'");
    view = finder.findRequiredView(source, 2131427615, "field 'clear' and method 'clear'");
    target.clear = finder.castView(view, 2131427615, "field 'clear'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.clear();
        }
      });
    view = finder.findRequiredView(source, 2131427401, "field 'listView'");
    target.listView = finder.castView(view, 2131427401, "field 'listView'");
    view = finder.findRequiredView(source, 2131427570, "field 'mDefView'");
    target.mDefView = finder.castView(view, 2131427570, "field 'mDefView'");
    view = finder.findRequiredView(source, 2131427617, "field 'mHotWordsView'");
    target.mHotWordsView = finder.castView(view, 2131427617, "field 'mHotWordsView'");
    view = finder.findRequiredView(source, 2131427613, "field 'mRootContainer'");
    target.mRootContainer = finder.castView(view, 2131427613, "field 'mRootContainer'");
    view = finder.findRequiredView(source, 2131427616, "method 'search'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.search();
        }
      });
  }

  @Override public void unbind(T target) {
    target.editText = null;
    target.clear = null;
    target.listView = null;
    target.mDefView = null;
    target.mHotWordsView = null;
    target.mRootContainer = null;
  }
}
