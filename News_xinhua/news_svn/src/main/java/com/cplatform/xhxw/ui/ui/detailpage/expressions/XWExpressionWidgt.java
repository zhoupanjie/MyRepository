package com.cplatform.xhxw.ui.ui.detailpage.expressions;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.os.Build;
import com.cplatform.xhxw.ui.R;
import com.viewpagerindicator.CirclePageIndicator;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;

public class XWExpressionWidgt extends LinearLayout {
	
	private static final int EXPR_COUNT_PER_PAGE = 20;
	
	private Context mCon;
	
	private ArrayList<XWExpression> mExpressions;
	private ArrayList<XWExpressionGridAdapter> mGridAdapters = new ArrayList<XWExpressionGridAdapter>();
	private int mCurrentExprPageIndex;
	private int mExprPageCount;
	private int mSoftKeyboardWid = 0;
	private int mSoftKeyboardHei = 0;
	
	private ViewPager mExprViewPager;
	private CirclePageIndicator mExprIndicator;
	private onExprItemClickListener mOnExprItemClickListener;
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public XWExpressionWidgt(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mCon = context;
	}

	public XWExpressionWidgt(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mCon = context;
	}

	public XWExpressionWidgt(Context context) {
		super(context);
		this.mCon = context;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		initExpressions();
	}

	/**
	 * 初始化表情相关
	 */
	private void initExpressions() {
		LayoutInflater lf = (LayoutInflater) mCon.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = lf.inflate(R.layout.view_comment_expressions, null);
		mExprViewPager = (ViewPager) v.findViewById(R.id.comment_expression_container);
		mExprIndicator = (CirclePageIndicator) v.findViewById(R.id.comment_expr_indicator);
		
		LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(mSoftKeyboardWid, mSoftKeyboardHei);
		
		mExpressions = XWExpressionManager.getInstance().getmExprsCN(mCon);
		mExprViewPager.setAdapter(new XWExpressionViewPagerAdapter(mCon, 
				initExprGridView()));
		initIndicator();
		removeAllViews();
		addView(v, lllp);
	}
	
	private void initIndicator() {
		float density = getResources().getDisplayMetrics().density;
		mExprIndicator.setViewPager(mExprViewPager);
		mExprIndicator.setOnPageChangeListener(mOnpageChange);
		mExprIndicator.setCurrentItem(0);
		mExprIndicator.notifyDataSetChanged();
		mExprIndicator.setRadius(6 * density);
		mExprIndicator.setFillColor(Color.rgb(18, 155, 251));
		mExprIndicator.setStrokeWidth(1 * density);
		mExprIndicator.setStrokeColor(Color.LTGRAY);
	}
	
	private void onResize() {
		LinearLayout.LayoutParams lllp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, mSoftKeyboardHei * 3 / 4);
		mExprViewPager.setLayoutParams(lllp);
		
		int indicatorHeight = mSoftKeyboardHei / 4;
		LinearLayout.LayoutParams lllpIndicator = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, indicatorHeight);
		lllpIndicator.gravity = Gravity.CENTER;
		mExprIndicator.setPadding(0, indicatorHeight / 2 - 10, 0, 0);
		mExprIndicator.setLayoutParams(lllpIndicator);
		for(int i = 0; i < mGridAdapters.size(); i++) {
			mGridAdapters.get(i).setmViewHeight(mSoftKeyboardHei * 3 / 4);
			mGridAdapters.get(i).setmViewWidth(mSoftKeyboardWid / 7);
		}
	}
	
	/**
	 * 初始化表情GridView
	 * @return
	 */
	private List<GridView> initExprGridView() {
		List<GridView> result = new ArrayList<GridView>();
		mExprPageCount = (int) Math.ceil(mExpressions.size() * 1.0 / EXPR_COUNT_PER_PAGE);
		XWExpression deleteIcon = new XWExpression();
		deleteIcon.setmExprInfo(null);
		deleteIcon.setmImgResId(R.drawable.expression_delete);
		for(int i = 0; i < mExprPageCount; i++) {
			List<XWExpression> data = getExprByPageIndex(i);
			data.add(deleteIcon);
			GridView view = new GridView(mCon);
			view.setNumColumns(7);
			XWExpressionGridAdapter adapter = new XWExpressionGridAdapter(data, mCon);
			mGridAdapters.add(adapter);
			view.setAdapter(adapter);
			view.setBackgroundColor(Color.TRANSPARENT);
			view.setHorizontalSpacing(1);
			view.setVerticalSpacing(1);
			view.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
			view.setCacheColorHint(0);
			view.setPadding(5, 0, 5, 0);
			view.setGravity(Gravity.CENTER);
			view.setOnItemClickListener(onExprItemClick);
			view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,  
                    LayoutParams.MATCH_PARENT));
			result.add(view);
		}
		return result;
	}
	
	private List<XWExpression> getExprByPageIndex(int pageIndex) {
		int total = mExpressions.size();
		int end = ((pageIndex + 1) * 20 > total) ? total : (pageIndex + 1) * 20;
		List<XWExpression> page = new ArrayList<XWExpression>();
		for(int i = pageIndex * 20; i < end; i++) {
			page.add(mExpressions.get(i));
		}
		return page;
	}
	
	public onExprItemClickListener getmOnExprItemClickListener() {
		return mOnExprItemClickListener;
	}

	public void setmOnExprItemClickListener(onExprItemClickListener mOnExprItemClickListener) {
		this.mOnExprItemClickListener = mOnExprItemClickListener;
	}

	OnItemClickListener onExprItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			int clickIndexOfAllExprs = mCurrentExprPageIndex * 20 + arg2;
			boolean isDel = (arg2 == EXPR_COUNT_PER_PAGE || clickIndexOfAllExprs >= mExpressions.size());
			XWExpression clickedExpr = null;
			if(!isDel) {
				clickedExpr = mExpressions.get(clickIndexOfAllExprs);
				mOnExprItemClickListener.onExprItemClick(clickedExpr.getmExprInfo(), isDel);
			} else {
				mOnExprItemClickListener.onExprItemClick(null, isDel);
			}
		}
	};
	
	OnPageChangeListener mOnpageChange = new OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int arg0) {
			mCurrentExprPageIndex = arg0;
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};
	
	public void setKeyboardSize(int wid, int hei) {
		this.mSoftKeyboardWid = wid;
		this.mSoftKeyboardHei = hei;
		initExpressions();
		onResize();
	}
	
	public interface onExprItemClickListener {
		void onExprItemClick(String exprInfo, boolean isDelete);
	}
}
