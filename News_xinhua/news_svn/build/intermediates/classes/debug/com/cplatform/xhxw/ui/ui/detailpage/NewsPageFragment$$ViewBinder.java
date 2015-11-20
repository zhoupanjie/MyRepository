// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.detailpage;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class NewsPageFragment$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.detailpage.NewsPageFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427596, "field 'mWebView'");
    target.mWebView = finder.castView(view, 2131427596, "field 'mWebView'");
    view = finder.findRequiredView(source, 2131427570, "field 'mDefView'");
    target.mDefView = finder.castView(view, 2131427570, "field 'mDefView'");
    view = finder.findRequiredView(source, 2131427874, "field 'bottomMediaplayer'");
    target.bottomMediaplayer = finder.castView(view, 2131427874, "field 'bottomMediaplayer'");
    view = finder.findRequiredView(source, 2131427578, "field 'mShare' and method 'onShareAction'");
    target.mShare = finder.castView(view, 2131427578, "field 'mShare'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onShareAction();
        }
      });
  }

  @Override public void unbind(T target) {
    target.mWebView = null;
    target.mDefView = null;
    target.bottomMediaplayer = null;
    target.mShare = null;
  }
}
