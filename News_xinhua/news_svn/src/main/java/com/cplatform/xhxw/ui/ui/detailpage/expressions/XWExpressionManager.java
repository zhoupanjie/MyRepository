package com.cplatform.xhxw.ui.ui.detailpage.expressions;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

public class XWExpressionManager {
	private static final String EXPR_INI_FILENAME_CN = "xuanwenbq_cn.txt";
	private static final String EXPR_INI_FILENAME_EN = "xuanwenbq_en.txt";
	private static XWExpressionManager mXWExprM;
	private static ArrayList<XWExpression> mExprsCN;
	private static HashMap<String, Integer> mExprInfoIdValuesCN;
	private static ArrayList<XWExpression> mExprsEN;
	private static HashMap<String, Integer> mExprInfoIdValuesEN;
	
	private XWExpressionManager() {
	}
	
	public static XWExpressionManager getInstance() {
		if(mXWExprM == null) {
			mXWExprM = new XWExpressionManager();
		}
		return mXWExprM;
	}

	public ArrayList<XWExpression> getmExprsCN(Context con) {
		if(mExprsCN == null) {
			mExprsCN = XWExpressionUtil.parseExprList(con, EXPR_INI_FILENAME_CN);
		}
		return mExprsCN;
	}

	public void setmExprsCN(ArrayList<XWExpression> mExprs) {
		XWExpressionManager.mExprsCN = mExprs;
	}

	public HashMap<String, Integer> getmExprInfoIdValuesCN(Context con) {
		if(mExprInfoIdValuesCN == null) {
			mExprInfoIdValuesCN = XWExpressionUtil.getExprNameAndId(getmExprsCN(con));
		}
		return mExprInfoIdValuesCN;
	}

	public void setmExprInfoIdValuesCN(HashMap<String, Integer> mExprInfoIdValues) {
		XWExpressionManager.mExprInfoIdValuesCN = mExprInfoIdValues;
	}

	public ArrayList<XWExpression> getmExprsEN(Context con) {
		if(mExprsEN == null) {
			mExprsEN = XWExpressionUtil.parseExprList(con, EXPR_INI_FILENAME_EN);
		}
		return mExprsEN;
	}

	public void setmExprsEN(ArrayList<XWExpression> mExprsEN) {
		XWExpressionManager.mExprsEN = mExprsEN;
	}

	public HashMap<String, Integer> getmExprInfoIdValuesEN(Context con) {
		if(mExprInfoIdValuesEN == null) {
			mExprInfoIdValuesEN = XWExpressionUtil.getExprNameAndId(getmExprsEN(con));
		}
		return mExprInfoIdValuesEN;
	}

	public void setmExprInfoIdValuesEN(
			HashMap<String, Integer> mExprInfoIdValuesEN) {
		XWExpressionManager.mExprInfoIdValuesEN = mExprInfoIdValuesEN;
	}
}
