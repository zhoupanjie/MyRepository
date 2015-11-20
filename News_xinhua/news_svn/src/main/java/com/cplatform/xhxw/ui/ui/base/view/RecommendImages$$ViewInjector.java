// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class RecommendImages$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.base.view.RecommendImages target, Object source) {
    View view;
    view = finder.findById(source, 2131493585);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493585' for field 'linearThird' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.linearThird = (android.widget.LinearLayout) view;
    view = finder.findById(source, 2131493579);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493579' for field 'linearFirst' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.linearFirst = (android.widget.LinearLayout) view;
    view = finder.findById(source, 2131493594);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493594' for field 'imageForth' and method 'forth' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.imageForth = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.forth();
        }
      });
    view = finder.findById(source, 2131493587);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493587' for field 'textThird' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.textThird = (android.widget.TextView) view;
    view = finder.findById(source, 2131493584);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493584' for field 'textSecond' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.textSecond = (android.widget.TextView) view;
    view = finder.findById(source, 2131493582);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493582' for field 'linearSecond' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.linearSecond = (android.widget.LinearLayout) view;
    view = finder.findById(source, 2131493591);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493591' for field 'imageFirst' and method 'first' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.imageFirst = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.first();
        }
      });
    view = finder.findById(source, 2131493590);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493590' for field 'textForth' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.textForth = (android.widget.TextView) view;
    view = finder.findById(source, 2131493593);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493593' for field 'imageThird' and method 'third' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.imageThird = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.third();
        }
      });
    view = finder.findById(source, 2131493581);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493581' for field 'textFirst' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.textFirst = (android.widget.TextView) view;
    view = finder.findById(source, 2131493588);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493588' for field 'linearForth' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.linearForth = (android.widget.LinearLayout) view;
    view = finder.findById(source, 2131493592);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493592' for field 'imageSecond' and method 'second' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.imageSecond = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.second();
        }
      });
  }

  public static void reset(com.cplatform.xhxw.ui.ui.base.view.RecommendImages target) {
    target.linearThird = null;
    target.linearFirst = null;
    target.imageForth = null;
    target.textThird = null;
    target.textSecond = null;
    target.linearSecond = null;
    target.imageFirst = null;
    target.textForth = null;
    target.imageThird = null;
    target.textFirst = null;
    target.linearForth = null;
    target.imageSecond = null;
  }
}
