// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class NewsAudioItem$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.base.view.NewsAudioItem> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131428003, "field 'mImg'");
    target.mImg = finder.castView(view, 2131428003, "field 'mImg'");
    view = finder.findRequiredView(source, 2131427594, "field 'mTitle'");
    target.mTitle = finder.castView(view, 2131427594, "field 'mTitle'");
    view = finder.findRequiredView(source, 2131427574, "field 'mDesc'");
    target.mDesc = finder.castView(view, 2131427574, "field 'mDesc'");
    view = finder.findRequiredView(source, 2131428330, "field 'mComment'");
    target.mComment = finder.castView(view, 2131428330, "field 'mComment'");
  }

  @Override public void unbind(T target) {
    target.mImg = null;
    target.mTitle = null;
    target.mDesc = null;
    target.mComment = null;
  }
}
