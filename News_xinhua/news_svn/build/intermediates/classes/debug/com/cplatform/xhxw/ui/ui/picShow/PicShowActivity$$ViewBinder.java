// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.picShow;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class PicShowActivity$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.picShow.PicShowActivity> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427569, "field 'mVp'");
    target.mVp = finder.castView(view, 2131427569, "field 'mVp'");
    view = finder.findRequiredView(source, 2131427576, "field 'mNum'");
    target.mNum = finder.castView(view, 2131427576, "field 'mNum'");
    view = finder.findRequiredView(source, 2131427573, "field 'mDescScrollView'");
    target.mDescScrollView = finder.castView(view, 2131427573, "field 'mDescScrollView'");
    view = finder.findRequiredView(source, 2131427574, "field 'mDesc'");
    target.mDesc = finder.castView(view, 2131427574, "field 'mDesc'");
    view = finder.findRequiredView(source, 2131427579, "field 'mFavoriteBtn' and method 'onFavoriteAction'");
    target.mFavoriteBtn = finder.castView(view, 2131427579, "field 'mFavoriteBtn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onFavoriteAction(p0);
        }
      });
    view = finder.findRequiredView(source, 2131427570, "field 'mDefView'");
    target.mDefView = finder.castView(view, 2131427570, "field 'mDefView'");
    view = finder.findRequiredView(source, 2131427571, "field 'mComment' and method 'goImageComment'");
    target.mComment = finder.castView(view, 2131427571, "field 'mComment'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.goImageComment(p0);
        }
      });
    view = finder.findRequiredView(source, 2131427355, "field 'titleView'");
    target.titleView = view;
    view = finder.findRequiredView(source, 2131427572, "field 'optionView'");
    target.optionView = view;
    view = finder.findRequiredView(source, 2131427577, "method 'onDownAction'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onDownAction(p0);
        }
      });
    view = finder.findRequiredView(source, 2131427578, "method 'onShareAction'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.onShareAction(p0);
        }
      });
  }

  @Override public void unbind(T target) {
    target.mVp = null;
    target.mNum = null;
    target.mDescScrollView = null;
    target.mDesc = null;
    target.mFavoriteBtn = null;
    target.mDefView = null;
    target.mComment = null;
    target.titleView = null;
    target.optionView = null;
  }
}
