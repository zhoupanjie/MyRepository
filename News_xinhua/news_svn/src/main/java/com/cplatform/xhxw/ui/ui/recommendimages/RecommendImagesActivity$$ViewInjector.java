// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.recommendimages;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class RecommendImagesActivity$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.recommendimages.RecommendImagesActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131493578);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493578' for field 'recommendImages' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.recommendImages = (com.cplatform.xhxw.ui.ui.base.view.RecommendImages) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.recommendimages.RecommendImagesActivity target) {
    target.recommendImages = null;
  }
}
