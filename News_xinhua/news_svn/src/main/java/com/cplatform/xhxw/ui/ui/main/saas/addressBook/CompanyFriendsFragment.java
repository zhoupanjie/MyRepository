package com.cplatform.xhxw.ui.ui.main.saas.addressBook;


import android.content.*;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.httputils.HttpHanderUtil;
import com.cplatform.xhxw.ui.http.net.DeleteFriendsFreshsMoodCommentRequest;
import com.cplatform.xhxw.ui.http.net.DeleteFriendsFreshsMoodCommentRsponse;
import com.cplatform.xhxw.ui.http.net.saas.*;
import com.cplatform.xhxw.ui.model.CompanyZoneCommentData;
import com.cplatform.xhxw.ui.model.saas.CommentSubData;
import com.cplatform.xhxw.ui.model.saas.CompanyZoneItemParisaData;
import com.cplatform.xhxw.ui.model.saas.CompanyZoneItemUserInfo;
import com.cplatform.xhxw.ui.model.saas.CompanyZoneList;
import com.cplatform.xhxw.ui.ui.base.BaseFragment;
import com.cplatform.xhxw.ui.ui.base.widget.DefaultView;
import com.cplatform.xhxw.ui.ui.base.widget.PullRefreshListView;
import com.cplatform.xhxw.ui.util.*;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 好友圈
 */
public class CompanyFriendsFragment extends BaseFragment {

    private static final String TAG = CompanyFriendsFragment.class.getSimpleName();
    private PullRefreshListView mListView;
    private DefaultView mDefView;
    private TextView mNew;
    private AsyncHttpResponseHandler mHttpHandler, mOptionHandler;
    private CompanyFriendsAdapter mAdapter;
    private MyReceiver mReceiver;

