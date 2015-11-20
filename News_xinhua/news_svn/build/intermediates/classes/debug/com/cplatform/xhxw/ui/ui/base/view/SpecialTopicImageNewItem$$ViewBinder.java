// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SpecialTopicImageNewItem$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.base.view.SpecialTopicImageNewItem> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427358, "field 'imageView1'");
    target.imageView1 = finder.castView(view, 2131427358, "field 'imageView1'");
    view = finder.findRequiredView(source, 2131427894, "field 'imageView2'");
    target.imageView2 = finder.castView(view, 2131427894, "field 'imageView2'");
    view = finder.findRequiredView(source, 2131428360, "field 'imageView3'");
    target.imageView3 = finder.castView(view, 2131428360, "field 'imageView3'");
    view = finder.findRequiredView(source, 2131428361, "field 'imageView4'");
    target.imageView4 = finder.castView(view, 2131428361, "field 'imageView4'");
    view = finder.findRequiredView(source, 2131428362, "field 'imageView5'");
    target.imageView5 = finder.castView(view, 2131428362, "field 'imageView5'");
    view = finder.findRequiredView(source, 2131428148, "field 'text'");
    target.text = finder.castView(view, 2131428148, "field 'text'");
  }

  @Override public void unbind(T target) {
    target.imageView1 = null;
    target.imageView2 = null;
    target.imageView3 = null;
    target.imageView4 = null;
    target.imageView5 = null;
    target.text = null;
  }
}
