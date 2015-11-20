package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.db.dao.ContactsDao;
import com.cplatform.xhxw.ui.db.dao.NewFriendsDao;
import com.cplatform.xhxw.ui.db.dao.SMessageChatDao;
import com.cplatform.xhxw.ui.db.dao.SMessageDao;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.DeleteFriendsRequest;
import com.cplatform.xhxw.ui.provider.XwContentProvider;
import com.cplatform.xhxw.ui.ui.base.BaseActivity;
import com.cplatform.xhxw.ui.util.LogUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * 修改用户信息
 */
public class UpdateInfoActivity extends BaseActivity implements OnClickListener{

	private static final String TAG = UpdateInfoActivity.class.getSimpleName();

	private EditText editText;
	private View updateBtn;
	private Button deleteBtn;
	
	private String friendsId;
	private String friendsName;
	
	private AsyncHttpResponseHandler mHttpResponseHandler;
	
	/** 暂时定为 总共可以输入16个字 */
	private static final int INPUTCOUNT = 16;
	
	public static Intent newIntent(Context context, String friendsId, String friendsName) {
		Intent intent = new Intent();
		intent.putExtra("friendsId", friendsId);
		intent.putExtra("friendsName", friendsName);
		intent.setClass(context, UpdateInfoActivity.class);
		return intent;
	}
	
	@Override
	protected String getScreenName() {
		return TAG;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_update_info);
		
		init();
		
		setListener();
	}

	private void init() {
		initActionBar();
		editText = (EditText) findViewById(R.id.update_sure_edit);
		updateBtn = findViewById(R.id.update_sure);
		deleteBtn = (Button) findViewById(R.id.delete_friends);
		
		friendsId = getIntent().getStringExtra("friendsId");
		friendsName = getIntent().getStringExtra("friendsName");
        if (!TextUtils.isEmpty(friendsName)) {
            editText.setText(friendsName);
            editText.setSelection(friendsName.length());
        }
        
        editText.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
				INPUTCOUNT) });
	}
	
	private void setListener() {
		updateBtn.setOnClickListener(this);
		deleteBtn.setOnClickListener(this);
	}
	

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.update_sure:
			if (!TextUtils.isEmpty(editText.getText().toString().trim())) {
				updateFriendRemark(friendsId);
			}else {
				Toast.makeText(UpdateInfoActivity.this, "请输入备注信息", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.delete_friends:
				deleteDialog(friendsId, friendsName);
			break;
		default:
			break;
		}
	}
	
	/** 更新好友备注 */
	private void updateFriendRemark(String userId) {

		UpdateFriendRemarkRequest request = new UpdateFriendRemarkRequest();
		request.setUserid(userId);
		request.setComment(editText.getText().toString().trim());
		request.setDevRequest(true);
		
		APIClient.updateFriendRemark(request, new AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				mHttpResponseHandler = null;
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
				UpdateFriendRemarkResponse response;
				try {
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content,
							UpdateFriendRemarkResponse.class);
				} catch (Exception e) {
					showToast(R.string.data_format_error);
					LogUtil.w(TAG, e);
					return;
				}
				if (response.isSuccess()) {
					if (response != null) {
						
						/**改变此人的备注名称*/
						ContentValues values = new ContentValues();
						values.put(NewFriendsDao.RENAME, editText.getText().toString().trim());
						String selectionFriend = NewFriendsDao.USER_ID + " = ? And " + NewFriendsDao.MY_UID + " = ? ";
						String[] projectionFriend = new String[] { friendsId, Constants.getUid()};
						getContentResolver().update(XwContentProvider.NEW_FRIENDS_URL,values, selectionFriend, projectionFriend);
						
						ContentValues contactValues = new ContentValues();
                        contactValues.put(ContactsDao.COMMENT, editText.getText().toString().trim());
						String selectionContact = ContactsDao.USER_ID + " = ? And " + ContactsDao.MY_UID + " = ? ";
						String[] projectionContact = new String[] { friendsId, Constants.getUid()};
						getContentResolver().update(XwContentProvider.CONTACTS_URI,contactValues, selectionContact, projectionContact);

                        ContentValues sMessageValues = new ContentValues();
                        sMessageValues.put(SMessageDao.COMMENT, editText.getText().toString().trim());
                        String sMessageContact = SMessageDao.ROOM_ID + " = ? And " + SMessageDao.MY_UID + " = ? ";
                        String[] sMessageProjection = new String[] { friendsId, Constants.getUid()};
                        getContentResolver().update(XwContentProvider.S_MESSAGE_URI,sMessageValues, sMessageContact, sMessageProjection);


						Intent intent = new Intent();
						intent.putExtra("remark", editText.getText().toString().trim());
						setResult(Activity.RESULT_OK, intent);
						UpdateInfoActivity.this.finish();
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

	/** 删除好友 */
	private void deleteFriends(final String friendId) {

		DeleteFriendsRequest request = new DeleteFriendsRequest();
		request.setUserid(friendId);
		request.setDevRequest(true);
		
		APIClient.deleteFriends(request, new AsyncHttpResponseHandler() {

			@Override
			public void onFinish() {
				hideLoadingView();
				
				mHttpResponseHandler = null;
				
				hideLoadingView();
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
				UpdateFriendRemarkResponse response;
				try {
					ResponseUtil.checkResponse(content);
					response = new Gson().fromJson(content,
							UpdateFriendRemarkResponse.class);
				} catch (Exception e) {
					showToast(R.string.data_format_error);
					LogUtil.w(TAG, e);
					return;
				}
				if (response.isSuccess()) {
					if (response != null) {
						/**将此人从数据库里面删除*/
						String selectionFriend = NewFriendsDao.USER_ID + " = ? " + " And " + NewFriendsDao.MY_UID + " = ? ";
						String[] projectionFriend = new String[] { friendId, Constants.getUid() };
						getContentResolver().delete(XwContentProvider.NEW_FRIENDS_URL, selectionFriend, projectionFriend);
						
						String selectionContact = ContactsDao.USER_ID + " = ? " + " And " + ContactsDao.MY_UID + " = ? ";
						String[] projectionContact = new String[] { friendId, Constants.getUid()};
						getContentResolver().delete(XwContentProvider.CONTACTS_URI, selectionContact, projectionContact);

                        String selectionSMessage = SMessageDao.ROOM_ID + " = ? " + " And " + SMessageDao.MY_UID + " = ? ";
                        String[] projectionSMessage = new String[] { friendId, Constants.getUid()};
                        getContentResolver().delete(XwContentProvider.S_MESSAGE_URI, selectionSMessage, projectionSMessage);

                        String selectionSMessageChat = SMessageChatDao.ROOM_ID + " = ? " + " And " + SMessageChatDao.MY_UID + " = ? ";
                        String[] projectionSMessageChat = new String[] { friendId, Constants.getUid()};
                        getContentResolver().delete(XwContentProvider.S_MESSAGE_CHAT_URI, selectionSMessageChat, projectionSMessageChat);
						
						Intent intent = new Intent();
						intent.putExtra("remark", "");
						setResult(Activity.RESULT_OK, intent);
						UpdateInfoActivity.this.finish();
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

	private void deleteDialog(final String friendsId, String friendName) {
		new DeleteFriendDialog(this, friendName,
				new DeleteFriendDialog.OnDeleteFriendDialogListener() {
					
					@Override
					public void deleteFriendDialog() {
						deleteFriends(friendsId);
					}
				}).show();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	
		if (mHttpResponseHandler != null) {
			mHttpResponseHandler.cancle();
		}
	}
}
