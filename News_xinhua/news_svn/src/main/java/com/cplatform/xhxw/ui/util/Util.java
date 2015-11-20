package com.cplatform.xhxw.ui.util;

import java.util.UUID;

import com.cplatform.xhxw.ui.App;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

/**
 * Created by cy-love on 14-8-28.
 */
public class Util {

    public static int string2Int(String str) {
        try {
            return Integer.valueOf(str);
        } catch (Exception e) {}
        return 0;
    }

    public static long string2Long(String str) {
        try {
            return Long.valueOf(str);
        } catch (Exception e) {}
        return 0;
    }

    public static void copy(Context context, String str) {
        ClipboardManager clip = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            clip.setText(str); // 复制
        } else {
            clip.setPrimaryClip(ClipData.newPlainText("copy", str));
        }
    }

    public static String getMyUUID(){
    	  final TelephonyManager tm = (TelephonyManager) App.CONTEXT.getSystemService(Context.TELEPHONY_SERVICE);   
    	  final String tmDevice, tmSerial, androidId;
    	  tmDevice = "" + tm.getDeviceId();  
    	  tmSerial = "" + tm.getSimSerialNumber();   
    	  androidId = "" + android.provider.Settings.Secure.getString(App.CONTEXT.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);   
    	  UUID deviceUuid = new UUID(androidId.hashCode(), (long)tmDevice.hashCode() << 32);   
//    	  UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
    	  String uniqueId = deviceUuid.toString();
//    	  LogUtil.e("debug","uuid="+uniqueId + " android_id>>>>>" + androidId + 
//    			  " deviceId-----" + tmDevice + " serial----->>>>" + tmSerial);
    	  return uniqueId;
    	 }
}
