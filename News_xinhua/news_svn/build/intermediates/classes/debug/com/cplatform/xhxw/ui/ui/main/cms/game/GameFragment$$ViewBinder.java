// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.cms.game;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class GameFragment$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.main.cms.game.GameFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427675, "field 'mListView'");
    target.mListView = finder.castView(view, 2131427675, "field 'mListView'");
    view = finder.findRequiredView(source, 2131427570, "field 'mDefView'");
    target.mDefView = finder.castView(view, 2131427570, "field 'mDefView'");
  }

  @Override public void unbind(T target) {
    target.mListView = null;
    target.mDefView = null;
  }
}
