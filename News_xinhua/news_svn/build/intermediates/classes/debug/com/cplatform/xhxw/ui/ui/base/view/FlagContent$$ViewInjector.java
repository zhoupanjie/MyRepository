// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class FlagContent$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.base.view.FlagContent target, Object source) {
    View view;
    view = finder.findById(source, 2131427669);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427669' for field 'mAvatar' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mAvatar = (android.widget.ImageView) view;
    view = finder.findById(source, 2131427670);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427670' for field 'mNickName' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mNickName = (android.widget.TextView) view;
    view = finder.findById(source, 2131428305);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428305' for field 'mDisModelIcon' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mDisModelIcon = (android.widget.ImageView) view;
    view = finder.findById(source, 2131428306);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428306' for field 'mDisModelText' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mDisModelText = (android.widget.TextView) view;
    view = finder.findById(source, 2131428040);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428040' for field 'progressBar' and method 'flagOfflinedownloadStopAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.progressBar = (com.cplatform.xhxw.ui.ui.base.widget.CircleProgressBar) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.flagOfflinedownloadStopAction();
        }
      });
    view = finder.findById(source, 2131427900);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427900' for field 'mMsgNew' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mMsgNew = (android.widget.ImageView) view;
    view = finder.findById(source, 2131428304);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428304' for field 'llDisplayModel' and method 'chengeDisplayModelAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.llDisplayModel = (android.widget.LinearLayout) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.chengeDisplayModelAction();
        }
      });
    view = finder.findById(source, 2131428307);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131428307' for field 'llDownList' and method 'openGameDownList' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.llDownList = (android.widget.LinearLayout) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.openGameDownList();
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
  }

  public static void reset(com.cplatform.xhxw.ui.ui.base.view.FlagContent target) {
    target.mAvatar = null;
    target.mNickName = null;
    target.mDisModelIcon = null;
    target.mDisModelText = null;
    target.progressBar = null;
    target.mMsgNew = null;
    target.llDisplayModel = null;
    target.llDownList = null;
  }
}
