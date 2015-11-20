// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.cms.game;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class GameAdapter$ViewHolderGameList$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.main.cms.game.GameAdapter.ViewHolderGameList> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427995, "field 'rlRoot'");
    target.rlRoot = finder.castView(view, 2131427995, "field 'rlRoot'");
    view = finder.findRequiredView(source, 2131427813, "field 'ivIcon'");
    target.ivIcon = finder.castView(view, 2131427813, "field 'ivIcon'");
    view = finder.findRequiredView(source, 2131427973, "field 'tvName'");
    target.tvName = finder.castView(view, 2131427973, "field 'tvName'");
    view = finder.findRequiredView(source, 2131427838, "field 'tvType'");
    target.tvType = finder.castView(view, 2131427838, "field 'tvType'");
    view = finder.findRequiredView(source, 2131427574, "field 'tvDescription'");
    target.tvDescription = finder.castView(view, 2131427574, "field 'tvDescription'");
    view = finder.findRequiredView(source, 2131427996, "field 'ivDown'");
    target.ivDown = finder.castView(view, 2131427996, "field 'ivDown'");
    view = finder.findRequiredView(source, 2131427845, "field 'pbDown'");
    target.pbDown = finder.castView(view, 2131427845, "field 'pbDown'");
    view = finder.findRequiredView(source, 2131427846, "field 'tvDownInfo'");
    target.tvDownInfo = finder.castView(view, 2131427846, "field 'tvDownInfo'");
  }

  @Override public void unbind(T target) {
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
