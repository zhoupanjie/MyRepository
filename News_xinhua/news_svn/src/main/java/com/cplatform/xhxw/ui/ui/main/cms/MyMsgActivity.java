package com.cplatform.xhxw.ui.ui.main.cms;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
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
import com.cplatform.xhxw.ui.db.dao.ChanneDao;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.GetMoreReplyRequest;
import com.cplatform.xhxw.ui.http.net.GetMoreReplyResponse;
import com.cplatform.xhxw.ui.model.CommentReplyMe;
import com.cplatform.xhxw.ui.model.ReplyComment;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.base.view.EnterpriseChannelBar;
import com.cplatform.xhxw.ui.ui.base.view.EnterpriseChannelBar.onChannelClickLisenter;
import com.cplatform.xhxw.ui.ui.base.widget.PullRefreshListView;
import com.cplatform.xhxw.ui.ui.base.widget.PullRefreshListView.PullRefreshListener;
import com.cplatform.xhxw.ui.ui.cyancomment.CYanReplyMeCommentFragment;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionUtil;
import com.cplatform.xhxw.ui.ui.main.cms.CommentReplyMeFragment.OnShowMoreBtnClick;
import com.cplatform.xhxw.ui.util.DateUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.cplatform.xhxw.ui.util.StringUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class MyMsgActivity extends BaseActivity implements onChannelClickLisenter,
		OnClickListener, OnShowMoreBtnClick, PullRefreshListener{
	private EnterpriseChannelBar mMenuBar;
	private ViewPager mViewPage;
	private RelativeLayout mCommentDetailLo;
	private PullRefreshListView mCommentDetailList;
	private ImageView mHideDetailBtn;
	
	//header
	private LinearLayout mHeaderLo;
	private TextView mNewsTitle;
	private ImageView mAvatar;
	private TextView mNickName;
	private TextView mCommentTime;
	private Button mPraiseCount;
	private TextView mCommentContent;
	private Button mBackBtn;
	
	private int mCurrentIndex = 0;
	private List<ChanneDao> mMsgChannels;
	
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_my_msg);
		super.onCreate(savedInstanceState);
		initView();
	}
	
	private void initView() {
		initMsgChannel();
		mMenuBar = (EnterpriseChannelBar) findViewById(R.id.my_msg_menu_bar);
		mMenuBar.setmChannels(mMsgChannels);
		mViewPage = (ViewPager) findViewById(R.id.my_msg_pager);
		mCommentDetailLo = (RelativeLayout) findViewById(R.id.my_msg_show_detail_lo);
		mCommentDetailList = (PullRefreshListView) findViewById(R.id.my_msg_show_detail_list);
		mHideDetailBtn = (ImageView) findViewById(R.id.my_msg_collapse_detail);
		mHideDetailBtn.setOnClickListener(this);
		mBackBtn = (Button) findViewById(R.id.comment_reply_back_ben);
		mBackBtn.setOnClickListener(this);
		
		mViewPage.setOnPageChangeListener(mOnPageChangeListener);
		mViewPage.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), mMsgChannels));
		mMenuBar.setChannelSelected(mCurrentIndex);
		mMenuBar.setOnChannelClickLis(this);
		mViewPage.setCurrentItem(mCurrentIndex);
		
		mCommentDetailList.setCanRefresh(false);
		mCommentDetailList.setCanLoadMore(true);
		mCommentDetailList.setPullRefreshListener(this);
		initCommentDetailListHeader();
		mDetailAdapter = new CommentDetailAdapter();
		mCommentDetailList.setAdapter(mDetailAdapter);
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

	private void initMsgChannel() {
		mMsgChannels = new ArrayList<ChanneDao>();
		ChanneDao replyMeChan = new ChanneDao();
		replyMeChan.setChannelID("0");
		replyMeChan.setChannelName("回复我的");
		replyMeChan.setListorder(0);
		
		ChanneDao sysMsgChan = new ChanneDao();
		sysMsgChan.setChannelID("1");
		sysMsgChan.setChannelName("全部通知");
		sysMsgChan.setListorder(1);
		
		mMsgChannels.add(replyMeChan);
		mMsgChannels.add(sysMsgChan);
	}
	
	OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int arg0) {
			mCurrentIndex = arg0;
			mMenuBar.setChannelSelected(mCurrentIndex);
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected String getScreenName() {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public void onChannelClicked(int channelIndex) {
		mViewPage.setCurrentItem(channelIndex);
	}

	/**
     * 栏目内容适配器
     */
    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
    	private List<ChanneDao> mChaanels;
        public ViewPagerAdapter(FragmentManager fm, List<ChanneDao> chaanels) {
            super(fm);
            mChaanels = chaanels;
        }

        @Override
        public Fragment getItem(int position) {
            int index = position % mChaanels.size();
            ChanneDao cd = mChaanels.get(index);
            if(cd.getChannelID().equals("0")) {
            	CYanReplyMeCommentFragment fragment = new CYanReplyMeCommentFragment();
                return fragment;
            } else if(cd.getChannelID().equals("1")) {
            	SystemMsgShowFragment pf = new SystemMsgShowFragment();
            	return pf;
            } else {
            	return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            int index = position % mChaanels.size();
            return mChaanels.get(index).getChannelName();
        }

        @Override
        public int getCount() {
            return mChaanels.size();
        }
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
					MyMsgActivity.this, item.getContent(), (int) vh.content.getTextSize()));
			
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
				MyMsgActivity.this, data.getContent(), (int) mCommentContent.getTextSize()));
		
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
		case R.id.my_msg_collapse_detail:
			showOrHideDetailList(false);
			break;
		case R.id.comment_reply_back_ben:
			finish();
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
