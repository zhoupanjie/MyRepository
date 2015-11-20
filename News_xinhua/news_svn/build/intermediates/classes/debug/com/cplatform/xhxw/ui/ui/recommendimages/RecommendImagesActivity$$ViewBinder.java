// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.recommendimages;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class RecommendImagesActivity$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.recommendimages.RecommendImagesActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131428042, "field 'recommendImages'");
    target.recommendImages = finder.castView(view, 2131428042, "field 'recommendImages'");
  }

  @Override public void unbind(T target) {
    target.recommendImages = null;
  }
}