    private CompanyZoneList mTmpItem;
    private CompanyZoneCommentData mTmpUser;
    private OnCommunityCommentaryListener mComLis;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_company_circle,
                container, false);
        mAdapter = new CompanyFriendsAdapter(getActivity(), new OnCommentClickListener() {
            @Override
            public void onCommentClick(int posi, Object tag, int index) {
                if (tag != null && tag instanceof CompanyZoneCommentData) {
                    CompanyZoneCommentData item = (CompanyZoneCommentData) tag;
                    if (item.getSenduser() == null) {
                        return;
                    }

                    CompanyZoneItemUserInfo user = item.getSenduser();
                    if (Constants.getUid().equals(user.getUserid())) { // 删除评论
                        final CompanyZoneList dataTmp = mAdapter.getData().get(posi);
                        final CompanyZoneCommentData itemTmp = item;
                        AlertDialogUtil.showAlert(getActivity(), getString(R.string.is_del_comment), getString(R.string.confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delComment(dataTmp, itemTmp);
                            }
                        });
                    } else { // 回复评论
                        CompanyZoneList data = mAdapter.getData().get(posi);
                        mTmpItem = data;
                        mTmpUser = item;
                        if (mComLis != null) {
                            if (data.getUserinfo() == null) {
                                showToast("创建者为空");
                                return;
                            }
                            String infoUserId = data.getUserinfo().getUserid();
                            mComLis.onShowCommunityCommentary(false, data.getInfoid(), infoUserId, item.getSenduser().getUserid(), item.getCommentid(), "回复" + SelectNameUtil.getName(user.getComment(), user.getNickname(), user.getName()));
                        }
                    }
                }

            }

            @Override
            public void onUnComment() {

            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CompanyZoneList item = (CompanyZoneList) v.getTag();
                final CompanyOptionPop pop = new CompanyOptionPop(getActivity(), "1".equals(item.getParisa()));
                pop.setLis(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pop.dismiss();
                        likeAction(item);
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pop.dismiss();
                        if ("1".equals(item.getIscomment())) {
                            mTmpItem = item;
                            if (mComLis != null) {
                                if (item.getUserinfo() == null) {
                                    showToast("创建者为空");
                                    return;
                                }
                                String infoUserId = item.getUserinfo().getUserid();
                                mComLis.onShowCommunityCommentary(false, item.getInfoid(), infoUserId, null, null, "评论");
                            }
//                            startNoAnimActivityForResult(CommunityCommentaryActivity.newIntent(getActivity(), item.getInfoid(), null, null, "评论"), HomeActivity.REQUEST_CODE_COMPANY_CIRCLE_COMMENTARY);
                        } else {
                            showToast(R.string.disable_comments);
                        }
                    }
                });
                pop.show(v);
            }
        });
        mAdapter.setUserInfoListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = (String) v.getTag();
                mAct.startActivity(PersonalMoodActivity.newIntent(getActivity(), userId));
            }
        });
        mNew = (TextView) view.findViewById(R.id.tv_new);
        mNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNew.setVisibility(View.GONE);
                App.getPreferenceManager().setMsgNewFriendsCircleCount(0);
                startActivity(FriendsMessageActivity.newIntent(getActivity()));
            }
        });
        mListView = (PullRefreshListView) view.findViewById(R.id.list_view);
        mListView.setAdapter(mAdapter);
        mDefView = (DefaultView) view.findViewById(R.id.ly_defult);
        mDefView.setHidenOtherView(mListView);
        mDefView.setListener(new DefaultView.OnTapListener() {
            @Override
            public void onTapAction() {
                listRefresh(true);
            }
        });
        return view;
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                handler.sendEmptyMessageDelayed(2, 20 * 1000);
                listRefresh(true);
            } else {
                if (mHttpHandler != null && mAdapter.getCount() == 0) {
                    mHttpHandler.cancle();
                    mHttpHandler = null;
                    mDefView.setStatus(DefaultView.Status.error);
                }
            }
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isButterKnife = false;
        mListView.setPullRefreshListener(new PullRefreshListView.PullRefreshListener() {
            @Override
            public void onRefresh() {
                listRefresh(false);
            }

            @Override
            public void onLoadMore() {
                listLoadMore();
            }
        });
        mListView.setCanRefresh(true);
        mDefView.setStatus(DefaultView.Status.loading);
        handler.sendEmptyMessageDelayed(1, 3 * 1000);
    }

    private void listRefresh(boolean isShowLoading) {
        loadData(1, "0", isShowLoading);
    }

    private void listLoadMore() {
        List<CompanyZoneList> list = mAdapter.getData();
        if (ListUtil.isEmpty(list)) {
            listRefresh(false);
        } else {
            loadData(2, list.get(list.size() - 1).getCtime(), false);
        }
    }

    /**
     * 获得数据
     */
    private void loadData(int type, final String infotime, final boolean isShowLoading) {
        APIClient.friendZoneList(new FriendZoneListRequest(type, infotime, Constants.LIST_LOAD_COUNT), new AsyncHttpResponseHandler() {

            @Override
            protected void onPreExecute() {
                if (isShowLoading) {
                    mDefView.setStatus(DefaultView.Status.loading);
                }
                HttpHanderUtil.cancel(mHttpHandler);
                mHttpHandler = this;
            }

            @Override
            public void onFinish() {
                mHttpHandler = null;
                mListView.onLoadMoreComplete();
                mListView.onRefreshComplete(null);
                if (isShowLoading && mAdapter.getCount() == 0) {
                    mDefView.setStatus(DefaultView.Status.error);
                } else if (mDefView.getStatus() != DefaultView.Status.showData) {
                    mDefView.setStatus(DefaultView.Status.showData);
                }
            }

            @Override
            public void onSuccess(int statusCode, String content) {
                CompanyZoneListResponse response;
                try {
                    response = new Gson().fromJson(content, CompanyZoneListResponse.class);
                    ResponseUtil.checkObjResponse(response);
                } catch (Exception e) {
                    LogUtil.w(TAG, e);
                    showToast(R.string.data_format_error);
                    return;
                }

                if (response.isSuccess() && response.getData() != null) {
                    if ("0".equals(infotime)) {
                        mAdapter.clearData();
                    }
                    mAdapter.addAllData(response.getData().getList());
                    mAdapter.notifyDataSetChanged();
                    mDefView.setStatus(DefaultView.Status.showData);
                    if (ListUtil.isEmpty(response.getData().getList()) || response.getData().getList().size() < Constants.LIST_LOAD_COUNT) {
                        mListView.setCanLoadMore(false);
                    } else {
                        mListView.setCanLoadMore(true);
                    }
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                showToast(R.string.load_server_failure);
            }
        });
    }

    @Override
    public void onDestroyView() {
        HttpHanderUtil.cancel(mHttpHandler, mOptionHandler);
        handler.removeCallbacksAndMessages(null);
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
        mReceiver = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        updatemNewMsgCount();
        mReceiver = new MyReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, new IntentFilter(Actions.ACTION_MSG_NEW_UPDATE));
    }

    /**
     * 更新新消息数量提示
     */
    private void updatemNewMsgCount() {
        int count = App.getPreferenceManager().getMsgNewFriendsCircleCount();
        if (count > 0) {
            mNew.setVisibility(View.VISIBLE);
            count = count > 99 ? 99 : count;
            mNew.setText(count + "条未读消息");
        } else {
            mNew.setVisibility(View.GONE);
        }
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Actions.ACTION_MSG_NEW_UPDATE.equals(intent.getAction())) {
                updatemNewMsgCount();
            }
        }
    }

    /**
     * 评论回复
     */
    public void setCompanyCircleCommentary(CommentSubData result) {
        if (mTmpItem == null) {
            return;
        }
//        String infoId = mTmpItem.getInfoid();
//        String userid = intent.getStringExtra("userid");
        String commentid = result.getCommentid();
        String content = result.getContent();
        long ctime = Util.string2Long(result.getCtime());

        CompanyZoneItemUserInfo replyUser = null;
        if (mTmpUser != null) {
            CompanyZoneCommentData tmpItem = mTmpUser;
            replyUser = tmpItem.getSenduser();
        }

        CompanyZoneList data = mTmpItem;
        List<CompanyZoneCommentData> list = data.getCommentdata();
        if (list == null) {
            list = new ArrayList<CompanyZoneCommentData>();
            data.setCommentdata(list);
        }
        CompanyZoneCommentData item = new CompanyZoneCommentData();
        item.setCommentid(commentid);
        item.setContent(content);
        item.setCtime(ctime);
        CompanyZoneItemUserInfo sendUser = new CompanyZoneItemUserInfo();
        sendUser.setComment(Constants.userInfo.getNickName());
        sendUser.setUserid(Constants.userInfo.getUserId());
        item.setSenduser(sendUser);
        if (replyUser != null) {
            item.setReplyuser(replyUser);
        }
//        item.setPid();
        list.add(item);
        mAdapter.notifyDataSetChanged();
        mTmpUser = null;
        mTmpItem = null;
    }

    /**
     * 赞和取消赞接口
     *
     * @param item
     */
    private void likeAction(final CompanyZoneList item) {
        final CompanyZoneItemUserInfo userInfo = item.getUserinfo();

        if (userInfo == null) {
            showToast("创建者为空");
            return;
        }
        APIClient.friendZoneParisaSub(new FriendZoneParisaSubRequest(item.getInfoid(), item.getUserinfo().getUserid()), new AsyncHttpResponseHandler() {

            @Override
            protected void onPreExecute() {
                HttpHanderUtil.cancel(mOptionHandler);
                mOptionHandler = null;
                showLoadingView();
            }

            @Override
            public void onFinish() {
                mOptionHandler = null;
                hideLoadingView();
            }

            @Override
            public void onSuccess(int statusCode, String content) {
                FriendZoneParisaSubResponse response;
                try {
                    response = new Gson().fromJson(content, FriendZoneParisaSubResponse.class);
                    ResponseUtil.checkObjResponse(response);
                } catch (Exception e) {
                    showToast(R.string.data_format_error);
                    return;
                }
                if (response.isSuccess()) {
                    CompanyZoneItemParisaData parisa = item.getParisadata();
                    if (parisa == null) {
                        parisa = new CompanyZoneItemParisaData();
                        item.setParisadata(parisa);
                    }
                    List<CompanyZoneItemUserInfo> user = parisa.getList();
                    if (user == null) {
                        user = new ArrayList<CompanyZoneItemUserInfo>();
                        parisa.setList(user);
                    }
                    if (TextUtils.isEmpty(item.getParisa()) || !"1".endsWith(item.getParisa())) { // 赞
                        item.setParisa("1"); // 设置为已评论
                        CompanyZoneItemUserInfo item = new CompanyZoneItemUserInfo();
                        item.setUserid(Constants.getUid());
                        item.setNickname(Constants.userInfo.getNickName());
                        item.setLogo(Constants.userInfo.getLogo());
                        user.add(item);
                        showToast(R.string.like_success);
                    } else { // 取消赞
                        item.setParisa("0"); // 设置为已评论
                        for (CompanyZoneItemUserInfo userItem : user) {
                            if (Constants.getUid().endsWith(userItem.getUserid())) {
                                user.remove(userItem);
                                break;
                            }
                        }
                        showToast(R.string.cancel_like_success);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                showToast(R.string.load_server_failure);
            }
        });
    }

    public void setCommunityCommentaryListener(OnCommunityCommentaryListener mComLis) {
        this.mComLis = mComLis;
    }

    /**
     * 社区发布内容
     */
    public void sendCompanyZoneListItem(CompanyZoneList item) {
        mAdapter.getData().add(0, item);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 删除评论
     *
     * @param data 单条内容
     * @param item 内容下的评论
     */
    private void delComment(final CompanyZoneList data, final CompanyZoneCommentData item) {
        if (data.getUserinfo() == null || TextUtils.isEmpty(data.getUserinfo().getUserid())) {
            showToast(R.string.data_format_error);
            return;
        }
        DeleteFriendsFreshsMoodCommentRequest request = new DeleteFriendsFreshsMoodCommentRequest();
        request.setCommentid(item.getCommentid());
        request.setInfoid(data.getInfoid());
        request.setInfouserid(data.getUserinfo().getUserid());
        request.setDevRequest(true);
        APIClient.deleteFriendsFreshsComment(request, new AsyncHttpResponseHandler() {

            @Override
            protected void onPreExecute() {
                HttpHanderUtil.cancel(mOptionHandler);
                mOptionHandler = null;
                showLoadingView();
            }

            @Override
            public void onFinish() {
                mOptionHandler = null;
                hideLoadingView();
            }

            @Override
            public void onSuccess(int statusCode, String content) {
                LogUtil.d(content);
                DeleteFriendsFreshsMoodCommentRsponse response;
                try {
                    response = new Gson().fromJson(content, DeleteFriendsFreshsMoodCommentRsponse.class);
                    ResponseUtil.checkObjResponse(response);
                } catch (Exception e) {
                    showToast(R.string.data_format_error);
                    LogUtil.w(TAG, e);
                    return;
                }

                if (response.isSuccess()) {
                    data.getCommentdata().remove(item);
                    mAdapter.notifyDataSetChanged();
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
}
