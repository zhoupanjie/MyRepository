package com.hy.superemsg.adapter.collections;

import android.content.Context;
import android.view.View;

import com.hy.superemsg.R;
import com.hy.superemsg.adapter.BookGridAdapter;
import com.hy.superemsg.db.DBUtils;
import com.hy.superemsg.db.DBUtils.DBHelper;
import com.hy.superemsg.rsp.AnimationContentDetail;

public class CartoonCollectionAdapter extends BookGridAdapter {

	public CartoonCollectionAdapter(Context ctx) {
		super(ctx);
	}

	@Override
	public void getView(int position, View convertView,
			final AnimationContentDetail data) {
		super.getView(position, convertView, data);
		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				DBUtils.getInst().remove(DBHelper.TABLE_SMS_COLLECTION, data);
				notifyDataSetChanged();
			}
		});
	}
}
