// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.about;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class AboutActivity$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.about.AboutActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427359, "field 'nowLayout'");
    target.nowLayout = finder.castView(view, 2131427359, "field 'nowLayout'");
    view = finder.findRequiredView(source, 2131427360, "field 'nowVersion1'");
    target.nowVersion1 = finder.castView(view, 2131427360, "field 'nowVersion1'");
    view = finder.findRequiredView(source, 2131427361, "field 'newLayout'");
    target.newLayout = finder.castView(view, 2131427361, "field 'newLayout'");
    view = finder.findRequiredView(source, 2131427362, "field 'nowVersion2'");
    target.nowVersion2 = finder.castView(view, 2131427362, "field 'nowVersion2'");
    view = finder.findRequiredView(source, 2131427363, "field 'newVersion'");
    target.newVersion = finder.castView(view, 2131427363, "field 'newVersion'");
    view = finder.findRequiredView(source, 2131427364, "field 'updateBtn' and method 'update'");
    target.updateBtn = finder.castView(view, 2131427364, "field 'updateBtn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.update();
        }
      });
    view = finder.findRequiredView(source, 2131427356, "method 'goBack'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.goBack();
        }
      });
  }

  @Override public void unbind(T target) {
    target.nowLayout = null;
    target.nowVersion1 = null;
    target.newLayout = null;
    target.nowVersion2 = null;
    target.newVersion = null;
    target.updateBtn = null;
  }
}
