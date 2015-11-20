// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class RecommendImages$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.base.view.RecommendImages target, Object source) {
    View view;
    view = finder.findById(source, 2131428043);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428043' for field 'linearFirst' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.linearFirst = (android.widget.LinearLayout) view;
    view = finder.findById(source, 2131428046);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428046' for field 'linearSecond' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.linearSecond = (android.widget.LinearLayout) view;
    view = finder.findById(source, 2131428049);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428049' for field 'linearThird' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.linearThird = (android.widget.LinearLayout) view;
    view = finder.findById(source, 2131428052);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428052' for field 'linearForth' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.linearForth = (android.widget.LinearLayout) view;
    view = finder.findById(source, 2131428055);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428055' for field 'imageFirst' and method 'first' was not found. If this view is optional add '@Optional' annotation.");
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
    view = finder.findById(source, 2131428056);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428056' for field 'imageSecond' and method 'second' was not found. If this view is optional add '@Optional' annotation.");
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
    view = finder.findById(source, 2131428057);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428057' for field 'imageThird' and method 'third' was not found. If this view is optional add '@Optional' annotation.");
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
    view = finder.findById(source, 2131428058);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428058' for field 'imageForth' and method 'forth' was not found. If this view is optional add '@Optional' annotation.");
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
    view = finder.findById(source, 2131428045);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428045' for field 'textFirst' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.textFirst = (android.widget.TextView) view;
    view = finder.findById(source, 2131428048);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428048' for field 'textSecond' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.textSecond = (android.widget.TextView) view;
    view = finder.findById(source, 2131428051);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428051' for field 'textThird' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.textThird = (android.widget.TextView) view;
    view = finder.findById(source, 2131428054);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428054' for field 'textForth' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.textForth = (android.widget.TextView) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.base.view.RecommendImages target) {
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
