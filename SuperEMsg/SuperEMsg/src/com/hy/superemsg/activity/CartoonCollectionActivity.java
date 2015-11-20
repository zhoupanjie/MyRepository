package com.hy.superemsg.activity;

import android.os.Bundle;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.hy.superemsg.R;
import com.hy.superemsg.adapter.collections.CartoonCollectionAdapter;
import com.hy.superemsg.db.DBUtils;
import com.hy.superemsg.db.DBUtils.DBHelper;

public class CartoonCollectionActivity extends AbsCollectionActivity {
	private PullToRefreshGridView bookshelf;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_bookshelf);
		initUI();
	}

	private void initUI() {
		bookshelf = (PullToRefreshGridView) this
				.findViewById(R.id.pull_refresh_grid);
		bookshelf.setMode(Mode.DISABLED);
		CartoonCollectionAdapter adapter = new CartoonCollectionAdapter(this);
		adapter.setDatas(DBUtils.getInst().queryCartoon(true, null));
		bookshelf.setAdapter(adapter);

	}

	@Override
	protected String getDBTable() {
		return DBHelper.TABLE_CARTOON_COLLECTION;
	}

	@Override
	protected String getTitleName() {
		return "漫画收藏";
	}

}
