
package com.hy.superemsg.activity;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.hy.superemsg.R;
import com.hy.superemsg.adapter.GameListAdapter;
import com.hy.superemsg.adapter.collections.GameCollectionAdapter;
import com.hy.superemsg.db.DBUtils;
import com.hy.superemsg.db.DBUtils.DBHelper;

public class GameCollectionsActivity extends AbsCollectionActivity {
    private PullToRefreshListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_collection);
        list = (PullToRefreshListView) this
                .findViewById(R.id.pull_refresh_list);
        list.setMode(Mode.DISABLED);
        final GameCollectionAdapter adapter = new GameCollectionAdapter(this);
        adapter.setDatas(DBUtils.getInst().queryGame(true, null));
        list.setAdapter(adapter);
        list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(
                        GameCollectionsActivity.this,
                        System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
                                | DateUtils.FORMAT_SHOW_DATE
                                | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                list.post(new Runnable() {

                    @Override
                    public void run() {
                        final GameListAdapter adapter = new GameListAdapter(
                                GameCollectionsActivity.this);
                        adapter.setDatas(DBUtils.getInst().queryGame(true, null));
                        list.setAdapter(adapter);
                        list.onRefreshComplete();
                    }
                });
            }
        });
    }

    @Override
    protected String getDBTable() {
        return DBHelper.TABLE_GAME_COLLECTION;
    }

    @Override
    protected String getTitleName() {
        return getString(R.string.game_zone);
    }
}
