// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.cms.game;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class GameDownloadCenterActivity$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.main.cms.game.GameDownloadCenterActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131427436);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427436' for field 'lv' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.lv = (android.widget.ListView) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.main.cms.game.GameDownloadCenterActivity target) {
    target.lv = null;
  }
}
