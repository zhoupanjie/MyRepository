// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SpecialTopicImageNewItem$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.base.view.SpecialTopicImageNewItem target, Object source) {
    View view;
    view = finder.findById(source, 2131492894);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131492894' for field 'imageView1' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.imageView1 = (android.widget.ImageView) view;
    view = finder.findById(source, 2131493897);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493897' for field 'imageView4' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.imageView4 = (android.widget.ImageView) view;
    view = finder.findById(source, 2131493896);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493896' for field 'imageView3' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.imageView3 = (android.widget.ImageView) view;
    view = finder.findById(source, 2131493430);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493430' for field 'imageView2' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.imageView2 = (android.widget.ImageView) view;
    view = finder.findById(source, 2131493898);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493898' for field 'imageView5' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.imageView5 = (android.widget.ImageView) view;
    view = finder.findById(source, 2131493684);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493684' for field 'text' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.text = (android.widget.TextView) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.base.view.SpecialTopicImageNewItem target) {
    target.imageView1 = null;
    target.imageView4 = null;
    target.imageView3 = null;
    target.imageView2 = null;
    target.imageView5 = null;
    target.text = null;
  }
}
