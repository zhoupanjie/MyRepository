// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class RadioBroadcastListFragment$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast.RadioBroadcastListFragment target, Object source) {
    View view;
    view = finder.findById(source, 2131493460);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493460' for field 'ivAd' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.ivAd = (android.widget.ImageView) view;
    view = finder.findById(source, 2131493106);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493106' for field 'mDefView' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mDefView = (com.cplatform.xhxw.ui.ui.base.widget.DefaultView) view;
    view = finder.findById(source, 2131493459);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493459' for field 'llType' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.llType = (android.widget.LinearLayout) view;
    view = finder.findById(source, 2131493211);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493211' for field 'mListView' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mListView = (com.cplatform.xhxw.ui.ui.base.widget.PullRefreshListView) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast.RadioBroadcastListFragment target) {
    target.ivAd = null;
    target.mDefView = null;
    target.llType = null;
    target.mListView = null;
  }
}
