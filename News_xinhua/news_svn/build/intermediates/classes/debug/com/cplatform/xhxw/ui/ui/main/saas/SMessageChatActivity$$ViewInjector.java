// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.saas;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SMessageChatActivity$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.main.saas.SMessageChatActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131427380);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427380' for field 'mListView' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mListView = (com.cplatform.xhxw.ui.ui.base.widget.CommentListView) view;
    view = finder.findById(source, 2131427408);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427408' for field 'editText' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.editText = (android.widget.EditText) view;
    view = finder.findById(source, 2131427411);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427411' for field 'mExpressionWidgt' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mExpressionWidgt = (com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionWidgt) view;
    view = finder.findById(source, 2131427410);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427410' for field 'mExprBtn' and method 'onClickExprBtn' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mExprBtn = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClickExprBtn();
        }
      });
    view = finder.findById(source, 2131427375);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427375' for field 'mRootContainer' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mRootContainer = (com.cplatform.xhxw.ui.ui.base.widget.InputViewSensitiveLinearLayout) view;
    view = finder.findById(source, 2131427407);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427407' for field 'mCommentOperateLo' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mCommentOperateLo = (android.widget.RelativeLayout) view;
    view = finder.findById(source, 2131427409);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427409' for method 'senAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.senAction();
        }
      });
  }

  public static void reset(com.cplatform.xhxw.ui.ui.main.saas.SMessageChatActivity target) {
    target.mListView = null;
    target.editText = null;
    target.mExpressionWidgt = null;
    target.mExprBtn = null;
    target.mRootContainer = null;
    target.mCommentOperateLo = null;
  }
}
