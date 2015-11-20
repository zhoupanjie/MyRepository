// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class VideoNewItem$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.base.view.VideoNewItem target, Object source) {
    View view;
    view = finder.findById(source, 2131428257);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428257' for field 'videoLayout' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.videoLayout = (android.widget.RelativeLayout) view;
    view = finder.findById(source, 2131428258);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428258' for field 'videoImage' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.videoImage = (android.widget.ImageView) view;
    view = finder.findById(source, 2131428259);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428259' for field 'videoNameText' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.videoNameText = (android.widget.TextView) view;
    view = finder.findById(source, 2131428260);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428260' for field 'videoPlayBtn' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.videoPlayBtn = (android.widget.ImageView) view;
    view = finder.findById(source, 2131428261);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428261' for field 'videoVideoLayout' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.videoVideoLayout = (android.widget.LinearLayout) view;
    view = finder.findById(source, 2131428265);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428265' for field 'videoRecommendLayout' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.videoRecommendLayout = (android.widget.LinearLayout) view;
    view = finder.findById(source, 2131428263);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428263' for field 'videoCommentBtn' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.videoCommentBtn = (android.widget.LinearLayout) view;
    view = finder.findById(source, 2131428264);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428264' for field 'videoShareBtn' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.videoShareBtn = (android.widget.LinearLayout) view;
    view = finder.findById(source, 2131428266);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428266' for field 'videoSourceTv' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.videoSourceTv = (android.widget.TextView) view;
    view = finder.findById(source, 2131428267);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428267' for field 'videoPublishedTimeTv' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.videoPublishedTimeTv = (android.widget.TextView) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.base.view.VideoNewItem target) {
    target.videoLayout = null;
    target.videoImage = null;
    target.videoNameText = null;
    target.videoPlayBtn = null;
    target.videoVideoLayout = null;
    target.videoRecommendLayout = null;
    target.videoCommentBtn = null;
    target.videoShareBtn = null;
    target.videoSourceTv = null;
    target.videoPublishedTimeTv = null;
  }
}
