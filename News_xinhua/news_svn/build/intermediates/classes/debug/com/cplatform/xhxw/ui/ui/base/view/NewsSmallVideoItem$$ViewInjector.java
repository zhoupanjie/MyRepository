// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class NewsSmallVideoItem$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.base.view.NewsSmallVideoItem target, Object source) {
    View view;
    view = finder.findById(source, 2131428003);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428003' for field 'mImg' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mImg = (android.widget.ImageView) view;
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
    view = finder.findById(source, 2131428002);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428002' for field 'mImgLo' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mImgLo = (android.widget.RelativeLayout) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.base.view.NewsSmallVideoItem target) {
    target.mImg = null;
    target.mTitle = null;
    target.mDesc = null;
    target.mImgLo = null;
  }
}
