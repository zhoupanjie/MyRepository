package com.cplatform.xhxw.ui.ui.main.cms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.GridLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import ca.laplanete.mobile.pageddragdropgrid.PagedDragDropGrid;
import ca.laplanete.mobile.pageddragdropgrid.PagedDragDropGridAdapter;

import com.cplatform.xhxw.ui.Constants;
import com.cplatform.xhxw.ui.R;
import com.cplatform.xhxw.ui.StatisticalKey;
import com.cplatform.xhxw.ui.db.ChanneDB;
import com.cplatform.xhxw.ui.db.dao.ChanneDao;
import com.cplatform.xhxw.ui.ui.base.view.ChannelView;
import com.cplatform.xhxw.ui.util.DateUtil;
import com.wbtech.ums.UmsAgent;

public class ChannelDragGridAdapter implements PagedDragDropGridAdapter {
	private Context mContext;
	private PagedDragDropGrid mGridview;
	private List<ChanneDao> mResource;
	private int mColumnCount;
	private onChannelOperateListner mOnChanneOperateListner;
	private ArrayList<Integer> mChangedDataIndexList = new ArrayList<Integer>();

	public ChannelDragGridAdapter(Context mContext,
			PagedDragDropGrid mGridview, List<ChanneDao> mResource) {
		super();
		this.mContext = mContext;
		this.mGridview = mGridview;
		this.mResource = mResource;
		this.mColumnCount = this.mContext.getResources().getInteger(
				R.integer.channel_manage_column_count);
	}

	@Override
	public int pageCount() {
		return 1;
	}

	@Override
	public int itemCountInPage(int page) {
		return mResource.size();
	}

	@Override
	public View view(final int page, final int index) {
		ChannelView view = getView();
		if (index < mResource.size()) {
			final ChanneDao item = mResource.get(index);
			view.setTitle(item.getChannelName());
			// 排除推荐栏目
			// if (item.getChannelCreateType() != null
			// && item.getChannelCreateType().equals(
			// "" + ChanneDao.CHANNEL_TYPE_GUDING)) {
			//如果为固定栏目，不可点击，字体为蓝色
			if (item.getChannelCreateType() != null
					&& item.getChannelCreateType().equals(
							"" + ChanneDao.CHANNEL_TYPE_GUDING)) {
				view.setClickable(false);
				view.setTextColor(Color.rgb(11, 127, 208));
			} else {
				view.setTextColor(Color.rgb(108, 108, 108));
				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						int[] v_location = new int[2];
						v.getLocationInWindow(v_location); // 获取在当前窗口内的绝对坐标
						mResource.get(index).XY = v_location;
						deleteItem(page, index);
					}
				});

