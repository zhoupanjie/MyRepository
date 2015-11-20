package com.cplatform.xhxw.ui.ui.base.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.db.dao.SearchDao;
import com.cplatform.xhxw.ui.ui.base.BaseAdapter;

public class SearchRecordAdapter extends BaseAdapter<SearchDao>{

	public SearchRecordAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		final SearchDao search = getItem(position);
		ViewHodler hodler;
		if (view == null) {
			hodler = new ViewHodler();
			view = LayoutInflater.from(mContext).inflate(R.layout.view_search_record_item, null);
			
			hodler.textView = (TextView) view.findViewById(R.id.search_text);
			
			view.setTag(hodler);
		}else {
			hodler = (ViewHodler) view.getTag();
		}
		hodler.textView.setText(search.getName());
        int bgRes = position == 0 ? R.drawable.bg_list_top : R.drawable.bg_list_bom;
        view.setBackgroundResource(bgRes);
		return view;
	}

	private class ViewHodler{
		TextView textView;
	}
}
