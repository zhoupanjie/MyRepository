// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class VideoNewItem$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.base.view.VideoNewItem> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131428257, "field 'videoLayout'");
    target.videoLayout = finder.castView(view, 2131428257, "field 'videoLayout'");
    view = finder.findRequiredView(source, 2131428258, "field 'videoImage'");
    target.videoImage = finder.castView(view, 2131428258, "field 'videoImage'");
    view = finder.findRequiredView(source, 2131428259, "field 'videoNameText'");
    target.videoNameText = finder.castView(view, 2131428259, "field 'videoNameText'");
    view = finder.findRequiredView(source, 2131428260, "field 'videoPlayBtn'");
    target.videoPlayBtn = finder.castView(view, 2131428260, "field 'videoPlayBtn'");
    view = finder.findRequiredView(source, 2131428261, "field 'videoVideoLayout'");
    target.videoVideoLayout = finder.castView(view, 2131428261, "field 'videoVideoLayout'");
    view = finder.findRequiredView(source, 2131428265, "field 'videoRecommendLayout'");
    target.videoRecommendLayout = finder.castView(view, 2131428265, "field 'videoRecommendLayout'");
    view = finder.findRequiredView(source, 2131428263, "field 'videoCommentBtn'");
    target.videoCommentBtn = finder.castView(view, 2131428263, "field 'videoCommentBtn'");
    view = finder.findRequiredView(source, 2131428264, "field 'videoShareBtn'");
    target.videoShareBtn = finder.castView(view, 2131428264, "field 'videoShareBtn'");
    view = finder.findRequiredView(source, 2131428266, "field 'videoSourceTv'");
    target.videoSourceTv = finder.castView(view, 2131428266, "field 'videoSourceTv'");
    view = finder.findRequiredView(source, 2131428267, "field 'videoPublishedTimeTv'");
    target.videoPublishedTimeTv = finder.castView(view, 2131428267, "field 'videoPublishedTimeTv'");
  }

  @Override public void unbind(T target) {
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
