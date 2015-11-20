// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SpecialTopicTextNewItem$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.base.view.SpecialTopicTextNewItem target, Object source) {
    View view;
    view = finder.findById(source, 2131428363);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428363' for field 'mHeader' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mHeader = (android.widget.TextView) view;
    view = finder.findById(source, 2131427594);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427594' for field 'mTitle' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mTitle = (android.widget.TextView) view;
    view = finder.findById(source, 2131427574);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427574' for field 'mDesc' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mDesc = (android.widget.TextView) view;
    view = finder.findById(source, 2131428005);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428005' for field 'mAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mAction = (android.widget.ImageView) view;
    view = finder.findById(source, 2131428326);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428326' for field 'mComment' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mComment = (android.widget.TextView) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.base.view.SpecialTopicTextNewItem target) {
    target.mHeader = null;
    target.mTitle = null;
    target.mDesc = null;
    target.mAction = null;
    target.mComment = null;
  }
}
