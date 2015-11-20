// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class FlagContent$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.base.view.FlagContent> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427669, "field 'mAvatar'");
    target.mAvatar = finder.castView(view, 2131427669, "field 'mAvatar'");
    view = finder.findRequiredView(source, 2131427670, "field 'mNickName'");
    target.mNickName = finder.castView(view, 2131427670, "field 'mNickName'");
    view = finder.findRequiredView(source, 2131428305, "field 'mDisModelIcon'");
    target.mDisModelIcon = finder.castView(view, 2131428305, "field 'mDisModelIcon'");
    view = finder.findRequiredView(source, 2131428306, "field 'mDisModelText'");
    target.mDisModelText = finder.castView(view, 2131428306, "field 'mDisModelText'");
    view = finder.findRequiredView(source, 2131428040, "field 'progressBar' and method 'flagOfflinedownloadStopAction'");
    target.progressBar = finder.castView(view, 2131428040, "field 'progressBar'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.flagOfflinedownloadStopAction();
        }
      });
    view = finder.findRequiredView(source, 2131427900, "field 'mMsgNew'");
    target.mMsgNew = finder.castView(view, 2131427900, "field 'mMsgNew'");
    view = finder.findRequiredView(source, 2131428304, "field 'llDisplayModel' and method 'chengeDisplayModelAction'");
    target.llDisplayModel = finder.castView(view, 2131428304, "field 'llDisplayModel'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.chengeDisplayModelAction();
        }
      });
    view = finder.findRequiredView(source, 2131428307, "field 'llDownList' and method 'openGameDownList'");
    target.llDownList = finder.castView(view, 2131428307, "field 'llDownList'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.openGameDownList();
        }
      });
    view = finder.findRequiredView(source, 2131428300, "method 'flagAlertAction'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.flagAlertAction();
        }
      });
    view = finder.findRequiredView(source, 2131428302, "method 'flagCommentAction'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.flagCommentAction();
        }
      });
    view = finder.findRequiredView(source, 2131428311, "method 'flagSearchAction'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.flagSearchAction();
        }
      });
    view = finder.findRequiredView(source, 2131428310, "method 'flagInviteAction'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.flagInviteAction();
        }
      });
    view = finder.findRequiredView(source, 2131428301, "method 'flagCollectAction'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.flagCollectAction();
        }
      });
    view = finder.findRequiredView(source, 2131428303, "method 'flagSettingAction'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.flagSettingAction();
        }
      });
    view = finder.findRequiredView(source, 2131428312, "method 'flagOfflinedownloadAction'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.flagOfflinedownloadAction();
        }
      });
  }

  @Override public void unbind(T target) {
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
