// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class NewsFunctionRecommend$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.base.view.NewsFunctionRecommend> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131428332, "field 'mImg1' and method 'img1OnClickAction'");
    target.mImg1 = finder.castView(view, 2131428332, "field 'mImg1'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.img1OnClickAction(p0);
        }
      });
    view = finder.findRequiredView(source, 2131428333, "field 'mImg2' and method 'img2OnClickAction'");
    target.mImg2 = finder.castView(view, 2131428333, "field 'mImg2'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.img2OnClickAction(p0);
        }
      });
    view = finder.findRequiredView(source, 2131428334, "field 'mImg3' and method 'img3OnClickAction'");
    target.mImg3 = finder.castView(view, 2131428334, "field 'mImg3'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.img3OnClickAction(p0);
        }
      });
    view = finder.findRequiredView(source, 2131428335, "field 'mImg4' and method 'img4OnClickAction'");
    target.mImg4 = finder.castView(view, 2131428335, "field 'mImg4'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.img4OnClickAction(p0);
        }
      });
    view = finder.findRequiredView(source, 2131428331, "field 'mAimgs'");
    target.mAimgs = view;
  }

  @Override public void unbind(T target) {
    target.mImg1 = null;
    target.mImg2 = null;
    target.mImg3 = null;
    target.mImg4 = null;
    target.mAimgs = null;
  }
}
