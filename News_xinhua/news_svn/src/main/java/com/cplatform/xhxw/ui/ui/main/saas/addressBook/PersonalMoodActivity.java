package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.httputils.HttpHanderUtil;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.widget.PullRefreshListView;
import com.cplatform.xhxw.ui.ui.base.widget.PullRefreshListView.PullRefreshListener;
import com.cplatform.xhxw.ui.util.ListUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.cplatform.xhxw.ui.util.StringUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 个人新鲜事列表
 */
public class PersonalMoodActivity extends BaseActivity implements PullRefreshListener, OnItemClickListener{
	
	private static final String TAG = PersonalMoodActivity.class.getSimpleName();
	private static final String REFESH = "1";
	private static final String LOADMORE = "2";
	private static final int FRESHMOODINFO = 3;
	
	private String userId;
	private int position;
	
	private PullRefreshListView mListView;
	private PersonalMoodAdapter mAdapter;
	private AsyncHttpResponseHandler mHttpResponseHandler;

    /**
     * 新鲜事儿列表
     * @param context 上下文
     * @param userId 用户id
     * @return
     */
	public static Intent newIntent(Context context, String userId) {
		Intent intent = new Intent(context, PersonalMoodActivity.class);
        intent.putExtra("userId", userId);
		return intent;
	}
	
	@Override
	protected String getScreenName() {
		return TAG;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_mood);
		init();
		mListView.triggerRefresh(true);
	}
	
	private void init() {
		initActionBar();
        View topRightBtn = findViewById(R.id.personal_mood_message);
        userId = getIntent().getStringExtra("userId");
        userId = Constants.getUid();
        if (StringUtil.isEquals(userId, Constants.getUid())) {
            topRightBtn.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View view) {
                    startActivity(HistoryMessageActivity.newIntent(PersonalMoodActivity.this));
                }
            });
        } else {
            topRightBtn.setVisibility(View.INVISIBLE);

        }
		mListView = (PullRefreshListView) findViewById(R.id.mine_mood_listView);
		mAdapter = new PersonalMoodAdapter(this);
		mListView.setAdapter(mAdapter);
		mListView.setCanRefresh(true);
		mListView.setCanLoadMore(false);
		mListView.setOnItemClickListener(this);
		mListView.setPullRefreshListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        PersonalMoods item = mAdapter.getItem(position - 1);
        this.position = position - 1;
		if (TextUtils.isEmpty(item.getCompanyid())) {
//			startActivity(FriendsFreshInfoActivity.newIntent(this, item.getInfoid()));
			startActivityForResult(FriendsFreshInfoNativeActivity.newIntent(this, item.getInfoid()), FRESHMOODINFO);
		}else {
//			startActivity(CompanyFreshInfoActivity.newIntent(this, item.getInfoid()));
			startActivityForResult(CompanyFreshInfoNativeActivity.newIntent(this, item.getInfoid()), FRESHMOODINFO);
		}
	}
	
	@Override
	public void onRefresh() {
		/**刷新时，不可能获取到新数据*/
		getPersonalMoods(userId, REFESH, "");
	}

	@Override
	public void onLoadMore() {
        String cTime = mAdapter.getCount() > 0 ? mAdapter.getItem(mAdapter.getCount() - 1).getCtime() : "0";
		getPersonalMoods(userId, LOADMORE, cTime);
	}
	
	/** 获取新鲜事 */
	private void getPersonalMoods(String userId, final String typeId, String ctime) {
        
		PersonalMoodsRequest request = new PersonalMoodsRequest();
		request.setTypeid(typeId);
        request.setUserid(userId);
		request.setCtime(ctime);
		request.setOffset(String.valueOf(Constants.LIST_LOAD_COUNT));
		request.setDevRequest(true);

		APIClient.getPersonalMoods(request, new AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				mHttpResponseHandler = null;
				mListView.onRefreshComplete(null);
				mListView.onLoadMoreComplete();
			}

			@Override
			protected void onPreExecute() {
				if (mHttpResponseHandler != null) {
					mHttpResponseHandler.cancle();
				}
				mHttpResponseHandler = this;
			}

			@Override
			public void onSuccess(String content) {
                LogUtil.d(content);
				PersonalMoodsResponse response;
				try {
					response = new Gson().fromJson(content,
							PersonalMoodsResponse.class);
                    ResponseUtil.checkObjResponse(response);
				} catch (Exception e) {
					showToast(R.string.data_format_error);
					LogUtil.w(TAG, e);
					return;
				}

				if (response.isSuccess()) { 
					
					/**如果是刷新， 清空适配器，重新加载数据*/
					if (REFESH.equals(typeId) && mAdapter.getCount() > 0) {
						mAdapter.clearData();
                        mAdapter.notifyDataSetChanged();
					}
					/**将数据拆分重新进行组装---转换成“新鲜事集合”*/
					List<PersonalMoods> list = new ArrayList<PersonalMoods>();
                    if (!ListUtil.isEmpty(response.getData())) {
                        for (PersonalMoodsData item : response.getData()) {
                            List<PersonalMoods> personalMoodses = item.getData();
                            if (!ListUtil.isEmpty(personalMoodses)) {
                                for (PersonalMoods personalMoods : personalMoodses) {
                                    personalMoods.setShowtime(item.getShowtime());
                                    list.add(personalMoods);
                                }
                            }
                        }

                        mAdapter.addAllData(list);
                        mAdapter.notifyDataSetChanged();
                        if (response.getData().size() < Constants.LIST_LOAD_COUNT) {
                            mListView.setCanLoadMore(false);
                        } else {
                            mListView.setCanLoadMore(true);
                        }
                    }else {
                        /**联网正常， 没有获取到数据，说明服务器已经没有数据了，设为false*/
                        mListView.setCanLoadMore(false);
                    }
				} else {
					showToast(response.getMsg());
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
                LogUtil.d(content);
                showToast(R.string.load_server_failure);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		HttpHanderUtil.cancel(mHttpResponseHandler);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		
		switch (requestCode) {
		case FRESHMOODINFO:
			mAdapter.getData().remove(position);
			mAdapter.notifyDataSetChanged();
			break;

		default:
			break;
		}
	}
	
}
