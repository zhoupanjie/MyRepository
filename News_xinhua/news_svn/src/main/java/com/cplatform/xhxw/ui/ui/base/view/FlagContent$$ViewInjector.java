// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class FlagContent$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.base.view.FlagContent target, Object source) {
    View view;
    view = finder.findById(source, 2131493436);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493436' for field 'mMsgNew' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mMsgNew = (android.widget.ImageView) view;
    view = finder.findById(source, 2131493206);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493206' for field 'mNickName' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mNickName = (android.widget.TextView) view;
    view = finder.findById(source, 2131493841);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493841' for field 'mDisModelIcon' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mDisModelIcon = (android.widget.ImageView) view;
    view = finder.findById(source, 2131493576);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493576' for field 'progressBar' and method 'flagOfflinedownloadStopAction' was not found. If this view is optional add '@Optional' annotation.");
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
    view = finder.findById(source, 2131493840);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493840' for field 'llDisplayModel' and method 'chengeDisplayModelAction' was not found. If this view is optional add '@Optional' annotation.");
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
    view = finder.findById(source, 2131493205);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493205' for field 'mAvatar' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mAvatar = (android.widget.ImageView) view;
    view = finder.findById(source, 2131493842);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493842' for field 'mDisModelText' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mDisModelText = (android.widget.TextView) view;
    view = finder.findById(source, 2131493843);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493843' for field 'llDownList' and method 'openGameDownList' was not found. If this view is optional add '@Optional' annotation.");
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
    view = finder.findById(source, 2131493836);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493836' for method 'flagAlertAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.flagAlertAction();
        }
      });
    view = finder.findById(source, 2131493837);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493837' for method 'flagCollectAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.flagCollectAction();
        }
      });
    view = finder.findById(source, 2131493838);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493838' for method 'flagCommentAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.flagCommentAction();
        }
      });
    view = finder.findById(source, 2131493847);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493847' for method 'flagSearchAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.flagSearchAction();
        }
      });
    view = finder.findById(source, 2131493848);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493848' for method 'flagOfflinedownloadAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.flagOfflinedownloadAction();
        }
      });
    view = finder.findById(source, 2131493846);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493846' for method 'flagInviteAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.flagInviteAction();
        }
      });
    view = finder.findById(source, 2131493839);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493839' for method 'flagSettingAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.flagSettingAction();
        }
      });
  }

  public static void reset(com.cplatform.xhxw.ui.ui.base.view.FlagContent target) {
    target.mMsgNew = null;
    target.mNickName = null;
    target.mDisModelIcon = null;
    target.progressBar = null;
    target.llDisplayModel = null;
    target.mAvatar = null;
    target.mDisModelText = null;
    target.llDownList = null;
  }
}
