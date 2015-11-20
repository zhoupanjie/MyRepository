// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FriendsFreshInfoActivity$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.main.saas.addressBook.FriendsFreshInfoActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427462, "field 'mWebView'");
    target.mWebView = finder.castView(view, 2131427462, "field 'mWebView'");
    view = finder.findRequiredView(source, 2131427408, "field 'editText'");
    target.editText = finder.castView(view, 2131427408, "field 'editText'");
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
    view = finder.findRequiredView(source, 2131427375, "field 'mRootContainer'");
    target.mRootContainer = finder.castView(view, 2131427375, "field 'mRootContainer'");
    view = finder.findRequiredView(source, 2131427407, "field 'mCommentOperateLo'");
    target.mCommentOperateLo = finder.castView(view, 2131427407, "field 'mCommentOperateLo'");
    view = finder.findRequiredView(source, 2131427463, "field 'mToolBar'");
    target.mToolBar = view;
    view = finder.findRequiredView(source, 2131427409, "method 'senAction'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.senAction();
        }
      });
  }

  @Override public void unbind(T target) {
    target.mWebView = null;
    target.editText = null;
    target.mExpressionWidgt = null;
    target.mExprBtn = null;
    target.mRootContainer = null;
    target.mCommentOperateLo = null;
    target.mToolBar = null;
  }
}
