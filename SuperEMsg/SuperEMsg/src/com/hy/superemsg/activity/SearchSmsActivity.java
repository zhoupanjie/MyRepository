package com.hy.superemsg.activity;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ListView;

import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.adapter.SmsListAdapter;
import com.hy.superemsg.components.UITitle;
import com.hy.superemsg.req.ReqSmsContentQuery;
import com.hy.superemsg.rsp.BaseRspApi;
import com.hy.superemsg.rsp.RspSmsContentQuery;
import com.hy.superemsg.utils.CommonUtils;
import com.hy.superemsg.utils.HttpUtils;
import com.hy.superemsg.utils.HttpUtils.AsynHttpCallback;

public class SearchSmsActivity extends Activity {

	private EditText searchInput;
	protected ListView list;
	protected int currPage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		UITitle title = (UITitle) this.findViewById(R.id.ui_title);
		if (title != null) {
			title.setTitleText(R.string.search);
			title.setLeftButton(R.drawable.back, new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
		initEditText();
		list = (ListView) this.findViewById(R.id.pull_refresh_list);
	}

	private void initEditText() {
		searchInput = (EditText) this.findViewById(R.id.search_input);
		final Drawable x = getResources().getDrawable(
				R.drawable.search_edit_delete);
		x.setBounds(0, 0, (int) getResources().getDimension(R.dimen.px30),
				(int) getResources().getDimension(R.dimen.px30));
		final Drawable search = getResources().getDrawable(
				R.drawable.search_edit_icon);
		search.setBounds(0, 0, (int) getResources().getDimension(R.dimen.px30),
				(int) getResources().getDimension(R.dimen.px30));
		// this.findViewById(R.id.btn_search).setOnClickListener(
		// new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// String keyword = searchInput.getText().toString();
		// if (!TextUtils.isEmpty(keyword)) {
		// doSearch(keyword);
		// } else {
		// SuperEMsgApplication.toast("请输入搜索关键词");
		// }
		// }
		// });

		searchInput.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (searchInput.getCompoundDrawables()[2] == null) {
					return false;
				}
				if (event.getAction() != MotionEvent.ACTION_UP) {
					return false;
				}
				if (event.getX() > searchInput.getWidth()
						- searchInput.getPaddingRight() - x.getIntrinsicWidth()) {
					searchInput.setText("");
					searchInput.setCompoundDrawables(search, null, searchInput
							.getText().toString().equals("") ? null : x, null);
				}
				return false;
			}
		});
		searchInput.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				searchInput.setCompoundDrawables(null, null, searchInput
						.getText().toString().equals("") ? null : x, null);
			}

			@Override
			public void afterTextChanged(Editable arg0) {
				String word = arg0.toString();
				if (TextUtils.isEmpty(word)) {
					word = "";
				} else {
					word = word.trim();
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
		});
		searchInput.setCompoundDrawables(search, null, searchInput.getText()
				.toString().equals("") ? null : x, null);
		searchInput.setOnKeyListener(new View.OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN
						&& keyCode == KeyEvent.KEYCODE_ENTER) {
					String keyword = searchInput.getText().toString();
					if (!TextUtils.isEmpty(keyword)) {
						doSearch(keyword);
					} else {
						SuperEMsgApplication.toast("请输入搜索关键词");
					}
					return true;
				}
				return false;
			}
		});
	}

	protected void doSearch(String keyword) {

		HttpUtils.getInst().excuteTask(
				new ReqSmsContentQuery("", keyword, currPage,
						SuperEMsgApplication.PAGE_SIZE),
				new AsynHttpCallback() {

					@Override
					public void onSuccess(BaseRspApi rsp) {
						RspSmsContentQuery content = (RspSmsContentQuery) rsp;
						if (CommonUtils.isNotEmpty(content.contentlist)) {
							final SmsListAdapter adapter = new SmsListAdapter(
									SearchSmsActivity.this);
							adapter.setDatas(content.contentlist);
							list.setAdapter(adapter);
						}
					}

					@Override
					public void onError(String error) {
						// TODO Auto-generated method stub

					}
				});

	}
}
