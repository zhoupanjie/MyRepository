// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.detailpage;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class NewsPageFragment$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.detailpage.NewsPageFragment target, Object source) {
    View view;
    view = finder.findById(source, 2131493106);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493106' for field 'mDefView' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mDefView = (com.cplatform.xhxw.ui.ui.base.widget.DefaultView) view;
    view = finder.findById(source, 2131493132);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493132' for field 'mWebView' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mWebView = (com.cplatform.xhxw.ui.ui.base.widget.NeteaseWebView) view;
    view = finder.findById(source, 2131493410);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493410' for field 'bottomMediaplayer' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.bottomMediaplayer = (com.cplatform.xhxw.ui.ui.base.view.BottomMediaplayer) view;
    view = finder.findById(source, 2131493114);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493114' for field 'mShare' and method 'onShareAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mShare = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onShareAction();
        }
      });
  }

  public static void reset(com.cplatform.xhxw.ui.ui.detailpage.NewsPageFragment target) {
    target.mDefView = null;
    target.mWebView = null;
    target.bottomMediaplayer = null;
    target.mShare = null;
  }
}
