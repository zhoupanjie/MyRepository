// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.specialTopic;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SpecialTopicActivity$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.specialTopic.SpecialTopicActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131493106);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493106' for field 'mDefView' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mDefView = (com.cplatform.xhxw.ui.ui.base.widget.DefaultView) view;
    view = finder.findById(source, 2131493210);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493210' for field 'mSpecialTitle' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mSpecialTitle = (android.widget.TextView) view;
    view = finder.findById(source, 2131493211);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493211' for field 'mListView' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mListView = (android.widget.ListView) view;
    view = finder.findById(source, 2131493114);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493114' for method 'onShareAction' was not found. If this view is optional add '@Optional' annotation.");
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
    target.mDefView = null;
    target.mSpecialTitle = null;
    target.mListView = null;
  }
}
