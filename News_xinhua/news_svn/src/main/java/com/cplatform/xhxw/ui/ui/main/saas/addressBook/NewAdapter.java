package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.db.dao.NewFriendsDao;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.cplatform.xhxw.ui.util.SelectNameUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class NewAdapter extends CursorAdapter {

	private LayoutInflater mInflater;
	private OnAddFriendsListener listener;

	public NewAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);

		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
		View view = mInflater.inflate(R.layout.activity_new_friends_item, null);
		ViewHolder holder = new ViewHolder(view);
		view.setTag(holder);
		return view;
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		int iuserId = cursor.getColumnIndex(NewFriendsDao.USER_ID);
		int iinfoId = cursor.getColumnIndex(NewFriendsDao.INFO_ID);
		int iPhoto = cursor.getColumnIndex(NewFriendsDao.LOGO);
		int iName = cursor.getColumnIndex(NewFriendsDao.NAME);
		int iNick = cursor.getColumnIndex(NewFriendsDao.NICK_NAME);
		int iReName = cursor.getColumnIndex(NewFriendsDao.RENAME);
		int iComment = cursor.getColumnIndex(NewFriendsDao.COMMENT);
		int istatue = cursor.getColumnIndex(NewFriendsDao.STATUS);
		
		String photo = cursor.getString(iPhoto);
		String name = cursor.getString(iName);
		String nick = cursor.getString(iNick);
		String reName = cursor.getString(iReName);
		final String friendId = cursor.getString(iuserId);
		final String infoId = cursor.getString(iinfoId);
		String comment = cursor.getString(iComment);
		final String statue = cursor.getString(istatue);

		ViewHolder holder = (ViewHolder) view.getTag();

//		String nameStr = (!TextUtils.isEmpty(comment)) ? comment : (!TextUtils
//				.isEmpty(nick)) ? nick : name;
//		holder.userName.setText(nameStr);
		holder.userName.setText(SelectNameUtil.getName(reName, nick, name));
		holder.comment.setText(comment);
		ImageLoader.getInstance().displayImage(photo, holder.userImage,
				DisplayImageOptionsUtil.avatarFriendsListImagesOptions);

		if ("1".equals(statue)) {// 添加
			Drawable drawable = context.getResources().getDrawable(R.drawable.new_friends_add);
			//调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			holder.addFriends.setCompoundDrawables(drawable, null, null, null); //设置左图标
			setStatues(holder, View.VISIBLE, "添加", View.GONE, "");
		} else if ("2".equals(statue)) {// 等待验证
			setStatues(holder, View.GONE, "", View.VISIBLE, "等待验证");
		} else if ("3".equals(statue)) {// 已添加
			setStatues(holder, View.GONE, "", View.VISIBLE, "已添加");
		} else {// 接受
			Drawable drawable = context.getResources().getDrawable(R.drawable.new_friends_receive);
			//调用setCompoundDrawables时，必须调用Drawable.setBounds()方法,否则图片不显示
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
			holder.addFriends.setCompoundDrawables(drawable, null, null, null); //设置左图标
			setStatues(holder, View.VISIBLE, "接受", View.GONE, "");
		}

		holder.addFriends.setTag(cursor.getPosition());
		holder.addFriends.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				int tag = (Integer) view.getTag();
				if (listener != null) {
					if ("1".equals(statue)) {// 添加
						listener.addFriends(friendId, tag);
					} else {
						listener.receiveFriends(friendId, infoId, tag);
					}
				}
			}
		});
		
		holder.userImage.setTag(cursor.getPosition());
		holder.userImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				int tag = (Integer) view.getTag();
				if (listener != null) {
						listener.goFriendsInfo(friendId, tag);
				}
			}
		});
	}

	class ViewHolder {
		@InjectView(R.id.new_friends_image)
		ImageView userImage;
		@InjectView(R.id.new_friends_name)
		TextView userName;
		@InjectView(R.id.new_friends_comment)
		TextView comment;
		@InjectView(R.id.new_friends_add)
		Button addFriends;
		@InjectView(R.id.new_friends_message)
		TextView addedFriends;

		public ViewHolder(View v) {
			ButterKnife.inject(this, v);
		}
	}

	private void setStatues(ViewHolder holder, int states1, String text1,
			int states2, String text2) {
		holder.addFriends.setVisibility(states1);
		holder.addFriends.setText(text1);

		holder.addedFriends.setVisibility(states2);
		holder.addedFriends.setText(text2);
	}

	public interface OnAddFriendsListener {
		public void addFriends(String friendId, int position);
		
		public void receiveFriends(String friendId, String infoId, int position);
		
		public void goFriendsInfo(String friendId, int position);
	}

	public void setOnAddFriendsListener(OnAddFriendsListener listener) {
		this.listener = listener;
	}
}
