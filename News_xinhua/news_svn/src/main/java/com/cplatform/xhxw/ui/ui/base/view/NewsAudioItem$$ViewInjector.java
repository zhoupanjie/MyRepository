// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class NewsAudioItem$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.base.view.NewsAudioItem target, Object source) {
    View view;
    view = finder.findById(source, 2131493130);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493130' for field 'mTitle' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mTitle = (android.widget.TextView) view;
    view = finder.findById(source, 2131493866);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493866' for field 'mComment' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mComment = (android.widget.TextView) view;
    view = finder.findById(source, 2131493110);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493110' for field 'mDesc' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mDesc = (android.widget.TextView) view;
    view = finder.findById(source, 2131493539);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493539' for field 'mImg' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mImg = (android.widget.ImageView) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.base.view.NewsAudioItem target) {
    target.mTitle = null;
    target.mComment = null;
    target.mDesc = null;
    target.mImg = null;
  }
}
