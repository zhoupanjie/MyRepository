package com.cplatform.xhxw.ui.util;

import android.text.TextUtils;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class Cn2SpellUtil {


    /**
     * 汉字转换位汉语拼音，英文字符不变
     *
     * @param chines 汉字
     * @return 拼音
     */
    private static String converterToSpell(String chines) {
        if (TextUtils.isEmpty(chines)) {
            return "";
        }
        chines = chines.replace(" ","");
        String pinyinName = "";
        char[] nameChar = chines.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < nameChar.length; i++) {
            if (nameChar[i] > 128) {
                try {
                    pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0];
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                pinyinName += nameChar[i];
            }
        }
        return pinyinName;
    }

    public static String converterToSpellToUpperCase(String comment, String nickName, String name) {
        String orderKey = null;
        if (!TextUtils.isEmpty(comment)) {
            orderKey = Cn2SpellUtil.converterToSpell(comment);
        } else if (!TextUtils.isEmpty(nickName)) {
            orderKey = Cn2SpellUtil.converterToSpell(nickName);
        } else if (!TextUtils.isEmpty(name)) {
            orderKey = Cn2SpellUtil.converterToSpell(name);
        }
        if (TextUtils.isEmpty(orderKey)) {
            orderKey = "#";
        }
        return orderKey.toUpperCase();
    }
}
