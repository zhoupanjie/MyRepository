package com.hy.superemsg.utils;


import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;


public class StringUtils {

	public StringUtils() {
	}

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public static String[] split(String line, String delim) {
		int startindex = 0;
		Vector v = new Vector();
		String[] str = null;

		// �洢ȡ�Ӵ�ʱ��ʼλ��
		int index = 0;

		// ���ƥ���Ӵ���λ��
		startindex = line.indexOf(delim);

		String temp = "";
		while (startindex < line.length() && startindex != -1) {
			temp = line.substring(index, startindex);
			// if (temp != null && temp.length() != 0)
			v.addElement(temp);

			// ����ȡ�Ӵ�����ʼλ��
			index = startindex + delim.length();
			// ���ƥ���Ӵ���λ��
			startindex = line.indexOf(delim, startindex + delim.length());

		}

		// ȡ������Ӵ�
		// temp = line.substring(index + 1 - delim.length());
		temp = line.substring(index);
		if (temp != null && temp.length() != 0)
			v.addElement(temp);

		// ��vector����ת��������
		str = new String[v.size()];
		for (int i = 0; i < v.size(); i++) {
			str[i] = (String) v.elementAt(i);
		}
		// ������ɵ�����
		return str;
	}
    
	public static String str2UTF8(String str) {

		// ������������
		try {
			byte[] bytes = str.getBytes();

			str = new String(bytes, "UTF-8");
			// str = new String(bytes, "GB2312");
		} catch (java.io.UnsupportedEncodingException ue) {
			ue.printStackTrace();
		}
		return str;
	}
    
	public static String str2gb(byte[] bytes) {
		// ������������
		String str = "";
		try {
			str = new String(bytes, "GB2312");
		} catch (java.io.UnsupportedEncodingException ue) {
			ue.printStackTrace();
		}
		return str;
	}
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static String[] getFixlenStrArr(String str, int len, boolean fromhttp) {
    	if (str == null)
        	return  null;
    	
    	Vector v = new Vector();    	
    	int strlen = str.getBytes().length;
    	//System.out.println("strlen:" + strlen + " len:" + len);
    	
    	if (strlen <= len) {
    		String s[] = new String[1];
    		s[0] = str;
    		return s;
    	}
    	    	
    	String FixlenStr = "";
    	int pos = 0;
    	while (pos < str.length()) {
    		FixlenStr = getFixlenStr(str.substring(pos), len, fromhttp);
    		if (FixlenStr.length() > 0) {
    			v.addElement(FixlenStr);
    		}
    		pos += FixlenStr.length();
    		if (pos >= str.length() - 1)
    			break;
    		if (FixlenStr != null && FixlenStr.length() < len/2) {
    			if (str.charAt(pos) == '\r' && str.charAt(pos + 1) == '\n') {
    				pos += 2;
    			}
    			if (str.charAt(pos) == '\t') {
    				pos += 1;
    			}
    		}
    	}
    	
    	if (v.size() > 0) {
    		String s[] = new String[v.size()];
    		v.copyInto(s);
    		//System.out.println(s);
    		return s;    		
    	} else {
    		return null;
    	}
    }
    
	public static String getFixlenStr(String str, int len, boolean fromhttp) {
		if (str == null)
			return null;

		byte[] s = null;
		s = str.getBytes();
		/*
		 * try { s = str.getBytes("GBK"); } catch (UnsupportedEncodingException
		 * ue) { ue.printStackTrace(); }
		 */
		if (s.length <= len)
			return str;

		int gbflag = 0;
		int ngbflag = 0;

		// for (; i < len; i++) {
		// for (int i = 0; (gbflag + ngbflag) < len * 2; i++) {
		for (int i = 0; (gbflag + ngbflag) < len; i++) {
			// ���?��
			// if (s[i] == '\r' || s[i] == '\n' || s[i] == 9) {
			if (s[i] == '\r' || s[i] == '\n' || s[i] == '\t') {
				break;
			}
			if (s[i] < 0)
				gbflag++;
			else
				ngbflag++;
		}
		if (gbflag % 2 != 0)
			gbflag--;

		String fixlenStr = str.substring(0, gbflag / 2 + ngbflag);
		return fixlenStr;
	}
    /*
    public static String[] splitString(String str,int width){

    	Vector strVector = new Vector();
    	int start = 0;
    	int w = 0;
    	for(int i = 0; i < str.length(); i++){
    		
    		char ch = str.charAt(i);
    		if(w >= width){
    		    strVector.addElement(str.substring(start,i));
    		    start = i;
    		    w = 0;
    		}
    		
    	}
    	String endStr = str.substring(start);
    	if(endStr.length() > 0){
    		strVector.addElement(endStr);
    	}
    	String[] temp = new String[strVector.size()];
    	strVector.copyInto(temp);
    	return temp;
    }
    */
	
