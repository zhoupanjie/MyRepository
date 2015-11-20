// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class RecommendImages$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.base.view.RecommendImages> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131428043, "field 'linearFirst'");
    target.linearFirst = finder.castView(view, 2131428043, "field 'linearFirst'");
    view = finder.findRequiredView(source, 2131428046, "field 'linearSecond'");
    target.linearSecond = finder.castView(view, 2131428046, "field 'linearSecond'");
    view = finder.findRequiredView(source, 2131428049, "field 'linearThird'");
    target.linearThird = finder.castView(view, 2131428049, "field 'linearThird'");
    view = finder.findRequiredView(source, 2131428052, "field 'linearForth'");
    target.linearForth = finder.castView(view, 2131428052, "field 'linearForth'");
    view = finder.findRequiredView(source, 2131428055, "field 'imageFirst' and method 'first'");
    target.imageFirst = finder.castView(view, 2131428055, "field 'imageFirst'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.first();
        }
      });
    view = finder.findRequiredView(source, 2131428056, "field 'imageSecond' and method 'second'");
    target.imageSecond = finder.castView(view, 2131428056, "field 'imageSecond'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.second();
        }
      });
    view = finder.findRequiredView(source, 2131428057, "field 'imageThird' and method 'third'");
    target.imageThird = finder.castView(view, 2131428057, "field 'imageThird'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.third();
        }
      });
    view = finder.findRequiredView(source, 2131428058, "field 'imageForth' and method 'forth'");
    target.imageForth = finder.castView(view, 2131428058, "field 'imageForth'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.forth();
        }
      });
    view = finder.findRequiredView(source, 2131428045, "field 'textFirst'");
    target.textFirst = finder.castView(view, 2131428045, "field 'textFirst'");
    view = finder.findRequiredView(source, 2131428048, "field 'textSecond'");
    target.textSecond = finder.castView(view, 2131428048, "field 'textSecond'");
    view = finder.findRequiredView(source, 2131428051, "field 'textThird'");
    target.textThird = finder.castView(view, 2131428051, "field 'textThird'");
    view = finder.findRequiredView(source, 2131428054, "field 'textForth'");
    target.textForth = finder.castView(view, 2131428054, "field 'textForth'");
  }

  @Override public void unbind(T target) {
    target.linearFirst = null;
    target.linearSecond = null;
    target.linearThird = null;
    target.linearForth = null;
    target.imageFirst = null;
    target.imageSecond = null;
    target.imageThird = null;
    target.imageForth = null;
    target.textFirst = null;
    target.textSecond = null;
    target.textThird = null;
    target.textForth = null;
  }
}
