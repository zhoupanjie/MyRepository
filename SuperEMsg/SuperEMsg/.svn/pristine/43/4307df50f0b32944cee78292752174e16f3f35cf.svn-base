package com.hy.superemsg.activity;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hy.superemsg.R;
import com.hy.superemsg.adapter.collections.SmsCollectionAdapter;
import com.hy.superemsg.db.DBUtils;
import com.hy.superemsg.db.DBUtils.DBHelper;

public class SmsCollectionsActivity extends AbsCollectionActivity {
	private PullToRefreshListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sms_collection);
		list = (PullToRefreshListView) this
				.findViewById(R.id.pull_refresh_list);
		final SmsCollectionAdapter adapter = new SmsCollectionAdapter(
				this);
		adapter.setDatas(DBUtils.getInst().querySms(true, null));
		list.setAdapter(adapter);
		list.setMode(Mode.DISABLED);
		list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {

				list.setMode(Mode.BOTH);
				String label = DateUtils.formatDateTime(
						SmsCollectionsActivity.this,
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				list.post(new Runnable() {

					@Override
					public void run() {
						adapter.setDatas(DBUtils.getInst().querySms(true, null));
						list.onRefreshComplete();
					}
				});

			}
		});
	}

	@Override
	protected String getDBTable() {
		return DBHelper.TABLE_SMS_COLLECTION;
	}

	@Override
	protected String getTitleName() {
		return getString(R.string.sms_collection);
	}
}