	public static String encodeUTF8(String value) {
		try {
			int strlen = value.length();
			StringBuffer out = new StringBuffer();
			for (int i = 0; i < strlen; i++) {
				char t = value.charAt(i);
				int c = 0;
				c |= (t & 0xffff);
				if (c >= 0 && c < 0x80) {
					switch (t) {
					case '=':
						out.append("%3d");
						break;
					case ' ':
						out.append("%20");
						break;
					case '+':
						out.append("%2b");
						break;
					case '\'':
						out.append("%27");
						break;
					case '/':
						out.append("%2F");
						break;
					case '.':
						out.append("%2E");
						break;
					case '<':
						out.append("%3c");
						break;
					case '>':
						out.append("%3e");
						break;
					case '#':
						out.append("%23");
						break;
					case '%':
						out.append("%25");
						break;
					case '&':
						out.append("%26");
						break;
					case '{':
						out.append("%7b");
						break;
					case '}':
						out.append("%7d");
						break;
					case '\\':
						out.append("%5c");
						break;
					case '^':
						out.append("%5e");
						break;
					case '~':
						out.append("%73");
						break;
					case '[':
						out.append("%5b");
						break;
					case ']':
						out.append("%5d");
						break;
					default:
						out.append(t);
						break;
					}
				} else if (c > 0x7f && c < 0x800) {
					out.append("%");
					out.append(byteArrayToHexString(new byte[] { (byte) (((c >>> 6) & 0x1f) | 0xc0) }));
					out.append("%");
					out.append(byteArrayToHexString(new byte[] { (byte) (((c >>> 0) & 0x3f) | 0x80) }));
				} else if (c > 0x7ff && c < 0x10000) {
					out.append("%");
					out.append(byteArrayToHexString(new byte[] { (byte) (((c >>> 12) & 0x0f) | 0xe0) }));
					out.append("%");
					out.append(byteArrayToHexString(new byte[] { (byte) (((c >>> 6) & 0x3f) | 0x80) }));
					out.append("%");
					out.append(byteArrayToHexString(new byte[] { (byte) (((c >>> 0) & 0x3f) | 0x80) }));
				} else if (c > 0x00ffff && c < 0xfffff) {
					out.append("%");
					out.append(byteArrayToHexString(new byte[] { (byte) (((c >>> 18) & 0x07) | 0xf0) }));
					out.append("%");
					out.append(byteArrayToHexString(new byte[] { (byte) (((c >>> 12) & 0x3f) | 0x80) }));
					out.append("%");
					out.append(byteArrayToHexString(new byte[] { (byte) (((c >>> 6) & 0x3f) | 0x80) }));
					out.append("%");
					out.append(byteArrayToHexString(new byte[] { (byte) (((c >>> 0) & 0x3f) | 0x80) }));
				}
			}
			return out.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return "";
	}
    
	static String byteArrayToHexString(byte in[]) {
		byte ch = 0x00;
		int i = 0;
		if (in == null || in.length <= 0)
			return null;

		String pseudo[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };
		StringBuffer out = new StringBuffer(in.length * 2);

		while (i < in.length) {
			ch = (byte) (in[i] & 0xF0); // Strip off high nibble
			ch = (byte) (ch >>> 4); // shift the bits down
			ch = (byte) (ch & 0x0F); // must do this is high order bit is on!

			out.append(pseudo[(int) ch]); // convert the nibble to a String
											// Character
			ch = (byte) (in[i] & 0x0F); // Strip off low nibble
			out.append(pseudo[(int) ch]); // convert the nibble to a String
											// Character
			i++;
		}

		String rslt = new String(out);
		return rslt;
	}
    
	public static String stringReplace(String sourceString, String toReplaceString, String replaceString) {
		String returnString = sourceString;
		int stringLength = 0;
		if (toReplaceString != null) {
			stringLength = toReplaceString.length();
		}
		if (returnString != null && returnString.length() > stringLength) {
			int max = 0;
			String S4 = "";
			for (int i = 0; i < sourceString.length(); i++) {
				max = i + toReplaceString.length() > sourceString.length() ? sourceString.length() : i + stringLength;
				String S3 = sourceString.substring(i, max);
				if (!S3.equals(toReplaceString)) {
					S4 += S3.substring(0, 1);
				} else {
					S4 += replaceString;
					i += stringLength - 1;
				}
			}
			returnString = S4;
		}
		return returnString;
	}

	public static boolean isEmpty(String str) {
		return str == null || "".equals(str);
	}
    
	public static String appendString(String[] strings) {
		if (strings != null && strings.length > 0) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < strings.length; i++) {
				sb.append(strings[i]);
			}
			return sb.toString();
		}
		return "";
	}
    
	public static String removeEnter(String string) {
		// Pattern p = Pattern.compile("\\s*|\t|\r|\n");
		// Pattern p = Pattern.compile("\n");
		Pattern p = Pattern.compile("\t|\r");
		// Pattern p = Pattern.compile("\r\n");

		Matcher m = p.matcher(string);
		String after = m.replaceAll("");
		return after;
	}

	public static String getStringFromXml(Context context, int id) {
		return context.getResources().getString(id);
	}
}
