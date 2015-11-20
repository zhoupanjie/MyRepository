// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.feedback;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class FeedbackActivity$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.feedback.FeedbackActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131427764);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427764' for field 'editText' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.editText = (android.widget.EditText) view;
    view = finder.findById(source, 2131427356);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427356' for method 'goBack' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.goBack();
        }
      });
    view = finder.findById(source, 2131427619);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427619' for method 'publishFeed' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.publishFeed();
        }
      });
  }

  public static void reset(com.cplatform.xhxw.ui.ui.feedback.FeedbackActivity target) {
    target.editText = null;
  }
}
