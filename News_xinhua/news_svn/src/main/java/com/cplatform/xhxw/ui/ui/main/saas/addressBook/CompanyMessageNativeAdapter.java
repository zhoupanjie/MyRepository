package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.Bind;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.db.dao.CompanyMessageDao;
import com.cplatform.xhxw.ui.db.dao.NewFriendsDao;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionUtil;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.cplatform.xhxw.ui.util.SelectNameUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CompanyMessageNativeAdapter extends CursorAdapter {

	private LayoutInflater mInflater;

	public CompanyMessageNativeAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);

		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
		View view = mInflater.inflate(R.layout.activity_message_item, null);
		ViewHolder holder = new ViewHolder(view);
		view.setTag(holder);
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {

		ViewHolder holder = (ViewHolder) view.getTag();

		ImageLoader.getInstance()
				.displayImage(
						cursor.getString(cursor
								.getColumnIndex(CompanyMessageDao.LOGO)),
						holder.userImage,
						DisplayImageOptionsUtil.avatarFriendsInfoImagesOptions);
		
		holder.userName
				.setText(SelectNameUtil.getName(cursor.getString(cursor
						.getColumnIndex(CompanyMessageDao.COMMENT)), cursor
						.getString(cursor
								.getColumnIndex(CompanyMessageDao.NICK_NAME)),
						cursor.getString(cursor
								.getColumnIndex(CompanyMessageDao.NAME))));

		holder.time.setText(cursor.getString(cursor
						.getColumnIndex(CompanyMessageDao.FRIENDSTIME)));
		
		holder.reply.setText(cursor.getString(cursor
				.getColumnIndex(CompanyMessageDao.BACKINFO)));
		
		holder.content.setText(XWExpressionUtil.generateSpanComment(
                mContext, cursor.getString(cursor
        				.getColumnIndex(CompanyMessageDao.INFODATA)),
                (int) holder.content.getTextSize()));
	}

	class ViewHolder {
		@Bind(R.id.message_item_logo)
		ImageView userImage;
		@Bind(R.id.message_item_name)
		TextView userName;
		@Bind(R.id.message_item_time)
		TextView time;
		@Bind(R.id.message_item_reply)
		TextView reply;
		@Bind(R.id.message_item_content)
		TextView content;

		public ViewHolder(View v) {
			ButterKnife.bind(this, v);
		}
	}

}
