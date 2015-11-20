package com.cplatform.xhxw.ui.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
	/**
	 * 创建密码的Md5
	 * 
	 * @param
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	public static String toMD5(String password) {

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		md.update(password.getBytes());

		byte byteData[] = md.digest();

		// convert the byte to hex format method 2
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			String hex = Integer.toHexString(0xff & byteData[i]);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}

	public static String getMd5(String string) {
		try {  
            MessageDigest md = MessageDigest.getInstance("MD5");
            String result = MD5(string,md);
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  
        }
		return ""; 
	}
	
	public static String MD5(String strSrc, MessageDigest md) {  
        byte[] bt = strSrc.getBytes();  
        md.update(bt);  
        String strDes = bytes2Hex(md.digest()); // to HexString  
        return strDes;  
    }  
	
    private static String bytes2Hex(byte[] bts) {  
        StringBuffer des = new StringBuffer();  
        String tmp = null;  
        for (int i = 0; i < bts.length; i++) {  
            tmp = (Integer.toHexString(bts[i] & 0xFF));  
            if (tmp.length() == 1) {  
                des.append("0");  
            }  
            des.append(tmp);  
        }  
        return des.toString();  
    }  
}
