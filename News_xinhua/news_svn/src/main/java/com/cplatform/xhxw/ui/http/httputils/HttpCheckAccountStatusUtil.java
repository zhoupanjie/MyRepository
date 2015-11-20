package com.cplatform.xhxw.ui.http.httputils;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.Constants.FileName;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.CheckAccountStatusRequest;
import com.cplatform.xhxw.ui.http.net.CheckAccountStatusResponse;
import com.cplatform.xhxw.ui.http.net.CheckAccountStatusResponse.Status;
import com.cplatform.xhxw.ui.model.UserInfo;
import com.cplatform.xhxw.ui.util.FileUtil;
import com.cplatform.xhxw.ui.util.StringUtil;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.content.Context;
import android.text.TextUtils;

public class HttpCheckAccountStatusUtil {

	/**
	 * 获取当前缓存账户在服务器状态，如两方状态不同，清除本地账户缓存
	 * @param con
	 */
	public static void checkAccountStatus(final Context con) {
		final UserInfo cachedUserInfo = getCachedUserId(con);
		if(cachedUserInfo == null) {
			return;
		}
		
		CheckAccountStatusRequest request = new CheckAccountStatusRequest();
		request.setSaasRequest(false);
		request.setUserId(cachedUserInfo.getUserId());
		APIClient.getAccountStatus(request, new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(String content) {
				CheckAccountStatusResponse response;
				try {
					ResponseUtil.checkResponse(content);
	                response = new Gson().fromJson(content, CheckAccountStatusResponse.class);
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				
				if(response.isSuccess()) {
					checkAccountStatus(con, response.getData(), cachedUserInfo);
				}
			}
		});
	}
	
	private static void checkAccountStatus(Context con, Status status, UserInfo userInfo) {
		if(status == null) {
			return;
		}
		if(status.getStatus() != StringUtil.parseIntegerFromString(userInfo.getType())) {
			FileUtil.writeFile(con, FileName.USER_INFO, "");
		} else {
			if(Constants.hasEnterpriseAccountLoggedIn()) {
				Constants.userInfo.getEnterprise().setAliases(status.getAliases());
				Constants.userInfo.getEnterprise().setLogo(status.getLogo());
			}
			FileUtil.writeFile(con, FileName.USER_INFO, "");
			Constants.saveUserInfo();
		}
	}
	
	/**
	 * 获取本地缓存的用户信息
	 * @param con
	 * @return
	 */
	public static UserInfo getCachedUserId(Context con) {
		UserInfo userInfo = null;
		String userInfoText = FileUtil.readFile(con,
                FileName.USER_INFO);
		if (!TextUtils.isEmpty(userInfoText)) {
            try {
            	ResponseUtil.checkResponse(userInfoText);
                userInfo = new Gson().fromJson(userInfoText, UserInfo.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		
		return userInfo;
	}
}
