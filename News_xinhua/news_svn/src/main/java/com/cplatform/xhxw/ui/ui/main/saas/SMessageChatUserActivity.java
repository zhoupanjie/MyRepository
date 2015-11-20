package com.cplatform.xhxw.ui.ui.main.saas;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.db.dao.SMessageChatDao;
import com.cplatform.xhxw.ui.db.dao.SMessageDao;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.httputils.HttpHanderUtil;
import com.cplatform.xhxw.ui.provider.XwContentProvider;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.ui.main.saas.addressBook.FriendInfoActivity;
import com.cplatform.xhxw.ui.ui.main.saas.addressBook.FriendsInfoRequest;
import com.cplatform.xhxw.ui.ui.main.saas.addressBook.FriendsInfoResponse;
import com.cplatform.xhxw.ui.util.DialogUtil;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.cplatform.xhxw.ui.util.SelectNameUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 聊天人详细资料
 */
public class SMessageChatUserActivity extends BaseActivity {

    private static final String TAG = SMessageChatUserActivity.class.getSimpleName();

    private ImageView userImage;
    private TextView userName;
    private ImageView userSex;
    private TextView userNickName;
    private TextView userRemark;

    private String friendsId;
    private AsyncHttpResponseHandler mHttpResponseHandler;

    public static Intent newIntent(Context context, String friendsId) {
        Intent intent = new Intent();
        intent.setClass(context, SMessageChatUserActivity.class);
        intent.putExtra("friendsId", friendsId);
        return intent;
    }

    @Override
    protected String getScreenName() {
        return TAG;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_message_chat_user_info);
        init();
    }

    private void init() {
        initActionBar();
        userImage = (ImageView) findViewById(R.id.friend_info_image);
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(FriendInfoActivity.newIntent(SMessageChatUserActivity.this, friendsId, true));
            }
        });
        userName = (TextView) findViewById(R.id.friend_info_name);
        userSex = (ImageView) findViewById(R.id.friend_info_sex);
        userNickName = (TextView) findViewById(R.id.friend_info_nickname);
        userRemark = (TextView) findViewById(R.id.friend_info_remark);
        findViewById(R.id.btn_del_send_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogUtil.showAlertDialog(SMessageChatUserActivity.this, getString(R.string.clean_chat_tip), getString(R.string.clean), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String sMessageChatWhere = SMessageChatDao.ROOM_ID + " = ? AND " + SMessageChatDao.MY_UID + " = ? ";
                        String[] sMessageChatWhereArgs = {friendsId, Constants.getUid()};
                        getContentResolver().delete(XwContentProvider.S_MESSAGE_CHAT_URI, sMessageChatWhere, sMessageChatWhereArgs);

                        String sMessageWhere = SMessageDao.ROOM_ID + " = ? AND " + SMessageDao.MY_UID + " = ? ";
                        String[] sMessageWhereArgs = {friendsId, Constants.getUid()};
                        getContentResolver().delete(XwContentProvider.S_MESSAGE_URI, sMessageWhere, sMessageWhereArgs);
                        showToast(R.string.clean_chat_success);
                    }
                });

            }
        });
        friendsId = getIntent().getStringExtra("friendsId");
        getFriendsInfo(friendsId);
    }

    private void getFriendsInfo(String friendsId) {

        FriendsInfoRequest request = new FriendsInfoRequest();
        request.setUserid(friendsId);
        request.setDevRequest(true);

        APIClient.getFriendsInfo(request, new AsyncHttpResponseHandler() {

            @Override
            public void onFinish() {
                hideLoadingView();

                mHttpResponseHandler = null;
            }

            @Override
            protected void onPreExecute() {
                if (mHttpResponseHandler != null) {
                    mHttpResponseHandler.cancle();
                }
                mHttpResponseHandler = this;

                showLoadingView();
            }

            @Override
            public void onSuccess(String content) {
                FriendsInfoResponse response;
                try {
                    ResponseUtil.checkResponse(content);
                    response = new Gson().fromJson(content,
                            FriendsInfoResponse.class);
                } catch (Exception e) {
                    showToast(R.string.data_format_error);
                    LogUtil.w(TAG, e);
                    return;
                }
                if (response.isSuccess()) {
                    if (response != null && response.getData() != null) {
                        ImageLoader
                                .getInstance()
                                .displayImage(
                                        response.getData().getLogo(),
                                        userImage,
                                        DisplayImageOptionsUtil.avatarFriendsInfoImagesOptions);
//						userName.setText(SelectNameUtil.getName(response.getData().getName(), response.getData().getNickname(), response.getData().getName()));
                        userName.setText(response.getData().getName());
                        if ("1".equals(response.getData().getGender())) {// 男
                            userSex.setBackgroundResource(R.drawable.friends_man);
                        } else if ("2".equals(response.getData().getGender())) { // 女
                            userSex.setBackgroundResource(R.drawable.friends_man);
                        } else {// 未定义

                        }
                        userNickName.setText(response.getData().getNickname());
                        userRemark.setText(response.getData().getComment());
                    }
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

    @Override
    protected void onResume() {
        super.onResume();
//        if (Constants.hasEnterpriseAccountLoggedIn()) {
//            backBtn.setText(Constants.userInfo.getEnterprise().getAliases().getIm_alias());
//        }
    }

    @Override
    protected void onDestroy() {
        HttpHanderUtil.cancel(mHttpResponseHandler);
        super.onDestroy();
    }
}
