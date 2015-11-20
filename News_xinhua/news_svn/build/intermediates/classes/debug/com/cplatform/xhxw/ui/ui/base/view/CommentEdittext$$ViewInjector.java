// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class CommentEdittext$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.base.view.CommentEdittext target, Object source) {
    View view;
    view = finder.findById(source, 2131428008);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428008' for method 'send' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.send();
        }
      });
  }

  public static void reset(com.cplatform.xhxw.ui.ui.base.view.CommentEdittext target) {
  }
}
