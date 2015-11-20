// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.feedback;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FeedbackActivity$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.feedback.FeedbackActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427764, "field 'editText'");
    target.editText = finder.castView(view, 2131427764, "field 'editText'");
    view = finder.findRequiredView(source, 2131427356, "method 'goBack'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.goBack();
        }
      });
    view = finder.findRequiredView(source, 2131427619, "method 'publishFeed'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.publishFeed();
        }
      });
  }

  @Override public void unbind(T target) {
    target.editText = null;
  }
}
