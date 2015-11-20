// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class NewAdapter$ViewHolder$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.main.saas.addressBook.NewAdapter.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427537, "field 'userImage'");
    target.userImage = finder.castView(view, 2131427537, "field 'userImage'");
    view = finder.findRequiredView(source, 2131427538, "field 'userName'");
    target.userName = finder.castView(view, 2131427538, "field 'userName'");
    view = finder.findRequiredView(source, 2131427539, "field 'comment'");
    target.comment = finder.castView(view, 2131427539, "field 'comment'");
    view = finder.findRequiredView(source, 2131427541, "field 'addFriends'");
    target.addFriends = finder.castView(view, 2131427541, "field 'addFriends'");
    view = finder.findRequiredView(source, 2131427540, "field 'addedFriends'");
    target.addedFriends = finder.castView(view, 2131427540, "field 'addedFriends'");
  }

  @Override public void unbind(T target) {
    target.userImage = null;
    target.userName = null;
    target.comment = null;
    target.addFriends = null;
    target.addedFriends = null;
  }
}
