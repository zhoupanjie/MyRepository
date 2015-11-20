// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class CommentEdittext$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.base.view.CommentEdittext> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131428007, "field 'editText'");
    target.editText = finder.castView(view, 2131428007, "field 'editText'");
    view = finder.findRequiredView(source, 2131428008, "field 'button' and method 'send'");
    target.button = finder.castView(view, 2131428008, "field 'button'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.send();
        }
      });
  }

  @Override public void unbind(T target) {
    target.editText = null;
    target.button = null;
  }
}
