// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class RadioBroadcastFragment$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast.RadioBroadcastFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427921, "field 'mViewGroup'");
    target.mViewGroup = finder.castView(view, 2131427921, "field 'mViewGroup'");
    view = finder.findRequiredView(source, 2131427920, "field 'ivSetting'");
    target.ivSetting = finder.castView(view, 2131427920, "field 'ivSetting'");
  }

  @Override public void unbind(T target) {
    target.mViewGroup = null;
    target.ivSetting = null;
  }
}
