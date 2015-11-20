// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.specialTopic;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SpecialTopicActivity$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.specialTopic.SpecialTopicActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427675, "field 'mListView'");
    target.mListView = finder.castView(view, 2131427675, "field 'mListView'");
    view = finder.findRequiredView(source, 2131427570, "field 'mDefView'");
    target.mDefView = finder.castView(view, 2131427570, "field 'mDefView'");
    view = finder.findRequiredView(source, 2131427674, "field 'mSpecialTitle'");
    target.mSpecialTitle = finder.castView(view, 2131427674, "field 'mSpecialTitle'");
    view = finder.findRequiredView(source, 2131427578, "method 'onShareAction'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onShareAction();
        }
      });
  }

  @Override public void unbind(T target) {
    target.mListView = null;
    target.mDefView = null;
    target.mSpecialTitle = null;
  }
}
