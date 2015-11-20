package com.cplatform.xhxw.ui.ui.collect;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.google.gson.Gson;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.db.CollectDB;
import com.cplatform.xhxw.ui.db.dao.CollectDao;
import com.cplatform.xhxw.ui.db.dao.CollectFlag;
import com.cplatform.xhxw.ui.model.PicShow;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.adapter.CollectAdapter;
import com.cplatform.xhxw.ui.ui.detailpage.NewsPageActivity;
import com.cplatform.xhxw.ui.ui.picShow.PicShowActivity;
import com.cplatform.xhxw.ui.util.CommonUtils;
import com.cplatform.xhxw.ui.util.ListUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.cplatform.xhxw.ui.util.StringUtil;
import com.cplatform.xhxw.ui.http.ResponseUtil;

import java.util.List;

/**
 * 收藏
 * Created by cy-love on 14-1-24.
 */
public class CollectActivity extends BaseActivity implements OnItemClickListener, OnItemLongClickListener {

    private static final String TAG = CollectActivity.class.getSimpleName();
    @InjectView(R.id.listview)
    ListView mListView;
    private CollectAdapter mAdapter;
    private int mShowPosi;

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, CollectActivity.class);
        return intent;
    }

    @Override
    protected String getScreenName() {
        return "CollectActivity";
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        ButterKnife.inject(this);
        initActionBar();
        mAdapter = new CollectAdapter(this);
        List<CollectDao> data = CollectDB.getCollectsByEnterPriseId(this);
        if (!ListUtil.isEmpty(data)) {
            mAdapter.addAllData(data);
        }
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (CommonUtils.isFastDoubleClick()) {
            return;
        }
        mShowPosi = position;
        CollectDao item = mAdapter.getItem(position);
        if (item.getFlag() == CollectFlag.COLLECT_NEWS_TYPE_NORMAL_PICS) {
            PicShow spec;
            try {
                ResponseUtil.checkResponse(item.getData());
                spec = new Gson().fromJson(item.getData(), PicShow.class);
            } catch (Exception e) {
                showToast(R.string.data_format_error);
                LogUtil.e(TAG, e);
                return;
            }
            startActivityForResult(PicShowActivity.getIntent(this,spec,0,spec.getNewsId(),true, false, spec.getTitle()), 1);
        } else if (item.getFlag() == CollectFlag.COLLECT_NEWS_TYPE_NORMAL_NORM) {
            startActivityForResult(NewsPageActivity.getIntent(this, item.getNewsId(), false), 1);
        } else {
        	onClickEnterpriseCollection(item);
        }
    }
    
    /**
     * 用户点击查看收藏列表中的企业新闻时，做查看权限判断
     * @param collectionFlag
     * @param newsId
     */
    private void onClickEnterpriseCollection(CollectDao item) {
    	if(Constants.hasEnterpriseAccountLoggedIn()) {
    		if(item.getFlag() == (CollectFlag.COLLECT_NEWS_TYPE_ENTERPRISE_NORM +
    				StringUtil.parseIntegerFromString(Constants.getEnterpriseId()))) {
    			startActivityForResult(NewsPageActivity.getIntent(this, item.getNewsId(), true), 1);
    		} else if(item.getFlag() == (CollectFlag.COLLECT_NEWS_TYPE_ENTERPRISE_PICS +
    				StringUtil.parseIntegerFromString(Constants.getEnterpriseId()))) {
    			PicShow spec;
                try {
                    ResponseUtil.checkResponse(item.getData());
                    spec = new Gson().fromJson(item.getData(), PicShow.class);
                } catch (Exception e) {
                    showToast(R.string.data_format_error);
                    LogUtil.e(TAG, e);
                    return;
                }
                startActivityForResult(PicShowActivity.getIntent(this,spec,0,spec.getNewsId(),true, true, spec.getTitle()), 1);
    		} else {
    			toastAlert(R.string.collect_not_available);
    		}
    	} else {
    		toastAlert(R.string.collect_not_available);
    	}
    }
    
    private void toastAlert(int resId) {
    	Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            boolean del = data.getExtras().getBoolean("removeCollect");
            if (del) {
                CollectDao item = mAdapter.getData().get(mShowPosi);
                CollectDB.delCollectByNewsId(this, item.getNewsId());
                mAdapter.getData().remove(item);
                mAdapter.notifyDataSetChanged();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        if (CommonUtils.isFastDoubleClick()) {
            return true;
        }
        new AlertDialog.Builder(this)
                .setTitle(R.string.hint)
                .setMessage(R.string.do_you_want_to_delete)
                .setNegativeButton(R.string.cancel, null)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CollectDao dao = mAdapter.getItem(position);
                        CollectDB.delCollectByNewsId(CollectActivity.this, dao.getNewsId());
                        mAdapter.getData().remove(position);
                        mAdapter.notifyDataSetChanged();
                    }
                }).show();

        return true;
    }

}