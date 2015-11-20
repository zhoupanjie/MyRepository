// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class CommunityCommentaryFragment$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.main.saas.addressBook.CommunityCommentaryFragment target, Object source) {
    View view;
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
    view = finder.findById(source, 2131427410);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427410' for method 'onClickExprBtn' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClickExprBtn();
        }
      });
  }

  public static void reset(com.cplatform.xhxw.ui.ui.main.saas.addressBook.CommunityCommentaryFragment target) {
  }
}
