// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SpecialTopicTextNewItem$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.base.view.SpecialTopicTextNewItem> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131428363, "field 'mHeader'");
    target.mHeader = finder.castView(view, 2131428363, "field 'mHeader'");
    view = finder.findRequiredView(source, 2131427594, "field 'mTitle'");
    target.mTitle = finder.castView(view, 2131427594, "field 'mTitle'");
    view = finder.findRequiredView(source, 2131427574, "field 'mDesc'");
    target.mDesc = finder.castView(view, 2131427574, "field 'mDesc'");
    view = finder.findRequiredView(source, 2131428005, "field 'mAction'");
    target.mAction = finder.castView(view, 2131428005, "field 'mAction'");
    view = finder.findRequiredView(source, 2131428326, "field 'mComment'");
    target.mComment = finder.castView(view, 2131428326, "field 'mComment'");
  }

  @Override public void unbind(T target) {
    target.mHeader = null;
    target.mTitle = null;
    target.mDesc = null;
    target.mAction = null;
    target.mComment = null;
  }
}
