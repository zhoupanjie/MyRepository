// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.cms.personal;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class PersonalFragment$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.main.cms.personal.PersonalFragment target, Object source) {
    View view;
    view = finder.findById(source, 2131493417);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493417' for field 'mRegLoginLo' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mRegLoginLo = (android.widget.LinearLayout) view;
    view = finder.findById(source, 2131493418);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493418' for field 'mRegBtn' and method 'personalReg' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mRegBtn = (android.widget.TextView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.personalReg();
        }
      });
    view = finder.findById(source, 2131493420);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493420' for field 'mLoginBtn' and method 'personalLog' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mLoginBtn = (android.widget.TextView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.personalLog();
        }
      });
    view = finder.findById(source, 2131493436);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493436' for field 'mMsgNew' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mMsgNew = (android.widget.ImageView) view;
    view = finder.findById(source, 2131493439);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493439' for field 'ivNewMyInfo' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.ivNewMyInfo = (android.widget.ImageView) view;
    view = finder.findById(source, 2131493206);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493206' for field 'mNickName' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mNickName = (android.widget.TextView) view;
    view = finder.findById(source, 2131493205);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493205' for field 'mAvatar' and method 'personalLogin' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mAvatar = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.personalLogin();
        }
      });
    view = finder.findById(source, 2131493452);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493452' for method 'openGameDownList' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.openGameDownList();
        }
      });
    view = finder.findById(source, 2131493446);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493446' for method 'personalSwitchLanguage' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.personalSwitchLanguage();
        }
      });
    view = finder.findById(source, 2131493437);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493437' for method 'personalMyInfo' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.personalMyInfo();
        }
      });
    view = finder.findById(source, 2131493444);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493444' for method 'personalSearchAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.personalSearchAction();
        }
      });
    view = finder.findById(source, 2131493450);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493450' for method 'personalSetting' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.personalSetting();
        }
      });
    view = finder.findById(source, 2131493442);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493442' for method 'personalCommentAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.personalCommentAction();
        }
      });
    view = finder.findById(source, 2131493440);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493440' for method 'personalCollectAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.personalCollectAction();
        }
      });
    view = finder.findById(source, 2131493434);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493434' for method 'personalAlertAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.personalAlertAction();
        }
      });
    view = finder.findById(source, 2131493448);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493448' for method 'personalSettingAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.personalSettingAction();
        }
      });
  }

  public static void reset(com.cplatform.xhxw.ui.ui.main.cms.personal.PersonalFragment target) {
    target.mRegLoginLo = null;
    target.mRegBtn = null;
    target.mLoginBtn = null;
    target.mMsgNew = null;
    target.ivNewMyInfo = null;
    target.mNickName = null;
    target.mAvatar = null;
  }
}
