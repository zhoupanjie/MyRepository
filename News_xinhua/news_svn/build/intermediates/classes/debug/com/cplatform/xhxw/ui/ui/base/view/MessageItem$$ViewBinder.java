// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class MessageItem$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.base.view.MessageItem> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131428319, "field 'mImg'");
    target.mImg = finder.castView(view, 2131428319, "field 'mImg'");
    view = finder.findRequiredView(source, 2131428320, "field 'mTitle'");
    target.mTitle = finder.castView(view, 2131428320, "field 'mTitle'");
    view = finder.findRequiredView(source, 2131428321, "field 'mDesc'");
    target.mDesc = finder.castView(view, 2131428321, "field 'mDesc'");
    view = finder.findRequiredView(source, 2131428318, "field 'mTag'");
    target.mTag = finder.castView(view, 2131428318, "field 'mTag'");
    view = finder.findRequiredView(source, 2131428322, "field 'mPublishDate'");
    target.mPublishDate = finder.castView(view, 2131428322, "field 'mPublishDate'");
  }

  @Override public void unbind(T target) {
    target.mImg = null;
    target.mTitle = null;
    target.mDesc = null;
    target.mTag = null;
    target.mPublishDate = null;
  }
}
