// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class FlagContent$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.base.view.FlagContent target, Object source) {
    View view;
    view = finder.findById(source, 2131428307);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428307' for method 'openGameDownList' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.openGameDownList();
        }
      });
    view = finder.findById(source, 2131428304);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428304' for method 'chengeDisplayModelAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.chengeDisplayModelAction();
        }
      });
    view = finder.findById(source, 2131428300);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428300' for method 'flagAlertAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.flagAlertAction();
        }
      });
    view = finder.findById(source, 2131428302);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428302' for method 'flagCommentAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.flagCommentAction();
        }
      });
    view = finder.findById(source, 2131428311);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428311' for method 'flagSearchAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.flagSearchAction();
        }
      });
    view = finder.findById(source, 2131428310);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428310' for method 'flagInviteAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.flagInviteAction();
        }
      });
    view = finder.findById(source, 2131428301);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428301' for method 'flagCollectAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.flagCollectAction();
        }
      });
    view = finder.findById(source, 2131428303);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428303' for method 'flagSettingAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.flagSettingAction();
        }
      });
    view = finder.findById(source, 2131428312);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428312' for method 'flagOfflinedownloadAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.flagOfflinedownloadAction();
        }
      });
    view = finder.findById(source, 2131428040);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428040' for method 'flagOfflinedownloadStopAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.flagOfflinedownloadStopAction();
        }
      });
  }

  public static void reset(com.cplatform.xhxw.ui.ui.base.view.FlagContent target) {
  }
}
