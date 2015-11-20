// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SpecialTopicVideoView$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.base.view.SpecialTopicVideoView> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131428003, "field 'mImg' and method 'onImgClickAction'");
    target.mImg = finder.castView(view, 2131428003, "field 'mImg'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onImgClickAction();
        }
      });
    view = finder.findRequiredView(source, 2131427574, "field 'mDesc'");
    target.mDesc = finder.castView(view, 2131427574, "field 'mDesc'");
  }

  @Override public void unbind(T target) {
    target.mImg = null;
    target.mDesc = null;
  }
}
