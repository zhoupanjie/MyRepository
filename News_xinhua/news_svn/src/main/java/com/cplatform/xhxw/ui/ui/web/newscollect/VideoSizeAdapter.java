package com.cplatform.xhxw.ui.ui.web.newscollect;

import java.util.List;

import android.hardware.Camera.Size;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class VideoSizeAdapter extends BaseAdapter {

	private List<Size> sizes; 
	
	public VideoSizeAdapter(List<Size> sizes) {
		this.sizes = sizes;
	}

	public void set(List<Size> sizes) {
		this.sizes.clear();
		this.sizes = sizes;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return sizes != null ? sizes.size() : 0;
	}

	@Override
	public Size getItem(int position) {
		return sizes.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView view = new TextView(parent.getContext());
		view.setText(String.format("%sx%s", sizes.get(position).width, sizes.get(position).height));
		view.setEllipsize(TruncateAt.END);
		view.setPadding(16, 16, 16, 16);
		return view;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		TextView view = new TextView(parent.getContext());
		view.setText(String.format("%sx%s", sizes.get(position).width, sizes.get(position).height));
		view.setPadding(16, 16, 16, 16);
		return view;
	}

}
