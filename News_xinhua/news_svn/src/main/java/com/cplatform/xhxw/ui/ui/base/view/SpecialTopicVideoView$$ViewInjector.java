// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class SpecialTopicVideoView$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.base.view.SpecialTopicVideoView target, Object source) {
    View view;
    view = finder.findById(source, 2131493110);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493110' for field 'mDesc' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mDesc = (android.widget.TextView) view;
    view = finder.findById(source, 2131493539);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493539' for field 'mImg' and method 'onImgClickAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mImg = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onImgClickAction();
        }
      });
  }

  public static void reset(com.cplatform.xhxw.ui.ui.base.view.SpecialTopicVideoView target) {
    target.mDesc = null;
    target.mImg = null;
  }
}
