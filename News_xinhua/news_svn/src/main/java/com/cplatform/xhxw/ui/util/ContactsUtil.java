package com.cplatform.xhxw.ui.util;

import org.json.JSONException;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.text.TextUtils;

/**
 * 联系人获取
 */
public class ContactsUtil {

	private static final String[] PHONES_PROJECTION = new String[] {  
		Phone.DISPLAY_NAME, Phone.NUMBER, Phone.CONTACT_ID };

	/**得到手机通讯录联系人信息
	 * @throws org.json.JSONException
	 */  
	public static String getPhoneContacts(Context context, ContentResolver  cr) throws JSONException {
		StringBuffer list = new StringBuffer();
		list.append("[");
		// 获取手机联系人
		Cursor phoneCursor = cr.query(Phone.CONTENT_URI,PHONES_PROJECTION, null, null, null);
		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {  
				//得到手机号码  
				String phoneNumber = phoneCursor.getString(1);  
				//当手机号码为空的或者为空字段 跳过当前循环  
				if (TextUtils.isEmpty(phoneNumber))  
					continue;
				if (phoneNumber.startsWith("+86")) {
					phoneNumber = phoneNumber.substring(3);
				}
				phoneNumber = phoneNumber.replaceAll("-", "");
				phoneNumber = phoneNumber.replaceAll(" ", "");
				//得到联系人名称  
				String contactName = phoneCursor.getString(0);  
				if (list.length() > 1) {
					list.append(",");
				}
				list.append("{\"name\":\"").append(contactName).append("\",\"phone\":\"").append(phoneNumber).append("\"}");
			}  
			phoneCursor.close(); 
		}  
		list.append("]");
		return list.toString();
	}  

}
