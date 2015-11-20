package com.cplatform.xhxw.ui.util;

import java.util.regex.Pattern;

/**
 * 正则判断
 * Created by cy-love on 14-1-15.
 */
public class RegexUtil {

    public static final String EMAIL_REGEX = "^[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.([A-Za-z]{2,})$";
    public static final String MOBILE_REGEX = "^1\\d{10}$";
    public static final String ENGLISH_REGEX = "^[A-Za-z]+$";

    public static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    public static final Pattern MOBILE_PATTERN = Pattern.compile(MOBILE_REGEX);
    public static final Pattern ENGLISH_PATTERN = Pattern.compile(ENGLISH_REGEX);

    /**
     * 判断是否为手机号码
     */
    public static boolean isMobileNum(String mobiles) {
        return MOBILE_PATTERN.matcher(mobiles).matches();
    }

    /**
     * 判断邮箱是否正确
     */
    public static boolean isEmail(String mobiles) {
        return EMAIL_PATTERN.matcher(mobiles).matches();
    }

    /**
     * 判断是否为英文字符
     * @param str
     * @return
     */
    public static boolean isEnglish(String str) {
        return ENGLISH_PATTERN.matcher(str).matches();
    }
}
