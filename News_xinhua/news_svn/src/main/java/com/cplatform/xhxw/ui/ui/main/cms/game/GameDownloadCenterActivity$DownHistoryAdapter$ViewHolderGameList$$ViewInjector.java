// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.cms.game;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class GameDownloadCenterActivity$DownHistoryAdapter$ViewHolderGameList$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.main.cms.game.GameDownloadCenterActivity.DownHistoryAdapter.ViewHolderGameList target, Object source) {
    View view;
    view = finder.findById(source, 2131493531);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493531' for field 'rlRoot' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.rlRoot = (android.widget.RelativeLayout) view;
    view = finder.findById(source, 2131493382);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493382' for field 'tvDownInfo' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.tvDownInfo = (android.widget.TextView) view;
    view = finder.findById(source, 2131493509);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493509' for field 'tvName' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.tvName = (android.widget.TextView) view;
    view = finder.findById(source, 2131493374);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493374' for field 'tvType' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.tvType = (android.widget.TextView) view;
    view = finder.findById(source, 2131493532);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493532' for field 'ivDown' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.ivDown = (android.widget.TextView) view;
    view = finder.findById(source, 2131493349);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493349' for field 'ivIcon' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.ivIcon = (android.widget.ImageView) view;
    view = finder.findById(source, 2131493381);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493381' for field 'pbDown' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.pbDown = (android.widget.ProgressBar) view;
    view = finder.findById(source, 2131493110);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493110' for field 'tvDescription' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.tvDescription = (android.widget.TextView) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.main.cms.game.GameDownloadCenterActivity.DownHistoryAdapter.ViewHolderGameList target) {
    target.rlRoot = null;
    target.tvDownInfo = null;
    target.tvName = null;
    target.tvType = null;
    target.ivDown = null;
    target.ivIcon = null;
    target.pbDown = null;
    target.tvDescription = null;
  }
}
