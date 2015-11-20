package com.cplatform.xhxw.ui.ui.detailpage.expressions;

import java.util.List;

import com.cplatform.xhxw.ui.util.LogUtil;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class XWExpressionGridAdapter extends BaseAdapter {
	private List<XWExpression> mResource;
	private Context mCon;
	private int mViewHeight = 0;
	private int mViewWidth = 0;

	public XWExpressionGridAdapter(List<XWExpression> mResource, Context mCon) {
		super();
		this.mResource = mResource;
		this.mCon = mCon;
	}

	@Override
	public int getCount() {
		return mResource.size();
	}

	@Override
	public Object getItem(int position) {
		return mResource.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null) {
			LinearLayout ll = new LinearLayout(mCon);
			ll.setPadding(10, 0, 10, 10);
			ll.setGravity(Gravity.CENTER);
			ll.setLayoutParams(new LayoutParams(mViewWidth, mViewHeight));
			
			ImageView iv = new ImageView(mCon);
			LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			iv.setPadding(10, 0, 10, 0);
			ll.addView(iv, llp);
			convertView = ll;
			convertView.setTag(iv);
		}
		((ImageView)convertView.getTag()).setImageResource(((XWExpression)getItem(position)).getmImgResId());
		return convertView;
	}
	
	public int getmViewHeight() {
		return mViewHeight;
	}

	public void setmViewHeight(int totalHeight) {
		this.mViewHeight = totalHeight / 3;
	}

	public int getmViewWidth() {
		return mViewWidth;
	}

	public void setmViewWidth(int mViewWidth) {
		this.mViewWidth = mViewWidth;
	}
}
