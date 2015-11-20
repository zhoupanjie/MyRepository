// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class CompanyMessageAdapter$ViewHolder$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.main.saas.addressBook.CompanyMessageAdapter.ViewHolder> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131427523, "field 'userImage'");
    target.userImage = finder.castView(view, 2131427523, "field 'userImage'");
    view = finder.findRequiredView(source, 2131427524, "field 'userName'");
    target.userName = finder.castView(view, 2131427524, "field 'userName'");
    view = finder.findRequiredView(source, 2131427525, "field 'time'");
    target.time = finder.castView(view, 2131427525, "field 'time'");
    view = finder.findRequiredView(source, 2131427526, "field 'reply'");
    target.reply = finder.castView(view, 2131427526, "field 'reply'");
    view = finder.findRequiredView(source, 2131427527, "field 'content'");
    target.content = finder.castView(view, 2131427527, "field 'content'");
  }

  @Override public void unbind(T target) {
    target.userImage = null;
    target.userName = null;
    target.time = null;
    target.reply = null;
    target.content = null;
  }
}
