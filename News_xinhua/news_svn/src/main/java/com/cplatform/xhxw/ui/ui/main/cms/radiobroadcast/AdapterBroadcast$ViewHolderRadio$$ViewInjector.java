// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class AdapterBroadcast$ViewHolderRadio$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast.AdapterBroadcast.ViewHolderRadio target, Object source) {
    View view;
    view = finder.findById(source, 2131493509);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493509' for field 'tvName' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.tvName = (android.widget.TextView) view;
    view = finder.findById(source, 2131493510);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493510' for field 'tvTime' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.tvTime = (android.widget.TextView) view;
    view = finder.findById(source, 2131493508);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493508' for field 'riv' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.riv = (com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast.RotationImageView) view;
  }

  public static void reset(com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast.AdapterBroadcast.ViewHolderRadio target) {
    target.tvName = null;
    target.tvTime = null;
    target.riv = null;
  }
}
