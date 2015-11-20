// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.detailpage;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class CommentActivity$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.detailpage.CommentActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131427354);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427354' for field 'linearLayout' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.linearLayout = (android.widget.LinearLayout) view;
    view = finder.findById(source, 2131427401);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427401' for field 'listView' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.listView = (com.cplatform.xhxw.ui.ui.base.widget.CommentListView) view;
    view = finder.findById(source, 2131427406);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427406' for field 'commentEdittext' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.commentEdittext = (com.cplatform.xhxw.ui.ui.base.view.CommentEdittext) view;
    view = finder.findById(source, 2131427409);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427409' for field 'sendbtn' and method 'send' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.sendbtn = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.send();
        }
      });
    view = finder.findById(source, 2131427408);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427408' for field 'editText' and method 'onEditTextClick' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.editText = (android.widget.EditText) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onEditTextClick();
        }
      });
    view = finder.findById(source, 2131427375);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427375' for field 'inputLayout' and field 'mRootContainer' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.inputLayout = (com.cplatform.xhxw.ui.ui.base.widget.InputViewSensitiveLinearLayout) view;
    target.mRootContainer = (com.cplatform.xhxw.ui.ui.base.widget.InputViewSensitiveLinearLayout) view;
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
    view = finder.findById(source, 2131427407);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427407' for field 'mCommentOperateLo' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mCommentOperateLo = (android.widget.RelativeLayout) view;
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
  }

  public static void reset(com.cplatform.xhxw.ui.ui.detailpage.CommentActivity target) {
    target.linearLayout = null;
    target.listView = null;
    target.commentEdittext = null;
    target.sendbtn = null;
    target.editText = null;
    target.inputLayout = null;
    target.mRootContainer = null;
    target.mExpressionWidgt = null;
    target.mExprBtn = null;
    target.mCommentOperateLo = null;
  }
}
