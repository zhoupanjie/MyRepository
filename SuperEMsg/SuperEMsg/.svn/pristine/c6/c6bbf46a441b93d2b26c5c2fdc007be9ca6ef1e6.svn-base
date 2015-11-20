package com.hy.superemsg.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hy.superemsg.R;
import com.hy.superemsg.SuperEMsgApplication;
import com.hy.superemsg.adapter.MmsListAdapter;
import com.hy.superemsg.adapter.collections.MmsCollectionAdapter;
import com.hy.superemsg.db.DBUtils;
import com.hy.superemsg.db.DBUtils.DBHelper;
import com.hy.superemsg.utils.CommonUtils;
import com.hy.superemsg.viewpager.AbsFragment;

public class MmsCollectionsActivity extends AbsCollectionActivity {
	private PullToRefreshListView list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sms_collection);
		list = (PullToRefreshListView) this
				.findViewById(R.id.pull_refresh_list);
		final MmsCollectionAdapter adapter = new MmsCollectionAdapter(this);
		adapter.setDatas(DBUtils.getInst().queryMms(true, null));
		list.setMode(Mode.DISABLED);
		list.setAdapter(adapter);
		list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				String label = DateUtils.formatDateTime(
						MmsCollectionsActivity.this,
						System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
								| DateUtils.FORMAT_SHOW_DATE
								| DateUtils.FORMAT_ABBREV_ALL);

				// Update the LastUpdatedLabel
				refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
				list.post(new Runnable() {

					@Override
					public void run() {
						final MmsListAdapter adapter = new MmsListAdapter(
								MmsCollectionsActivity.this);
						adapter.setDatas(DBUtils.getInst().queryMms(true, null));
						list.setAdapter(adapter);
						list.onRefreshComplete();
					}
				});
			}
		});
	}

	@Override
	protected String getDBTable() {
		return DBHelper.TABLE_MMS_COLLECTION;
	}

	@Override
	protected String getTitleName() {
		return getString(R.string.mms_collection);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == SuperEMsgApplication.REQUEST_LIST_TO_MMS_DETAIL) {
				final MmsListAdapter adapter = new MmsListAdapter(
						MmsCollectionsActivity.this);
				adapter.setDatas(DBUtils.getInst().queryMms(true, null));
				list.setAdapter(adapter);
			}
		}
	}
}
