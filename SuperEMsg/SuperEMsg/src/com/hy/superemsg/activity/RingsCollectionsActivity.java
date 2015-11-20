package com.hy.superemsg.activity;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hy.superemsg.R;
import com.hy.superemsg.adapter.RingListAdapter;
import com.hy.superemsg.adapter.collections.RingCollectionAdapter;
import com.hy.superemsg.db.DBUtils;
import com.hy.superemsg.db.DBUtils.DBHelper;

public class RingsCollectionsActivity extends AbsCollectionActivity {
	private PullToRefreshListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sms_collection);
		list = (PullToRefreshListView) this
				.findViewById(R.id.pull_refresh_list);
		list.setMode(Mode.DISABLED);
		final RingCollectionAdapter adapter = new RingCollectionAdapter(this);
		adapter.setDatas(DBUtils.getInst().queryRing(true, null));
		list.setAdapter(adapter);
		list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(
						RingsCollectionsActivity.this,
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				list.post(new Runnable() {

					@Override
					public void run() {
						final RingListAdapter adapter = new RingListAdapter(
								RingsCollectionsActivity.this);
						adapter.setDatas(DBUtils.getInst().queryRing(true, null));
						list.setAdapter(adapter);
						list.onRefreshComplete();
					}
				});
			}
		});
	}

	@Override
	protected String getDBTable() {
		return DBHelper.TABLE_RING_COLLECTION;
	}

	@Override
	protected String getTitleName() {
		return "彩信收藏";
	}
}
