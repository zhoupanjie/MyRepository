package com.cplatform.xhxw.ui.util;

import android.content.pm.PackageManager;
import android.os.Bundle;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.cplatform.xhxw.ui.http.responseType.AppKey;
import com.cplatform.xhxw.ui.model.OauthVerify;

import java.util.Map;

/**
 * 第三方认证解析
 * Created by cy-love on 14-1-15.
 */
public class OauthVerifyUtil {

    /**
     * 返回数据解析
     * @param type 登录类型
     * @param info 用户信息
     * @return
     */
    public static OauthVerify doParse(SHARE_MEDIA type, Map<String, Object> info,String token) throws PackageManager.NameNotFoundException {
        switch (type) {
            case TENCENT:
                return tencentWeiboParse(info);
            case RENREN:
                return renrenParse(info);
            case QZONE:
                return qqParse(info);
            case WEIXIN:
            	return weixinParse(info,token);
            default:
                return sinaParse(info);
        }
    }

    private static OauthVerify weixinParse(Map<String, Object> info,String token) throws PackageManager.NameNotFoundException{
		// TODO Auto-generated method stub
    	  OauthVerify oa = new OauthVerify();
          oa.setAppkey(AppKey.WEIXIN);// weixin key
          oa.setOpenid(getStringValue(info, "openid"));
          oa.setOpenlogo(getStringValue(info, "headimgurl"));
          oa.setOpennickname(getStringValue(info, "nickname"));
          oa.setOpentoken(token);
          return oa;
	}

	/**
     * 获得qq(qzone)认证信息
     * @return
     */
    public static OauthVerify qqParse(Map<String, Object> info) throws PackageManager.NameNotFoundException {
        /**
         uid=
         screen_name=
         openid=
         profile_image_url=
         access_token=
         verified=0
         */
        OauthVerify oa = new OauthVerify();
        oa.setAppkey(AppKey.QQ);// qq key
        oa.setOpenid(getStringValue(info, "openid"));
        oa.setOpenlogo(getStringValue(info, "profile_image_url"));
        oa.setOpennickname(getStringValue(info, "screen_name"));
        oa.setOpentoken(getStringValue(info, "access_token"));
        return oa;
    }

    /**
     * 获得新浪认证信息
     * @return
     */
    public static OauthVerify sinaParse(Map<String, Object> info) throws PackageManager.NameNotFoundException {
        /*
         uid,
         favourites_count=1,
         location
         description
         verified=false,
         friends_count=509,
         gender=1,
         screen_name
         statuses_count=33,
         followers_count=18,
         profile_image_url
         access_token
         */
        OauthVerify oa = new OauthVerify();
        oa.setAppkey(AppKey.SINA);
        oa.setOpenid(getStringValue(info, "uid"));
        oa.setOpenlogo(getStringValue(info, "profile_image_url"));
        oa.setOpennickname(getStringValue(info, "screen_name"));
        oa.setOpentoken(getStringValue(info, "access_token"));
        return oa;
    }

    /**
     * 获得人人认证信息
     * @return
     */
    public static OauthVerify renrenParse(Map<String, Object> info) throws PackageManager.NameNotFoundException {
        /*
         uid
         gender=null
         screen_name
         location=
         profile_image_url
         access_token
         verified=null
         */
        OauthVerify oa = new OauthVerify();
        oa.setAppkey(AppKey.RENREN);
        oa.setOpenid(getStringValue(info, "uid"));
        oa.setOpenlogo(getStringValue(info, "profile_image_url"));
        oa.setOpennickname(getStringValue(info, "screen_name"));
        oa.setOpentoken(getStringValue(info, "access_token"));
        return oa;
    }

    /**
     * 腾讯微博
     * @param info
     * @return
     */
    public static OauthVerify tencentWeiboParse(Map<String, Object> info) throws PackageManager.NameNotFoundException {
        /*
        uid=
        birthday=
        location=
        statuses_count=349
        favourites_count=0
        verified=0
        description=
                friends_count=217
        gender=1
        screen_name=
        openid=
                followers_count=123
        profile_image_url=
        access_token=
        */
        OauthVerify oa = new OauthVerify();
        oa.setAppkey(AppKey.TENCENT_WEIBO);
        oa.setOpenid(getStringValue(info, "openid"));
        oa.setOpenlogo(getStringValue(info, "profile_image_url"));
        oa.setOpennickname(getStringValue(info, "screen_name"));
        oa.setOpentoken(getStringValue(info, "access_token"));
        return oa;
    }


    private static String getStringValue(Map<String, Object> info, String key) {
        Object value = info.get(key);
        return value.toString();
    }
}
