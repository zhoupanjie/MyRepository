// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.cms.game;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class GameAdapter$ViewHolderGameList$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.main.cms.game.GameAdapter.ViewHolderGameList target, Object source) {
    View view;
    view = finder.findById(source, 2131427995);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427995' for field 'rlRoot' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.rlRoot = (android.widget.RelativeLayout) view;
    view = finder.findById(source, 2131427813);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427813' for field 'ivIcon' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.ivIcon = (android.widget.ImageView) view;
    view = finder.findById(source, 2131427973);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427973' for field 'tvName' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.tvName = (android.widget.TextView) view;
    view = finder.findById(source, 2131427838);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427838' for field 'tvType' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.tvType = (android.widget.TextView) view;
    view = finder.findById(source, 2131427574);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427574' for field 'tvDescription' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.tvDescription = (android.widget.TextView) view;
    view = finder.findById(source, 2131427996);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427996' for field 'ivDown' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.ivDown = (android.widget.TextView) view;
    view = finder.findById(source, 2131427845);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427845' for field 'pbDown' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.pbDown = (android.widget.ProgressBar) view;
    view = finder.findById(source, 2131427846);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131427846' for field 'tvDownInfo' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.tvDownInfo = (android.widget.TextView) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.main.cms.game.GameAdapter.ViewHolderGameList target) {
    target.rlRoot = null;
    target.ivIcon = null;
    target.tvName = null;
    target.tvType = null;
    target.tvDescription = null;
    target.ivDown = null;
    target.pbDown = null;
    target.tvDownInfo = null;
  }
}
