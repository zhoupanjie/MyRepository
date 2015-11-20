package com.hy.superemsg.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * �ṩ���������ת���� �Լ������ַ�������ʽ����
 * 1.�ַ����ת��
 * 2.�ַ�byte2XB��λת��
 * 3.�ַ�null����
 * 4.����ת��
 * 5.���������ת��
 * 6.�����ж�Email
 * .������������ַ�
 */
public class ConverUtil {

	public static String encode(String str) {
		return isEmpty(str) ? "" : java.net.URLEncoder.encode(str);
	}

	public static String subString(String str, int end) {
		if (null == str || end < 0) {
			return "";
		}
		return str.length() > end ? str.substring(0, end) : str;
	}

	public static String dateString2String(String str, String format) {
		try {
			return ConverUtil.dataFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str), format);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return "";
	}

	public static String date2String(Date _date) {
		if (null == _date) {
			_date = Calendar.getInstance().getTime();
		}

		return dataFormat(_date);
	}

	public static String dataFormat(Date date) {
		return dataFormat(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static String dataFormat(Date date, String format) {
		return null == date ? "" : new SimpleDateFormat(format).format(date);
	}

	public static Double string2DoubleScale(String str, int scale) {
		return ConverUtil.isEmpty(str) ? 0.0 : new BigDecimal(str).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() / 100;
	}

	public static Integer toDoubleScale(String str, int scale) {
		if (ConverUtil.isEmpty(str) || scale < 1) {
			return 0;
		}

		Double _double = new BigDecimal(str).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue() * 100;
		return _double.intValue();
	}

	public static double toDouble(Object obj, double def) {
		if (ConverUtil.isEmpty(obj)) {
			return 0;
		}
		try {
			return Double.parseDouble(obj.toString().trim());
		} catch (RuntimeException e) {
			return def;
		}
	}

	public static String byte2XB(float b) {
		int i = 1 << 10;
		if (b < i) {
			return b + "B";
		}
		i = 1 << 20;
		if (b < i)
			return calXB(b / (1 << 10)) + "KB";
		i = 1 << 30;
		if (b < i)
			return calXB(b / (1 << 20)) + "MB";
		return b + "B";
	}

	private static String calXB(float r) {
		String result = r + "";
		int index = result.indexOf(".");
		String s = result.substring(0, index + 1);
		String n = result.substring(index + 1);
		if (n.length() > 2)
			n = n.substring(0, 2);
		return s + n;
	}

	public static final float toFloat(Object val, float def) {
		if (val == null) {
			return def;
		}

		try {
			return Float.parseFloat(val.toString().trim());
		} catch (RuntimeException e) {
		}

		return 0f;
	}

	public static final int toInt(Object val, int def) {
		if (val == null) {
			return def;
		}

		try {
			return Integer.parseInt(val.toString().trim());
		} catch (RuntimeException e) {
			return def;
		}
	}

	public static final boolean toBool(Object val, boolean def) {
		if (val == null)
			return def;
		try {
			return Boolean.parseBoolean(val.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return def;
		}
	}

	public static final String toStr(Object val, String def) {
		if (val == null)
			return def;

		try {
			return val.toString().trim();
		} catch (RuntimeException e) {
			return def;
		}
	}

	public static String iso2Gbk(String str) {
		try {
			return new String(str.getBytes("iso-8859-1"), "GBK");
		} catch (Exception e) {
		}

		return "";
	}

	public static String gbk2iso(String str) {
		try {
			return new String(str.getBytes("GBK"), "iso-8859-1");
		} catch (Exception e) {
		}

		return "";
	}

	public static String maskNull(String str) {
		return (null == str ? "" : str);
	}

	public static boolean isEmpty(String str) {
		return (null == str || "".equals(str)) ? true : false;
	}

	public static boolean isEmpty(Object obj) {
		return obj == null;
	}

	public static final String maskUrl(String strUrl) {
		if (ConverUtil.isEmpty(strUrl)) {
			return "";
		}
		String url = strUrl.trim().replaceAll("&amp;", "&");
		url = url.replaceAll(" ", "%20").trim();
		if (ConverUtil.isEmpty(url))
			return "";
		if (!url.startsWith("http://")) {
			url = "http://" + url;
			return url;
		}
		return url;
	}

	public static boolean isEmail(String str) {
		if (isEmpty(str)) {
			return false;
		}

		Pattern pattern = Pattern.compile("^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
		Matcher mat = pattern.matcher(str);

		return mat.find();
	}

	public static boolean isSpeSign(String str) {
		if (isEmpty(str)) {
			return false;
		}

		Pattern pattern = Pattern.compile("[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~��@#��%����&*��������+|{}������������������������]");
		Matcher mat = pattern.matcher(str);

		return mat.find();
	}
}
