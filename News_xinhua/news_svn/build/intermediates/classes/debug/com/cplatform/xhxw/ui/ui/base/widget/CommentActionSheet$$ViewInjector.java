// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.widget;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class CommentActionSheet$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.base.widget.CommentActionSheet target, Object source) {
    View view;
    view = finder.findById(source, 2131427739);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427739' for field 'linearLayout' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.linearLayout = (android.widget.LinearLayout) view;
    view = finder.findById(source, 2131427740);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427740' for field 'touchLayout' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.touchLayout = (android.widget.LinearLayout) view;
    view = finder.findById(source, 2131427741);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427741' for field 'mCommentCancelTouchLayout' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mCommentCancelTouchLayout = (android.widget.LinearLayout) view;
    view = finder.findById(source, 2131427742);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427742' for field 'replyLayout' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.replyLayout = (android.widget.LinearLayout) view;
    view = finder.findById(source, 2131427743);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427743' for field 'deleteLayout' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.deleteLayout = (android.widget.LinearLayout) view;
    view = finder.findById(source, 2131427744);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427744' for field 'mCopyLayout' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mCopyLayout = (android.widget.LinearLayout) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.base.widget.CommentActionSheet target) {
    target.linearLayout = null;
    target.touchLayout = null;
    target.mCommentCancelTouchLayout = null;
    target.replyLayout = null;
    target.deleteLayout = null;
    target.mCopyLayout = null;
  }
}
