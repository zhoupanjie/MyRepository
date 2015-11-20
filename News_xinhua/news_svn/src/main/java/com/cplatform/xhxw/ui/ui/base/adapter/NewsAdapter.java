//package com.cplatform.xhxw.ui.ui.base.adapter;
//
//import android.content.Context;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.cplatform.xhxw.ui.http.responseType.ShowType;
//import com.cplatform.xhxw.ui.model.New;
//import com.cplatform.xhxw.ui.ui.base.BaseAdapter;
//import com.cplatform.xhxw.ui.ui.base.view.NewItem;
//import com.cplatform.xhxw.ui.ui.base.view.NewsAdvertiseItem;
//import com.cplatform.xhxw.ui.ui.base.view.NewsAudioItem;
//import com.cplatform.xhxw.ui.ui.base.view.NewsBroadcastItem;
//import com.cplatform.xhxw.ui.ui.base.view.NewsMultiHorizontalItem;
//import com.cplatform.xhxw.ui.ui.base.view.NewsMultiHorizontalItem.OnMultiImgOnClickListener;
//import com.cplatform.xhxw.ui.ui.base.view.NewsSmallVideoItem;
//import com.cplatform.xhxw.ui.ui.base.view.NewsVideoItem;
//import com.cplatform.xhxw.ui.ui.base.view.NewsVideoItem.onItemVideoPlayLisenter;
//import com.cplatform.xhxw.ui.ui.base.view.PhotoSingleItem;
//import com.cplatform.xhxw.ui.ui.base.view.VideoNewItem;
//import com.cplatform.xhxw.ui.ui.base.view.VideoSingleItem;
//
///**
// * 新闻列表适配器 Created by cy-love on 13-12-25.
// */
//public class NewsAdapter extends BaseAdapter<New> {
//
//	private OnMultiImgOnClickListener mMultClickLis;
//	private onItemVideoPlayLisenter mOnVideoPlayLis;
//	private Context con;
//
//	public NewsAdapter(Context context, OnMultiImgOnClickListener multClickLis,
//			onItemVideoPlayLisenter OnVideoPlayLisenter) {
//		super(context);
//		con = context;
//		this.mMultClickLis = multClickLis;
//		this.mOnVideoPlayLis = OnVideoPlayLisenter;
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		New item = getItem(position);
//		int type = getItemViewType(position);
//		if (convertView == null
//				|| needCreateNewView((Integer) convertView.getTag(),
//						item.getListStyle())) {
//			switch (type) {
//			case ShowType.IMAGE: {
//				NewsMultiHorizontalItem view = new NewsMultiHorizontalItem(
//						mContext);
//				view.setOnMultiImgOnClickListener(mMultClickLis);
//				convertView = view;
//			}
//				break;
//			case ShowType.BIG_IMAGE: {
//				convertView = new PhotoSingleItem(mContext);
//			}
//				break;
//			case ShowType.BIG_VIDEO: {
//				convertView = new VideoSingleItem(mContext);
//			}
//				break;
//			case ShowType.AUDIO: {
//				convertView = new NewsAudioItem(mContext);
//			}
//				break;
//			case ShowType.SMALL_VIDEO: {
//				convertView = new NewsSmallVideoItem(mContext);
//			}
//				break;
//			case ShowType.LIST_BIG_VIDEO: {
//				VideoNewItem view = new VideoNewItem(mContext);
////				view.setOnItemVideoPlayLisenter(mOnVideoPlayLis);
//				convertView = view;
//			}
//				break;
//			case ShowType.BIG_IMG_NEWS: {
//				convertView = new NewsVideoItem(mContext, false);
//			}
//				break;
//			case ShowType.LIST_ADVER_BIG_IMG: {
//				convertView = new NewsAdvertiseItem(mContext);
//			}
//				break;
//			case ShowType.BROADCAST_AUDIO:{
//				convertView = new NewsBroadcastItem(mContext);
//			}
//				break;
//			default:
//				if (item.getListStyle() == New.LIST_STYLE_PICS) {
//					NewsMultiHorizontalItem view = new NewsMultiHorizontalItem(
//							mContext);
//					view.setOnMultiImgOnClickListener(mMultClickLis);
//					convertView = view;
//				} else {
//					convertView = new NewItem(mContext);
//				}
//				break;
//			}
//			convertView.setTag(item.getListStyle());
//		}
//		switch (type) {
//		case ShowType.IMAGE: {
//			NewsMultiHorizontalItem view = (NewsMultiHorizontalItem) convertView;
//			view.setData(item, isToShowDividerBg(position, true),
//					isToShowDividerBg(position, false));
//		}
//			break;
//		case ShowType.BIG_IMAGE: {
//			PhotoSingleItem view = (PhotoSingleItem) convertView;
//			view.setData(item);
//		}
//			break;
//		case ShowType.BIG_VIDEO: {
//			VideoSingleItem view = (VideoSingleItem) convertView;
//			view.setData(item);
//		}
//			break;
//		case ShowType.AUDIO: {
//			NewsAudioItem view = (NewsAudioItem) convertView;
//			view.setData(item);
//		}
//			break;
//		case ShowType.SMALL_VIDEO: {
//			NewsSmallVideoItem view = (NewsSmallVideoItem) convertView;
//			view.setData(item);
//		}
//			break;
//		case ShowType.LIST_ADVER_BIG_IMG: {
//			NewsAdvertiseItem view = (NewsAdvertiseItem) convertView;
//			view.setData(item);
//		}
//			break;
//		case ShowType.LIST_BIG_VIDEO:{
//			VideoNewItem view = (VideoNewItem) convertView;
//			view.setData(item);
//		}
//			break;
//		case ShowType.BIG_IMG_NEWS: {
//			NewsVideoItem view = (NewsVideoItem) convertView;
//			view.setData(item);
//		}
//			break;
//		case ShowType.BROADCAST_AUDIO: {
//			NewsBroadcastItem view = (NewsBroadcastItem) convertView;
//			view.setData(item);
//		}
//			break;
//		default:
//			if (item.getListStyle() == New.LIST_STYLE_PICS) {
//				NewsMultiHorizontalItem view = (NewsMultiHorizontalItem) convertView;
//				view.setData(item, isToShowDividerBg(position, true),
//						isToShowDividerBg(position, false));
//			} else {
//				NewItem view = (NewItem) convertView;
//				view.setData(item);
//			}
//			break;
//		}
//		return convertView;
//	}
//
//	@Override
//	public int getItemViewType(int position) {
//		int type = getItem(position).getShowType();
//		return type > 13 ? 0 : type;
//	}
//
//	@Override
//	public int getViewTypeCount() {
//		return 13;
//	}
//
//	private boolean needCreateNewView(int olderViewType, int newViewTpe) {
//
//		return (olderViewType == New.LIST_STYLE_PICS && newViewTpe != New.LIST_STYLE_PICS)
//				|| (olderViewType != New.LIST_STYLE_PICS && newViewTpe == New.LIST_STYLE_PICS);
//	}
//
//	public void close() {
//		clearData();
//		mData = null;
//		mContext = null;
//		mInflater = null;
//	}
//
//	private boolean isToShowDividerBg(int position, boolean isTop) {
//		if (isTop) {
//			if (position == 0) {
//				return true;
//			} else {
//				int type = getItemViewType(position - 1);
//				if (type == ShowType.IMAGE
//						|| getItem(position - 1).getListStyle() == New.LIST_STYLE_PICS) {
//					return false;
//				}
//				return true;
//			}
//		} else {
//			if (position == getCount() - 1) {
//				return true;
//			} else {
//				int type = getItemViewType(position + 1);
//				if (type == ShowType.IMAGE
//						|| getItem(position + 1).getListStyle() == New.LIST_STYLE_PICS) {
//					return false;
//				}
//				return true;
//			}
//		}
//	}
//}
