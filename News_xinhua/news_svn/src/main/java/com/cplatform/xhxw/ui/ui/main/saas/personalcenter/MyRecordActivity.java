package com.cplatform.xhxw.ui.ui.main.saas.personalcenter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.GetMoreReplyRequest;
import com.cplatform.xhxw.ui.http.net.GetMoreReplyResponse;
import com.cplatform.xhxw.ui.model.CommentReplyMe;
import com.cplatform.xhxw.ui.model.ReplyComment;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.view.PagerSlidingTabStrip;
import com.cplatform.xhxw.ui.ui.base.widget.PullRefreshListView;
import com.cplatform.xhxw.ui.ui.base.widget.PullRefreshListView.PullRefreshListener;
import com.cplatform.xhxw.ui.ui.comment.MyCommentFragment;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionUtil;
import com.cplatform.xhxw.ui.ui.main.cms.CommentReplyMeFragment;
import com.cplatform.xhxw.ui.ui.main.cms.CommentReplyMeFragment.OnShowMoreBtnClick;
import com.cplatform.xhxw.ui.util.DateUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.cplatform.xhxw.ui.util.StringUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 我的记录
 */
public class MyRecordActivity extends BaseActivity implements OnClickListener, 
		PullRefreshListener, OnShowMoreBtnClick {
	private PagerSlidingTabStrip mTabbar;
	private ViewPager mViewPager;
	private SectionsPagerAdapter mVPAdapter;
	
	//header
	private LinearLayout mHeaderLo;
	private TextView mNewsTitle;
	private ImageView mAvatar;
	private TextView mNickName;
	private TextView mCommentTime;
	private Button mPraiseCount;
	private TextView mCommentContent;
	private RelativeLayout mCommentDetailLo;
	private PullRefreshListView mCommentDetailList;
	private ImageView mHideDetailBtn;
	
	private boolean isDetailCommentShow = false;
	private List<ReplyComment> mReplyComments = new ArrayList<ReplyComment>();
	private CommentReplyMe mCommentInfo;
	private CommentDetailAdapter mDetailAdapter;
	
	private DisplayImageOptions opts = new DisplayImageOptions.Builder()
								.showStubImage(R.drawable.comment_userimage)
								.showImageForEmptyUri(R.drawable.comment_userimage)
								.showImageOnFail(R.drawable.comment_userimage)
								.displayer(new RoundedBitmapDisplayer(5)).cacheInMemory()
								.cacheOnDisc().build();
	
	private String[] mTabNames = new String[]{"我的评论", "回复我的", "我的投稿", "我的圈阅"};
	@Override
	protected String getScreenName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.saas_activity_my_record);
		initActionBar();
		initViews();
	}

	private void initViews() {
		mTabbar = (PagerSlidingTabStrip) findViewById(R.id.saas_my_record_tabbar);
		mViewPager = (ViewPager) findViewById(R.id.saas_my_record_vp);
		mVPAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mVPAdapter);
		
		mTabbar.setShouldExpand(true);
		mTabbar.setIndicatorColor(Color.rgb(65, 105, 225));
		mTabbar.setViewPager(mViewPager);
		
		mCommentDetailLo = (RelativeLayout) findViewById(R.id.saas_my_comment_detail_lo);
		mCommentDetailList = (PullRefreshListView) findViewById(R.id.saas_my_comment_detail_list);
		mHideDetailBtn = (ImageView) findViewById(R.id.saas_my_comment_collapse_detail);
		mHideDetailBtn.setOnClickListener(this);

		mCommentDetailList.setCanRefresh(false);
		mCommentDetailList.setCanLoadMore(true);
		mCommentDetailList.setPullRefreshListener(this);
		initCommentDetailListHeader();
		mDetailAdapter = new CommentDetailAdapter();
		mCommentDetailList.setAdapter(mDetailAdapter);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			if(isDetailCommentShow) {
				showOrHideDetailList(false);
				return true;
			}
		}
		return super.onKeyUp(keyCode, event);
	}
	
	/**
	 * 栏目适配器
	 */
	private class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			Fragment frag = null;
			if(getPageTitle(arg0).equals(mTabNames[1])) {
				CommentReplyMeFragment commentFrag = new CommentReplyMeFragment();
				commentFrag.setOnShowMoreBtnClickLis(MyRecordActivity.this);
				frag = commentFrag;
			} else if(getPageTitle(arg0).equals(mTabNames[2])) {
				frag = new MyRecordContributeFragment();
			} else if(getPageTitle(arg0).equals(mTabNames[3])) {
				frag = new MyRecordSignListFragment();
			} else if(getPageTitle(arg0).equals(mTabNames[0])) {
				frag = new MyCommentFragment();
			}
			return frag;
		}

		@Override
		public int getCount() {
			return mTabNames.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mTabNames[position];
		}
	}
	
	private void initCommentDetailListHeader() {
		LayoutInflater lif = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mHeaderLo = (LinearLayout) lif.inflate(R.layout.view_comment_list_header, null);
		mNewsTitle = (TextView) mHeaderLo.findViewById(R.id.comment_reply_news_title_tv);
		mAvatar = (ImageView) mHeaderLo.findViewById(R.id.comment_reply_my_avatar);
		mNickName = (TextView) mHeaderLo.findViewById(R.id.comment_reply_my_nickname);
		mCommentTime = (TextView) mHeaderLo.findViewById(R.id.comment_reply_my_sent_time);
		mPraiseCount = (Button) mHeaderLo.findViewById(R.id.comment_reply_praise_count);
		mCommentContent = (TextView) mHeaderLo.findViewById(R.id.comment_reply_my_content);
		mCommentDetailList.addHeaderView(mHeaderLo);
	}
	
	/**
     * 所有回复评论数据适配
     * @author jinyz
     *
     */
    private class CommentDetailAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return mReplyComments.size();
		}

		@Override
		public Object getItem(int position) {
			return mReplyComments.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vh;
			if(convertView == null) {
				vh = new ViewHolder();
				LayoutInflater lif = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = lif.inflate(R.layout.item_comment_detail, null);
				vh.avatar = (ImageView) convertView.findViewById(R.id.comment_reply_avatar);
				vh.content = (TextView) convertView.findViewById(R.id.comment_reply_reply_content);
				convertView.setTag(vh);
			} else {
				vh = (ViewHolder) convertView.getTag();
			}
			ReplyComment item = (ReplyComment) getItem(position);
			vh.content.setText("");
			vh.content.append(StringUtil.generateSpanComment(item.getNickname() + "回复："));
			vh.content.append(XWExpressionUtil.generateSpanComment(
					MyRecordActivity.this, item.getContent(), (int) vh.content.getTextSize()));
			
			ImageLoader.getInstance().displayImage(item.getLogo(), vh.avatar, opts);
			
			return convertView;
		}
		
		class ViewHolder {
			ImageView avatar;
			TextView content;
		}
    }
    
    /**
	 * 显示已有内容，并开始加载更多内容
	 * @param data
	 */
	private void displayData(CommentReplyMe data) {
		mCommentInfo = data;
		mReplyComments.clear();
		mReplyComments.addAll(data.getList());
		mNewsTitle.setText(data.getNewsTitle());
		ImageLoader.getInstance().displayImage(data.getLogo(), mAvatar, opts);
		mNickName.setText(data.getNickname());
		mCommentTime.setText(DateUtil.getCommentFormattedDate(Long.valueOf(data.getPublished())));
		mPraiseCount.setText(data.getPraiseCount());
		mCommentContent.setText(XWExpressionUtil.generateSpanComment(
				MyRecordActivity.this, data.getContent(), (int) mCommentContent.getTextSize()));
		
		mDetailAdapter.notifyDataSetChanged();
		showOrHideDetailList(true);
		mCommentDetailList.tryLoadMore();
	}
	
	/**
	 * 加载更多回复
	 */
	private void loadMoreReply() {
		GetMoreReplyRequest request = new GetMoreReplyRequest();
		request.setCommentid(mCommentInfo.getId());
		request.setReplyid(mReplyComments.size() > 0 ? mReplyComments.get(mReplyComments.size() -1).getId() : "");
		
		APIClient.getMoreReply(request, new AsyncHttpResponseHandler(){
			boolean isSuccess = false;
			@Override
			public void onFinish() {
				if(isSuccess) {
					mCommentDetailList.setCanLoadMore(false);
				} else {
					mCommentDetailList.setCanLoadMore(true);
				}
				mCommentDetailList.onLoadMoreComplete();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				GetMoreReplyResponse response = null;
				try {
					ResponseUtil.checkResponse(content);
                    response = new Gson().fromJson(content, GetMoreReplyResponse.class);
				} catch (Exception e) {
					showToast(R.string.data_format_error);
                    LogUtil.w("comment reply", e);
				}
				if(response == null) {
					return;
				}
				
				if(response.isSuccess()) {
					isSuccess = true;
					List<ReplyComment> resdata = response.getData();
					mReplyComments.addAll(resdata);
					mDetailAdapter.notifyDataSetChanged();
					mCommentDetailList.setCanLoadMore(false);
				} else {
					showToast(response.getMsg());
				}
			}

			@Override
			public void onFailure(Throwable error, String content) {
				showToast(R.string.load_server_failure);
			}
		});
	}
	
	private void showOrHideDetailList(boolean isToShow) {
		if(isToShow) {
			mCommentDetailLo.setVisibility(View.VISIBLE);
			isDetailCommentShow = true;
		} else {
			mCommentDetailLo.setVisibility(View.GONE);
			mCommentDetailList.setCanLoadMore(true);
			mReplyComments.clear();
			mDetailAdapter.notifyDataSetChanged();
			isDetailCommentShow = false;
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.saas_my_comment_collapse_detail:
			showOrHideDetailList(false);
			break;
		default:
			break;
		}
	}

	@Override
	public void onShowMoreClick(CommentReplyMe data) {
		displayData(data);
	}

	@Override
	public void onRefresh() {
		
	}

	@Override
	public void onLoadMore() {
		loadMoreReply();
	}
}
