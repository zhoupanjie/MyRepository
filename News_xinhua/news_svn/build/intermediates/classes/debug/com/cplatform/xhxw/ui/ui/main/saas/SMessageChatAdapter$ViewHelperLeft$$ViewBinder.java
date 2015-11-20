// Generated code from Butter Knife. Do not modify!
package com.cplatform.xhxw.ui.ui.main.saas;

import android.view.View;
import butterknife.ButterKnife.Finder;
import butterknife.ButterKnife.ViewBinder;

public class SMessageChatAdapter$ViewHelperLeft$$ViewBinder<T extends com.cplatform.xhxw.ui.ui.main.saas.SMessageChatAdapter.ViewHelperLeft> implements ViewBinder<T> {
  @Override public void bind(final Finder finder, final T target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131428349, "field 'date'");
    target.date = finder.castView(view, 2131428349, "field 'date'");
    view = finder.findRequiredView(source, 2131428350, "field 'avatar'");
    target.avatar = finder.castView(view, 2131428350, "field 'avatar'");
    view = finder.findRequiredView(source, 2131428352, "field 'msg'");
    target.msg = finder.castView(view, 2131428352, "field 'msg'");
    view = finder.findRequiredView(source, 2131428353, "field 'pic'");
    target.pic = finder.castView(view, 2131428353, "field 'pic'");
    view = finder.findRequiredView(source, 2131428351, "field 'body'");
    target.body = view;
  }

  @Override public void unbind(T target) {
    target.date = null;
    target.avatar = null;
    target.msg = null;
    target.pic = null;
    target.body = null;
  }
}
