package com.cplatform.xhxw.ui.ui.detailpage.expressions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.db.dao.ContactsDao;
import com.cplatform.xhxw.ui.provider.XwContentProvider;
import com.cplatform.xhxw.ui.util.SelectNameUtil;

public class XWExpressionUtil {
	/**
	 * 从配置文件中读取所有表情信息
	 * 
	 * @param con
	 * @return
	 */
	public static ArrayList<XWExpression> parseExprList(Context con, String iniFileName) {
		ArrayList<XWExpression> result = new ArrayList<XWExpression>();
		BufferedReader reader = null;
		try {
			InputStreamReader isr = new InputStreamReader(con.getAssets().open(iniFileName));
			reader = new BufferedReader(isr);
			String line = reader.readLine();
			while (line != null) {
				String[] parts = line.split(";");
				if (parts.length != 2) {
					line = reader.readLine();
					continue;
				}
				String fileName = parts[0].substring(0,
						parts[0].lastIndexOf("."));
				int resId = getResIdByFilename(con, fileName);
				if (resId != 0) {
					XWExpression data = new XWExpression();
					data.setmImgResId(resId);
					data.setmExprInfo(parts[1]);
					result.add(data);
				}
				line = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	public static HashMap<String, Integer> getExprNameAndId(List<XWExpression> data) {
		if(data == null) return null;
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for(XWExpression expr : data) {
			map.put(expr.getmExprInfo(), expr.getmImgResId());
		}
		return map;
	}

	/**
	 * 根据表情文件名获取资源文件id
	 * 
	 * @param con
	 * @param filenname
	 * @return
	 */
	private static int getResIdByFilename(Context con, String filenname) {
		return con.getResources().getIdentifier(filenname, "drawable",
				con.getPackageName());
	}

	/**
	 * 处理评论信息，将符合条件的文字转化为图片显示
	 * @param con
	 * @param sourceComment
	 * @param map
	 * @return
	 */
	public static final SpannableString generateSpanComment(Context con,
			String sourceComment, int textSize) {
		SpannableString result = new SpannableString(sourceComment);
		String regular = "\\[[^\\]]+\\]";
		Pattern pattern = Pattern.compile(regular, Pattern.CASE_INSENSITIVE);
		try {
			dealExpression(con, result, pattern, 0, textSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

    /**
     * 处理评论信息，将符合条件的文字转化为图片显示
     * @param con
     * @param result
     * @param textSize
     * @return
     */
    public static final SpannableString generateSpanComment(Context con,
                                                            SpannableString result, int textSize) {
        String regular = "\\[[^\\]]+\\]";
        Pattern pattern = Pattern.compile(regular, Pattern.CASE_INSENSITIVE);
        try {
            dealExpression(con, result, pattern, 0, textSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

	/**
	 * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
	 * 
	 * @param context
	 * @param spannableString
	 * @param patten
	 * @param start
	 * @throws Exception
	 */
	private static void dealExpression(Context context,
			SpannableString spannableString, Pattern pattern, int start,
			int textSize) throws Exception {
		Matcher matcher = pattern.matcher(spannableString);
		
		HashMap<String, Integer> exprInfoIdValueCN = XWExpressionManager.getInstance()
				.getmExprInfoIdValuesCN(context);
		HashMap<String, Integer> exprInfoIdValueEN = XWExpressionManager.getInstance()
				.getmExprInfoIdValuesEN(context);
		while (matcher.find()) {
			String key = matcher.group();
			// 返回第一个字符的索引的文本匹配整个正则表达式,ture 则继续递归
			if (matcher.start() < start) {
				continue;
			}
			
			int resIdCN = exprInfoIdValueCN.get(key) == null ? 0 : exprInfoIdValueCN.get(key);
			int resIdEN = exprInfoIdValueEN.get(key) == null ? 0 : exprInfoIdValueEN.get(key);
			int resId = Math.max(resIdCN, resIdEN);
			if (resId == 0) {
				continue;
			}
			Drawable drawa = context.getResources().getDrawable(resId);
			drawa.setBounds(0, 0, textSize, textSize);
			ImageSpan imageSpan = new ImageSpan(drawa, ImageSpan.ALIGN_BOTTOM);
			int end = matcher.start() + key.length();
			spannableString.setSpan(imageSpan, matcher.start(), end,
					Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
			if (end < spannableString.length()) {
				dealExpression(context, spannableString, pattern, end, textSize);
			}
			break;
		}
	}
	
	/**
	 * 删除字符串末尾的一个字符或者表情
	 * @param content
	 * @return
	 */
	public static String deleteOneWord(String content, HashMap<String, Integer> map) {
		String ss = content;
		if(ss.length() <= 0) return ss;
		if(content.endsWith("]")) {
			int index = content.lastIndexOf("[");
			if(index >= 0) {
				String exprName = content.substring(index);
				if(map.get(exprName) != null) {
					ss = ss.substring(0, index);
				}
			} else {
				ss = ss.substring(0, ss.length() - 1);
			}
		} else {
			ss = ss.substring(0, ss.length() - 1);
		}
		return ss;
	}

    /**
     * 获得昵称
     * @param context
     * @param userId
     * @return 优先返回RENAME,其次 NICK_NAME,最后NAME
     */
    public static String getUserName(Context context, String userId) {
        String[] projection = {ContactsDao.NAME, ContactsDao.NICK_NAME, ContactsDao.COMMENT};

        String selection = ContactsDao.USER_ID+" = ? AND "+ContactsDao.MY_UID+" = ? ";
        String[] selectionArgs = {userId, Constants.getUid()};
        Cursor cursor = context.getContentResolver().query(XwContentProvider.CONTACTS_URI, projection, selection, selectionArgs, null);
        String name = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int iIndex = cursor.getColumnIndex(ContactsDao.COMMENT);
                name = cursor.getString(iIndex);
                if (TextUtils.isEmpty(name)) {
                    iIndex = cursor.getColumnIndex(ContactsDao.NICK_NAME);
                    name = cursor.getString(iIndex);
                    if (TextUtils.isEmpty(name)) {
                        iIndex = cursor.getColumnIndex(ContactsDao.NAME);
                        name = cursor.getString(iIndex);
                    }
                }
            }
            cursor.close();
        }
        return name;
    }
}
