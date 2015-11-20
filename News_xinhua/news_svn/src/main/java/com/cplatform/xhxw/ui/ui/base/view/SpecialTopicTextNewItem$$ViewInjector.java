// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SpecialTopicTextNewItem$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.base.view.SpecialTopicTextNewItem target, Object source) {
    View view;
    view = finder.findById(source, 2131493899);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493899' for field 'mHeader' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mHeader = (android.widget.TextView) view;
    view = finder.findById(source, 2131493541);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493541' for field 'mAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mAction = (android.widget.ImageView) view;
    view = finder.findById(source, 2131493862);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493862' for field 'mComment' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mComment = (android.widget.TextView) view;
    view = finder.findById(source, 2131493130);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493130' for field 'mTitle' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mTitle = (android.widget.TextView) view;
    view = finder.findById(source, 2131493110);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493110' for field 'mDesc' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mDesc = (android.widget.TextView) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.base.view.SpecialTopicTextNewItem target) {
    target.mHeader = null;
    target.mAction = null;
    target.mComment = null;
    target.mTitle = null;
    target.mDesc = null;
  }
}
