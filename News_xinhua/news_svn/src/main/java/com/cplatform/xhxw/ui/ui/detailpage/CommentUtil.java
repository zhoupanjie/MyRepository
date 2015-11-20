package com.cplatform.xhxw.ui.ui.detailpage;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.SendCommentOrReplyPersonRequest;
import com.cplatform.xhxw.ui.http.net.SendCommentOrReplyPersonResponse;
import com.cplatform.xhxw.ui.location.LocationClientController;
import com.cplatform.xhxw.ui.location.LocationUtil;
import com.cplatform.xhxw.ui.model.Comment;
import com.cplatform.xhxw.ui.model.LocationPoint;
import com.cplatform.xhxw.ui.util.KeyboardUtil;
import com.cplatform.xhxw.ui.util.NetUtils;
import com.cplatform.xhxw.ui.util.NetUtils.Status;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class CommentUtil {

	/**
	 * 生成评论发送的请求参数
	 * @param con
	 * @param content
	 * @param newsId
	 * @param mLocationClient
	 * @param commentToReply
	 * @return
	 */
	public static SendCommentOrReplyPersonRequest getRequest(Context con, String content, 
			String newsId, LocationClientController mLocationClient, Comment commentToReply,
			boolean isEnterprise) {

		if (NetUtils.getNetworkState() == Status.NONE) {
			Toast.makeText(con,
					con.getResources().getString(R.string.network_invalid),
					Toast.LENGTH_LONG).show();
			return null;
		}
		if (TextUtils.isEmpty(content)) {
			Toast.makeText(
					con,
					con.getResources().getString(
							R.string.comment_content_demand),
					Toast.LENGTH_SHORT).show();
			return null;
		}

		SendCommentOrReplyPersonRequest request = new SendCommentOrReplyPersonRequest();
		request.setNewsId(Integer.valueOf(newsId));
		request.setContent(content);
		if(isEnterprise) {
			request.setSaasRequest(true);
			request.setLogo(Constants.userInfo.getLogo());
			request.setNickname(Constants.userInfo.getNickName());
		}
		LocationPoint point = LocationUtil.getAPosition(con, mLocationClient);
		if (point != null) {
			request.setLongitude(point.getLongitude());
			request.setLatitude(point.getLatitude());
		}

		if (commentToReply != null) {
			request.setUid(commentToReply.getSenderId());
			request.setCommentid(commentToReply.getId());
		}
		return request;
	}

	/**
	 * 发送评论数据
	 * @param act
	 * @param request
	 * @param edit
	 */
	public static void sendCommentData(final Activity act, SendCommentOrReplyPersonRequest request,
			final EditText edit, final NewsPageFragment frag) {
		final ProgressDialog pd = ProgressDialog.show(act, null, "发送中，请稍候");
		APIClient.sendCommentOrReplyPerson(request,
				new AsyncHttpResponseHandler() {

					@Override
					public void onFinish() {
						pd.dismiss();
					}

					@Override
					protected void onPreExecute() {
					}

					@Override
					public void onSuccess(String content) {
						SendCommentOrReplyPersonResponse response;
						try {
							ResponseUtil.checkResponse(content);
							response = new Gson().fromJson(content,
									SendCommentOrReplyPersonResponse.class);
						} catch (Exception e) {
							return;
						}

						if (response.isSuccess()) {
							edit.setText("");
							Toast.makeText(act, "发送成功", Toast.LENGTH_LONG).show();
							frag.controlKeyboardOrExpr(false, false);
							KeyboardUtil.hideKeyboard(act, edit);
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						Toast.makeText(act, "发送失败", Toast.LENGTH_LONG).show();
					}
				});
	}
}
