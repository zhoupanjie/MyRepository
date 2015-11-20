// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class NewsFunctionRecommend$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.base.view.NewsFunctionRecommend target, Object source) {
    View view;
    view = finder.findById(source, 2131493867);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493867' for field 'mAimgs' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mAimgs = view;
    view = finder.findById(source, 2131493870);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493870' for field 'mImg3' and method 'img3OnClickAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mImg3 = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.img3OnClickAction(p0);
        }
      });
    view = finder.findById(source, 2131493868);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493868' for field 'mImg1' and method 'img1OnClickAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mImg1 = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.img1OnClickAction(p0);
        }
      });
    view = finder.findById(source, 2131493869);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493869' for field 'mImg2' and method 'img2OnClickAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mImg2 = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.img2OnClickAction(p0);
        }
      });
    view = finder.findById(source, 2131493871);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493871' for field 'mImg4' and method 'img4OnClickAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mImg4 = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.img4OnClickAction(p0);
        }
      });
  }

  public static void reset(com.cplatform.xhxw.ui.ui.base.view.NewsFunctionRecommend target) {
    target.mAimgs = null;
    target.mImg3 = null;
    target.mImg1 = null;
    target.mImg2 = null;
    target.mImg4 = null;
  }
}
