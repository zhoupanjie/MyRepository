// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.detailpage;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class VideoPlayerActivity$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.detailpage.VideoPlayerActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427570, "field 'mDefView'");
    target.mDefView = finder.castView(view, 2131427570, "field 'mDefView'");
    view = finder.findRequiredView(source, 2131427714, "field 'mVideoView'");
    target.mVideoView = finder.castView(view, 2131427714, "field 'mVideoView'");
  }

  @Override public void unbind(T target) {
    target.mDefView = null;
    target.mVideoView = null;
  }
}
