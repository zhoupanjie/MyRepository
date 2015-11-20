package com.cplatform.xhxw.ui.ui.main.cms.radiobroadcast;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.http.responseType.NewsDetTextSize;
import com.cplatform.xhxw.ui.ui.base.BaseAdapter;
/**
 * 
 * @ClassName AdapterBroadcast 
 * @Description TODO 广播适配器
 * @Version 1.0
 * @Author zxe
 * @Creation 2015年9月23日 上午10:03:47 
 * @Mender zxe
 * @Modification 2015年9月23日 上午10:03:47 
 * @Copyright Copyright © 2013 - 2015 Channelsoft (Beijing) Technology Co., Ltd.All Rights Reserved.
*
 */
public class AdapterBroadcast extends BaseAdapter<DataRadioBroadcast> {

	private Context con;
	ListView listView;

	public AdapterBroadcast(Context context) {
		super(context);
		con = context;
	}

	public ListView getListView() {
		return listView;
	}

	public void setListView(ListView listView) {
		this.listView = listView;
	}

	public void close() {
		clearData();
		mData = null;
		mContext = null;
		mInflater = null;
	}

	/**
	 * 获取新闻列表页标题文字大小
	 * 
	 * @return
	 */
	public static float getTitleTextSize() {
		int textStyle = Constants.getNewsDetTextSize();
		int textSize = 18;
		switch (textStyle) {
		case NewsDetTextSize.SMALL:
			textSize = 14;
			break;
		case NewsDetTextSize.MIDDLE:
			textSize = 16;
			break;
		case NewsDetTextSize.BIG:
			textSize = 18;
			break;
		case NewsDetTextSize.SUPER_BIG:
			textSize = 22;
			break;
		}
		return textSize;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolderRadio holder;
		if (convertView == null) {
			convertView = View.inflate(con, R.layout.item_broadcast, null);
			holder = new ViewHolderRadio();
			ButterKnife.inject(holder, convertView);
			/* 得到各个控件的对象 */
			convertView.setTag(holder); // 绑定ViewHolder对象
		} else {
			holder = (ViewHolderRadio) convertView.getTag(); // 取出ViewHolder对象
		}
		DataRadioBroadcast item = getItem(position);
		if (item != null) {
			setTextsize(holder, item);
			setItemData(holder, item);
		}
		return convertView;
	}

	void setTextsize(ViewHolderRadio holder, DataRadioBroadcast item) {
		float textSize = getTitleTextSize();
		holder.tvName.setTextSize(textSize);
		if (textSize > 16) {
			holder.tvTime.setTextSize(Constants.LIST_SUMMARY_TEXT_SIZE + 2);
		} else {
			holder.tvTime.setTextSize(Constants.LIST_SUMMARY_TEXT_SIZE);
		}
	}

	void setItemData(ViewHolderRadio holder, DataRadioBroadcast item) {
		if (!TextUtils.isEmpty(item.getName())) {
			holder.tvName.setText(item.getName());
		}
		if (!TextUtils.isEmpty(item.getAudiotime())) {
			holder.tvTime.setText(item.getAudiotime());
		}
		holder.riv.setAudioid(item.getAudioid());
		holder.riv.setDescription(item.getName());
		if (item.getAudioid()
				.equals(RadioBroadcastFragment.player.getAudioid())) {
			holder.riv.startRotation();
		} else {
			holder.riv.stopRotation();
		}
	}

	View getItemRootViewByIndex(int pos) {
		View vRoot = null;

		if (pos != -1) {

			int visiblePos = listView.getFirstVisiblePosition()
					- listView.getHeaderViewsCount();
			int offset = pos - visiblePos;
			// Log.d("",
			// "index="+index+"visiblePos="+visiblePos+"offset="+offset);
			// 只有在可见区域才更新
			if (false == (offset < 0)) {
				vRoot = listView.getChildAt(offset);
			}
		}
		return vRoot;
	}

	void updateItem(int pos, boolean isRotation) {
		View view = getItemRootViewByIndex(pos);
		if ((view != null) && (view.getTag() != null)) {
			ViewHolderRadio holder = (ViewHolderRadio) view.getTag();
			if (holder.riv.isRotation()) {
				if (!isRotation) {
					holder.riv.stopRotation();
				}
			} else {
				if (isRotation) {
					holder.riv.startRotation();
				}
			}
		}

	}

	public class ViewHolderRadio {
		@InjectView(R.id.tv_name)
		TextView tvName;// 名称
		@InjectView(R.id.tv_time)
		TextView tvTime;// 时间
		@InjectView(R.id.civ_icon)
		RotationImageView riv;// 图标

	}
}
