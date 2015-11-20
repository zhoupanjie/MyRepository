// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class MessageItem$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.base.view.MessageItem target, Object source) {
    View view;
    view = finder.findById(source, 2131428319);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428319' for field 'mImg' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mImg = (android.widget.ImageView) view;
    view = finder.findById(source, 2131428320);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428320' for field 'mTitle' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mTitle = (android.widget.TextView) view;
    view = finder.findById(source, 2131428321);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428321' for field 'mDesc' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mDesc = (android.widget.TextView) view;
    view = finder.findById(source, 2131428318);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428318' for field 'mTag' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mTag = (android.widget.TextView) view;
    view = finder.findById(source, 2131428322);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428322' for field 'mPublishDate' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mPublishDate = (android.widget.TextView) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.base.view.MessageItem target) {
    target.mImg = null;
    target.mTitle = null;
    target.mDesc = null;
    target.mTag = null;
    target.mPublishDate = null;
  }
}
