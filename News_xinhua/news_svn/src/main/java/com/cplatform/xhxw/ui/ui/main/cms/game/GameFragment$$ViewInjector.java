// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.cms.game;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class GameFragment$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.main.cms.game.GameFragment target, Object source) {
    View view;
    view = finder.findById(source, 2131493106);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493106' for field 'mDefView' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mDefView = (com.cplatform.xhxw.ui.ui.base.widget.DefaultView) view;
    view = finder.findById(source, 2131493211);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493211' for field 'mListView' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mListView = (com.cplatform.xhxw.ui.ui.base.widget.PullRefreshListView) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.main.cms.game.GameFragment target) {
    target.mDefView = null;
    target.mListView = null;
  }
}
