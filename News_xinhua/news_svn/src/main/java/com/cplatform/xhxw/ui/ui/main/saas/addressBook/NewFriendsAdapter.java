package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.ui.base.BaseAdapter;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class NewFriendsAdapter extends BaseAdapter<NewFriends> {

	public NewFriendsAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		final NewFriends item = getItem(position);
		ViewHodle hodle = null;
		if (view == null) {
			hodle = new ViewHodle();
			view = LayoutInflater.from(mContext).inflate(
					R.layout.activity_new_friends_item, null);

			hodle.userImage = (ImageView) view
					.findViewById(R.id.new_friends_image);
			hodle.userName = (TextView) view
					.findViewById(R.id.new_friends_name);
			hodle.describe = (TextView) view
					.findViewById(R.id.new_friends_comment);
			hodle.add = (Button) view.findViewById(R.id.new_friends_add);
			hodle.message = (TextView) view
					.findViewById(R.id.new_friends_message);

			view.setTag(hodle);
		} else {
			hodle = (ViewHodle) view.getTag();
		}

		ImageLoader.getInstance().displayImage(item.getLogo(), hodle.userImage, DisplayImageOptionsUtil.avatarImagesOptions);
		hodle.userName.setText(item.getName());
		
		hodle.add.setTag(position);
		hodle.add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				/**判断
				 * 点击的时候是   添加     接受
				 * 
				 * 添加跳转验证页面
				 * */
				
				/**此功能仅仅是接受功能*/
				
				int tag = (Integer) view.getTag();
			}
		});

		hodle.userImage.setTag(position);
		hodle.userImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				int tag = (Integer) view.getTag();
				mContext.startActivity(FriendInfoActivity.newIntent(mContext, getData().get(tag).getUserid(), true));
			}
		});

		return view;
	}

	private class ViewHodle {
		private ImageView userImage;
		private TextView userName;
		private TextView describe;
		private Button add;
		private TextView message;
	}

}
