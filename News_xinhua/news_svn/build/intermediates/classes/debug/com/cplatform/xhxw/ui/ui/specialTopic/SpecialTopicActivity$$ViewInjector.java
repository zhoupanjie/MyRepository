// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.specialTopic;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SpecialTopicActivity$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.specialTopic.SpecialTopicActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131427675);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427675' for field 'mListView' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mListView = (android.widget.ListView) view;
    view = finder.findById(source, 2131427570);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427570' for field 'mDefView' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mDefView = (com.cplatform.xhxw.ui.ui.base.widget.DefaultView) view;
    view = finder.findById(source, 2131427674);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427674' for field 'mSpecialTitle' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mSpecialTitle = (android.widget.TextView) view;
    view = finder.findById(source, 2131427578);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427578' for method 'onShareAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onShareAction();
        }
      });
  }

  public static void reset(com.cplatform.xhxw.ui.ui.specialTopic.SpecialTopicActivity target) {
    target.mListView = null;
    target.mDefView = null;
    target.mSpecialTitle = null;
  }
}
