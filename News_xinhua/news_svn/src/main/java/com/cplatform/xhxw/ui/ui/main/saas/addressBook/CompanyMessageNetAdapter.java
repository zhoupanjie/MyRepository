package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.ui.base.BaseAdapter;
import com.cplatform.xhxw.ui.ui.detailpage.expressions.XWExpressionUtil;
import com.cplatform.xhxw.ui.util.DisplayImageOptionsUtil;
import com.cplatform.xhxw.ui.util.SelectNameUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class CompanyMessageNetAdapter extends BaseAdapter<CompanyMessage> {

	public CompanyMessageNetAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		final CompanyMessage item = getItem(position);
		ViewHolder holder = null;
		if (view == null) {
			holder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(
					R.layout.activity_message_item, null);

			holder.userImage = (ImageView) view
					.findViewById(R.id.message_item_logo);
			holder.userName = (TextView) view
					.findViewById(R.id.message_item_name);
			holder.time = (TextView) view.findViewById(R.id.message_item_time);
			holder.reply = (TextView) view
					.findViewById(R.id.message_item_reply);
			holder.content = (TextView) view
					.findViewById(R.id.message_item_content);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		ImageLoader.getInstance().displayImage(item.getUserinfo().getLogo(),
				holder.userImage,
				DisplayImageOptionsUtil.avatarFriendsInfoImagesOptions);
		holder.userName.setText(SelectNameUtil.getName(item.getUserinfo()
				.getComment(), item.getUserinfo().getNickname(), item
				.getUserinfo().getName()));
		holder.time.setText(item.getFriendtime());
//		holder.reply.setText(item.getBackinfo());
		holder.reply.setText(XWExpressionUtil.generateSpanComment(
                mContext, item.getBackinfo(),
                (int) holder.reply.getTextSize()));
		holder.content.setText(XWExpressionUtil.generateSpanComment(
                mContext, item.getInfodata(),
                (int) holder.content.getTextSize()));
		return view;
	}

	class ViewHolder {
		ImageView userImage;
		TextView userName;
		TextView time;
		TextView reply;
		TextView content;
	}
}
