// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.base.view;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class ChannelManageView$$ViewInjector {
  public static void inject(Finder finder, final com.cplatform.xhxw.ui.ui.base.view.ChannelManageView target, Object source) {
    View view;
    view = finder.findById(source, 2131493812);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493812' for field 'mContent' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mContent = view;
    view = finder.findById(source, 2131493814);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493814' for field 'mShow' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mShow = (ca.laplanete.mobile.pageddragdropgrid.PagedDragDropGrid) view;
    view = finder.findById(source, 2131493816);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493816' for field 'mHide' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.mHide = (android.support.v7.widget.GridLayout) view;
    view = finder.findById(source, 2131493808);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493808' for field 'view_channel_manage_layout' was not found. If this view is optional add '@Optional' annotation.");
    }
    target.view_channel_manage_layout = (android.widget.RelativeLayout) view;
    view = finder.findById(source, 2131493811);
    if (view == null) {
      throw new IllegalStateException("Required view with id '2131493811' for method 'closeAction' was not found. If this view is optional add '@Optional' annotation.");
    }
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.closeAction();
        }
      });
  }

  public static void reset(com.cplatform.xhxw.ui.ui.base.view.ChannelManageView target) {
    target.mContent = null;
    target.mShow = null;
    target.mHide = null;
    target.view_channel_manage_layout = null;
  }
}
