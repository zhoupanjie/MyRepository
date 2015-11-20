package com.cplatform.xhxw.ui.ui.cyancomment;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.ui.login.LoginActivity;
import com.sohu.cyan.android.sdk.api.Config;
import com.sohu.cyan.android.sdk.api.CyanSdk;
import com.sohu.cyan.android.sdk.push.PushManager;

import android.content.Context;
import android.graphics.Color;

public class CYanUtil {

	public static final void initCyan(Context con) {
		Config config = new Config();
		config.ui.toolbar_bg = Color.WHITE;
		config.comment.showScore = false;
		config.comment.uploadFiles = false;
		config.comment.useFace = true;
		config.comment.hotssize = 10;
		config.comment.latestsize = 10;
		config.comment.pagesize = 10;
        config.login.SSO_Assets_ICon = "xuanwen.png";
        config.login.SSOLogin = false;
        config.login.loginActivityClass = LoginActivity.class;
        String appid = "cyrCnR9J9"; 
        String appKey = "82cf80aec52ab2a7c9b0528c537fb4f2";
        config.login.Custom_oauth_login = false;
        config.login.QQ = false;
        config.login.SINA = false;
        config.login.SOHU = false;
        try {
        	CyanSdk.register(con, appid, appKey, "", config);
        	PushManager.setUseService(App.CONTEXT, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static final String getCyanNickName() {
		String nickName = "炫闻用户";
		try {
			String subUid = (Constants.getUid() == null) ? "" : Constants.getUid().substring(Constants.getUid().length() - 4);
			nickName = "炫闻用户" + subUid;
			if(Constants.hasLogin()) {
				nickName = Constants.userInfo.getNickName();
				if(nickName == null || nickName.length() == 0) {
					nickName = "炫闻用户" + subUid;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return nickName;
	}
}
