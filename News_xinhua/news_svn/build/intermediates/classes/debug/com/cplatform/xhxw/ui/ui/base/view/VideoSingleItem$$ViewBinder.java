// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class VideoSingleItem$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.base.view.VideoSingleItem> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131428346, "field 'mImg'");
    target.mImg = finder.castView(view, 2131428346, "field 'mImg'");
    view = finder.findRequiredView(source, 2131428010, "field 'mTitle'");
    target.mTitle = finder.castView(view, 2131428010, "field 'mTitle'");
    view = finder.findRequiredView(source, 2131427574, "field 'mDesc'");
    target.mDesc = finder.castView(view, 2131427574, "field 'mDesc'");
  }

  @Override public void unbind(T target) {
    target.mImg = null;
    target.mTitle = null;
    target.mDesc = null;
  }
}
