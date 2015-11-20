// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.picShow;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class PicShowActivity$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.picShow.PicShowActivity target, Object source) {
    View view;
    view = finder.findById(source, 2131493106);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493106' for field 'mDefView' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mDefView = (com.cplatform.xhxw.ui.ui.base.widget.DefaultView) view;
    view = finder.findById(source, 2131492891);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131492891' for field 'titleView' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.titleView = view;
    view = finder.findById(source, 2131493115);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493115' for field 'mFavoriteBtn' and method 'onFavoriteAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mFavoriteBtn = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onFavoriteAction(p0);
        }
      });
    view = finder.findById(source, 2131493108);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493108' for field 'optionView' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.optionView = view;
    view = finder.findById(source, 2131493107);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493107' for field 'mComment' and method 'goImageComment' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mComment = (android.widget.Button) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.goImageComment(p0);
        }
      });
    view = finder.findById(source, 2131493110);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493110' for field 'mDesc' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mDesc = (android.widget.TextView) view;
    view = finder.findById(source, 2131493112);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493112' for field 'mNum' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mNum = (android.widget.TextView) view;
    view = finder.findById(source, 2131493105);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493105' for field 'mVp' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mVp = (com.cplatform.xhxw.ui.ui.picShow.HackyViewPager) view;
    view = finder.findById(source, 2131493109);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493109' for field 'mDescScrollView' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mDescScrollView = (android.widget.ScrollView) view;
    view = finder.findById(source, 2131493114);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493114' for method 'onShareAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onShareAction(p0);
        }
      });
    view = finder.findById(source, 2131493113);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493113' for method 'onDownAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onDownAction(p0);
        }
      });
  }

  public static void reset(com.cplatform.xhxw.ui.ui.picShow.PicShowActivity target) {
    target.mDefView = null;
    target.titleView = null;
    target.mFavoriteBtn = null;
    target.optionView = null;
    target.mComment = null;
    target.mDesc = null;
    target.mNum = null;
    target.mVp = null;
    target.mDescScrollView = null;
  }
}
