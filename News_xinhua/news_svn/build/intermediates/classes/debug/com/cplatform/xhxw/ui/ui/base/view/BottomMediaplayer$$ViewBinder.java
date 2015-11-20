// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class BottomMediaplayer$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.base.view.BottomMediaplayer> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427727, "field 'imageStart' and method 'imageMediaStart'");
    target.imageStart = finder.castView(view, 2131427727, "field 'imageStart'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.imageMediaStart();
        }
      });
    view = finder.findRequiredView(source, 2131427729, "field 'imagePause' and method 'imageMediaPause'");
    target.imagePause = finder.castView(view, 2131427729, "field 'imagePause'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.imageMediaPause();
        }
      });
    view = finder.findRequiredView(source, 2131427730, "field 'progressBar'");
    target.progressBar = finder.castView(view, 2131427730, "field 'progressBar'");
    view = finder.findRequiredView(source, 2131427728, "field 'linearLayout'");
    target.linearLayout = finder.castView(view, 2131427728, "field 'linearLayout'");
  }

  @Override public void unbind(T target) {
    target.imageStart = null;
    target.imagePause = null;
    target.progressBar = null;
    target.linearLayout = null;
  }
}
