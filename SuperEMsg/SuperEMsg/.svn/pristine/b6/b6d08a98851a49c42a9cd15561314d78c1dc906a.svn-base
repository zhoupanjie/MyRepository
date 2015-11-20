
package com.hy.superemsg.viewpager.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.activity.BookDetailActivity;
import com.hy.superemsg.adapter.BookGridAdapter;
import com.hy.superemsg.req.ReqAnimationContentQuery;
import com.hy.superemsg.rsp.AnimationContentDetail;
import com.hy.superemsg.rsp.BaseRspApi;
import com.hy.superemsg.rsp.RspAnimationContentQuery;
import com.hy.superemsg.utils.CommonUtils;
import com.hy.superemsg.utils.HttpUtils;
import com.hy.superemsg.utils.HttpUtils.AsynHttpCallback;
import com.hy.superemsg.utils.ImageUtils;
import com.hy.superemsg.viewpager.AbsFragment;

public class CartoonShelfFragment2 extends AbsFragment {
    private ScrollView scroll;
    private LinearLayout tableLayout;
    private String categoryid;
    private int currPage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bookshelf, null);
    }

    @Override
    protected void initUI() {
        categoryid = getArguments().getString("categoryid");

        scroll = (ScrollView) getView().findViewById(R.id.scroll);
        tableLayout = (LinearLayout) getView().findViewById(R.id.tablelayout);
        HttpUtils.getInst().excuteTask(
                new ReqAnimationContentQuery(categoryid, "", currPage,
                        SuperEMsgApplication.PAGE_SIZE),
                new AsynHttpCallback() {

                    @Override
                    public void onSuccess(BaseRspApi rsp) {
                        if (getActivity() == null) {
                            return;
                        }
                        RspAnimationContentQuery content = (RspAnimationContentQuery) rsp;
                        if (CommonUtils.isNotEmpty(content.contentlist)) {
                            int count = content.contentlist.size();
                            int column = 3;
                            int offset = (count % column) > 0 ? 1 : 0;
                            int rowCount = count / column + offset;
                            
                            for (int i = 0; i < rowCount; i++) {
                                TableRow row = (TableRow) LayoutInflater.from(
                                        getActivity()).inflate(
                                        R.layout.tablerow_books, null);
                                for (int j = 0; j < column; j++) {
                                    int index = i * column + j;
                                    LinearLayout layout = null;
                                    if (j == 0) {
                                        layout = (LinearLayout) row
                                                .findViewById(R.id.item_book1);
                                    } else if (j == 1) {
                                        layout = (LinearLayout) row
                                                .findViewById(R.id.item_book2);
                                    } else {
                                        layout = (LinearLayout) row
                                                .findViewById(R.id.item_book3);
                                    }
                                    if (index < content.contentlist.size()) {
                                        doSetBook(layout,
                                                content.contentlist.get(index));
                                    } else {
                                        doSetBook(layout, null);
                                    }
                                }
                                tableLayout.addView(row);
                            }
                        }
                    }

                    @Override
                    public void onError(String error) {
                        SuperEMsgApplication.toast(error);
                    }
                });
    }

    private void doSetBook(LinearLayout layout, AnimationContentDetail book) {
        if (book == null) {
            layout.setVisibility(View.INVISIBLE);
        } else {
            layout.setVisibility(View.VISIBLE);
            TextView title = (TextView) layout.findViewById(R.id.item_text);
            ImageView cover = (ImageView) layout.findViewById(R.id.item_image);
            title.setText(book.amname);
            ImageUtils.Image.loadImage(book.amcoverpicurl, cover);
            layout.setTag(book);
            layout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Activity act = getActivity();
                    Intent i = new Intent(act, BookDetailActivity.class);
                    i.putExtra(SuperEMsgApplication.EXTRA_BOOK_DETAIL,
                            (Parcelable) v.getTag());
                    act.startActivity(i);
                }
            });
        }
    }

    @Override
    protected void excuteTask() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void resetUI() {
        // TODO Auto-generated method stub

    }
}
