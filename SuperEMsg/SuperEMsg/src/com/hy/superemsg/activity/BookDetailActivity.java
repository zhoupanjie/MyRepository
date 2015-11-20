package com.hy.superemsg.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.adapter.AbsCommonAdapter;
import com.hy.superemsg.components.UITitle;
import com.hy.superemsg.req.ReqAnimationDetailQuery;
import com.hy.superemsg.rsp.AnimationContentDetail;
import com.hy.superemsg.rsp.AnimationDramaDetail;
import com.hy.superemsg.rsp.BaseRspApi;
import com.hy.superemsg.rsp.RspAnimationDetailQuery;
import com.hy.superemsg.utils.HttpUtils;
import com.hy.superemsg.utils.HttpUtils.AsynHttpCallback;
import com.hy.superemsg.utils.ImageUtils;

public class BookDetailActivity extends Activity {
	private AnimationContentDetail book;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_detail);
		book = this.getIntent().getParcelableExtra(
				SuperEMsgApplication.EXTRA_BOOK_DETAIL);
		UITitle title = (UITitle) this.findViewById(R.id.ui_title);
		if (title != null) {
			title.setTitleText(book.amname);
			title.setLeftButton(R.drawable.back, new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
		HttpUtils.getInst().excuteTask(new ReqAnimationDetailQuery(book.amid),
				new AsynHttpCallback() {

					@Override
					public void onSuccess(BaseRspApi rsp) {
						RspAnimationDetailQuery detail = (RspAnimationDetailQuery) rsp;
						book = detail.amdetail;
						setData();
					}

					@Override
					public void onError(String error) {
						SuperEMsgApplication.toast(error);
					}
				});
	}

	private void setData() {
		ImageView iv = (ImageView) this.findViewById(R.id.item_image);
		ImageUtils.Image.loadImage(book.amcoverpicurl, iv);
		TextView name = (TextView) this.findViewById(R.id.item_text);
		name.setText(book.amname);
		TextView author = (TextView) this.findViewById(R.id.item_text1);
		author.setText("作者:" + book.amauthor);
		TextView desc = (TextView) this.findViewById(R.id.item_text2);
		desc.setText(book.amintroduce);
		GridView grid = (GridView) this.findViewById(R.id.grid);
		DramaGridAdapter adapter = new DramaGridAdapter(this);
		adapter.setDatas(book.amdramalist);
		grid.setAdapter(adapter);
	}

	public class DramaGridAdapter extends
			AbsCommonAdapter<AnimationDramaDetail> {

		public DramaGridAdapter(Context ctx) {
			super(ctx, R.layout.item_drama_grid);
		}

		@Override
		public void getView(int position, View convertView,
				AnimationDramaDetail data) {
			convertView.setTag(data);
			TextView title = (TextView) convertView
					.findViewById(R.id.item_text);
			title.setText(data.dramaname);
			convertView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent(BookDetailActivity.this,
							ReadBookActivity.class);
					i.putExtra(SuperEMsgApplication.EXTRA_DRAMA_DETAIL,
							(AnimationDramaDetail) v.getTag());
					startActivity(i);
				}
			});
		}
	}
}
