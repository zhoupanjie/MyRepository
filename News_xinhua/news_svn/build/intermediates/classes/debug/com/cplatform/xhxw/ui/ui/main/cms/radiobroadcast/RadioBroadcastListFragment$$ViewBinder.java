// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class RadioBroadcastListFragment$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast.RadioBroadcastListFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427675, "field 'mListView'");
    target.mListView = finder.castView(view, 2131427675, "field 'mListView'");
    view = finder.findRequiredView(source, 2131427570, "field 'mDefView'");
    target.mDefView = finder.castView(view, 2131427570, "field 'mDefView'");
    view = finder.findRequiredView(source, 2131427923, "field 'llType'");
    target.llType = finder.castView(view, 2131427923, "field 'llType'");
    view = finder.findRequiredView(source, 2131427924, "field 'ivAd'");
    target.ivAd = finder.castView(view, 2131427924, "field 'ivAd'");
  }

  @Override public void unbind(T target) {
    target.mListView = null;
    target.mDefView = null;
    target.llType = null;
    target.ivAd = null;
  }
}
