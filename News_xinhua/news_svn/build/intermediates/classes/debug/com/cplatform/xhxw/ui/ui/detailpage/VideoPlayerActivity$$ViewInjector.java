// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.detailpage;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class VideoPlayerActivity$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.detailpage.VideoPlayerActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131427570);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427570' for field 'mDefView' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mDefView = (com.cplatform.xhxw.ui.ui.base.widget.DefaultView) view;
    view = finder.findById(source, 2131427714);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427714' for field 'mVideoView' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mVideoView = (android.widget.VideoView) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.detailpage.VideoPlayerActivity target) {
    target.mDefView = null;
    target.mVideoView = null;
  }
}
