// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.cms.personal;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class PersonalFragment$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.main.cms.personal.PersonalFragment> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427669, "field 'mAvatar' and method 'personalLogin'");
    target.mAvatar = finder.castView(view, 2131427669, "field 'mAvatar'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.personalLogin();
        }
      });
    view = finder.findRequiredView(source, 2131427670, "field 'mNickName'");
    target.mNickName = finder.castView(view, 2131427670, "field 'mNickName'");
    view = finder.findRequiredView(source, 2131427881, "field 'mRegLoginLo'");
    target.mRegLoginLo = finder.castView(view, 2131427881, "field 'mRegLoginLo'");
    view = finder.findRequiredView(source, 2131427882, "field 'mRegBtn' and method 'personalReg'");
    target.mRegBtn = finder.castView(view, 2131427882, "field 'mRegBtn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.personalReg();
        }
      });
    view = finder.findRequiredView(source, 2131427884, "field 'mLoginBtn' and method 'personalLog'");
    target.mLoginBtn = finder.castView(view, 2131427884, "field 'mLoginBtn'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.personalLog();
        }
      });
    view = finder.findRequiredView(source, 2131427900, "field 'mMsgNew'");
    target.mMsgNew = finder.castView(view, 2131427900, "field 'mMsgNew'");
    view = finder.findRequiredView(source, 2131427903, "field 'ivNewMyInfo'");
    target.ivNewMyInfo = finder.castView(view, 2131427903, "field 'ivNewMyInfo'");
    view = finder.findRequiredView(source, 2131427901, "method 'personalMyInfo'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.personalMyInfo();
        }
      });
    view = finder.findRequiredView(source, 2131427914, "method 'personalSetting'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.personalSetting();
        }
      });
    view = finder.findRequiredView(source, 2131427916, "method 'openGameDownList'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.openGameDownList();
        }
      });
    view = finder.findRequiredView(source, 2131427910, "method 'personalSwitchLanguage'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.personalSwitchLanguage();
        }
      });
    view = finder.findRequiredView(source, 2131427898, "method 'personalAlertAction'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.personalAlertAction();
        }
      });
    view = finder.findRequiredView(source, 2131427906, "method 'personalCommentAction'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.personalCommentAction();
        }
      });
    view = finder.findRequiredView(source, 2131427908, "method 'personalSearchAction'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.personalSearchAction();
        }
      });
    view = finder.findRequiredView(source, 2131427904, "method 'personalCollectAction'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.personalCollectAction();
        }
      });
    view = finder.findRequiredView(source, 2131427912, "method 'personalSettingAction'");
    view.setOnClickListener(
      new butterknife.internal.DebouncingOnClickListener() {
        @Override public void doClick(
          android.view.View p0
        ) {
          target.personalSettingAction();
        }
      });
  }

  @Override public void unbind(T target) {
    target.mAvatar = null;
    target.mNickName = null;
    target.mRegLoginLo = null;
    target.mRegBtn = null;
    target.mLoginBtn = null;
    target.mMsgNew = null;
    target.ivNewMyInfo = null;
  }
}
