// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class RadioBroadcastFragment$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast.RadioBroadcastFragment target, Object source) {
    View view;
    view = finder.findById(source, 2131493457);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493457' for field 'mViewGroup' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mViewGroup = (android.view.ViewGroup) view;
    view = finder.findById(source, 2131493456);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493456' for field 'ivSetting' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.ivSetting = (android.widget.ImageView) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast.RadioBroadcastFragment target) {
    target.mViewGroup = null;
    target.ivSetting = null;
  }
}
