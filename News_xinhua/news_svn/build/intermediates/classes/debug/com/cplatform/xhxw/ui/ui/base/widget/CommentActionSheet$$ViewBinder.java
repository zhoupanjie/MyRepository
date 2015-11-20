// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.widget;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class CommentActionSheet$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.base.widget.CommentActionSheet> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427739, "field 'linearLayout'");
    target.linearLayout = finder.castView(view, 2131427739, "field 'linearLayout'");
    view = finder.findRequiredView(source, 2131427740, "field 'touchLayout'");
    target.touchLayout = finder.castView(view, 2131427740, "field 'touchLayout'");
    view = finder.findRequiredView(source, 2131427741, "field 'mCommentCancelTouchLayout'");
    target.mCommentCancelTouchLayout = finder.castView(view, 2131427741, "field 'mCommentCancelTouchLayout'");
    view = finder.findRequiredView(source, 2131427742, "field 'replyLayout'");
    target.replyLayout = finder.castView(view, 2131427742, "field 'replyLayout'");
    view = finder.findRequiredView(source, 2131427743, "field 'deleteLayout'");
    target.deleteLayout = finder.castView(view, 2131427743, "field 'deleteLayout'");
    view = finder.findRequiredView(source, 2131427744, "field 'mCopyLayout'");
    target.mCopyLayout = finder.castView(view, 2131427744, "field 'mCopyLayout'");
  }

  @Override public void unbind(T target) {
    target.linearLayout = null;
    target.touchLayout = null;
    target.mCommentCancelTouchLayout = null;
    target.replyLayout = null;
    target.deleteLayout = null;
    target.mCopyLayout = null;
  }
}
