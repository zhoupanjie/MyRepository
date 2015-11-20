package com.cplatform.xhxw.ui.ui.main.saas.addressBook;

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
import com.cplatform.xhxw.ui.util.SelectNameUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 搜索好友---》搜索结果
 */
public class SearchResultAdapter extends BaseAdapter<SearchResult>{

	private OnAttentionFriendListener listener;
	
	public SearchResultAdapter(Context context) {
		super(context);
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		final SearchResult item = getItem(position);
		ViewHodler hodler = null;
		if (view == null) {
			hodler = new ViewHodler();
			view = LayoutInflater.from(mContext).inflate(R.layout.activity_search_result_item, null);

			hodler.userImage = (ImageView) view.findViewById(R.id.search_result_image);
			hodler.userName = (TextView) view.findViewById(R.id.search_result_name);
			hodler.add = (Button) view.findViewById(R.id.search_result_add);
			hodler.message = (TextView) view.findViewById(R.id.search_result_message);
			
			view.setTag(hodler);
		}else {
			hodler = (ViewHodler) view.getTag();
		}
		
		ImageLoader.getInstance().displayImage(item.getLogo(), hodler.userImage, DisplayImageOptionsUtil.avatarFriendsListImagesOptions);
		hodler.userName.setText(SelectNameUtil.getName("", item.getNickname(), item.getName()));
		
		/**0:非好友，1是好友*/
		if ("1".equals(item.getIsfriend())) {
			hodler.message.setVisibility(View.VISIBLE);
			hodler.add.setVisibility(View.GONE);
			hodler.message.setText("已添加");
		}else {
			hodler.message.setVisibility(View.GONE);
			hodler.add.setVisibility(View.VISIBLE);
			hodler.add.setText("添加好友");
		}
		
		hodler.add.setTag(position);
		hodler.add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				int tag = (Integer) view.getTag();
				if (listener != null) {
					listener.attentionListener(tag);
				}
			}
		});
		
		return view;
	}

	private class ViewHodler{
		private ImageView userImage;
		private TextView userName;
		private Button add;
		private TextView message;
	}
	
	public interface OnAttentionFriendListener{
		public void attentionListener(int position);
	}
	
	public void setOnAddFriendListener(OnAttentionFriendListener listener) {
		this.listener = listener;
	}
}
