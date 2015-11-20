package com.cplatform.xhxw.ui.util;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;

/**
 * Created by cy-love on 13-12-31.
 */
public class StringUtil {

	public static StringBuffer getStringBuffer(Object... obj) {
		StringBuffer buf = new StringBuffer();
		for (Object tmp : obj)
			buf.append(tmp);
		return buf;
	}

	public static String getString(Object... obj) {
		return getStringBuffer(obj).toString();
	}

	public static int getLength(String str) {
		if (TextUtils.isEmpty(str)) {
			return 0;
		}
		return str.length();
	}

	/**
	 * 字符串转整数
	 * 
	 * @param source
	 * @return 为空或者转型失败时返回默认数值0
	 */
	public static int parseIntegerFromString(String source) {
		int result = 0;
		if (TextUtils.isEmpty(source)) {
			return result;
		}

		try {
			result = Integer.valueOf(source);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * 生成评论回复人显示字符串
	 * 
	 * @param sourceComment
	 * @return
	 */
	public static SpannableString generateSpanComment(String sourceComment) {
		SpannableString spanString = new SpannableString(sourceComment);
		ForegroundColorSpan span = new ForegroundColorSpan(Color.rgb(0, 178,
				238));
		spanString.setSpan(span, 0, sourceComment.length(),
				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		spanString.setSpan(new AbsoluteSizeSpan(14, true), 0,
				sourceComment.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spanString;
	}

	/**
	 * 判断两个String是否相同
	 * 
	 * @param obj1
	 *            String1
	 * @param obj2
	 *            String2
	 * @return true为相同
	 */
	public static boolean isEquals(String obj1, String obj2) {
		return (!TextUtils.isEmpty(obj1) && obj1.equals(obj2));
	}

}
