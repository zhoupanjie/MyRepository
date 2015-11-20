// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.detailpage;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class CommentActivity$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.detailpage.CommentActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427354, "field 'linearLayout'");
    target.linearLayout = finder.castView(view, 2131427354, "field 'linearLayout'");
    view = finder.findRequiredView(source, 2131427401, "field 'listView'");
    target.listView = finder.castView(view, 2131427401, "field 'listView'");
    view = finder.findRequiredView(source, 2131427406, "field 'commentEdittext'");
    target.commentEdittext = finder.castView(view, 2131427406, "field 'commentEdittext'");
    view = finder.findRequiredView(source, 2131427409, "field 'sendbtn' and method 'send'");
    target.sendbtn = finder.castView(view, 2131427409, "field 'sendbtn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.send();
        }
      });
    view = finder.findRequiredView(source, 2131427408, "field 'editText' and method 'onEditTextClick'");
    target.editText = finder.castView(view, 2131427408, "field 'editText'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onEditTextClick();
        }
      });
    view = finder.findRequiredView(source, 2131427375, "field 'inputLayout'");
    target.inputLayout = finder.castView(view, 2131427375, "field 'inputLayout'");
    view = finder.findRequiredView(source, 2131427411, "field 'mExpressionWidgt'");
    target.mExpressionWidgt = finder.castView(view, 2131427411, "field 'mExpressionWidgt'");
    view = finder.findRequiredView(source, 2131427410, "field 'mExprBtn' and method 'onClickExprBtn'");
    target.mExprBtn = finder.castView(view, 2131427410, "field 'mExprBtn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onClickExprBtn();
        }
      });
    view = finder.findRequiredView(source, 2131427407, "field 'mCommentOperateLo'");
    target.mCommentOperateLo = finder.castView(view, 2131427407, "field 'mCommentOperateLo'");
    view = finder.findRequiredView(source, 2131427356, "method 'goBack'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.goBack();
        }
      });
  }

  @Override public void unbind(T target) {
    target.linearLayout = null;
    target.listView = null;
    target.commentEdittext = null;
    target.sendbtn = null;
    target.editText = null;
    target.inputLayout = null;
    target.mExpressionWidgt = null;
    target.mExprBtn = null;
    target.mCommentOperateLo = null;
  }
}