				view.setClickable(true);
				view.setOnLongClickListener(new OnLongClickListener() {
					@Override
					public boolean onLongClick(View v) {
						UmsAgent.onEvent(mContext, StatisticalKey.channel_sort,
								new String[] { StatisticalKey.key_channelid },
								new String[] { item.getChannelID() });
						return mGridview.onLongClick(v);
					}
				});
			}
			view.setTag(item.getChannelName());
			if (item.isInVisible()) {
				view.setVisibility(View.INVISIBLE);
			} else {
				view.setVisibility(View.VISIBLE);
			}
		}
		return view;
	}

	private ChannelView getView() {
		int itemWidth = Constants.screenWidth / mColumnCount;
		ChannelView view = new ChannelView(this.mContext);
		view.setLayoutParams(new GridLayout.LayoutParams(
				new ViewGroup.LayoutParams(itemWidth,
						ViewGroup.LayoutParams.WRAP_CONTENT)));
		return view;
	}

	@Override
	public int rowCount() {
		return AUTOMATIC;
	}

	@Override
	public int columnCount() {
		return mColumnCount;
	}

	/**
	 * 暂时使用此方法更新数据库数据状态
	 */
	@Override
	public void printLayout() {
		for (int changedIndex : mChangedDataIndexList) {
			updateDirtData(changedIndex, true);
		}
		if (mChangedDataIndexList.size() > 0) {
			this.mOnChanneOperateListner.onOrderChange();
		}
		mChangedDataIndexList.clear();
	}

	@Override
	public void swapItems(int pageIndex, int itemIndexA, int itemIndexB) {
		if (!mChangedDataIndexList.contains(itemIndexA)) {
			mChangedDataIndexList.add(itemIndexA);
		}
		if (!mChangedDataIndexList.contains(itemIndexB)) {
			mChangedDataIndexList.add(itemIndexB);
		}
		Collections.swap(mResource, itemIndexA, itemIndexB);
	}

	private void updateDirtData(int itemIndex, boolean isShow) {
		ChanneDao data = mResource.get(itemIndex);
		// if(StringUtil.parseIntegerFromString(data.getChannelCreateType())
		// == ChanneDao.CHANNEL_TYPE_KEY_WORDS){
		// return;
		// }
		data.setDirty(1);
		data.setShow(isShow ? 0 : 1);
		data.setOperatetime(DateUtil.getTime());
		ChanneDB.updateChanneById(mContext, data);
	}

	@Override
	public void moveItemToPreviousPage(int pageIndex, int itemIndex) {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveItemToNextPage(int pageIndex, int itemIndex) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteItem(int pageIndex, int itemIndex) {
		updateDirtData(itemIndex, false);
		mOnChanneOperateListner.onDeleteShow(mResource.get(itemIndex));
		mResource.remove(itemIndex);
		mGridview.notifyDataSetChanged();
	}

	public int[] addItem(ChanneDao item) {
		if (mResource == null) {
			mResource = new ArrayList<ChanneDao>();
		}
		int[] mGridview_location = new int[2];
		mGridview.getLocationInWindow(mGridview_location); // 获取在当前窗口内的绝对坐标
		int XY[] = new int[2];
		int size = mResource.size();
		int mGridview_h = mGridview.getHeight();
		int line = 1;
		int line_height = 100;
		if (size > 0) {
			if (size % 3 == 0) {
				line = size / 3;
			} else {
				line = size / 3 + 1;
			}
			line_height = mGridview_h / line;
		}

		mResource.add(item);
		size++;
		if (size % 3 == 1) {
			// 新起一行,第一个
			XY[0] = 0;
			XY[1] = mGridview_location[1] + mGridview_h;
			// XY[1] = mGridview_location[1]+mGridview_h;
		} else if (size % 3 == 2) {
			// 第二个
			XY[0] = Constants.screenWidth * 1 / 3;
			XY[1] = mGridview_location[1] + mGridview_h - line_height;
		} else if (size % 3 == 0) {
			// 第三个
			XY[0] = Constants.screenWidth * 2 / 3;
			XY[1] = mGridview_location[1] + mGridview_h - line_height;
		}
		updateDirtData(mResource.size() - 1, true);
		mGridview.notifyDataSetChanged();
		return XY;
	}

	public void notifyDataChanged() {
		mGridview.notifyDataSetChanged();
	}

	@Override
	public int deleteDropZoneLocation() {
		return 0;
	}

	public List<ChanneDao> getmResource() {
		return mResource;
	}

	public void setmResource(List<ChanneDao> mResource) {
		this.mResource = mResource;
	}

	public onChannelOperateListner getmOnChanneOperateListner() {
		return mOnChanneOperateListner;
	}

	public void setmOnChanneOperateListner(
			onChannelOperateListner mOnChanneOperateListner) {
		this.mOnChanneOperateListner = mOnChanneOperateListner;
	}

	public interface onChannelOperateListner {
		void onAddShow(ChanneDao item);

		void onDeleteShow(ChanneDao item);

		void onOrderChange();
	}

	@Override
	public ArrayList<Integer> getUnmoveAbleItemsIndex() {
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < mResource.size(); i++) {
			if (mResource.get(i).getChannelCreateType() != null
					&& (mResource.get(i).getChannelCreateType()
							.equals("" + ChanneDao.CHANNEL_TYPE_GUDING))) {
				result.add(Integer.valueOf(i));
			}
		}
		return result;
	}
}
