package com.cplatform.xhxw.ui.ui.settings;

import java.util.List;

import com.cplatform.xhxw.ui.App;
import com.cplatform.xhxw.ui.PreferencesManager;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.APIClient;
import com.cplatform.xhxw.ui.http.ResponseUtil;
import com.cplatform.xhxw.ui.http.net.BaseRequest;
import com.cplatform.xhxw.ui.http.net.GetUserSettingPropertiesResponse;
import com.cplatform.xhxw.ui.model.CareerOrBloodOptions;
import com.cplatform.xhxw.ui.model.KeyValueParams;
import com.cplatform.xhxw.ui.model.UserInfo;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class UserinfoUtil {
	public static final String KEY_VALUE_SPERATOR = "---";
	
	/**
	 * 获取职业选项列表和血型选项列表
	 */
	public static final void getUserSettingProperties() {
		APIClient.getUserSettingProperties(new BaseRequest(), 
				new AsyncHttpResponseHandler(){

					@Override
					public void onSuccess(int statusCode, String content) {
						GetUserSettingPropertiesResponse response;
						try {
							ResponseUtil.checkResponse(content);
							response = new Gson().fromJson(content, GetUserSettingPropertiesResponse.class);
						} catch (Exception e) {
							return;
						}
						
						if(response.isSuccess()) {
							CareerOrBloodOptions data = response.getData();
							List<KeyValueParams> mCareers = data.getCareer();
							List<KeyValueParams> mBlood = data.getBlood();
							saveUserProperties(mCareers, true);
							saveUserProperties(mBlood, false);
						}
					}
		});
	}
	
	/**
	 * 存储获取的职业列表或者血型列表
	 * @param data
	 * @param isCareer
	 */
	public static void saveUserProperties(List<KeyValueParams> data, boolean isCareer) {
		if(isCareer) {
			PreferencesManager.saveCareerList(App.CONTEXT, composeString(data));
		} else {
			PreferencesManager.saveBloodList(App.CONTEXT, composeString(data));
		}
	}
	
	private static String composeString(List<KeyValueParams> data) {
		StringBuilder sb = new StringBuilder();
		if(data == null) {
			return null;
		}
		
		for(KeyValueParams par : data) {
			sb.append(par.getKey());
			sb.append(KEY_VALUE_SPERATOR);
			sb.append(par.getValue());
			sb.append(",");
		}
		
		if(sb.length() > 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}
	
	/**
	 * 根据键值获取性别信息
	 * @param key
	 * @return
	 */
	public static String getSexBykey(int key) {
		String sex = "未知";
		if(key == 1) {
			sex = "男";
		} else if(key == 2) {
			sex = "女";
		}
		return sex;
	}
	
	/**
	 * 根据键值获取职业或血型信息
	 * @param key
	 * @param isCareer
	 * @return
	 */
	public static String getCareerOrBloodByKey(int key, boolean isCareer) {
		String result = "未知";
		String content = null;
		if(isCareer) {
			content = PreferencesManager.getCareerList(App.CONTEXT);
		} else {
			content = PreferencesManager.getBloodList(App.CONTEXT);
		}
		if(content == null || content.length() <= 0) {
			return result;
		}
		
		String[] keyValues = content.split(",");
		String starts = key + KEY_VALUE_SPERATOR;
		for(String item : keyValues) {
			if(item.startsWith(starts)) {
				result = item.split(KEY_VALUE_SPERATOR)[1];
				break;
			}
		}
		return result;
	}
	
	/**
	 * 根据账户类型获取
	 * @param accountType
	 * @return
	 */
	public static final int getImageSourceByAccountType(int accountType) {
		int sourceId = R.drawable.ic_launcher;
		switch (accountType) {
		case UserInfo.ACCOUNT_TYPE_SINAWEIBO:
			sourceId = R.drawable.account_sinaweibo;
			break;
		case UserInfo.ACCOUNT_TYPE_RENREN:
			sourceId = R.drawable.account_renren;
			break;
		case UserInfo.ACCOUNT_TYPE_QQ:
			sourceId = R.drawable.account_qq;
			break;
		case UserInfo.ACCOUNT_TYPE_TENCENTWEIBO:
			sourceId = R.drawable.account_tencentweibo;
			break;

		default:
			break;
		}
		return sourceId;
	}
}
