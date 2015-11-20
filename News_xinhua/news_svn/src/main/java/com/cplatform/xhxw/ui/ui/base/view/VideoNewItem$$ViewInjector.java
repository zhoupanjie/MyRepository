// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class VideoNewItem$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.base.view.VideoNewItem target, Object source) {
    View view;
    view = finder.findById(source, 2131493793);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493793' for field 'videoLayout' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.videoLayout = (android.widget.RelativeLayout) view;
    view = finder.findById(source, 2131493797);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493797' for field 'videoVideoLayout' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.videoVideoLayout = (android.widget.LinearLayout) view;
    view = finder.findById(source, 2131493794);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493794' for field 'videoImage' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.videoImage = (android.widget.ImageView) view;
    view = finder.findById(source, 2131493796);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493796' for field 'videoPlayBtn' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.videoPlayBtn = (android.widget.ImageView) view;
    view = finder.findById(source, 2131493799);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493799' for field 'videoCommentBtn' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.videoCommentBtn = (android.widget.LinearLayout) view;
    view = finder.findById(source, 2131493801);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493801' for field 'videoRecommendLayout' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.videoRecommendLayout = (android.widget.LinearLayout) view;
    view = finder.findById(source, 2131493803);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493803' for field 'videoPublishedTimeTv' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.videoPublishedTimeTv = (android.widget.TextView) view;
    view = finder.findById(source, 2131493802);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493802' for field 'videoSourceTv' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.videoSourceTv = (android.widget.TextView) view;
    view = finder.findById(source, 2131493795);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493795' for field 'videoNameText' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.videoNameText = (android.widget.TextView) view;
    view = finder.findById(source, 2131493800);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493800' for field 'videoShareBtn' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.videoShareBtn = (android.widget.LinearLayout) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.base.view.VideoNewItem target) {
    target.videoLayout = null;
    target.videoVideoLayout = null;
    target.videoImage = null;
    target.videoPlayBtn = null;
    target.videoCommentBtn = null;
    target.videoRecommendLayout = null;
    target.videoPublishedTimeTv = null;
    target.videoSourceTv = null;
    target.videoNameText = null;
    target.videoShareBtn = null;
  }
}
